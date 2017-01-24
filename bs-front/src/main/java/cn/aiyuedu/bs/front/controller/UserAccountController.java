package cn.aiyuedu.bs.front.controller;

import cn.aiyuedu.bs.common.Constants;
import cn.aiyuedu.bs.common.dto.ClientInfo;
import cn.aiyuedu.bs.front.service.UserAccountService;
import cn.aiyuedu.bs.front.vo.RechargeVo;
import cn.aiyuedu.bs.front.exception.IllegalUserException;
import cn.aiyuedu.bs.front.vo.PayTotalVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Description:
 *
 * @author Scott
 */
@Controller
@RequestMapping("/**/ft/user/*")
public class UserAccountController {
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private UserAccountService userAccountService;
    @Autowired
    private UserService userService;
    private  int pageSize =15;
//    private  int firstPageSize =10;

    /**
     * 我的帐户
     *
     * @param request
     * @param uid
     * @param model
     * @return
     */
    @RequestMapping("account.do")
    public ModelAndView account(HttpServletRequest request,
                                @RequestParam(value = "id", required = true) Integer uid,
                                ModelMap model) {
        User user = userService.get(uid);
        model.put("user", user);
        ClientInfo cli = (ClientInfo) request.getAttribute("xclient");
        if (null != cli) {
            Integer platformId = cli.clientParam().getPlatform();
            String version = cli.clientParam().getVersion();
            Integer ditchId = cli.clientParam().getChannelId();
            model.put("platformId", platformId);
            model.put("version", version);
            model.put("ditchId", ditchId);
        }
        List<RechargeBase> rechList = userAccountService.listRecharge(uid, 1, 3);
        List<PayTotalVo> payBookList = userAccountService.listPay(uid, Constants.PayType.book.val(), 1, 3);
        List<PayTotalVo> payChapList = userAccountService.listPay(uid, Constants.PayType.chapter.val(), 1, 3);
        model.put("rechList", rechList);
        model.put("payBookList", payBookList);
        model.put("payChapList", payChapList);
        return new ModelAndView("/pages/account");
    }

    /**
     * 充值记录
     *
     * @return
     * @throws cn.aiyuedu.bs.front.exception.IllegalUserException
     */
    @RequestMapping("rechList.do")
    public ModelAndView rechList(HttpServletRequest request,
                                @RequestParam(value = "id", required = true) Integer uid,
                                @RequestParam(value = "p", required = false, defaultValue = "1") Integer pageNum,
                                ModelMap model) throws IllegalUserException {
        model.put("uid", uid);
        List<RechargeVo> rechList = userAccountService.listRechargeVo(uid, pageNum, pageSize);
        model.put("rechList", rechList);
        model.put("tabName", "chargeList");
        model.put("tabIndex", 0);
        return new ModelAndView("/pages/rech_list");
    }

    @RequestMapping("rechListAjax.do")
    @ResponseBody
    public List<RechargeVo> rechListAjax(HttpServletRequest request,
                                      @RequestParam(value = "id", required = true) Integer uid,
                                      @RequestParam(value = "p", required = false, defaultValue = "1") Integer pageNum,
                                      ModelMap model) {
        log.info("rechListAjax.do");
        List<RechargeVo> rechList = userAccountService.listRechargeVo(uid, pageNum, pageSize);
        return rechList;
    }

    /**
     * 消费记录
     *
     * @return
     * @throws cn.aiyuedu.bs.front.exception.IllegalUserException
     */
    @RequestMapping("payList.do")
    public ModelAndView payList(HttpServletRequest request,
                               @RequestParam(value = "id", required = true) Integer uid,
                               @RequestParam(value = "p", required = false, defaultValue = "1") Integer pageNum,
                               ModelMap model) throws IllegalUserException {
        model.put("uid", uid);
        List<PayTotalVo> payList = userAccountService.listPay(uid, null, pageNum, pageSize);
        model.put("payList", payList);
        model.put("tabName", "consumeList");
        model.put("tabIndex", 1);
        return new ModelAndView("/pages/rech_list");
    }


    @RequestMapping("payListAjax.do")
    @ResponseBody
    public List<PayTotalVo> payListAjax(HttpServletRequest request,
                                       @RequestParam(value = "id", required = true) Integer uid,
                                       @RequestParam(value = "p", required = false, defaultValue = "1") Integer pageNum,
                                       ModelMap model) {
        log.info("payListAjax.do");
        List<PayTotalVo> payList = userAccountService.listPay(uid, null, pageNum, pageSize);
        return payList;
    }
}
