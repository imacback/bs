package cn.aiyuedu.bs.wap.controller;

import cn.aiyuedu.bs.common.util.IdUtil;
import cn.aiyuedu.bs.dao.entity.User;
import cn.aiyuedu.bs.wap.dto.ConsumeResultDto;
import cn.aiyuedu.bs.wap.dto.ParamDto;
import cn.aiyuedu.bs.wap.dto.UserResultDto;
import cn.aiyuedu.bs.wap.service.BookshelfService;
import cn.aiyuedu.bs.wap.service.CookieService;
import cn.aiyuedu.bs.wap.service.RechargeService;
import cn.aiyuedu.bs.wap.service.UserService;
import com.duoqu.commons.web.spring.RequestAttribute;
import com.duoqu.commons.web.spring.SessionAttribute;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Properties;

/**
 * Description:
 *
 * @author yz.wu
 */
@Controller
@RequestMapping("/user/*")
public class UserController {

    @Autowired
    private BookshelfService bookshelfService;
    
	@Autowired
	private UserService userService;
	@Autowired
	private Properties bsWapConfig;
	@Autowired
	private RechargeService rechargeService;

	@RequestMapping("login.do")
	public String login(HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {

		return "/page/login.html";
	}

	@RequestMapping("loginSave.do")
	public String loginSave(
			HttpServletRequest request,
			HttpServletResponse response,
			@SessionAttribute(value = "user", required = true) User user,
			@RequestParam(value = "userName", required = false) String userName,
			@RequestParam(value = "password", required = false) String password,
			ModelMap model) {

		UserResultDto result = userService.login(userName, password);
		if (result.getSuccess()) {

			User u = result.getUser();

			// u.setUid(user.getUid());//
			if (StringUtils.isBlank(u.getUid()))
				u.setUid(IdUtil.uuid());

			// BUG FiXED by ZhengQian 20150908
			// 1)已登录用户重复登陆双倍加豆的 bug
			// 2）Cookie回写UUID机制
			//
			if (user.getId() == null) {
				// 识别为尚未登陆的用户 检查Session 临时用户信息及状态，如果有充值的情况，则修正数据
				int corn = 0;
				if (user.getVirtualCorn() != null) {
					corn = user.getVirtualCorn();
				}
				u.setVirtualCorn(u.getVirtualCorn() != null ? u
						.getVirtualCorn() + corn : corn);// 加入内存中的虚拟货币值

				u = userService.update(u);
				// TODO 此处应记录一下虚拟货币充值/变更记录

				if (StringUtils.isNotBlank(user.getUid()) && u.getId() != null
						&& u.getVirtualCorn() > 0) {
					rechargeService.updateUserId(user.getUid(), u.getId());// 非登录用户充值后首次登陆订单数据自动回写/关联充值数据
					// TODO 此处应记录一下关联日志

				}
			}
			// 如果UUID不一致的情况下 回写UUID写入Cookie
			if (!StringUtils.equals(u.getUid(), user.getUid())) {
				Cookie c = new Cookie(bsWapConfig.getProperty("cookie.uid"),
						u.getUid());
				c.setPath("/");
				c.setDomain(bsWapConfig.getProperty("cookie.domain"));
				c.setMaxAge(CookieService.maxAge);
				response.addCookie(c);
			}

			request.getSession().setAttribute("user", u);

			Object o = request.getSession().getAttribute("refer");
			if (o != null) {
				return "redirect:" + o.toString();
			} else {
				return "redirect:" + bsWapConfig.getProperty("home.url");
			}
		} else {
			model.put("msg", result.getInfo());
			return "/page/login.html";
		}
	}

	@RequestMapping("register.do")
	public String register(HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {

		return "/page/register.html";
	}

	@RequestMapping("registerSave.do")
	public String registerSave(
			HttpServletRequest request,
			HttpServletResponse response,
			@SessionAttribute(value = "user", required = true) User temp,
			@ModelAttribute("user") User user,
			@RequestParam(value = "confirmPassword", required = false) String confirmPassword,
			ModelMap model) {

		// FIXED 注册新用户的时候 分配一个新的UUID，防止一个Session下反复注册出现问题
		// by ZhengQian 20150909
		String tempUid = temp.getUid();//备份一下UUID 以便操作失败后还原数据
				
		temp.setUid(IdUtil.uuid());//TODO 可以优化一下代码 检查数据库中是否已经存在此UUID 如果没有就可以继续使用
		
		UserResultDto result = userService
				.register(temp, user, confirmPassword);

		if (result.getSuccess()) {

			User u = result.getUser();

			// 如果当前Session是非登录状态（防止数据复制），则尝试绑定原有订单
			if (temp.getId() == null) {
				if (StringUtils.isNotBlank(tempUid) && u.getId() != null
						&& u.getVirtualCorn() > 0) {
					rechargeService.updateUserId(tempUid, u.getId()); // 非登录用户充值后首次登陆订单数据自动回写/关联充值数据
				}
			}
			// 回写UUID写入Cookie
			if (!StringUtils.isNotBlank(u.getUid())) {
				Cookie c = new Cookie(bsWapConfig.getProperty("cookie.uid"),
						u.getUid());
				c.setPath("/");
				c.setDomain(bsWapConfig.getProperty("cookie.domain"));
				c.setMaxAge(CookieService.maxAge);
				response.addCookie(c);
			}
			request.getSession().setAttribute("user", u);

			Object o = request.getSession().getAttribute("refer");
			
			if (o != null) {
				return "redirect:" + o.toString();
			} else {
				return "redirect:" + bsWapConfig.getProperty("home.url");
			}

		} else {
			//操作失败 还原Session中的 UUID
			temp.setUid(tempUid);
			
			model.put("msg", result.getInfo());
			return "/page/register.html";
		}
	}

	@RequestMapping("alert.do")
	public String alert(HttpServletRequest request,
			HttpServletResponse response,
			@SessionAttribute(value = "user", required = true) User user,
			@RequestAttribute(value = "param", required = true) ParamDto param,
			ModelMap model) {

		return "/page/loginAlert.html";
	}

	@RequestMapping("info.do")
	public String info(HttpServletRequest request,
			HttpServletResponse response,
			@SessionAttribute(value = "user", required = true) User user,
			@RequestAttribute(value = "param", required = true) ParamDto param,
			ModelMap model) {

		model.put("user", user);
		
		//加载书架信息
        if (param != null && CollectionUtils.isNotEmpty(param.getBookshelfDtos())) {
            model.put("bookshelfDtos", bookshelfService.getBooks(param));
        }
		return "/page/userCenter.html";
	}

	@RequestMapping("updateName.do")
	@ResponseBody
	public String updateName(
			HttpServletRequest request,
			@RequestParam(value = "nickname", required = false) String nickname,
			@SessionAttribute(value = "user", required = false) User user) {

		if (StringUtils.isNotBlank(nickname)) {
			user = userService.updateName(user.getId(), nickname);
			request.getSession().setAttribute("user", user);
		}
		ConsumeResultDto result = new ConsumeResultDto();

		result.setResult("success");

		return "success";
	}

	@RequestMapping("logout.do")
	public String logout(
			HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "out_trade_no", required = false) String outTradeNo,
			@SessionAttribute(value = "user", required = true) User user,
			ModelMap model) {

		request.getSession().removeAttribute("user");

		return "redirect:" + bsWapConfig.getProperty("home.url");
	}

	@RequestMapping("goBack.do")
	public String goBack(HttpServletRequest request,
			@SessionAttribute(value = "user", required = true) User user,
			ModelMap model) {

		if (user != null) {
			user = userService.findOne(user.getId(), user.getUid());
			request.getSession().setAttribute("user", user);
		}

		Object o = request.getSession().getAttribute("refer");
		if (o != null) {
			return "redirect:" + o.toString();
		} else {
			return "redirect:" + bsWapConfig.getProperty("home.url");
		}
	}
}
