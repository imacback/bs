package cn.aiyuedu.bs.wap.interceptor;

import cn.aiyuedu.bs.wap.dto.CookieDto;
import cn.aiyuedu.bs.wap.dto.ParamDto;
import cn.aiyuedu.bs.wap.service.CookieService;
import cn.aiyuedu.bs.wap.service.ParamService;
import cn.aiyuedu.bs.wap.util.LogUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Properties;


public class SessionInterceptor extends HandlerInterceptorAdapter {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private Properties bsWapConfig;
    @Autowired
    private Properties bsBgConfig;
    @Autowired
    private CookieService cookieService;
    @Autowired
    private ParamService paramService;

    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {

        request.setAttribute("CTX_PATH", bsWapConfig.getProperty("url.prefix"));
        request.setAttribute("RES_PATH", bsWapConfig.getProperty("resource.path"));
        request.setAttribute("RES_VERSION", bsWapConfig.getProperty("resource.version"));

        request.setAttribute("DEFAULT_COVER", bsBgConfig.getProperty("noimg.large"));
        request.setAttribute("DEFAULT_SMALL_COVER", bsBgConfig.getProperty("noimg.small"));

        CookieDto c = cookieService.load(request, response);
        ParamDto p = paramService.load(c, request, response);

        paramService.store(request, p);

        return true;
    }

    public void postHandle(HttpServletRequest request,
                           HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {

        if (log.isDebugEnabled()) {
            log.debug("into postHandle **, " + request.getRequestURI());
        }

        LogUtil.statisHandler(request);
    }
}
