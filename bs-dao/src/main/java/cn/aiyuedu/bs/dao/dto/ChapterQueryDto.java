package cn.aiyuedu.bs.dao.dto;

import java.util.List;

/**
 * Description:
 *
 * @author yz.wu
 */
public class ChapterQueryDto {

    private List<Long> ids;
    private Long bookId;
    private String originName;
    private String name;
    private Integer status;
    private List<Integer> statuses;
    private Integer start;
    private Integer limit;
    private Integer isDesc;
    private Integer orderId;
    private Integer startOrderId;
    private Integer orderType;              //排序，默认1:id, 2:orderId
    private Integer charIndex;

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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

    public List<Long> getIds() {
        return ids;
    }

    public void setIds(List<Long> ids) {
        this.ids = ids;
    }

    public Integer getOrderType() {
        return orderType;
    }

    public void setOrderType(Integer orderType) {
        this.orderType = orderType;
    }

    public Integer getCharIndex() {
        return charIndex;
    }

    public void setCharIndex(Integer charIndex) {
        this.charIndex = charIndex;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public String getOriginName() {
        return originName;
    }

    public void setOriginName(String originName) {
        this.originName = originName;
    }

    public Integer getStartOrderId() {
        return startOrderId;
    }

    public void setStartOrderId(Integer startOrderId) {
        this.startOrderId = startOrderId;
    }

    public List<Integer> getStatuses() {
        return statuses;
    }

    public void setStatuses(List<Integer> statuses) {
        this.statuses = statuses;
    }
}
