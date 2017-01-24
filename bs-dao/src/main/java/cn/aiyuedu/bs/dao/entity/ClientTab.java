package cn.aiyuedu.bs.dao.entity;

import java.util.Date;

import org.msgpack.annotation.Message;

import cn.aiyuedu.bs.common.model.ClientTabBase;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "clientTab")
@Message
public class ClientTab extends ClientTabBase {
	//**创建者编号**/
	private int creatorId;
	//**修改者编号**/
	private int editorId;
	//**创建时间**/
	private Date createDate;
	//**修改时间**/
	private Date editDate;
	private String status;

	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
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
	 
	
	
	
}
