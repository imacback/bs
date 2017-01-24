package cn.aiyuedu.bs.common.model;

import com.alibaba.fastjson.annotation.JSONField;
import org.msgpack.annotation.Message;
import org.springframework.data.annotation.Transient;

import java.util.List;

/**
 * Description:
 * @author yz.wu
 */
@Message
public class ContainerBase {

    private Integer id;
    private String name;
    private Integer siteId;
    @Transient
    @JSONField(serialize = true)
    private List<ComponentBase> components;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<ComponentBase> getComponents() {
        return components;
    }

    public void setComponents(List<ComponentBase> components) {
        this.components = components;
    }

    public Integer getSiteId() {
        return siteId;
    }

    public void setSiteId(Integer siteId) {
        this.siteId = siteId;
    }
}
