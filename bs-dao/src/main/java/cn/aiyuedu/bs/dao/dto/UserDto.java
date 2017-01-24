package cn.aiyuedu.bs.dao.dto;

import cn.aiyuedu.bs.dao.entity.User;

public class UserDto extends User {
	//平台名称
	private String platformName;
	
	//性别名称
	private String sexName;

    //用户金额 = realBalance + virtualBalance
    private Integer totalBalance;

	public String getPlatformName() {
		return platformName;
	}

	public void setPlatformName(String platformName) {
		this.platformName = platformName;
	}

	public String getSexName() {
		return sexName;
	}

	public void setSexName(String sexName) {
		this.sexName = sexName;
	}

}
