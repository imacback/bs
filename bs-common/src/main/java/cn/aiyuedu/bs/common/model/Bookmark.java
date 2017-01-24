package cn.aiyuedu.bs.common.model;


import cn.aiyuedu.bs.common.util.CDataAdapter;
import cn.aiyuedu.bs.common.util.JaxbDateAdapter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;
import java.util.Date;

/**
 * @author Scott
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class Bookmark implements Serializable {
	@XmlAttribute(name = "id")
    private Integer id;//",1234,
    @XmlJavaTypeAdapter(CDataAdapter.class)
    private String text;//":"书签的截取内容",
    private Integer chapterIndex;//":23,
    @XmlJavaTypeAdapter(CDataAdapter.class)
    private String chapterName;//":"第三章",
    private Boolean visible;//": true,
    private Integer spara;//":3,
    private Integer sele;//":44,
    private Integer schar;//":"0",
    private Integer epara;//":3,
    private Integer eele;//":67,
    private Integer ecahr;//":0,
    private Integer lenght;//": 60,
    @XmlJavaTypeAdapter(JaxbDateAdapter.class)
    private Date createDate;//":"2014-12-23 15:34:23",
    @XmlJavaTypeAdapter(JaxbDateAdapter.class)
    private Date updateDate;//":"2014-12-25 16:08:23",
    private Integer style;//":2 }]},

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Integer getChapterIndex() {
        return chapterIndex;
    }

    public void setChapterIndex(Integer chapterIndex) {
        this.chapterIndex = chapterIndex;
    }

    public String getChapterName() {
        return chapterName;
    }

    public void setChapterName(String chapterName) {
        this.chapterName = chapterName;
    }

    public Boolean getVisible() {
        return visible;
    }

    public void setVisible(Boolean visible) {
        this.visible = visible;
    }

    public Integer getSpara() {
        return spara;
    }

    public void setSpara(Integer spara) {
        this.spara = spara;
    }

    public Integer getSele() {
        return sele;
    }

    public void setSele(Integer sele) {
        this.sele = sele;
    }

    public Integer getSchar() {
        return schar;
    }

    public void setSchar(Integer schar) {
        this.schar = schar;
    }

    public Integer getEpara() {
        return epara;
    }

    public void setEpara(Integer epara) {
        this.epara = epara;
    }

    public Integer getEele() {
        return eele;
    }

    public void setEele(Integer eele) {
        this.eele = eele;
    }

    public Integer getEcahr() {
        return ecahr;
    }

    public void setEcahr(Integer ecahr) {
        this.ecahr = ecahr;
    }

    public Integer getLenght() {
        return lenght;
    }

    public void setLenght(Integer lenght) {
        this.lenght = lenght;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public Integer getStyle() {
        return style;
    }

    public void setStyle(Integer style) {
        this.style = style;
    }
}
