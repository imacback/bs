package cn.aiyuedu.bs.wap.util;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Thinkpad on 2014/12/5.
 */
public class StringUtil {

    private static Pattern bookIdPattern = Pattern.compile("bid=([\\d]+)");

    public static String replaceAll(String str) {
        String dest = "<p>" + str.trim().replaceAll("　", " ").replaceAll("\n", "</p><p>");
        return dest.substring(0, dest.lastIndexOf("<p>") - 1);
    }

    public static String getContent(String str) {
        return str.replaceAll("\n", "<br>");
    }

    public static String getBookId(String query) {
        if (StringUtils.isNotBlank(query)) {
            Matcher matcher = bookIdPattern.matcher(query);
            if (matcher.find()) {
                return matcher.group(1);
            }
        }

        return null;
    }

}
