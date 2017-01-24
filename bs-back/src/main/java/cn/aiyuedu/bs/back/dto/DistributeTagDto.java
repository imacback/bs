package cn.aiyuedu.bs.back.dto;

import cn.aiyuedu.bs.dao.entity.Distribute;

/**
 * Created by Thinkpad on 2014/11/19.
 */
public class DistributeTagDto extends Distribute {

    private  Long count;

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    private  String userName;



}
