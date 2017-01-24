package cn.aiyuedu.bs.dao.entity;

import cn.aiyuedu.bs.common.model.SiteBase;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * Description:
 *
 * @author yz.wu
 */
@Document(collection = "site")
public class Site extends SiteBase {

    private Integer status;
    private Integer creatorId;
    private Date createDate;
    private Integer editorId;
    private Date editDate;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Integer creatorId) {
        this.creatorId = creatorId;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Integer getEditorId() {
        return editorId;
    }

    public void setEditorId(Integer editorId) {
        this.editorId = editorId;
    }

    public Date getEditDate() {
        return editDate;
    }

    public void setEditDate(Date editDate) {
        this.editDate = editDate;
    }
}
