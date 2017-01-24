package cn.aiyuedu.bs.wap.interceptor;

import cn.aiyuedu.bs.dao.entity.User;
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
 *
 * @author yz.wu
 */
public class UserInterceptor extends HandlerInterceptorAdapter {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private Properties bsWapConfig;

    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {

        Object o = request.getSession().getAttribute("user");
        boolean isRegister = false;
        if (o != null) {
            User u = (User) o;
            if (u.getId() != null) {
                isRegister = true;
            }
        }

        if (!isRegister) {
            response.sendRedirect(bsWapConfig.getProperty("userAlert.url"));
            return false;
        }

        return true;
    }
}
