package cn.aiyuedu.statistics.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.aiyuedu.bs.dao.entity.User;
import cn.aiyuedu.bs.wap.dto.LogDto;

/**
 * 
 * @author QianZheng
 * 
 */
@WebServlet(name = "StatisticsServlet", loadOnStartup = 1, urlPatterns = { "/servlet/pxImg" })
public class StatisticsServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7414903515873293969L;
	private static final Logger statis = LoggerFactory.getLogger("imgstatis");

	/**
	 * Constructor of the object.
	 */
	public StatisticsServlet() {
		super();
	}

	/**
	 * The doGet method of the servlet. <br>
	 * <p/>
	 * This method is called when a form has its tag value method equals to get.
	 *
	 * @param request
	 *            the request send by the client to the server
	 * @param response
	 *            the response send by the server to the client
	 * @throws ServletException
	 *             if an error occurred
	 * @throws IOException
	 *             if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doProcess(request, response);

	}

	private void doProcess(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {

		Cookie cookie[] = request.getCookies();
		String uid = null;
		String distId = null;
		if (cookie != null && cookie.length > 0) {
			for (Cookie c : cookie) {
				switch (c.getName()) {
				case "uid":
					uid = c.getValue();
					break;

				case "distId":
					distId = c.getValue();
					break;
				}

			}
		}

		LogDto l = new LogDto();
		l.setPlatformId("");

		l.setParentUrl("");
		l.setBookId("");
		l.setChapterId("");
		l.setOrderId("");
		l.setTagId("");
		l.setCategoryId("");

		l.setActionType("");
		l.setPageType("");
		l.setPageNo("");
		l.setPageId("");
		l.setUid("");
		l.setUserId("");
		l.setIsRegister("");

		l.setDistributeId(distId);

		l.setCurrentUrl(request.getHeader("referer"));

		l.setIp(com.duoqu.commons.web.utils.LogUtil.getIpAddr(request));

		User user = (User) request.getSession().getAttribute("user");
		if (user != null) {
			l.setUid(user.getUid());
			l.setUserId(user.getId() == null ? "" : user.getId().toString());
		}

		l.setUserAgent(request.getHeader("User-Agent"));

		statis.info(l.toString());

		response.setDateHeader("Expires", 0);
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Prama", "no-cache");
		request.getRequestDispatcher("/img/px.png").forward(request, response);
	}

	/**
	 * The doPost method of the servlet. <br>
	 * <p/>
	 * This method is called when a form has its tag value method equals to
	 * post.
	 *
	 * @param request
	 *            the request send by the client to the server
	 * @param response
	 *            the response send by the server to the client
	 * @throws ServletException
	 *             if an error occurred
	 * @throws IOException
	 *             if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doProcess(request, response);
	}

}
