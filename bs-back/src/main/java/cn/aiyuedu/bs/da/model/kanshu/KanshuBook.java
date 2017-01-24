package cn.aiyuedu.bs.da.model.kanshu;


import cn.aiyuedu.bs.da.model.CpBook;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by tonydeng on 14/10/28.
 * 看书网原始书籍XML例子
 * <bookinfo>
 *     <id>69771</id>
 *     <bookName>逆天战血</bookName>
 *     <subTitle>至尊皇族血脉：逆天邪君</subTitle>
 *     <detail>真火，寒冰，搜魂，血脉的力量可毁天灭地，而不灭血脉，更是血脉中的霸者！林云身负不灭血脉异世重生，易筋骨，炼血脉，强横实力让他横贯天下！强敌？在他面前如同草芥；圣女？不过是他后宫中的藏品之一；神器？也只是他手中随意丢弃的玩具！一腔逆天战血，一身无敌意志，至尊巅峰，终将被他踩在脚下……</detail>
 *     <bookType>31059</bookType>
 *     <keyWord></keyWord>
 *     <bookStatus>0</bookStatus>
 *     <size>2198620</size>
 *     <author>心如纸水</author>
 *     <isVip>1</isVip>
 *     <isFee>0</isFee>
 *     <maxFree>20</maxFree>
 *     <price>0</price>
 *     <weekVisit>0</weekVisit>
 *     <monthVisit>0</monthVisit>
 *     <allVisit>0</allVisit>
 *     <bookTypeName>东方玄幻</bookTypeName>
 *     <imagePath>http://hezuo.kanshu.cn/cover.php?channel_id=100224&book_id=69771&type=3</imagePath>
 *     <imageMidPath>http://hezuo.kanshu.cn/cover.php?channel_id=100224&book_id=69771&type=2</imageMidPath>
 *     <imageMinPath>http://hezuo.kanshu.cn/cover.php?channel_id=100224&book_id=69771&type=1</imageMinPath>
 * </bookinfo>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "bookinfo")
public class KanshuBook extends CpBook {
    @XmlElement()
    private Integer id;
    @XmlElement
    private String bookName;
    @XmlElement
    private String subTitle;
    @XmlElement
    private String detail;
    @XmlElement
    private Integer bookType;
    @XmlElement
    private String keyWord;
    @XmlElement
    private Integer bookStatus;
    @XmlElement
    private Integer size;
    @XmlElement
    private String author;
    @XmlElement
    private Integer isVip;
    @XmlElement
    private Integer isFee;
    @XmlElement
    private Integer maxFee;
    @XmlElement
    private Integer price;
    @XmlElement
    private Integer weekVisit;
    @XmlElement
    private Integer monthVisit;
    @XmlElement
    private Integer allVisit;
    @XmlElement
    private String bookTypeName;
    @XmlElement
    private String imagePath;
    @XmlElement
    private String imageMidPath;
    @XmlElement
    private String imageMinPath;

    @Override
    public String getCpBookName() {
        return bookName;
    }

    @Override
    public String getCpAuthor() {
        return author;
    }

    @Override
    public String getCpCover() {
        return imageMidPath;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public Integer getBookType() {
        return bookType;
    }

    public void setBookType(Integer bookType) {
        this.bookType = bookType;
    }

    public String getKeyWord() {
        return keyWord;
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }

    public Integer getBookStatus() {
        return bookStatus;
    }

    public void setBookStatus(Integer bookStatus) {
        this.bookStatus = bookStatus;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Integer getIsVip() {
        return isVip;
    }

    public void setIsVip(Integer isVip) {
        this.isVip = isVip;
    }

    public Integer getIsFee() {
        return isFee;
    }

    public void setIsFee(Integer isFee) {
        this.isFee = isFee;
    }

    public Integer getMaxFee() {
        return maxFee;
    }

    public void setMaxFee(Integer maxFee) {
        this.maxFee = maxFee;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getWeekVisit() {
        return weekVisit;
    }

    public void setWeekVisit(Integer weekVisit) {
        this.weekVisit = weekVisit;
    }

    public Integer getMonthVisit() {
        return monthVisit;
    }

    public void setMonthVisit(Integer monthVisit) {
        this.monthVisit = monthVisit;
    }

    public Integer getAllVisit() {
        return allVisit;
    }

    public void setAllVisit(Integer allVisit) {
        this.allVisit = allVisit;
    }

    public String getBookTypeName() {
        return bookTypeName;
    }

    public void setBookTypeName(String bookTypeName) {
        this.bookTypeName = bookTypeName;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getImageMidPath() {
        return imageMidPath;
    }

    public void setImageMidPath(String imageMidPath) {
        this.imageMidPath = imageMidPath;
    }

    public String getImageMinPath() {
        return imageMinPath;
    }

    public void setImageMinPath(String imageMinPath) {
        this.imageMinPath = imageMinPath;
    }

    @Override
    public String toString() {
        return super.toString() + "KanshuBook{" +
                "id=" + id +
                ", bookName='" + bookName + '\'' +
                ", subTitle='" + subTitle + '\'' +
                ", detail='" + detail + '\'' +
                ", bookType=" + bookType +
                ", keyWord='" + keyWord + '\'' +
                ", bookStatus=" + bookStatus +
                ", size=" + size +
                ", author='" + author + '\'' +
                ", isVip=" + isVip +
                ", isFee=" + isFee +
                ", maxFee=" + maxFee +
                ", price=" + price +
                ", weekVisit=" + weekVisit +
                ", monthVisit=" + monthVisit +
                ", allVisit=" + allVisit +
                ", bookTypeName='" + bookTypeName + '\'' +
                ", imagePath='" + imagePath + '\'' +
                ", imageMidPath='" + imageMidPath + '\'' +
                ", imageMinPath='" + imageMinPath + '\'' +
                '}';
    }
}
