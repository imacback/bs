package cn.aiyuedu.bs.dao.entity;

import org.msgpack.annotation.Message;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * Created by Thinkpad on 2014/11/18.
 */

@Message
@Document(collection = "distributeBook")
public class DistributeBook {

    private Integer id;
    private Long bookId;
    private Integer disId;
    //**创建者编号**/
    private int creatorId;
    //**修改者编号**/
    private int editorId;
    //**创建时间**/
    private Date createDate;
    //**修改时间**/
    private Date editDate;

    public int getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(int creatorId) {
        this.creatorId = creatorId;
    }

    public int getEditorId() {
        return editorId;
    }

    public void setEditorId(int editorId) {
        this.editorId = editorId;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getEditDate() {
        return editDate;
    }

    public void setEditDate(Date editDate) {
        this.editDate = editDate;
    }

    public Integer getDisId() {
        return disId;
    }

    public void setDisId(Integer disId) {
        this.disId = disId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getBookId() {
        return bookId;
    }

    public void setBookid(Long bookid) {
        this.bookId = bookid;
    }
}
