package cn.aiyuedu.bs.common.model;


import java.io.Serializable;

/**
 *
 * @author Scott
 */
public class ReadPosition implements Serializable {
    private Integer position;//    position:阅读进度
    private Integer paraph;//    paraph:阅读段落
    private Integer element;//    element:阅读词
    private Integer cha;//    char:如果是英文，截断的字符序号
    private Integer chapter;//    chapter:阅读章节

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public Integer getParaph() {
        return paraph;
    }

    public void setParaph(Integer paraph) {
        this.paraph = paraph;
    }

    public Integer getElement() {
        return element;
    }

    public void setElement(Integer element) {
        this.element = element;
    }

    public Integer getCha() {
        return cha;
    }

    public void setCha(Integer cha) {
        this.cha = cha;
    }

    public Integer getChapter() {
        return chapter;
    }

    public void setChapter(Integer chapter) {
        this.chapter = chapter;
    }
}
