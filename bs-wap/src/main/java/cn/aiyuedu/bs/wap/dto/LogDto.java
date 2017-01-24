package cn.aiyuedu.bs.wap.dto;

/**
 * Description:
 *
 * @author yz.wu
 */
public class LogDto {

    private String distributeId;               // DIST_ID           渠道id
    private String platformId;                 // OPT_ID            平台id
    private String currentUrl;                 // URL               当前URL
    private String parentUrl;                  // REFERER           来源URL[可从refer获取]
    private String bookId;                     // BID               书籍id
    private String chapterId;                  // CID               章节id
    private String orderId;                    // CHAPTER_ORDER     章节序号
    private String tagId;                      // TAG_ID            标签id
    private String categoryId;                 // CATE_ID           分类id
    private String ip;                         // IP                ip地址
    private String actionType;				   // ACTION_TYPE	    点击类型（1:开关灯，2:换样式）
    private String pageType;				   // PAGE_TYPE			页面类型，1：书籍详情，1：章节列表
    private String pageNo;					   // PAGE_NO			页码
    private String pageId;                     // ID                页面id

    private String uid;                        // 用户uuid
    private String userId;                     // 用户id
    private String isRegister;                 // 是否注册用户，1是

    private String userAgent;                  // UA

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public String getDistributeId() {
        return distributeId;
    }

    public void setDistributeId(String distributeId) {
        this.distributeId = distributeId;
    }

    public String getPlatformId() {
        return platformId;
    }

    public void setPlatformId(String platformId) {
        this.platformId = platformId;
    }

    public String getCurrentUrl() {
        return currentUrl;
    }

    public void setCurrentUrl(String currentUrl) {
        this.currentUrl = currentUrl;
    }

    public String getParentUrl() {
        return parentUrl;
    }

    public void setParentUrl(String parentUrl) {
        this.parentUrl = parentUrl;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getTagId() {
        return tagId;
    }

    public void setTagId(String tagId) {
        this.tagId = tagId;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public String getPageType() {
        return pageType;
    }

    public void setPageType(String pageType) {
        this.pageType = pageType;
    }

    public String getPageNo() {
        return pageNo;
    }

    public void setPageNo(String pageNo) {
        this.pageNo = pageNo;
    }

    public String getPageId() {
        return pageId;
    }

    public void setPageId(String pageId) {
        this.pageId = pageId;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getIsRegister() {
        return isRegister;
    }

    public void setIsRegister(String isRegister) {
        this.isRegister = isRegister;
    }

    public String getChapterId() {
        return chapterId;
    }

    public void setChapterId(String chapterId) {
        this.chapterId = chapterId;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("[").append(distributeId)
                .append("] [").append(platformId)
                .append("] [").append(currentUrl)
                .append("] [").append(parentUrl)
                .append("] [").append(bookId)
                .append("] [").append(chapterId)
                .append("] [").append(orderId)
                .append("] [").append(tagId)
                .append("] [").append(categoryId)
                .append("] [").append(ip)
                .append("] [").append(actionType)
                .append("] [").append(pageType)
                .append("] [").append(pageNo)
                .append("] [").append(pageId)
                .append("] [").append(uid)
                .append("] [").append(userId)
                .append("] [").append(isRegister)
                .append("] [").append(userAgent)
                .append("]");
        return sb.toString();
    }
}
