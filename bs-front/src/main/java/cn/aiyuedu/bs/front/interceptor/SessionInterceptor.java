package cn.aiyuedu.bs.front.interceptor;

import cn.aiyuedu.bs.common.dto.ClientInfo;
import cn.aiyuedu.bs.front.service.log.LoggerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Properties;


public class SessionInterceptor extends HandlerInterceptorAdapter {
    private final Logger log = LoggerFactory.getLogger(getClass());
    //    public static String CTX_PATH = "";
    @Autowired
    private Properties frontConfig;
    @Autowired
    LoggerService loggerService;

    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {
        log.info("SessionInterceptor: " + request.getRequestURL());

        environment(request, response);
        versionParam(request);
        return true;
    }

    private void versionParam(HttpServletRequest request) {
        ClientInfo cli = (ClientInfo) request.getAttribute("xclient");
//        log.info("appVersion=" + appVersion);
        if (cli.getApp() != null) {
//            ClientInfo tmp = new ClientInfo();
//            tmp.setApp(appVersion);
            ClientInfo.ClientParam cliParam = cli.clientParam();
            request.setAttribute("platform", cliParam.getPlatform());
            request.setAttribute("version", cliParam.getVersion());
            request.setAttribute("channelId", cliParam.getChannelId());
        } else {
            String appVersion = request.getParameter("_c");
            cli.setApp(appVersion);
            ClientInfo.ClientParam cliParam = cli.clientParam();
            request.setAttribute("platform", cliParam.getPlatform());
            request.setAttribute("version", cliParam.getVersion());
            request.setAttribute("channelId", cliParam.getChannelId());
        }
        log.info(" appVersion=" + cli.getApp()+"  url="+request.getRequestURL()+"  ua="+request.getHeader("User-Agent"));
    }

    private void environment(HttpServletRequest request, HttpServletResponse response) {
        //        CTX_PATH = request.getContextPath();
        ClientInfo cli = loggerService.clientInfo(request, response);
        //        ClientInfo.ClientParam cliParam = cli.clientParam();
        String serverVersion = loggerService.requestVersion(request);
        String staticVersion = null;
        if (StringUtils.hasText(serverVersion)) {
            staticVersion = frontConfig.getProperty(serverVersion);
        } else {
            serverVersion = "";
            staticVersion = frontConfig.getProperty(frontConfig.getProperty("v_current"));
        }
        request.setAttribute("xclient", cli);
        request.setAttribute("RES_PATH", frontConfig.getProperty("resource.path") + staticVersion);
        request.setAttribute("RES_VERSION", frontConfig.getProperty("resource.version"));
        request.setAttribute("SERVER_VERSION", serverVersion);
        request.setAttribute("PRODUCT_NAME", frontConfig.getProperty("pay.productName"));
        String jsdebug = request.getParameter("jsdebug");
        if ("false".equals(jsdebug) || !StringUtils.hasText(jsdebug)) {
            request.setAttribute("RES_PARAM", ".min");
        }
    }
}
