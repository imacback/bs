package cn.aiyuedu.bs.wap.dto;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Description:
 *
 * @author yz.wu
 */
public class BookshelfDto {

    private Long bookId;
    private Long chapterId;
    private Integer orderId;

    @JSONField(serialize = false)
    private String bookName;
    @JSONField(serialize = false)
    private String largePic;

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

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public String getLargePic() {
        return largePic;
    }

    public void setLargePic(String largePic) {
        this.largePic = largePic;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }
}
