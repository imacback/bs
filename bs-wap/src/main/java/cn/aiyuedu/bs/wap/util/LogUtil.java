package cn.aiyuedu.bs.wap.util;

import cn.aiyuedu.bs.wap.dto.LogDto;
import cn.aiyuedu.bs.wap.dto.ParamDto;
import com.duoqu.commons.utils.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;

/**
 * Description:
 *
 * @author yz.wu
 */
public class LogUtil {

    private static final Logger statis = LoggerFactory.getLogger("statis");

    public static void statisHandler(HttpServletRequest request) {
        String query = request.getQueryString();
        if (StringUtils.isNotEmpty(query)) {
            query = "?" + query;
        } else {
            query = "";
        }
        String url = request.getRequestURL().toString() + query;

        Object o = request.getAttribute("param");
        ParamDto p = null;
        if (o != null) {
            p = (ParamDto) o;
        }
        LogDto l = new LogDto();
        l.setDistributeId(p.getDistributeId() != null ? p.getDistributeId().toString() : "");
        l.setPlatformId(p.getPlatformId() != null ? p.getPlatformId().toString() : "");
        l.setCurrentUrl(url);
        l.setParentUrl(StringUtils.isNotBlank(p.getRefer()) ? p.getRefer() : "");
        l.setBookId(p.getBookId() != null && p.getBookId() > 0 ? p.getBookId().toString() : "");
        l.setChapterId(p.getChapterId() != null && p.getChapterId() > 0 ? p.getChapterId().toString() : "");
        l.setOrderId(p.getOrderId() != null ? p.getOrderId().toString() : "");
        l.setTagId(p.getTagId() != null && p.getTagId() > 0 ? p.getTagId().toString() : "");
        l.setCategoryId(p.getCategoryId() != null && p.getCategoryId() > 0 ? p.getCategoryId().toString() : "");
        l.setIp(com.duoqu.commons.web.utils.LogUtil.getIpAddr(request));
        l.setActionType(StringUtils.isNotBlank(p.getActionType()) ? p.getActionType() : "");
        l.setPageType(p.getPageType() != null ? p.getPageType().toString() : "");
        l.setPageNo(p.getPageNo() != null ? p.getPageNo().toString() : "");
        l.setPageId(p.getPageId() != null ? p.getPageId().toString() : "");
        l.setUid(p.getUid() != null ? p.getUid() : "");
        l.setUserId(p.getUserId() != null ? p.getUserId().toString() : "");
        l.setIsRegister(p.getIsRegister() != null ? p.getIsRegister() : "");
        l.setUserAgent(request.getHeader("User-Agent"));

        statis.info(l.toString());
    }
}
