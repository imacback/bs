package cn.aiyuedu.bs.dao.entity;

import cn.aiyuedu.bs.common.model.UserBase;
import org.springframework.data.mongodb.core.index.Indexed;

import java.util.Date;

/**
 * Description:
 *
 * @author yz.wu
 */
public class User extends UserBase {

    @Indexed
    private Integer status;
    private Date createDate;
    
    @Indexed
    private String qqOpenId;
	private String qqAppId;
	
    @Indexed
	private String weixinOpenId;
	private String weixinAppId;

    public String getQqOpenId() {
		return qqOpenId;
	}

	public void setQqOpenId(String qqOpenId) {
		this.qqOpenId = qqOpenId;
	}

	public String getWeixinOpenId() {
		return weixinOpenId;
	}

	public void setWeixinOpenId(String weixinOpenId) {
		this.weixinOpenId = weixinOpenId;
	}

	public String getWeixinAppId() {
		return weixinAppId;
	}

	public void setWeixinAppId(String weixinAppId) {
		this.weixinAppId = weixinAppId;
	}

	public String getQqAppId() {
		return qqAppId;
	}

	public void setQqAppId(String qqAppId) {
		this.qqAppId = qqAppId;
	}
	
	
	
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}
