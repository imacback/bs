package cn.aiyuedu.bs.da.model.kanshu;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Description:
 *
 * @author yz.wu
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "books")
public class KanshuBookBases {

    @XmlElement(name = "book")
    private List<KanshuBookBase> kanshuBookBases;

    public List<KanshuBookBase> getKanshuBookBases() {
        return kanshuBookBases;
    }

    public void setKanshuBookBases(List<KanshuBookBase> kanshuBookBases) {
        this.kanshuBookBases = kanshuBookBases;
    }
}
