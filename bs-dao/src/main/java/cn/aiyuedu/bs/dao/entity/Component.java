package cn.aiyuedu.bs.dao.entity;

import cn.aiyuedu.bs.common.model.ComponentBase;
import org.msgpack.annotation.Message;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "component")
@Message
public class Component extends ComponentBase {

    private Integer isUse;
    private Integer status;
    private Integer orderId;
    private String memo;
    private String entryTitle;
    private Integer entryDataType;
    private String entryData;
    private Date createDate;
    private Integer creatorId;
    private Date editDate;
    private Integer editorId;

    public Component(){};

    public Component(Integer id, Integer orderId){
        setId(id);
        setOrderId(orderId);
    };

    public Integer getIsUse() {
        return isUse;
    }

    public void setIsUse(Integer isUse) {
        this.isUse = isUse;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getEntryTitle() {
        return entryTitle;
    }

    public void setEntryTitle(String entryTitle) {
        this.entryTitle = entryTitle;
    }

    public Integer getEntryDataType() {
        return entryDataType;
    }

    public void setEntryDataType(Integer entryDataType) {
        this.entryDataType = entryDataType;
    }

    public String getEntryData() {
        return entryData;
    }

    public void setEntryData(String entryData) {
        this.entryData = entryData;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Integer getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Integer creatorId) {
        this.creatorId = creatorId;
    }

    public Date getEditDate() {
        return editDate;
    }

    public void setEditDate(Date editDate) {
        this.editDate = editDate;
    }

    public Integer getEditorId() {
        return editorId;
    }

    public void setEditorId(Integer editorId) {
        this.editorId = editorId;
    }
}
