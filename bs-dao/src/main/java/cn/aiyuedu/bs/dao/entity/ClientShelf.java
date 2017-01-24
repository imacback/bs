package cn.aiyuedu.bs.dao.entity;

import org.msgpack.annotation.Message;
import cn.aiyuedu.bs.common.model.ClientShelfBase;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "clientShelf")
@Message
public class ClientShelf extends ClientShelfBase {
	 
	private Date createDate;
	private int creatorId;
	private Date editDate;
	private int editorId;

	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public int getCreatorId() {
		return creatorId;
	}
	public void setCreatorId(int creatorId) {
		this.creatorId = creatorId;
	}
	public Date getEditDate() {
		return editDate;
	}
	public void setEditDate(Date editDate) {
		this.editDate = editDate;
	}
	public int getEditorId() {
		return editorId;
	}
	public void setEditorId(int editorId) {
		this.editorId = editorId;
	}
	
	 
}
