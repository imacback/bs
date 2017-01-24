package cn.aiyuedu.bs.dao.entity;

import cn.aiyuedu.bs.common.model.CategoryBase;
import com.google.common.collect.Lists;
import org.msgpack.annotation.Message;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

/**
 * Description:
 *
 * @author yz.wu
 */
@Message
@Document(collection = "category")
public class Category extends CategoryBase {


    @Indexed
    private Integer isLeaf;
    private Integer isUse;
    private Integer bookCount;
    private Integer tagClassifyId;
    private Integer tagSexId;
    private List<Integer> tagContentIds;
    private List<Integer> tagSupplyIds;
    private Integer creatorId;
    private Date createDate;
    private Integer editorId;
    private Date editDate;
    @Indexed
    private Integer parentId;

    private List<Integer> distributeIds = Lists.newArrayList();

    public List<Integer> getDistributeIds() {
        return distributeIds;
    }
    public void setDistributeIds(List<Integer> distributeIds) {
        this.distributeIds = distributeIds;
    }

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

    public Integer getIsUse() {
        return isUse;
    }

    public void setIsUse(Integer isUse) {
        this.isUse = isUse;
    }

    public Integer getBookCount() {
        return bookCount;
    }

    public void setBookCount(Integer bookCount) {
        this.bookCount = bookCount;
    }

    public Integer getTagClassifyId() {
        return tagClassifyId;
    }

    public void setTagClassifyId(Integer tagClassifyId) {
        this.tagClassifyId = tagClassifyId;
    }

    public Integer getTagSexId() {
        return tagSexId;
    }

    public void setTagSexId(Integer tagSexId) {
        this.tagSexId = tagSexId;
    }

    public List<Integer> getTagContentIds() {
        return tagContentIds;
    }

    public void setTagContentIds(List<Integer> tagContentIds) {
        this.tagContentIds = tagContentIds;
    }

    public List<Integer> getTagSupplyIds() {
        return tagSupplyIds;
    }

    public void setTagSupplyIds(List<Integer> tagSupplyIds) {
        this.tagSupplyIds = tagSupplyIds;
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
