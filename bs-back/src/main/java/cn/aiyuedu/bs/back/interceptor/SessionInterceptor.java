package cn.aiyuedu.bs.back.interceptor;

import cn.aiyuedu.bs.back.Constants;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Properties;


/**
 * Description:
 * @author yz.wu
 */
public class SessionInterceptor extends HandlerInterceptorAdapter {

    private final Logger logger = LoggerFactory.getLogger(SessionInterceptor.class);

    @Autowired
    private Properties sessionConfig;
    @Autowired
    private Properties bsBgConfig;

    @Override
    public boolean preHandle(
			HttpServletRequest request,
			HttpServletResponse response,
			Object handler) throws Exception {

        Object o = request.getSession().getAttribute(Constants.USER_SESSION_KEY);
        String mode = bsBgConfig.getProperty("mode");
        String referer = request.getHeader("referer");
        boolean validAccess = true;

        if (o == null) {
            validAccess = false;
        }
        if (StringUtils.equals("deploy", mode)) {
            if (!StringUtils.contains(referer, request.getServerName())) {
                validAccess = false;
            }
        }

        if (!validAccess) {
            if(request.getHeader("x-requested-with") != null &&
                    request.getHeader("x-requested-with").equalsIgnoreCase("XMLHttpRequest")){
                logger.debug("ajax request, session timeout");
                response.sendRedirect(sessionConfig.getProperty("url.timeout"));
            } else {
                response.sendRedirect(sessionConfig.getProperty("url.login.forward"));
            }
            return false;
        }

		return true;
	}
}
