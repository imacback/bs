package cn.aiyuedu.bs.dao.entity;

import cn.aiyuedu.bs.common.model.ContainerBase;
import org.msgpack.annotation.Message;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "container")
@Message
public class Container extends ContainerBase {

    private Integer isUse;
    private Integer status;
    private Date createDate;
    private Integer creatorId;
    private Date editDate;
    private Integer editorId;

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
