package cn.aiyuedu.bs.da.model.kanshu;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Created by tonydeng on 14/10/28.
 * 章节列表例子
 * <p>
 * <?xml version="1.0" encoding="utf-8"?>
 *  <chapters>
 *      <chapter>
 *          <chapterId>19432996</chapterId>
 *          <chapterName>第1章：残酷的世界</chapterName>
 *          <chapterSize>1144</chapterSize>
 *          <isVip>0</isVip><
 *          price>0</price>
 *      </chapter>
 *      <lastnum>0</lastnum>
 *  </chapters>
 * </p>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "chapters")
public class KanshuChapters {
    @XmlElement(name = "chapter")
    private List<KanshuChapter> chapters;
    public List<KanshuChapter> getChapters() {
        return chapters;
    }

    public void setChapters(List<KanshuChapter> chapters) {
        this.chapters = chapters;
    }

    @Override
    public String toString() {
        return "KanshuChapters{" +
                "chapters=" + chapters +
                '}';
    }
}
