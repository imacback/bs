package cn.aiyuedu.bs.da.model;

import javax.xml.bind.annotation.XmlElement;

/**
 * Created by tonydeng on 14-10-17.
 */
public class CpBook {

    private String cpBookName;
    private String cpAuthor;
    private String cpCover;

    public String getCpBookName() {
        return cpBookName;
    }

    public void setCpBookName(String cpBookName) {
        this.cpBookName = cpBookName;
    }

    public String getCpAuthor() {
        return cpAuthor;
    }

    public void setCpAuthor(String cpAuthor) {
        this.cpAuthor = cpAuthor;
    }

    public String getCpCover() {
        return cpCover;
    }

    public void setCpCover(String cpCover) {
        this.cpCover = cpCover;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("CpBook{");
        sb.append("cpBookName='").append(cpBookName).append('\'');
        sb.append(", cpAuthor='").append(cpAuthor).append('\'');
        sb.append(", cpCover='").append(cpCover).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
