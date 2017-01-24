package cn.aiyuedu.bs.front.vo.component.data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import javax.xml.bind.annotation.XmlAttribute;
import java.io.UnsupportedEncodingException;

import static cn.aiyuedu.bs.common.Constants.ComponentDataType;

/**
 * Description:
 *
 * @author Scott
 */
public class DataBaseVo {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private String id;
    private String logo;
    private String name;
    private String url;
    private String desc;

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @XmlAttribute()
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public DataBaseVo() {

    }

    public DataBaseVo(Integer dataTypeId, String id, String logo, String name, String data, String desc) throws UnsupportedEncodingException {
//        log.info("DataBaseVo:" + id + "##" + dataTypeId + "##" + data+"##"+desc);
        this.id = id;
        this.logo = logo;
        this.name = name;
        this.desc = desc;
        if(!StringUtils.hasText(data)){
            this.url="";
            return;
        }
        if (ComponentDataType.Book.getValue() == dataTypeId.intValue()) {
            this.url = "/ft/book/detail.do?id=" + data;
        } else if (ComponentDataType.Category.getValue() == dataTypeId.intValue()) {
            this.url = "/ft/type/list.do?id=" + data;
        } else if (ComponentDataType.Container.getValue() == dataTypeId.intValue()) {
            this.url = "/ft/page/show.do?id=" + data;
        } else if (ComponentDataType.Search.getValue() == dataTypeId.intValue()) {
            this.url = "/ft/search/list.do?kw=" + data;
        } else {
            //this.url = AdvGotoService.gotoUrl(data);
        }
    }
}
