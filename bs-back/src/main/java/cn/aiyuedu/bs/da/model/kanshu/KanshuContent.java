package cn.aiyuedu.bs.da.model.kanshu;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by tonydeng on 14/10/28.
 * 章节内容例子
 *  <chapter>
*          <chapterid>19432996</chapterid>
*          <content>
*                 　　　　　漆黑的山林中，迷雾重重，皎洁的明月当空悬挂，时不时会被薄薄的云层遮挡住光辉！
*          </content>
*      </chapter>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "chapter")
public class KanshuContent {
    @XmlElement
    private Integer chapterid;
    @XmlElement
    private String content;

    public Integer getChapterid() {
        return chapterid;
    }

    public void setChapterid(Integer chapterid) {
        this.chapterid = chapterid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "KanshuContent{" +
                "chapterid=" + chapterid +
                ", content='" + content + '\'' +
                '}';
    }
}
