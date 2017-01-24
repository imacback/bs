package cn.aiyuedu.bs.dao.dto;

import cn.aiyuedu.bs.dao.entity.PayDetail;

/**
 * Created by Thinkpad on 2014/12/5.
 */
public class PayDetailDto extends PayDetail {
    private String userName;
    private String platformName;
    private String bookName;
    private String chapterName;
    private Integer orderId;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPlatformName() {
        return platformName;
    }

    public void setPlatformName(String platformName) {
        this.platformName = platformName;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getChapterName() {
        return chapterName;
    }

    public void setChapterName(String chapterName) {
        this.chapterName = chapterName;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }
}
