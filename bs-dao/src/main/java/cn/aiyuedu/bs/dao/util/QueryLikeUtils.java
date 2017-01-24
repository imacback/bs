package cn.aiyuedu.bs.dao.util;

import org.apache.commons.lang3.StringUtils;

/**
 * Description:
 *
 * @author yz.wu
 */
public final class QueryLikeUtils {

    private static final String DOT_MATCH_STR           = ".";

    private static final String ASTERISKS_MATCH_STR     = "*";

    private static final String ESCAPE_STR              = "\\";

    private QueryLikeUtils() {
    }

    public static String normalizeKeyword(String keyword) {

        //空格
        keyword = StringUtils.trim(keyword);
        keyword = StringUtils.replace(keyword, DOT_MATCH_STR, ESCAPE_STR + DOT_MATCH_STR);
        keyword = StringUtils.replace(keyword, ASTERISKS_MATCH_STR, ESCAPE_STR + ASTERISKS_MATCH_STR);

        if (StringUtils.isBlank(keyword)) {
            return null;
        }

        return keyword;
    }

    public static String notStartWith(String keyword) {
        return "^(?!"+keyword+").*$";
    }
}
