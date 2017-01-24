package cn.aiyuedu.bs.dao.entity;

import cn.aiyuedu.bs.common.model.BatchBookBase;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * Description:
 *
 * @author yz.wu
 */
@Document(collection = "batchBook")
public class BatchBook extends BatchBookBase {

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
