package cn.aiyuedu.bs.common.model;

import org.msgpack.annotation.Message;

@Message
public class ClientShelfBase   {
	
	private Integer id;

	//**书籍编号，号分隔**/
    private String bookIds;
    //**预置章节数，1-10*/
    private Integer chapters;
    //**平台id**/
    private Integer platformId;
    //**版本号，一个**/
    private String version;
    //**0否，1是**/
    private Integer isUseDitch;
    //**渠道号，通过;分隔**/
    private String ditchIds;
    //**0下线，1上线**/
    private Integer status;

    public String getBookIds() {
        return bookIds;
    }
    public void setBookIds(String bookIds) {
        this.bookIds = bookIds;
    }
    public Integer getChapters() {
        return chapters;
    }
    public void setChapters(Integer chapters) {
        this.chapters = chapters;
    }

    public String getVersion() {
        return version;
    }
    public void setVersion(String version) {
        this.version = version;
    }

    public Integer getPlatformId() {
        return platformId;
    }
    public void setPlatformId(Integer platformId) {
        this.platformId = platformId;
    }
    public Integer getIsUseDitch() {
        return isUseDitch;
    }
    public void setIsUseDitch(Integer isUseDitch) {
        this.isUseDitch = isUseDitch;
    }
    public String getDitchIds() {
        return ditchIds;
    }
    public void setDitchIds(String ditchIds) {
        this.ditchIds = ditchIds;
    }
    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
