package cn.aiyuedu.bs.wap.dto;

import java.io.Serializable;
import java.util.List;

/**
 * Description:
 *
 * @author yz.wu
 */
public class ParamDto implements Serializable {

    private static final long serialVersionUID = 1389304497587540312L;

    private Integer distributeId;
    private Integer platformId;
    private Long bookId;
    private Long chapterId;
    private String actionType;                // 操作类型: epost,comment,fav, url参数：at
    private Integer pageType;                // 页面类型	0：首页， 1：列表， 2：详情
    private Integer pageId;
    private Integer pageNo;                    // 页码， url参数：p
    private String searchWord;                // 用户输入搜索词
    private String refer;
    private String resPath;                    // 资源访问路径
    private String ctxPath;                    // context path

    private Integer isLight;
    private String style;
    private String fontSize;
    private String spacing; //行间距

    private Integer operation;
    private Integer orderId;
    private Integer isNext;
    private Integer mark;
    private Integer start;
    private Integer pageSize;
    private Integer maxChapter;
    private String name;
    private Integer bookCount;
    private Integer tagId;
    private Integer tagType;
    private Integer categoryId;

    private String uid;
    private Integer userId;
    private String bookshelf;
    private String isRegister;

    private List<BookshelfDto> bookshelfDtos;

    public Integer getDistributeId() {
        return distributeId;
    }

    public void setDistributeId(Integer distributeId) {
        this.distributeId = distributeId;
    }

    public Integer getPlatformId() {
        return platformId;
    }

    public void setPlatformId(Integer platformId) {
        this.platformId = platformId;
    }

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public Long getChapterId() {
        return chapterId;
    }

    public void setChapterId(Long chapterId) {
        this.chapterId = chapterId;
    }

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public Integer getPageType() {
        return pageType;
    }

    public void setPageType(Integer pageType) {
        this.pageType = pageType;
    }

    public Integer getPageNo() {
        return pageNo;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }

    public String getSearchWord() {
        return searchWord;
    }

    public void setSearchWord(String searchWord) {
        this.searchWord = searchWord;
    }

    public String getRefer() {
        return refer;
    }

    public void setRefer(String refer) {
        this.refer = refer;
    }

    public String getResPath() {
        return resPath;
    }

    public void setResPath(String resPath) {
        this.resPath = resPath;
    }

    public String getCtxPath() {
        return ctxPath;
    }

    public void setCtxPath(String ctxPath) {
        this.ctxPath = ctxPath;
    }

    public Integer getIsLight() {
        return isLight;
    }

    public void setIsLight(Integer isLight) {
        this.isLight = isLight;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public String getFontSize() {
        return fontSize;
    }

    public void setFontSize(String fontSize) {
        this.fontSize = fontSize;
    }

    public String getSpacing() {
        return spacing;
    }

    public void setSpacing(String spacing) {
        this.spacing = spacing;
    }

    public Integer getOperation() {
        return operation;
    }

    public void setOperation(Integer operation) {
        this.operation = operation;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Integer getIsNext() {
        return isNext;
    }

    public void setIsNext(Integer isNext) {
        this.isNext = isNext;
    }

    public Integer getMark() {
        return mark;
    }

    public void setMark(Integer mark) {
        this.mark = mark;
    }

    public Integer getStart() {
        return start;
    }

    public void setStart(Integer start) {
        this.start = start;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getBookCount() {
        return bookCount;
    }

    public void setBookCount(Integer bookCount) {
        this.bookCount = bookCount;
    }

    public Integer getTagId() {
        return tagId;
    }

    public void setTagId(Integer tagId) {
        this.tagId = tagId;
    }

    public Integer getTagType() {
        return tagType;
    }

    public void setTagType(Integer tagType) {
        this.tagType = tagType;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public Integer getMaxChapter() {
        return maxChapter;
    }

    public void setMaxChapter(Integer maxChapter) {
        this.maxChapter = maxChapter;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getBookshelf() {
        return bookshelf;
    }

    public void setBookshelf(String bookshelf) {
        this.bookshelf = bookshelf;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public List<BookshelfDto> getBookshelfDtos() {
        return bookshelfDtos;
    }

    public void setBookshelfDtos(List<BookshelfDto> bookshelfDtos) {
        this.bookshelfDtos = bookshelfDtos;
    }

    public Integer getPageId() {
        return pageId;
    }

    public void setPageId(Integer pageId) {
        this.pageId = pageId;
    }

    public String getIsRegister() {
        return isRegister;
    }

    public void setIsRegister(String isRegister) {
        this.isRegister = isRegister;
    }

    public String getQuery() {
        StringBuilder sb = new StringBuilder();
        sb.append("&amp;");
        if (bookId != null) {
            sb.append("&amp;bid=").append(bookId);
        }
        if (chapterId != null) {
            sb.append("&amp;cid=").append(chapterId);
        }
        if (pageNo != null) {
            sb.append("&amp;p=").append(pageNo);
        }
        String query = sb.toString();
        sb.setLength(0);
        sb = null;

        return query;
    }
}
