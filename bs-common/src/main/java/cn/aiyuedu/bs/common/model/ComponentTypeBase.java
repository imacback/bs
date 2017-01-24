package cn.aiyuedu.bs.common.model;

import org.msgpack.annotation.Message;

/**
 * Description:
 *
 * @author yz.wu
 */
@Message
public class ComponentTypeBase {

    private Integer id;
    private String name;
    private String template;
    private Integer dataLimit;
    private Integer dataGroup;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public Integer getDataGroup() {
        return dataGroup;
    }

    public void setDataGroup(Integer dataGroup) {
        this.dataGroup = dataGroup;
    }

    public Integer getDataLimit() {
        return dataLimit;
    }

    public void setDataLimit(Integer dataLimit) {
        this.dataLimit = dataLimit;
    }
}
