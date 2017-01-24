package cn.aiyuedu.bs.dao.entity;

import java.sql.Date;

/**
 * Created by Thinkpad on 2014/11/28.
 */
public class DistributeTime {
    private Integer disId;
    private Date  updateTime;

    public Integer getDisId() {
        return disId;
    }

    public void setDisId(Integer disId) {
        this.disId = disId;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
