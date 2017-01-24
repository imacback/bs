package cn.aiyuedu.bs.wap.controller;

import cn.aiyuedu.bs.common.Constants.RechWay;
import cn.aiyuedu.bs.common.orm.Page;
import cn.aiyuedu.bs.common.util.IdUtil;
import cn.aiyuedu.bs.common.util.JSONUtils;
import cn.aiyuedu.bs.dao.entity.Recharge;
import cn.aiyuedu.bs.dao.entity.User;
import cn.aiyuedu.bs.wap.dto.ParamDto;
import cn.aiyuedu.bs.wap.dto.RechargeOrder;
import cn.aiyuedu.bs.wap.dto.ZhiFukaNotifyDto;
import cn.aiyuedu.bs.wap.service.*;
import com.duoqu.commons.web.spring.RequestAttribute;
import com.duoqu.commons.web.spring.SessionAttribute;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;

/**
 * Description:
 *
 * @author yz.wu
 */
@Controller
@RequestMapping("/recharge/*")
public class RechargeController {


    public static final int PAGE_SIZE = 20;
    static final String[] CM = {"134", "135", "136", "137", "138", "139", "150", "151", "152", "157", "158", "159", "182", "183", "184", "187", "178", "188", "147", "1705"};
    static final String[] CU = {"130", "131", "132", "145", "155", "156", "176", "185", "186", "1709"};
    static final String[] CT = {"133", "153", "177", "180", "181", "189", "1349", "1700"};
    private final Logger log = LoggerFactory.getLogger(getClass());
    @Autowired
    private RechargeService rechargeService;
    @Autowired
    private RechargeZfbService rechargeZfbService;
    @Autowired
    private RechargeZfkService rechargeZfkService;
    @Autowired
    private RechargeMobilepayService rechargeMobilepayService;
    @Autowired
    private UserService userService;
    @Autowired
    private Properties bsWapConfig;
    @Autowired
    private ParamService paramService;

    String buildParamString(Map<String, String> map, String mercKey) {
        StringBuilder sb = new StringBuilder(1024);
        for (String pname : map.keySet()) {
            if ("sign".equals(pname) || StringUtils.isEmpty(map.get(pname))) {
                continue;
            }
            try {
                sb.append(pname).append("=").append(URLEncoder.encode(map.get(pname), "UTF-8")).append("&");
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        sb.append("sign=").append(DigestUtils.md5Hex(buildSignParamStr(map, mercKey)));
        return sb.toString();
    }

    String buildSignParamStr(Map<String, String> map, String mercKey) {
        SortedMap<String, String> sortedMap = new TreeMap<String, String>(map);
        sortedMap.put("merc_key", mercKey);
        StringBuilder sb = new StringBuilder(1024);
        for (String pname : sortedMap.keySet()) {
            if ("sign".equals(pname) || StringUtils.isEmpty(sortedMap.get(pname))) {
                continue;
            }
            sb.append(pname).append("=").append(sortedMap.get(pname)).append("&");
        }
        sb = sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }

    @RequestMapping("entry.do")
    public String entry(HttpServletRequest request, HttpServletResponse response,
                        @RequestAttribute(value = "param", required = true) ParamDto param,
                        @SessionAttribute(value = "user", required = true) User user, ModelMap model) {

        model.put("param", param);

        if (user != null && user.getId() != null) {
            user = userService.findOne(user.getId(), user.getUid());
            request.getSession().setAttribute("user", user);
        } else if (NumberUtils.toInt(bsWapConfig.getProperty("auth.required"), 0) > 0) {
            // 新增强制验证用户登录鉴权开关 By ZhengQian 20150915
            return "/page/loginAlert.html";
        }
        model.put("user", user);
        return "/page/recharge.html";
    }

    @RequestMapping("list.do")
    public String list(HttpServletRequest request, HttpServletResponse response,
                       @RequestAttribute(value = "param", required = true) ParamDto param,
                       @SessionAttribute(value = "user", required = true) User user,
                       @RequestParam(value = "pageNo", required = false, defaultValue = "1") Integer pageNo, ModelMap model) {
        Page<Recharge> page = rechargeService.getList(user.getId(), (pageNo - 1) * PAGE_SIZE, PAGE_SIZE);
        page.setPageSize(PAGE_SIZE);
        model.put("page", page);
        model.put("param", param);
        model.put("user", user);
        return "/page/rechargeList.html";
    }

    @RequestMapping("submit.do")
    public String submit(HttpServletRequest request, @RequestAttribute(value = "param", required = true) ParamDto param,
                         @SessionAttribute(value = "user", required = true) User user,
                         @RequestParam(value = "paymoney", required = true) Double totalFee,
                         @RequestParam(value = "payType", required = true) Integer payType,
                         @RequestParam(value = "mobile", required = false) String mobile,
                         ModelMap model) throws IOException {

        // 新增强制验证用户登录鉴权开关 By ZhengQian 20150915
        if (NumberUtils.toInt(bsWapConfig.getProperty("auth.required"), 0) > 0
                && (user == null || user.getId() == null)) {
            return "/page/loginAlert.html";
        }

        RechWay rechWay = RechWay.zfb;
        if (payType == RechWay.zfk.val()) {
            rechWay = RechWay.zfk;
        }
        //by ZhengQian 20160612
        else if (payType == RechWay.mobilepay.val()) {
            rechWay = RechWay.mobilepay;
        }

        RechargeOrder a = new RechargeOrder();
        a.setRechWay(rechWay);
        a.setTradeNo(IdUtil.uuid());
        a.setNotifyUrl(bsWapConfig.getProperty("url.prefix") + rechWay.getNotifyUrl());
        a.setReturnUrl(bsWapConfig.getProperty("url.prefix") + rechWay.getReturnUrl());
        a.setSubject("psReadRecharge");
        a.setTotalFee(totalFee);

        String errMsg = null;
        if (!a.getRechWay().equals(RechWay.mobilepay)) {
            rechargeService.createOrder(param, a);
            String url = rechargeService.getUrl(a);
            return "redirect:" + url;
        } else {


            //By ZhengQian 20160612 话费支付需要一个单独预处理订单 从订单中获取支付页面地址 然后进行跳转 此处进行单独特殊处理

            String mercId = "2000197";
            String mercKey = "19dcbc79a5c532ff073f62559bf0e84e";
            String appId = "7846792a2d3d11e6b8453ab47144b7ab";
            String preUrl = RechWay.mobilepay.getSubmitUrl();

            String corpType = null;
            if (StringUtils.isBlank(mobile) || mobile.length() != 11 || !StringUtils.isNumeric(mobile)) {
                errMsg = "手机号码不正确，请输入正确的手机号码";
            } else if (StringUtils.startsWithAny(mobile, CM)) {
                corpType = "1";
            } else if (StringUtils.startsWithAny(mobile, CU)) {
                corpType = "2";
            } else if (StringUtils.startsWithAny(mobile, CT)) {
                corpType = "3";
            } else {
                errMsg = "无法识别手机运营商，请尝试其他方式充值";
            }
            if (errMsg == null) {

                Map<String, String> paramMap = new HashMap<String, String>();
                paramMap.put("merc_id", mercId);
                paramMap.put("app_orderid", a.getTradeNo());
                paramMap.put("app_id", appId);
                paramMap.put("app_level", "1");

                int payCode = a.getTotalFee().intValue();
                paramMap.put("pay_code", Integer.toString(payCode));
                paramMap.put("amount", Integer.toString(payCode * 100));
                paramMap.put("time", Long.toString(System.currentTimeMillis() / 1000));


                /*
                1-中国移动：134、135、136、137、138、139、150、151、152、157(TD)、158、159、182、183、184、187、178、188、147（数据卡号段） 、1705（虚拟运营商移动号段）
                2-中国联通：130、131、132、145(数据卡号段)155、156、176、185、186、1709（虚拟运营商联通号段）
                3-中国电信：133、153、177、180、181、189、（1349卫通）、1700（虚拟运营商电信号段）
                */

                paramMap.put("corp_type", corpType);
                paramMap.put("phone", mobile);

                String ip = com.duoqu.commons.web.utils.LogUtil.getIpAddr(request);
                paramMap.put("ip", StringUtils.isNotBlank(ip) ? ip : "127.0.0.1");
                paramMap.put("ret_url", a.getReturnUrl());
                paramMap.put("noti_url", a.getNotifyUrl());
                paramMap.put("site_type", "3");
                //20160818 接口变更  0-->2
                //paramMap.put("scheme", "0");
                paramMap.put("scheme", "2");
                paramMap.put("ver", "3");

                String paramString = buildParamString(paramMap, mercKey);
                preUrl += paramString;
                System.out.println("request:\t" + preUrl);
                CloseableHttpClient httpclient = HttpClients.createDefault();

                String feecode = "";
                switch (totalFee.intValue()) {
                    case 2 : //2元
                        feecode = "92050002";
                        break;
                    case 5: //5元
                        feecode = "92050005";
                        break;
                    case 8: //8元
                        feecode = "92050008";
                        break;
                    case 10: //10元
                        feecode = "92050010";
                        break;
                }

                String redirecturl = "http://www.baidu.com";

                String url = "http://101.201.148.52:13888/mng_jh/ds/hyRdo.do?feecode=" + feecode + "&phone=" + mobile + "&cm=M20U0010&mcpid=aiyuemw&redirecturl=" + redirecturl + "&cpparam=00AEE122606804";

                HttpGet httpget = new HttpGet(url);//FIXIT 接口文档要求 POST？？？？
                CloseableHttpResponse response = httpclient.execute(httpget);

                try {

                    String msg = new String(IOUtils.toByteArray(response.getEntity().getContent()), "utf-8");
                    System.out.println("response:\t" + msg);
                    Map result = JSONUtils.deserializeObject(msg);

                    System.out.println("parse status:\t" + result.get("status"));

                    String status = (String) result.get("status");
                    if ("-1".equals(status)) {

//                        Map res = (Map) result.get("res");

                        String fee_url = (String) result.get("payUrl");
//                        String orderId = (String) res.get("orderid");
//                        System.out.println("parse orderid:\t" + orderId);

                        String orderId = String.valueOf(System.currentTimeMillis());

                        if (StringUtils.isNotBlank(orderId)) {

                            //生成本地订单
                            model.put("orderId", orderId);
                            model.put("mobile", mobile);
                            model.put("param", param);
                            model.put("user", user);
                            rechargeService.createOrder(param, a);

//                            String fee_url = (String) res.get("fee_url");
                            System.out.println("redirect:\t" + fee_url);
                            //20160818 计费流程修改
                            return "redirect:" + fee_url;
                            //  return "/page/rechargeVerifyCode.html";

                        } else {
                            errMsg = "计费订单生成异常，请稍后再试或尝试其方式充值！";
                        }
                    } else {
                        errMsg = (String) result.get("msg") + "\t(错误代码:" + result.get("status") + ")";
                    }
                } finally {
                    response.close();
                }
            }
            //TODO 存在错误 返回充值页面 提示信息
            if (StringUtils.isBlank(errMsg)) {
                errMsg = "充值失败！发生未知错误，请稍后再试或者使用其他方式充值！";
            }

        }
        model.put("errorMessage", errMsg);
        return "forward:/recharge/entry.do";
    }

    @RequestMapping("submitVerificationCode.do")
    public String submitVerificationCode(HttpServletRequest request, @RequestAttribute(value = "param", required = true) ParamDto param,
                                         @SessionAttribute(value = "user", required = true) User user,
                                         @RequestParam(value = "orderId", required = true) String orderId,
                                         @RequestParam(value = "verifyCode", required = true) String verifyCode,
                                         ModelMap model) throws IOException {

        // 新增强制验证用户登录鉴权开关 By ZhengQian 20150915
        if (NumberUtils.toInt(bsWapConfig.getProperty("auth.required"), 0) > 0
                && (user == null || user.getId() == null)) {
            return "/page/loginAlert.html";
        }
        //By ZhengQian 20160615 话费支付短代方式 需要验证码进行验证
        String mercId = "2000197";
        String mercKey = "19dcbc79a5c532ff073f62559bf0e84e";
        String appId = "7846792a2d3d11e6b8453ab47144b7ab";
        String confirmURL = "http://113.31.25.56:23000/sdkfee/api2/ver_confirm?";
        String errMsg = null;
        if (StringUtils.isBlank(verifyCode)) {
            errMsg = "请输入验证码！";
        } else if (StringUtils.isBlank(orderId)) {
            errMsg = "订单号错误！";
        }

        //测试用手机号码   15210621360
        if (errMsg == null) {

            Map<String, String> paramMap = new HashMap<String, String>();
            paramMap.put("merc_id", mercId);
            paramMap.put("app_id", appId);

            paramMap.put("orderid", orderId);
            paramMap.put("verify_code", verifyCode);
            paramMap.put("ver", "3");

            String paramString = buildParamString(paramMap, mercKey);
            confirmURL += paramString;
            System.out.println("request:\t" + confirmURL);
            CloseableHttpClient httpclient = HttpClients.createDefault();
            HttpGet httpget = new HttpGet(confirmURL);//FIXIT 接口文档要求 POST？？？？
            CloseableHttpResponse response = httpclient.execute(httpget);

            try {


                String msg = new String(IOUtils.toByteArray(response.getEntity().getContent()), "utf-8");
                System.out.println("response:\t" + msg);
                Map result = JSONUtils.deserializeObject(msg);

                System.out.println("parse status:\t" + result.get("status"));

                if ((Integer) result.get("status") == 0) {

                    Map res = (Map) result.get("res");
                    String code = (String) res.get("code");
                    System.out.println("parse code:\t" + code);

                    if (StringUtils.isNotBlank(code) && code.equals("0") || code.equals("200")) {

                        //短信验证计费成功
                        // TODO ????
                        //这倆代码仅作为支付成功标识 不作为计费成功标识  计费情况等待异步通知，此处不做其他充值处理，直接返回


                        //
                        return "redirect:" + bsWapConfig.getProperty("url.prefix") + RechWay.mobilepay.getReturnUrl();

                    } else {
                        errMsg = "充值失败！请尝试其他方式充值\t(错误代码：" + code + ")";
                    }
                } else {
                    errMsg = (String) result.get("msg") + "\t(错误代码:" + result.get("status") + ")";
                }
            } finally {
                response.close();
            }
        }
        //TODO 存在错误 返回充值页面 提示信息
        if (StringUtils.isBlank(errMsg)) {
            errMsg = "充值失败！发生未知错误，请稍后再试或者使用其他方式充值！";
        }


        model.put("errorMessage", errMsg);
        return "forward:/recharge/entry.do";
    }

    @RequestMapping("gotowechatpay.do")
    public String gotowechatpay(HttpServletRequest request,
                                @RequestAttribute(value = "param", required = true) ParamDto param,
                                @RequestAttribute(value = "tradeNo", required = true) String tradeNo,
                                @RequestParam(value = "paymoney", required = true) Double totalFee) {

        // testurl
        // http://m.psread.cn/recharge/gotowechatpay.do?paymoney=1&tradeNo=12323123123123123

        RechWay rechWay = RechWay.zfk;

        RechargeOrder a = new RechargeOrder();
        a.setRechWay(rechWay);
        a.setTradeNo(tradeNo);
        a.setNotifyUrl(bsWapConfig.getProperty("url.prefix") + "/recharge/wechatpayCallback.do");
        a.setReturnUrl(bsWapConfig.getProperty("url.prefix") + "/recharge/wechatpayGoBack.do");
        a.setSubject("psReadRecharge");
        a.setTotalFee(totalFee);

        // rechargeService.createOrder(param, a);

        String url = rechargeService.getUrl(a);

        return "redirect:" + url;
    }

    @RequestMapping("wechatpayNotify.do")
    public String wechatpayNotify(HttpServletRequest request, ModelMap model) {
        /*
         * Map<String, String> param = new HashMap<>(); Enumeration<String> e =
		 * request.getParameterNames(); String name, value; while
		 * (e.hasMoreElements()) { name = e.nextElement(); value =
		 * request.getParameter(name); param.put(name, value); log.debug(
		 * "callback, name:" + name + ", value:" + value); } String
		 * callbackResult = rechargeZfbService.callbackUpdate(param) ? "success"
		 * : "failed"; model.put("callbackResult", callbackResult);
		 * 
		 */
        // 结果通知
        String url = "http://mabao.aiyuedu.cn:19080/select/fee_notify?" + request.getQueryString();

        return "redirect:" + url;
    }

    @RequestMapping("wechatpayReturn.do")
    public String wechatpayReturn(HttpServletRequest request,
                                  @RequestAttribute(value = "param", required = true) ParamDto param,
                                  @SessionAttribute(value = "user", required = true) User user, ModelMap model) {

		/*
         * rechargeService.postRecharge(request, user);
		 * 
		 * Object o = request.getSession().getAttribute("refer"); if (o != null)
		 * { String refer = o.toString(); param.setRefer(refer);
		 * paramService.store(request, param); return "redirect:" + refer; }
		 * else { return "redirect:" + bsWapConfig.getProperty("home.url"); }
		 */
        String url = "http://mabao.aiyuedu.cn:19080/select/fee_result?" + request.getQueryString();
        return "redirect:" + url;
    }

    @RequestMapping("callback.do")
    public String callback(HttpServletRequest request, ModelMap model) {
        Map<String, String> param = new HashMap<>();
        Enumeration<String> e = request.getParameterNames();
        String name, value;
        while (e.hasMoreElements()) {
            name = e.nextElement();
            value = request.getParameter(name);
            param.put(name, value);
            log.debug("callback, name:" + name + ", value:" + value);
        }
        String callbackResult = rechargeZfbService.callbackUpdate(param) ? "success" : "failed";
        model.put("callbackResult", callbackResult);

        return "/page/callbackResult.html";
    }

    @RequestMapping("zfkCallback.do")
    public String zfkCallback(HttpServletRequest request, @ModelAttribute("notify") ZhiFukaNotifyDto notify,
                              ModelMap model) {
        String callbackResult = rechargeZfkService.callbackUpdate(notify) ? "success" : "failed";
        model.put("callbackResult", callbackResult);

        return "/page/callbackResultZfk.html";
    }


    @RequestMapping("mobilepayCallback.do")
    public String mobilepayCallback(HttpServletRequest request, ModelMap model) {
        Map<String, String> param = new HashMap<>();
        Enumeration<String> e = request.getParameterNames();
        String name, value;
        while (e.hasMoreElements()) {
            name = e.nextElement();
            value = request.getParameter(name);
            param.put(name, value);
            log.debug("mobilepayCallback, name:" + name + ", value:" + value);
        }
        String callbackResult = rechargeMobilepayService.callbackUpdate(param) ? "success" : "failed";
        model.put("callbackResult", callbackResult);

        return "/page/callbackResult.html";
    }

    @RequestMapping("goBack.do")
    public String goBack(HttpServletRequest request, @RequestAttribute(value = "param", required = true) ParamDto
            param,
                         @SessionAttribute(value = "user", required = true) User user, ModelMap model) {

        rechargeService.postRecharge(request, user);

        Object o = request.getSession().getAttribute("refer");
        if (o != null) {
            String refer = o.toString();
            param.setRefer(refer);
            paramService.store(request, param);
            return "redirect:" + refer;
        } else {
            return "redirect:" + bsWapConfig.getProperty("home.url");
        }
    }
}
