package cn.aiyuedu.bs.front.controller;

import cn.aiyuedu.bs.front.controller.resp.UserUpdateResp;
import com.duoqu.commons.web.spring.RequestAttribute;
import cn.aiyuedu.bs.common.dto.ClientInfo;
import cn.aiyuedu.bs.front.controller.form.UserForm;
import cn.aiyuedu.bs.front.controller.resp.AttendResp;
import cn.aiyuedu.bs.front.exception.IllegalUserException;
import cn.aiyuedu.bs.front.service.BookService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * Description:
 *
 * @author Scott
 */
@Controller
@RequestMapping("/**/ft/user/*")
public class UserController {
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private UserService userService;
    @Autowired
    private UserAttendService userAttendService;
    @Autowired
    private BookService bookService;

    @RequestMapping("show.do")
    public ModelAndView show(HttpServletRequest request,
                             @RequestParam(value = "id", required = true) Integer uid,
                              @RequestAttribute(value = "platform", required = false) Integer platform,
                             ModelMap model) throws IllegalUserException {
        User user = userService.get(uid);
        if (user == null) {
            throw new IllegalUserException();
        }
        ClientInfo cli = (ClientInfo) request.getAttribute("xclient");
        if (null != cli) {
            Integer platformId = cli.clientParam().getPlatform();
            String version = cli.clientParam().getVersion();
            Integer ditchId = cli.clientParam().getChannelId();
            model.put("platformId", platformId);
            model.put("version", version);
            model.put("ditchId", ditchId);
        }
        model.put("user", user);
        model.put("isAttend", userAttendService.isAttend(uid));
//        ClientInfo cli = (ClientInfo) request.getAttribute("xclient");
        model.put("rcmdList", bookService.rcmdList(platform));
        return new ModelAndView("/pages/user_show");
    }

    @RequestMapping("edit.do")
    public ModelAndView edit(HttpServletRequest request,
                             @RequestParam(value = "id", required = true) Integer uid,
                             ModelMap model) {
        User user = userService.get(uid);
        model.put("user", user);
        return new ModelAndView("/pages/user_edit");
    }

    @RequestMapping(value = "save.do", method = RequestMethod.POST)
    @ResponseBody
    public UserUpdateResp save(HttpServletRequest request,
                               @RequestBody UserForm userForm,
                               ModelMap model) {
        UserUpdateResp out = new UserUpdateResp();
        UserFull user = userService.getUserFull(userForm.getId());
        BeanUtils.copyProperties(userForm, user);
        UserService.Result result = userService.update(user);
        switch (result.getStatus()) {
            case UserService.USVC_DUPLICATE_KEY:
                out.setStatus(3);
                out.setMsg("昵称重复");
                break;
            case UserService.USVC_UPDATE_FAILED:
                out.setStatus(2);
                out.setMsg("更新失败");
                break;
        }
        out.setSuccess(true);
        return out;
    }


    @RequestMapping("doAttend.do")
    @ResponseBody
    public AttendResp doAttend(HttpServletRequest request,
                               @RequestParam(value = "id", required = true) Integer uid,
                               ModelMap model) {
        ClientInfo cli = (ClientInfo) request.getAttribute("xclient");
        User user = userAttendService.doAttend(cli);
        AttendResp out = new AttendResp();
        if (user == null) {
            out.setSuccess(false);
        } else {
            out.setSuccess(true);
            out.setExp(user.getBalance());
        }
        return out;
    }
}
