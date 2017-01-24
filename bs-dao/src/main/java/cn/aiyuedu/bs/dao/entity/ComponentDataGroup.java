package cn.aiyuedu.bs.dao.entity;

import cn.aiyuedu.bs.common.model.ComponentDataGroupBase;
import org.msgpack.annotation.Message;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "componentDataGroup")
@Message
public class ComponentDataGroup extends ComponentDataGroupBase {

    public ComponentDataGroup(){};
    public ComponentDataGroup(Integer id, Integer orderId){
        setId(id);
        setOrderId(orderId);
    }

    private Date createDate;
    private Integer creatorId;
    private Date editDate;
    private Integer editorId;

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
