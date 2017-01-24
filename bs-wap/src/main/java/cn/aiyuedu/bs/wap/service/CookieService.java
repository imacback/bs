package cn.aiyuedu.bs.wap.service;

import cn.aiyuedu.bs.common.util.IdUtil;
import cn.aiyuedu.bs.dao.entity.User;
import cn.aiyuedu.bs.wap.dto.CookieDto;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Properties;


/**
 * Description:
 * @author yz.wu
 */
/**
 * @author User
 *
 */
@Service("cookieService")
public class CookieService {
	
	private static final Logger log = LoggerFactory.getLogger(CookieService.class);
	
	@Autowired
	private Properties bsWapConfig;
	@Autowired
	private Properties bsBgConfig;
	@Autowired
	private UserService userService;
	
	public static final int maxAge = 30*24*60*60;//单位:秒
	public static final int distIdAge = 24*60*60;//单位:秒
	
	public CookieDto load(HttpServletRequest request, HttpServletResponse response) {
		Cookie[] cookies = request.getCookies();
		CookieDto c = new CookieDto();
		if (cookies != null && cookies.length > 0) {
			for (Cookie cookie : cookies) {
				if (StringUtils.equalsIgnoreCase(cookie.getName(),
						bsWapConfig.getProperty("cookie.isLight")) &&
						StringUtils.isNumeric(cookie.getValue())) {
					c.setIsLight(Integer.valueOf(cookie.getValue()));
					if (log.isDebugEnabled()) {
						log.debug("read cookie, isLight=" + cookie.getValue());
					}
				} else if (StringUtils.equalsIgnoreCase(cookie.getName(),
						bsWapConfig.getProperty("cookie.style")) &&
						StringUtils.isNotEmpty(cookie.getValue())) {
					c.setStyle(cookie.getValue());
					if (log.isDebugEnabled()) {
						log.debug("read cookie, style=" + cookie.getValue());
					}
				} else if (StringUtils.equalsIgnoreCase(cookie.getName(),
						bsWapConfig.getProperty("cookie.fontSize")) &&
						StringUtils.isNumeric(cookie.getValue())) {
					c.setFontSize(cookie.getValue());
					if (log.isDebugEnabled()) {
						log.debug("read cookie, fontSize=" + cookie.getValue());
					}
				} else if (StringUtils.equalsIgnoreCase(cookie.getName(),
						bsWapConfig.getProperty("cookie.spacing"))&&
						StringUtils.isNotEmpty(cookie.getValue())){
					c.setSpacing(cookie.getValue());
					if (log.isDebugEnabled()) {
						log.debug("read cookie, spacing=" + cookie.getValue());
					}
				} else if (StringUtils.equalsIgnoreCase(cookie.getName(),
						bsWapConfig.getProperty("cookie.uid")) &&
						StringUtils.isNotEmpty(cookie.getValue())){
					c.setUid(cookie.getValue());
					if (log.isDebugEnabled()) {
						log.debug("read cookie, uid=" + cookie.getValue());
					}
				} else if (StringUtils.endsWithIgnoreCase(cookie.getName(),
						bsWapConfig.getProperty("cookie.bookshelf")) &&
						StringUtils.isNotEmpty(cookie.getValue())){
					c.setBookshelf(cookie.getValue());
					if (log.isDebugEnabled()) {
						log.debug("read cookie, bookshelf=" + cookie.getValue());
					}
				}
			}
		}

		if (StringUtils.isBlank(c.getUid())) {
			c.setUid(IdUtil.uuid());
			setUid(response, c.getUid());
		}
		Object o = request.getSession().getAttribute("user");
		if (o == null) {
			User user = new User();
			user.setUid(c.getUid());
			user.setVirtualCorn(0);
			if (StringUtils.equals(bsBgConfig.getProperty("mode"), "develop")) {
				user.setVirtualCorn(1000);
			}

			request.getSession().setAttribute("user", user);
		}

		return c;
	}

	public void setIsLight(HttpServletResponse response, Integer isLight) {
		Cookie c = new Cookie(bsWapConfig.getProperty("cookie.isLight"), isLight.toString());
		c.setPath("/");
		c.setDomain(bsWapConfig.getProperty("cookie.domain"));
		c.setMaxAge(maxAge);
		response.addCookie(c);
	}

	public void setStyle(HttpServletResponse response, String style) {
		Cookie c = new Cookie(bsWapConfig.getProperty("cookie.style"), style);
		c.setPath("/");
		c.setDomain(bsWapConfig.getProperty("cookie.domain"));
		c.setMaxAge(maxAge);
		response.addCookie(c);
	}

	public void setFontSize(HttpServletResponse response, String fontSize) {
		Cookie c = new Cookie(bsWapConfig.getProperty("cookie.fontSize"), fontSize);
		c.setPath("/");
		c.setDomain(bsWapConfig.getProperty("cookie.domain"));
		c.setMaxAge(maxAge);
		response.addCookie(c);
	}

	public void setSpacing(HttpServletResponse response, String spacing) {
		Cookie c = new Cookie(bsWapConfig.getProperty("cookie.spacing"), spacing);
		c.setPath("/");
		c.setDomain(bsWapConfig.getProperty("cookie.domain"));
		c.setMaxAge(maxAge);
		response.addCookie(c);
	}

	public void setUid(HttpServletResponse response, String uid) {
		Cookie c = new Cookie(bsWapConfig.getProperty("cookie.uid"), uid);
		c.setPath("/");
		c.setDomain(bsWapConfig.getProperty("cookie.domain"));
		c.setMaxAge(maxAge);
		response.addCookie(c);
	}

	public void setBookShelf(HttpServletResponse response, String bookshelf) {
		Cookie c = new Cookie(bsWapConfig.getProperty("cookie.bookshelf"), bookshelf);
		c.setPath("/");
		c.setDomain(bsWapConfig.getProperty("cookie.domain"));
		c.setMaxAge(maxAge);
		response.addCookie(c);
	}
}
