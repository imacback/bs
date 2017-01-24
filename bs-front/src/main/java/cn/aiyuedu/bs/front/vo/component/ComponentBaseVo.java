package cn.aiyuedu.bs.front.vo.component;


import cn.aiyuedu.bs.front.vo.component.data.DataBaseVo;
import org.springframework.util.StringUtils;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

/**
 * Description:
 *
 * @author Scott
 */
@XmlRootElement
public class ComponentBaseVo {
    private Integer id;
    private String logo;
    private String name;
    private String entryName;
    private String url;
    private String type;
    private String styleColor;
    private String template;
    private Integer index;
    private DataBaseVo dv;
    private List<DataBaseVo> list = new ArrayList<>();

    @XmlAttribute
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public DataBaseVo getDv() {
        return dv;
    }

    public void setDv(DataBaseVo dv) {
        this.dv = dv;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    @XmlElementWrapper(name = "wrapper")
    @XmlElement(name = "DataBaseVo")
    public List<DataBaseVo> getList() {
        return list;
    }

    public void setList(List<DataBaseVo> list) {
        this.list = list;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
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

    public String getEntryName() {
        return entryName;
    }

    public void setEntryName(String entryName) {
        this.entryName = entryName;
    }

    public String getStyleColor() {
        if (StringUtils.hasText(this.styleColor) && styleColor.startsWith("#")) {
            String r = styleColor.substring(1, 3);
            String g = styleColor.substring(3, 5);
            String b = styleColor.substring(5, 7);
//            return 255 - Integer.parseInt(r, 16) + "," + (255 - Integer.parseInt(g, 16)) + "," + (255 - Integer.parseInt(b, 16));
            return Integer.parseInt(r, 16) + "," +  Integer.parseInt(g, 16)+ "," +  Integer.parseInt(b, 16);
        }
        return styleColor;
    }

    public void setStyleColor(String styleColor) {
        this.styleColor = styleColor;
    }
}
