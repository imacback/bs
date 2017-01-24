package cn.aiyuedu.bs.dao.dto;

import cn.aiyuedu.bs.dao.entity.ClientShelf;

/**
 * Created by Thinkpad on 2014/9/29.
 */
public class ClientShelfDto extends ClientShelf {

   private String platformName;

    public String getPlatformName() {
        return platformName;
    }

    public void setPlatformName(String platformName) {
        this.platformName = platformName;
    }
}
