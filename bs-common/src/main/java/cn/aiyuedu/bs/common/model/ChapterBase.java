package cn.aiyuedu.bs.common.model;

import org.msgpack.annotation.Ignore;
import org.msgpack.annotation.Message;
import org.springframework.data.mongodb.core.index.Indexed;

import java.util.Date;

/**
 * Description:
 *
 * @author yz.wu
 */
@Message
public class ChapterBase {

    private Long id;
    @Ignore
    private String originName;//原章节名
    private String name;//过滤后的章节名
    private String volume;//卷名
    @Indexed
    private Long bookId;
    @Indexed
    private Integer orderId;
    @Indexed
    private Integer isFee;//章节是否收费,1收费 0免费
    private Integer price;//价格,价格多币
    private Integer words;//章节总字数
    private Integer sumWords;//全本到目前章节的总偏移量(包括当前章节总字数)
    private Date publishDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Integer getPrice() {
        if (price != null) {
            return price;
        } else {
            return 0;
        }
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getWords() {
        return words;
    }

    public void setWords(Integer words) {
        this.words = words;
    }

    public Date getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(Date publishDate) {
        this.publishDate = publishDate;
    }

    public Integer getIsFee() {
        return isFee;
    }

    public void setIsFee(Integer isFee) {
        this.isFee = isFee;
    }

    public Integer getSumWords() {
        return sumWords;
    }

    public void setSumWords(Integer sumWords) {
        this.sumWords = sumWords;
    }

    public String getOriginName() {
        return originName;
    }

    public void setOriginName(String originName) {
        this.originName = originName;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ChapterBase{");
        sb.append("id=").append(id);
        sb.append(", originName='").append(originName).append('\'');
        sb.append(", name='").append(name).append('\'');
        sb.append(", volume='").append(volume).append('\'');
        sb.append(", bookId=").append(bookId);
        sb.append(", orderId=").append(orderId);
        sb.append(", isFee=").append(isFee);
        sb.append(", price=").append(price);
        sb.append(", words=").append(words);
        sb.append(", sumWords=").append(sumWords);
        sb.append(", publishDate=").append(publishDate);
        sb.append('}');
        return sb.toString();
    }
}
