package cn.aiyuedu.bs.dao.entity;

import cn.aiyuedu.bs.common.model.BookBase;
import org.msgpack.annotation.Message;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Message
@Document(collection="book")
public class Book extends BookBase {

    private String cpBookId;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date onlineDate;
    private Date offlineDate;
    private String isbn;
    @Indexed
    private Integer batchId;
    private Integer dayPublishChapters;
    private Integer creatorId;
    private Date editDate;
    private Integer editorId;

    public Date getOnlineDate() {
        return onlineDate;
    }

    public void setOnlineDate(Date onlineDate) {
        this.onlineDate = onlineDate;
    }

    public Date getOfflineDate() {
        return offlineDate;
    }

    public void setOfflineDate(Date offlineDate) {
        this.offlineDate = offlineDate;
    }

    public Integer getBatchId() {
        return batchId;
    }

    public void setBatchId(Integer batchId) {
        this.batchId = batchId;
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

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public Integer getDayPublishChapters() {
        return dayPublishChapters;
    }

    public void setDayPublishChapters(Integer dayPublishChapters) {
        this.dayPublishChapters = dayPublishChapters;
    }

    public String getCpBookId() {
        return cpBookId;
    }

    public void setCpBookId(String cpBookId) {
        this.cpBookId = cpBookId;
    }
}
