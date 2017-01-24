package cn.aiyuedu.bs.wap.service;

import cn.aiyuedu.bs.dao.entity.User;
import cn.aiyuedu.bs.wap.Constants;
import cn.aiyuedu.bs.wap.dto.BookshelfDto;
import cn.aiyuedu.bs.wap.dto.CookieDto;
import cn.aiyuedu.bs.wap.dto.ParamDto;
import cn.aiyuedu.bs.wap.util.CookieUtil;
import cn.aiyuedu.bs.wap.util.RequestUtil;
import cn.aiyuedu.bs.wap.util.StringUtil;
import com.alibaba.fastjson.JSON;
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
 *
 * @author yz.wu
 */
@Service("paramService")
public class ParamService {

	private static final Logger log = LoggerFactory
			.getLogger(ParamService.class);

	@Autowired
	private Properties bsWapConfig;
	@Autowired
	private CookieService cookieService;

	public void store(HttpServletRequest request, ParamDto paramDto) {
		request.setAttribute("param", paramDto);
	}

	public ParamDto getParam(HttpServletRequest reques) {
		Object o = reques.getAttribute("param");
		if (o != null) {
			return (ParamDto) o;
		}

		return null;
	}

	public ParamDto load(CookieDto c, HttpServletRequest request,
			HttpServletResponse response) {
		ParamDto p = new ParamDto();

		// cookie变量处理
		if (c != null) {
			if (c.getIsLight() != null) {
				p.setIsLight(c.getIsLight());
			}
			if (StringUtils.isNotBlank(c.getStyle())) {
				p.setStyle(c.getStyle());
			}
			if (StringUtils.isNotBlank(c.getFontSize())) {
				p.setFontSize(c.getFontSize());
			}
			if (StringUtils.isNotBlank(c.getSpacing())) {
				p.setSpacing(c.getSpacing());
			}
			if (StringUtils.isNotBlank(c.getUid())) {
				p.setUid(c.getUid());
			}
			if (StringUtils.isNotBlank(c.getBookshelf())) {
				p.setBookshelf(c.getBookshelf());
				p.setBookshelfDtos(JSON.parseArray(p.getBookshelf(),
						BookshelfDto.class));
			}
		}

		// 默认处理
		if (p.getIsLight() == null) {
			p.setIsLight(0);
		}
		if (StringUtils.isNotBlank(p.getFontSize())) {
			p.setFontSize("");
		}
		if (StringUtils.isNotBlank(p.getStyle())) {
			p.setStyle("");
		}
		if (StringUtils.isNotBlank(p.getSpacing())) {
			p.setSpacing("");
		}

		// 原始渠道标识代码
		/*
		 * p.setDistributeId(RequestUtil.getIntFromParam(request,
		 * bsWapConfig.getProperty("param.distId"), null)); if
		 * (p.getDistributeId() == null) { Object o =
		 * request.getSession().getAttribute("distId"); if (o != null) {
		 * p.setDistributeId(Integer.valueOf(o.toString())); } } else {
		 * request.getSession().setAttribute("distId", p.getDistributeId()); }
		 */
		// 写入渠道ID (兼容原始代码) By ZhengQian 20150908
		if (request.getSession().getAttribute("distId") == null) {
			// 新Session
			// 检查Cookie
			Cookie cookie = CookieUtil.getCookie(request, "distId");

			// 检查参数是否带有渠道代码
			Integer distId = RequestUtil.getIntFromParam(request,
					bsWapConfig.getProperty("param.distId"), 0);
			
			if (cookie == null) { 
				
				// Cookie 中没有渠道信息，将该用户识别为渠道用户
				// 渠道代码写入 Cookie
				cookie = new Cookie("distId", Integer.toString(distId));
				cookie.setPath("/");
				cookie.setDomain(bsWapConfig.getProperty("cookie.domain"));
				cookie.setMaxAge(60 * 60 * 24 * 30);
				response.addCookie(cookie);

			}

			distId = Integer.parseInt(cookie.getValue());
			
			// 将Cookie读取的渠道ID 写入Session
			request.getSession().setAttribute("distId", distId);

		}

		p.setDistributeId((Integer) request.getSession().getAttribute("distId"));

		p.setPlatformId(Constants.PLATFORM_ID);

		// 书籍id
		p.setBookId(RequestUtil.getLongFromParam(request,
				bsWapConfig.getProperty("param.bookId"), 0l));
		if (p.getBookId() == null || p.getBookId() == 0) {
			String queryString = request.getQueryString();
			if (StringUtils.isNotBlank(queryString)
					&& queryString.indexOf("bid=") > -1) {
				String bid = StringUtil.getBookId(queryString);
				if (StringUtils.isNotBlank(bid) && StringUtils.isNumeric(bid)) {
					p.setBookId(Long.valueOf(bid));
				}
			}
		}
		// 章节id
		p.setChapterId(RequestUtil.getLongFromParam(request,
				bsWapConfig.getProperty("param.chapterId"), 0l));
		// 页码
		p.setPageNo(RequestUtil.getIntFromParam(request,
				bsWapConfig.getProperty("param.pageNo"), 0));
		// 操作类型
		p.setActionType(RequestUtil.getFromParam(request,
				bsWapConfig.getProperty("param.actionType"), ""));

		// 开关灯设置
		p.setIsLight(RequestUtil.getIntFromParam(request,
				bsWapConfig.getProperty("cookie.isLight"), 0));
		// 颜色
		p.setStyle(RequestUtil.getFromParam(request,
				bsWapConfig.getProperty("cookie.style"), ""));
		// 行间距
		p.setSpacing(RequestUtil.getFromParam(request,
				bsWapConfig.getProperty("cookie.spacing"), ""));
		// 字体大小
		p.setFontSize(RequestUtil.getFromParam(request,
				bsWapConfig.getProperty("cookie.fontSize"), ""));
		p.setOrderId(RequestUtil.getIntFromParam(request,
				bsWapConfig.getProperty("param.orderId"), 0));
		p.setIsNext(RequestUtil.getIntParam(request,
				bsWapConfig.getProperty("param.isNext"), 0));
		p.setStart(RequestUtil.getIntFromParam(request,
				bsWapConfig.getProperty("param.start"), 0));
		p.setPageSize(RequestUtil.getIntFromParam(request,
				bsWapConfig.getProperty("param.pageSize"), 20));
		p.setMark(RequestUtil.getIntFromParam(request,
				bsWapConfig.getProperty("param.mark"), 0));
		p.setMaxChapter(RequestUtil.getIntFromParam(request,
				bsWapConfig.getProperty("param.maxChaper"), 0));

		p.setTagId(RequestUtil.getIntFromParam(request,
				bsWapConfig.getProperty("param.tagId"), 0));
		p.setTagType(RequestUtil.getIntFromParam(request,
				bsWapConfig.getProperty("param.tagType"), 0));
		p.setName(RequestUtil.getFromParam(request,
				bsWapConfig.getProperty("param.name"), ""));
		
		p.setBookCount(RequestUtil.getIntFromParam(request,
				bsWapConfig.getProperty("param.bookCount"), 0));
		p.setCategoryId(RequestUtil.getIntFromParam(request,
				bsWapConfig.getProperty("param.categoryId"), 0));
		// 上下文路径
		p.setCtxPath(request.getContextPath());
		// 资源路径
		p.setResPath(bsWapConfig.getProperty("resource.url"));
		if (StringUtils.isBlank(p.getResPath())) {
			p.setResPath(p.getCtxPath());
		}

		// 来源url
		p.setRefer(RequestUtil.getFromHeader(request, "Referer", ""));

		Object o = request.getSession().getAttribute("user");
		if (o != null) {
			User user = (User) o;
			if (user.getId() != null) {
				p.setUserId(user.getId());
			}
		}

		return p;
	}
}
