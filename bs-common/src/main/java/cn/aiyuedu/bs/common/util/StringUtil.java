package cn.aiyuedu.bs.common.util;

import cn.aiyuedu.bs.common.Constants;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Description:
 *
 * @author yz.wu
 */
public class StringUtil {

    public static final String REGEX_FONT = "<\\/?(?:font|span)[^>]*>";
    public static final String REGEX_EXTRA_CHAR = "(.)\\1{29,}";

    private static Pattern fontPattern = Pattern.compile(REGEX_FONT, Pattern.CASE_INSENSITIVE);
    private static Pattern extraCharPattern = Pattern.compile(REGEX_EXTRA_CHAR, Pattern.CASE_INSENSITIVE);

    public static String removeHtmlFontTag(String html) {
        Matcher matcher = fontPattern.matcher(html);
        return matcher.replaceAll("");
    }

    public static String removeLastBrTag(String html) {
        if (StringUtils.isNotBlank(html)) {
            html = StringUtils.replace(html, "<BR", "<br");
            return StringUtils.removeEnd(StringUtils.removeEnd(html, "<br>"), "<br/>");
        }

        return html;
    }

    public static boolean hasExtraChar(String value) {
        if (StringUtils.isNotEmpty(value)) {
            Matcher matcher = extraCharPattern.matcher(value);
            return matcher.find();
        }

        return false;
    }

    public static String removeExtraChar(String value) {
        if (StringUtils.isNotEmpty(value)) {
            return value.replaceAll(REGEX_EXTRA_CHAR, "");
        }

        return null;
    }

    public static void main(String[] args) {
        String html = "这是<br>个<font color=\"red\">gao</font>代码<br>";
        String s = "aaaaabaaadddddbrrtrrrrrr";
        //System.out.println(hasExtraChar(s));
        //System.out.println(removeExtraChar(s));
        //System.out.println(removeLastBrTag(html));
    }

    public static String fmtLog(Object obj, String defaultStr) {
        if (obj == null || !org.springframework.util.StringUtils.hasText(obj.toString()))
            return defaultStr;
        else
            return obj.toString();
    }

    public static String join(List<Object> list) {
        return Joiner.on(Constants.SEPARATOR).join(list);
    }

    public static Iterable<String> split(String ids) {
        return Splitter.on(Constants.SEPARATOR).omitEmptyStrings().trimResults().split(ids);
    }

    public static Iterable<String> split(String ids, String separator) {
        return Splitter.on(separator).omitEmptyStrings().trimResults().split(ids);
    }

    public static List<Integer> split2Int(String ids) {
        Iterable<String> list = split(ids);
        List<Integer> result = Lists.newArrayList();
        for (String id : list) {
            if (org.apache.commons.lang3.StringUtils.isNumeric(id)) {
                result.add(Integer.valueOf(id));
            }
        }
        return result;
    }

    public static List<Long> split2Long(String ids) {
        Iterable<String> list = split(ids);
        List<Long> result = Lists.newArrayList();
        for (String id : list) {
            if (org.apache.commons.lang3.StringUtils.isNumeric(id)) {
                result.add(Long.valueOf(id));
            }
        }
        return result;
    }

    public static List<Long> split2Long(String ids , String split){
        Iterable<String> list =  Splitter.on(split).omitEmptyStrings().trimResults().split(ids);
        List<Long> result = Lists.newArrayList();
        for (String id : list) {
            if (org.apache.commons.lang3.StringUtils.isNumeric(id)) {
                result.add(Long.valueOf(id));
            }
        }
        return result;
    }

}
