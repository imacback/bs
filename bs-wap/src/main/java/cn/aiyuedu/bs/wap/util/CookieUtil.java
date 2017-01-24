package cn.aiyuedu.bs.wap.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Thinkpad on 2014/12/2.
 */
public class CookieUtil {
    private static final Logger logger = LoggerFactory.getLogger(CookieUtil.class);

    public static Cookie getCookie(HttpServletRequest request, String name) {
        Cookie cookies[] = request.getCookies();
        if (cookies == null || name == null || name.length() == 0) {
            return null;
        }
        for (Cookie cookie : cookies) {
            if (name.equals(cookie.getName())) {
                return cookie;
            }
        }
        return null;
    }

    public static String getCookinBookId(HttpServletRequest request, Long id, String name) {
        Cookie cookie = getCookie(request, name);
        if (cookie != null) {
            String info = cookie.getValue();
            return info.substring(info.indexOf("#") + 1);
        }
        return "0";
    }

    public static String getCookinByName(HttpServletRequest request, String name) {
        Cookie cookie = getCookie(request, name);
        if (cookie != null) {
            return cookie.getValue();
        }

        return "";
    }

    public static void deleteCookie(HttpServletRequest request,
                                    HttpServletResponse response, Cookie cookie) {
        if (cookie != null) {
            cookie.setPath(getPath(request));
            cookie.setValue("");
            cookie.setMaxAge(0);
            response.addCookie(cookie);
        }
    }


    public static void setCookie(HttpServletRequest request, HttpServletResponse response, Long bookId, Integer orderid, String name) {
        Cookie cookie = getCookie(request, name + bookId);
        String book = bookId + "#" + orderid;
        if (null == cookie) {
            cookie = new Cookie(name + bookId, book);
        } else {
            cookie = new Cookie(name + bookId, book);
        }
        cookie.setMaxAge(86400 * 30);
        response.addCookie(cookie);

    }

    public static void setCookie(HttpServletRequest request, HttpServletResponse response, String name, String vlue) {
        Cookie cookie = new Cookie(name, vlue);
        cookie.setMaxAge(86400 * 300);
        response.addCookie(cookie);
    }

    public static void setCookie(HttpServletRequest request, HttpServletResponse response, String vlue) {
        Cookie cookie = getCookie(request, "wapVersion");
        if (cookie != null) {
            cookie.setValue(vlue);
        } else {
            cookie = new Cookie("wapVersion", vlue);
        }
        cookie.setMaxAge(86400 * 300*10);
        response.addCookie(cookie);
    }


    private static String getPath(HttpServletRequest request) {
        String path = request.getContextPath();
        return (path == null || path.length() == 0) ? "/" : path;
    }


    public static String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("http_client_ip");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip != null && ip.indexOf(",") != -1) {
            ip = ip.substring(ip.lastIndexOf(",") + 1, ip.length()).trim();
        }
        return ip;
    }

}
