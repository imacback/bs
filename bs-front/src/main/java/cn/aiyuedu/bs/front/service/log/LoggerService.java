package cn.aiyuedu.bs.front.service.log;

import com.alibaba.fastjson.JSON;
import com.duoqu.commons.web.utils.LogUtil;
import cn.aiyuedu.bs.common.dto.ClientInfo;
import cn.aiyuedu.bs.common.util.StringUtil;
import cn.aiyuedu.bs.front.pojo.StaticLog;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 记录统计日志
 *
 * @author Scott
 */
@Service
public class LoggerService {
    private final Logger statis = LoggerFactory.getLogger("static");
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private Properties frontConfig;
    @Autowired
    private Properties operMappingConfig;
    @Autowired
    private Properties lvlMappingConfig;

    //	@Autowired
//	private DeviceService deviceService;
//	@Autowired
//	private ProfileService profileService;
//    public static final int maxAge = 30 * 60;

    public ClientInfo clientInfo(HttpServletRequest request, HttpServletResponse response) {
        //////////////////////
        boolean hasCookie = false;
        Cookie[] cookies = request.getCookies();
        String requestUrl = requestUrl(request);
        log.info("cookie_arr: [" + requestUrl + "]" + cookies);
        if (cookies != null) {
            for (Cookie c : cookies) {
                log.info("cookie: [" + requestUrl + "]" + c.getName() + "##" + c.getValue() + "##" + c.getDomain());
                if (("sclient".equalsIgnoreCase(c.getName()))
                        || ("xclient".equalsIgnoreCase(c.getName())))
                    hasCookie = true;
            }
        }
        ClientInfo cli = hearderClientInfo(request, response);
        log.info("client watch: cookie=" + hasCookie + ",header=" + (cli != null));
        if (cli != null) {
            return cli;
        }
        //客户端写的cookie
        cli = cookieClientInfo(request, response, "xclient");
        if (cli != null)
            return cli;
        //服务端写的cookie
        cli = cookieClientInfo(request, response, "sclient");
        if (cli != null)
            return cli;
        else
            cli = new ClientInfo();
        return cli;
    }

    public ClientInfo hearderClientInfo(HttpServletRequest request, HttpServletResponse response) {
        String clientJson = request.getHeader("xclient");
        log.info("hearderClientInfo: " + clientJson);

        if (StringUtils.hasText(clientJson)) {
            writeCookie(request, response, "sclient", clientJson);
            return JSON.parseObject(clientJson, ClientInfo.class);
        } else {
            return null;
        }
    }

    private void writeCookie(HttpServletRequest request, HttpServletResponse response, String cookieName, String clientJson) {
        //1.0.3
        try {
            URL url = new URL(request.getRequestURL().toString());
            log.info("writeCookie: host=" + url.getHost());
            Cookie clientCookie = new Cookie(cookieName, new String(Base64.encodeBase64(clientJson.getBytes())));
            clientCookie.setPath("/");
            clientCookie.setDomain(url.getHost());
            clientCookie.setMaxAge(60 * 60 * 24 * 365);
            response.addCookie(clientCookie);
        } catch (MalformedURLException e) {
            log.error("MalformedURLException", e);
        }
    }

    public ClientInfo cookieClientInfo(HttpServletRequest request, HttpServletResponse response, String cookieName) {

        Cookie[] cookies = request.getCookies();
        ClientInfo out = null;

        if (cookies != null) {
            for (Cookie c : cookies) {
                if (!cookieName.equalsIgnoreCase(c.getName()))
                    continue;
                String clientJson = new String(Base64.decodeBase64(c.getValue()));
                writeCookie(request, response, "sclient", clientJson);
                log.info("cookieClientInfo: " + clientJson);
                out = JSON.parseObject(clientJson, ClientInfo.class);
                return out;
            }
        }
        return null;
    }

    private String requestUrl(HttpServletRequest request) {
        String query = request.getQueryString();
        if (StringUtils.hasText(query)) {
            query = "?" + query;
        } else {
            query = "";
        }
        return request.getRequestURL().toString() + query;
    }


    public void writeLog(HttpServletRequest request) {
        ClientInfo cli = (ClientInfo) request.getAttribute("xclient");

        String requestUrl = requestUrl(request);
        StaticLog logObj = new StaticLog();
        ClientInfo.ClientParam param = cli.clientParam();
        logObj.setPlatform(StringUtil.fmtLog(param.getPlatform(), ""));
        logObj.setVersion(StringUtil.fmtLog(param.getVersion(), ""));
        logObj.setChannelId(StringUtil.fmtLog(param.getChannelId(), ""));
        logObj.setUid(StringUtil.fmtLog(cli.getUid(), ""));
        logObj.setModel(StringUtil.fmtLog(cli.getModel(), ""));
        logObj.setOs(StringUtil.fmtLog(cli.getOs(), ""));
        logObj.setWidth(StringUtil.fmtLog(cli.getWidth(), ""));
        logObj.setHeight(StringUtil.fmtLog(cli.getHeight(), ""));
        logObj.setImei(StringUtil.fmtLog(cli.getImei(), ""));
        logObj.setImsi(StringUtil.fmtLog(cli.getImsi(), ""));
        writeIp(cli,logObj,request);
        logObj.setRefer(StringUtil.fmtLog(request.getHeader("Referer"), ""));
        logObj.setCurrUrl(StringUtil.fmtLog(requestUrl, ""));
        logObj.setOperation(StringUtil.fmtLog(operMapping(request), ""));
        logObj.setObjId(StringUtil.fmtLog(objId(request), ""));
        logObj.setGotoUrl(StringUtil.fmtLog(request.getParameter("link"), ""));
        logObj.setLevel(StringUtil.fmtLog(lvl(request), ""));
        logObj.setSourcePath(StringUtil.fmtLog(sourcePath(request), ""));
        logObj.setServerVersion(StringUtil.fmtLog(requestVersion(request), ""));
        statis.info(logObj.toString());
    }

    private void writeIp(ClientInfo cli, StaticLog logObj, HttpServletRequest request) {
//        if (StringUtils.hasText(cli.getIp())) {
//            logObj.setIp(StringUtil.fmtLog(cli.getIp(), ""));
//            return;
//        }
        logObj.setIp(StringUtil.fmtLog(LogUtil.getIpAddr(request), ""));
    }
    private Object lvl(HttpServletRequest request) {
        String current = request.getRequestURI();
        current = current.replaceAll(".*/ft/", "/");
        return lvlMappingConfig.getProperty(current);
    }

    private Object sourcePath(HttpServletRequest request) {
        String s = request.getRequestURI();
        return s.replaceAll("/ft/.*", "");
    }

    private String objId(HttpServletRequest request) {
        return request.getParameter("id");
    }

    private String operMapping(HttpServletRequest request) {
        String current = request.getRequestURI();
        current = current.replaceAll(".*/ft/", "/");
        return operMappingConfig.getProperty(current);
    }

    public String requestVersion(HttpServletRequest request) {
        String out = regexGet(request.getRequestURI(), "v_[0-9\\.]+", 0);
        return out;
    }

    private static String regexGet(String str, String reg, int group) {
        Matcher m = Pattern.compile(reg).matcher(str);
        if (m.find()) {
//            System.out.println(m.groupCount());
            if (group > m.groupCount())
                return null;
            else
                return m.group(group);
        } else {
            return null;
        }
    }

    public static void main(String[] args) {
//        System.out.println(new String(Base64.decodeBase64("eyJ1aWQiOjEwMDEsInNpdGUiOiIxIiwiYXBwIjoiMS4xLjAwLjEiLCJvcyI6IkFuZHJvaWQiLCJtb2RlbCI6IkgzMC1UMTAiLCJyb290IjoiXC9zdG9yYWdlXC9lbXVsYXRlZFwvMFwvZHVvcXV5dWVkdVwvYm9va3MiLCJ3aWR0aCI6NzIwLCJpbWVpIjoiODYzNjU0MDI5NzIxMTQ5Iiwic2Vzc2lvbmlkIjoiNDJkNjVjZDAtNzkxMS00MzQzLWI5NDItNTRiMGM1MmRhNjUxIiwiY2lkIjoiZGQyZjM5YzIwODU5YTAyMzMyYWQ3NTJiYWNmMjU0NWUiLCJvc192ZXJzaW9uIjoiNC4yLjIiLCJoZWlnaHQiOjEyODAsIm9zX2lkIjoiZjA3ZWQ4M2NhYjQ1ZWFlOCIsImJyYW5kIjoiSHVhd2VpIiwiZHBpIjozMjAsImltc2kiOiI0NjAwMjAzOTc1MDMxODciLCJjaGFubmVsIjoxfQ")));
//        String json = "{\"uid\":1022,\"site\":\"1\",\"app\":\"1.1.00.1\",\"os\":\"Android\",\"model\":\"U9200\",\"root\":\"\\/mnt\\/sdcard\\/duoquyuedu\\/books\",\"width\":540,\"imei\":\"863101010285783\",\"sessionid\":\"6012bd3b-a800-45f5-be24-f92071d34688\",\"cid\":\"ee0dbc6eaf28d05ff824e1fcd8c08d61\",\"os_version\":\"4.0.3\",\"height\":960,\"os_id\":\"5a94d0cbf96bc8fa\",\"brand\":\"Huawei\",\"dpi\":240,\"imsi\":\"460001331849607\",\"channel\":1}";
//        System.out.println(JSON.parseObject(json));

//        System.out.println(new LoggerService().requestVersion("http://localhost:8080/v_1.1/ft/user/show.do?id=3"));
    }
}
