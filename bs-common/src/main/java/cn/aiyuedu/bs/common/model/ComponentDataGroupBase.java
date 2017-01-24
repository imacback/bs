package cn.aiyuedu.bs.common.model;

import org.msgpack.annotation.Message;
import org.springframework.data.annotation.Transient;

import java.util.List;

/**
 * Description:
 *
 * @author yz.wu
 */
@Message
public class ComponentDataGroupBase {

    private Integer id;
    private String title;
    private Integer orderId;
    private Integer componentId;
    @Transient
    private List<ComponentDataBase> list;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public List<ComponentDataBase> getList() {
        return list;
    }

    public void setList(List<ComponentDataBase> list) {
        this.list = list;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getComponentId() {
        return componentId;
    }

    public void setComponentId(Integer componentId) {
        this.componentId = componentId;
    }
}
