package cn.aiyuedu.bs.da.model.kanshu;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by tonydeng on 14/10/28.
 *
 * 章节内容例子
 * <p>
 * <chapters>
 *      <chapter>
 *          <chapterid>19432996</chapterid>
 *          <content>
 *                 　　　　　漆黑的山林中，迷雾重重，皎洁的明月当空悬挂，时不时会被薄薄的云层遮挡住光辉！
 *          </content>
 *      </chapter>
 * </chapters>
 * </p>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "chapters")
public class KanshuContents {
    @XmlElement(name = "chapter")
    private KanshuContent content;

    public KanshuContent getContent() {
        return content;
    }

    public void setContent(KanshuContent content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "KanshuContents{" +
                "content=" + content +
                '}';
    }
}
