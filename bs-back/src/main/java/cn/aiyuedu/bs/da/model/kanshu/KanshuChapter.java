package cn.aiyuedu.bs.da.model.kanshu;


import cn.aiyuedu.bs.da.model.CpChapter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by tonydeng on 14/10/28.
 * 看书网原始章节列表XML例子
 * <chapter>
 *     <chapterId>19432996</chapterId>
 *     <chapterName>第1章：残酷的世界</chapterName>
 *     <chapterSize>1144</chapterSize>
 *     <isVip>0</isVip>
 *     <price>0</price>
 * </chapter>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "chapter")
public class KanshuChapter extends CpChapter {
    @XmlElement
    private Integer chapterId;
    @XmlElement
    private String chapterName;
    @XmlElement
    private Integer chapterSize;
    @XmlElement
    private Integer isVip;
    @XmlElement
    private Integer price;

    @Override
    public String getCpChapterTitle() {
        return chapterName;
    }

    @Override
    public String getCpChapterId() {
        return (chapterId!= null) ? chapterId.toString() : null;
    }

    public Integer getChapterId() {
        return chapterId;
    }

    public void setChapterId(Integer chapterId) {
        this.chapterId = chapterId;
    }

    public String getChapterName() {
        return chapterName;
    }

    public void setChapterName(String chapterName) {
        this.chapterName = chapterName;
    }

    public Integer getChapterSize() {
        return chapterSize;
    }

    public void setChapterSize(Integer chapterSize) {
        this.chapterSize = chapterSize;
    }

    public Integer getIsVip() {
        return isVip;
    }

    public void setIsVip(Integer isVip) {
        this.isVip = isVip;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return super.toString()+"KanshuChapter{" +
                "chapterId=" + chapterId +
                ", chapterName='" + chapterName + '\'' +
                ", chapterSize=" + chapterSize +
                ", isVip=" + isVip +
                ", price=" + price +
                '}';
    }
}
