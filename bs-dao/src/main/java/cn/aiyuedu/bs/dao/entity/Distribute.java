package cn.aiyuedu.bs.dao.entity;

import org.msgpack.annotation.Message;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * Created by Thinkpad on 2014/11/18.
 */
@Message
@Document(collection = "distribute")
public class Distribute {
    private Integer id;
    private String name;
    //**创建者编号**/
    private int creatorId;
    //**修改者编号**/
    private int editorId;
    //**创建时间**/
    private Date createDate;
    //**修改时间**/
    private Date editDate;
    //**是否使用*****/
    private int status;

    private String key;

    private Integer  isCategory;

    public Integer getIsCategory() {
        return isCategory;
    }

    public void setIsCategory(Integer isCategory) {
        this.isCategory = isCategory;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCreatorId(int creatorId) {
        this.creatorId = creatorId;
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

    public int getCreatorId() {
        return creatorId;
    }

    public int getEditorId() {
        return editorId;
    }


    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
