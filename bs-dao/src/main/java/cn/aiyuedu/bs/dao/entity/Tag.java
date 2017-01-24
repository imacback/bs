package cn.aiyuedu.bs.dao.entity;

import cn.aiyuedu.bs.common.dto.ChildrenDto;
import cn.aiyuedu.bs.common.model.TagBase;
import org.msgpack.annotation.Message;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * Description:
 *
 * @author yz.wu
 */
@Document(collection = "tag")
@Message
public class Tag extends TagBase {


    private Integer isLeaf;
    @Transient
    private ChildrenDto<Tag> children;
    private Integer isUse;
    private Integer bookCount;
    private Date createDate;
    private String scope;
    private Integer creatorId;
    private Date editDate;
    private Integer editorId;
    private Integer parentId;

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public Integer getIsLeaf() {
        return isLeaf;
    }

    public void setIsLeaf(Integer isLeaf) {
        this.isLeaf = isLeaf;
    }

    public Integer getBookCount() {
        return bookCount;
    }

    public void setBookCount(Integer bookCount) {
        this.bookCount = bookCount;
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

    public Integer getIsUse() {
        return isUse;
    }

    public void setIsUse(Integer isUse) {
        this.isUse = isUse;
    }

    public ChildrenDto<Tag> getChildren() {
        return children;
    }

    public void setChildren(ChildrenDto<Tag> children) {
        this.children = children;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }
}
