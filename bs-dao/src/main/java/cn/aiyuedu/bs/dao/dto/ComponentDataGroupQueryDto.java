package cn.aiyuedu.bs.dao.dto;

/**
 * Created by Thinkpad on 2015/1/6.
 */
public class ComponentDataGroupQueryDto {

    private Integer componentId;

    private Integer start;
    private Integer limit;
    private Integer isDesc;
    private Integer orderType;//排序，默认1:id, 2:orderId

    public Integer getComponentId() {
        return componentId;
    }

    public void setComponentId(Integer componentId) {
        this.componentId = componentId;
    }

    public Integer getStart() {
        return start;
    }

    public void setStart(Integer start) {
        this.start = start;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public Integer getIsDesc() {
        return isDesc;
    }

    public void setIsDesc(Integer isDesc) {
        this.isDesc = isDesc;
    }

    public Integer getOrderType() {
        return orderType;
    }

    public void setOrderType(Integer orderType) {
        this.orderType = orderType;
    }
}
