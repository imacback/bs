package cn.aiyuedu.bs.wap.dto;

import java.io.Serializable;

/**
 * Description:
 * @author yz.wu
 */
public class CookieDto implements Serializable {

	private static final long serialVersionUID = 6016052178827159485L;

	private String uid;			//用户id
	private Integer isLight;
	private String style;
	private String fontSize;
	private String spacing;
	private String bookshelf;	//阅读记录，json
	private Integer distId;

	public Integer getIsLight() {
		return isLight;
	}

	public void setIsLight(Integer isLight) {
		this.isLight = isLight;
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	public String getFontSize() {
		return fontSize;
	}

	public void setFontSize(String fontSize) {
		this.fontSize = fontSize;
	}

	public String getSpacing() {
		return spacing;
	}

	public void setSpacing(String spacing) {
		this.spacing = spacing;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getBookshelf() {
		return bookshelf;
	}

	public void setBookshelf(String bookshelf) {
		this.bookshelf = bookshelf;
	}

	public Integer getDistId() {
		return distId;
	}

	public void setDistId(Integer distId) {
		this.distId = distId;
	}
}
