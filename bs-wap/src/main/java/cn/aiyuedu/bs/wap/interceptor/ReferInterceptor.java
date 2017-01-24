package cn.aiyuedu.bs.wap.interceptor;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Description:
 *
 * @author yz.wu
 */
public class ReferInterceptor extends HandlerInterceptorAdapter {

    private final Logger log = LoggerFactory.getLogger(getClass());

    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {

        String query = request.getQueryString();
        if (StringUtils.isNotEmpty(query)) {
            query = "?" + query;
        } else {
            query = "";
        }
        String url = request.getRequestURL().toString() + query;
        if (StringUtils.isNotBlank(url)) {
            request.getSession().setAttribute("refer", url);
        }

        if (log.isDebugEnabled()) {
            log.debug("refer, " + url);
        }

        return true;
    }
}
