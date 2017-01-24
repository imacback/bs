package cn.aiyuedu.bs.da.model.kanshu;

import cn.aiyuedu.bs.da.model.CpBook;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Description:
 *
 * @author yz.wu
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "book")
public class KanshuBookBase extends CpBook {

    @XmlElement()
    private Integer id;
    @XmlElement()
    private String bookName;
    @XmlElement()
    private Integer categoryId;
    @XmlElement()
    private Integer categoryPid;

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

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public Integer getCategoryPid() {
        return categoryPid;
    }

    public void setCategoryPid(Integer categoryPid) {
        this.categoryPid = categoryPid;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("KanshuBookBase{");
        sb.append("id=").append(id);
        sb.append(", bookName='").append(bookName).append('\'');
        sb.append(", categoryId=").append(categoryId);
        sb.append(", categoryPid=").append(categoryPid);
        sb.append('}');
        return sb.toString();
    }
}
