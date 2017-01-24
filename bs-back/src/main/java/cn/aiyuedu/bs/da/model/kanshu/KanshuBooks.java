package cn.aiyuedu.bs.da.model.kanshu;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by tonydeng on 14/10/28.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "books")
public class KanshuBooks {
    @XmlElement(name = "bookinfo")
    private KanshuBook kanshuBook;

    public KanshuBook getKanshuBook() {
        return kanshuBook;
    }

    public void setKanshuBook(KanshuBook kanshuBook) {
        this.kanshuBook = kanshuBook;
    }
}
