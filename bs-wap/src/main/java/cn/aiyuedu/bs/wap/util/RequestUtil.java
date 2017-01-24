package cn.aiyuedu.bs.wap.util;

import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * Description:
 *
 * @author yz.wu
 */
public class RequestUtil {

    public static String getFromHeader(HttpServletRequest request, String name, String defaultValue) {
        String value = request.getHeader(name);
        return StringUtils.isNotEmpty(value) ? value : defaultValue;
    }

    public static String getFromParam(HttpServletRequest request, String name, String defaultValue) {
        String value = request.getParameter(name);
        return StringUtils.isNotEmpty(value) ? value : defaultValue;
    }
    
    public static Integer getIntFromParam(HttpServletRequest request, String name, Integer defaultValue) {
        String value = request.getParameter(name);
        return StringUtils.isNotEmpty(value) && StringUtils.isNumeric(value) ? Integer.valueOf(value) : defaultValue;
    }

    public static Long getLongFromParam(HttpServletRequest request, String name, Long defaultValue) {
        String value = request.getParameter(name);
        return StringUtils.isNotEmpty(value) && StringUtils.isNumeric(value) ? Long.valueOf(value) : defaultValue;
    }

    public static String getFromAttribte(HttpServletRequest request, String name, String defaultValue) {
        Object o = request.getAttribute(name);
        return o != null ? o.toString() : defaultValue;
    }

    //可以为负值
    public static Integer getIntParam(HttpServletRequest request, String name, Integer defaultValue) {
        String value = request.getParameter(name);
        return StringUtils.isNotEmpty(value) ? Integer.valueOf(value) : defaultValue;
    }
}
