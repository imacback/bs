package cn.aiyuedu.bs.back.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Properties;

/**
 * Description:
 *
 * @author yz.wu
 */
public class CommonInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private Properties bsBgConfig;

    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler) throws Exception {

        request.getSession().setAttribute("imgUrl", bsBgConfig.getProperty("url.image"));
        request.getSession().setAttribute("htmlUrl", bsBgConfig.getProperty("url.html"));
        request.getSession().setAttribute("frontUrl", bsBgConfig.getProperty("front.preview"));
        request.getSession().setAttribute("noimgSmall", bsBgConfig.getProperty("noimg.small"));
        request.getSession().setAttribute("mode", bsBgConfig.getProperty("mode"));

        return true;
    }
}
