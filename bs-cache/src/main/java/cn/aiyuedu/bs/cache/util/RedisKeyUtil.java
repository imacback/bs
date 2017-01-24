package cn.aiyuedu.bs.cache.util;

import cn.aiyuedu.bs.common.Constants.RankingDateType;
import cn.aiyuedu.bs.common.Constants.RankingListType;

/**
 * Description:
 *
 * @author yz.wu
 */
public class RedisKeyUtil {

    public static final String HASH_BOOK = "HASH_BOOK";
    public static final String HASH_UPGRADE = "HASH_UPGRADE";
    public static final String HASH_TAG = "HASH_TAG";
    public static final String HASH_PROVIDER = "HASH_PROVIDER";
    public static final String HASH_COMPONENT = "HASH_COMPONENT";
    public static final String HASH_CONTAINER = "HASH_CONTAINER";
    public static final String LIST_CATEGORY = "LIST_CATEGORY";
    public static final String LIST_RANKING = "LIST_RANKING";
    public static final String HASH_DISTRIBUTE_LASTDATE = "HASH_DIST_DATE";

    //****消息配置***/

    public static final String LIST_PUSH = "LIST_PUSH";

    //****书城tab配置***/
    public static final String HASH_TAB = "HASH_CLI_TAB";
 
    //****书城资源配置***/

    public static final String LIST_CLIENT_SHELF = "LIST_CLIENT_SHELF";


    public static final String HASH_BOOK_PV_COUNTER = "HASH_BOOK_PV_COUNTER";
    //****书架广告***/
    public static final String HASH_CLIENT_ADVSHELF = "HASH_CLIENT_ADVSHELF";
    //****正文 章节广告***/
    public static final String HASH_CLIENT_ADVFIEX= "HASH_CLIENT_ADVFIEX";

    public static final String HASH_USERCOMMEND = "HASH_USERCOMMEND";
    //****平台 ***/
    public static final String HASH_RS_PLATFORM= "HASH_RS_PLATFORM";

    //启动入口配置
    public static final String HASH_CLIENT_ENTRY = "HASH_CLIENT_ENTRY";
    //书籍评论
    public static final String LIST_BOOK_COMMENT = "LIST_BOOK_COMMENT";
    public static String getBookCommentKey(Long bookId){
        return LIST_BOOK_COMMENT + "_" + bookId;
    }
    //启动页配置
    public static final String HASH_SPLASH = "HASH_SPLASH";
    public static final String HASH_SPLASH_IMAGE_URL = "HASH_SPLASH_IMAGE_URL";
    public static final String HASH_SPLASH_BEAN = "HASH_SPLASH_BEAN";
    public static final String getSplashKey(Integer splashId){
        return HASH_SPLASH_BEAN + "_" + splashId;
    }
    //支付开关及支付配置
    public static final String HASH_PAY_SWITCH = "HASH_PAY_SWITCH";

    public static final String HASH_WAP_BOOKLIST = "HASH_WAP_BOOKLIST";
    public static final String HASH_WAP_TIME = "HASH_WAP_TIME";

    public static final String SET_UPGRADE_KEY = "SET_UPGRADE_KEY";

    public static String getBookCategoryKey(Integer categoryId, Integer operatePlatformId) {
        return "RS_CA_" + categoryId + "_" + operatePlatformId;
    }

    public static String getBookCategoryZSetKey(Integer categoryId, Integer operatePlatformId) {
        return "ZSET_CA_" + categoryId + "_" + operatePlatformId;
    }

    public static String getBookChapterIdsZSetKey(Long bookId) {
        return "ZSET_BOOK_CHAPTERS_IDS_" + bookId;
    }

    public static String getBookChapterIdsByOrderIdHashKey(Long bookId) {
        return "HASH_BOOK_CHAPTERS_ORDER_" + bookId;
    }

    public static String getBookChaptersHashKey(Long bookId) {
        return "HASH_BOOK_CHAPTER_" + bookId;
    }

    public static String getPageViewHashKey(String counter) {
        return "HASH_BOOK_PV_" + counter;
    }

    public static String getRankingZsetKey(RankingListType rankingListType, RankingDateType rankingDateType) {
        return "ZSET_RANKING_" + rankingListType.getValue() + "_" + rankingDateType.getValue();
    }

    public static String advBookCountKey(String userId, String bookId) {
        return "RS_ADV_BOOK_COUNTER_" + userId + "_" + bookId;
    }

    public static String advUserCountKey(String bookId) {
        return "RS_ADV_USER_COUNTER_" + bookId;
    }
    public static String userKey(String userId) {
        return "RS_USER_INFO_" + userId;
    }

    public static String getUpgradeKey(Integer distributeId, Integer platformId) {
        return "ZSET_UPGRADE_" + distributeId + "_" + platformId;
    }
}
