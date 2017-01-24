package cn.aiyuedu.bs.back.controller;

import cn.aiyuedu.bs.back.service.*;
import com.duoqu.commons.utils.DigestUtils;
import com.duoqu.commons.utils.RandomUtil;
import cn.aiyuedu.bs.common.util.IdUtil;
import cn.aiyuedu.bs.dao.entity.AdminUser;
import com.google.code.kaptcha.Constants;
import com.google.common.collect.Maps;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

/**
 * Description:
 *
 * @author yz.wu
 */
@Controller
@RequestMapping("/*")
public class SessionController {

    private final Logger logger = LoggerFactory.getLogger(SessionController.class);
    private Logger operateLogger = LoggerFactory.getLogger("operateLogger");

    @Autowired
    private AdminUserService adminUserService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private MenuService menuService;
    @Autowired
    private MailService mailService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private FilterWordService filterWordService;
    @Autowired
    private SiteService siteService;

    private boolean hasInit = false;

    @RequestMapping("login.do")
    public ModelAndView login(
            HttpServletResponse response) {
        setSessionId(response, IdUtil.uuid());
        return new ModelAndView("/home/login");
    }

    @RequestMapping("loginForward.do")
    public ModelAndView loginForward(ModelMap model) {
        return new ModelAndView("/home/loginForward");
    }

    @RequestMapping("loginCheck.do")
    @ResponseBody
    public Map<String, String> loginCheck(
            @RequestParam(value = "userName", required = false) String userName,
            @RequestParam(value = "userPass", required = false) String userPass,
            @RequestParam(value = "verifyCode", required = false) String verifyCode,
            HttpServletRequest request) {

        Map<String, String> result = Maps.newHashMap();

        if (StringUtils.isNotEmpty(userName)
                && StringUtils.isNotEmpty(userPass)
                && StringUtils.isNotEmpty(verifyCode)) {

            Object o = request.getSession().getAttribute(
                    Constants.KAPTCHA_SESSION_KEY);
            if (o != null && verifyCode != null
                    && StringUtils.equals(o.toString(), verifyCode)) {
                String pass = DigestUtils.sha1ToBase64UrlSafe(userPass);
                List<AdminUser> list =adminUserService.getByNameList(userName);

                if(CollectionUtils.isEmpty(list)){
                    result.put("success", "false");
                    result.put("info", "该用户不存在！");
                    return result;
                }
                AdminUser user = list.get(0);
                if (user.getPassword().equals(pass)){
                    if(user.getIsUse()==1) {
                        HttpSession session = request.getSession(false);
                        if (session != null) {
                            session.invalidate();
                        }
                        operateLogger.info("adminUser:" + userName + " login");
                        session = request.getSession(true);
                        session.setAttribute(cn.aiyuedu.bs.back.Constants.USER_SESSION_KEY, user);
                        result.put("success", "true");
                        result.put("info", "登录成功！");
                    }else{
                        result.put("success", "false");
                        result.put("info", "用户被禁用，请联系管理员！");
                    }
                    }else {
                    result.put("success", "false");
                    result.put("info", "用户名或密码错误！");
                }
             } else {
                result.put("success", "false");
                result.put("info", "验证码错误！");
                return result;
            }
        }
        return result;

    }

    @RequestMapping("logout.do")
    public ModelAndView logout(
            HttpServletRequest request,
            HttpServletResponse response) {

        request.getSession().removeAttribute("user");
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        request.getSession(true);
        setSessionId(response, IdUtil.uuid());
        return new ModelAndView("/home/loginForward");
    }

    @RequestMapping("timeout.do")
    @ResponseBody
    public Map<String, Object> timeout(HttpServletResponse response) {
        response.addHeader("_timeout", "true");
        Map<String, Object> responseMap = Maps.newHashMap();
        responseMap.put("success", "false");
        responseMap.put("info", "会话超时，请重新登陆！");

        return responseMap;
    }

    @RequestMapping("forbid.do")
    @ResponseBody
    public Map<String, Object> forbid(HttpServletResponse response) {
        Map<String, Object> responseMap = Maps.newHashMap();
        responseMap.put("success", "false");
        responseMap.put("info", "没有操作该功能的权限！");

        return responseMap;
    }

    public void setSessionId(HttpServletResponse response, String sessionId) {
        Cookie c = new Cookie("JSESSIONID", sessionId);
        c.setPath("/");
        c.setMaxAge(Integer.MAX_VALUE);
        response.addCookie(c);
    }

    @RequestMapping("forgetPassword.do")
    @ResponseBody
    public Map<String, Object> forgetPassword(
            @RequestParam(value = "userName", required = false) String userName,
            @RequestParam(value = "verifyCode", required = false) String verifyCode,
            HttpServletRequest request) {

        Map<String, Object> responseMap = Maps.newHashMap();
        responseMap.put("success", "false");

        if (StringUtils.isNotEmpty(userName)
                && StringUtils.isNotEmpty(verifyCode)) {

            Object o = request.getSession().getAttribute(
                    Constants.KAPTCHA_SESSION_KEY);
            if (o != null && verifyCode != null
                    && StringUtils.equals(o.toString(), verifyCode)) {
                AdminUser user = adminUserService.getByName(userName);
                if (user != null) {
                    String password = RandomUtil.getRandomInt(6);
                    user.setPassword(DigestUtils.sha1ToBase64UrlSafe(password));
                    adminUserService.update(user);

                    mailService.resetPassword(password, user.getEmail());

                    responseMap.put("success", "true");
                    responseMap.put("info", "临时密码已发送到您的邮箱！");
                } else {
                    responseMap.put("info", "用户名不存在");
                }
            }
        } else {
            responseMap.put("info", "验证码错误");
        }

        return responseMap;
    }

    @RequestMapping("initData.do")
    @ResponseBody
    public Map<String, Object> initData(
            @RequestParam(value = "secret", required = false) String secret) {

        Map<String, Object> responseMap = Maps.newHashMap();

        if (StringUtils.isNotEmpty(secret)){
            if(!hasInit){
                switch (secret){
                    case  "bsInitData":
                        roleService.initData();
                        adminUserService.initData();
                        menuService.initData();
                        hasInit = true;
                        responseMap.put("success", "true");
                        responseMap.put("info", "bsInitData 数据初始化完成！");
                        break;
                    default:
                        responseMap.put("success", "false");
                        responseMap.put("info", "没有指定的初始化方法");
                        break;
                }
            } else {
                responseMap.put("success", "false");
                responseMap.put("info", "数据初始化已经执行！");
            }
        } else {
            responseMap.put("success", "false");
            responseMap.put("info", "数据初始化执行失败，请联系管理员！");
        }

        return responseMap;
    }
}
