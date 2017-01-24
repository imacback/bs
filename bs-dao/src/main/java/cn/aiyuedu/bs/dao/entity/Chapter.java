package cn.aiyuedu.bs.dao.entity;

import cn.aiyuedu.bs.common.model.ChapterBase;
import org.msgpack.annotation.Message;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Message
@Document(collection="chapter")
@CompoundIndexes({
        @CompoundIndex(name = "bookId_asc_idx", def = "{'bookId': 1, '_id': 1}"),
        @CompoundIndex(name = "bookId_desc_idx", def = "{'bookId': -1, '_id': -1}"),
        @CompoundIndex(name = "orderId_asc_idx", def = "{'bookId': 1, 'orderId': 1}"),
        @CompoundIndex(name = "orderId_desc_idx", def = "{'bookId': -1, 'orderId': -1}"),
})
public class Chapter extends ChapterBase {

    private String cpChapterId;
    private Integer filteredWords;//过滤后章节的总字数
    private String filterWords;//被过滤的敏感词串,以分号";"分隔
    private Integer status;
    private Date createDate;
    private Integer creatorId;
    private Date editDate;
    private Integer editorId;

    public Integer getFilteredWords() {
        return filteredWords;
    }

    public void setFilteredWords(Integer filteredWords) {
        this.filteredWords = filteredWords;
    }

    public String getFilterWords() {
        return filterWords;
    }

    public void setFilterWords(String filterWords) {
        this.filterWords = filterWords;
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getCpChapterId() {
        return cpChapterId;
    }

    public void setCpChapterId(String cpChapterId) {
        this.cpChapterId = cpChapterId;
    }
}
