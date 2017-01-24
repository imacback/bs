package cn.aiyuedu.bs.common.model;

import org.msgpack.annotation.Message;

import java.util.List;

/**
 * Created by Thinkpad on 2014/11/24.
 */
@Message
public class DistributeBookList {
    private Integer disId;

    private List<Long> bookIds;

    public Integer getDisId() {
        return disId;
    }

    public void setDisId(Integer disId) {
        this.disId = disId;
    }

    public List<Long> getBookIds() {
        return bookIds;
    }

    public void setBookIds(List<Long> bookIds) {
        this.bookIds = bookIds;
    }
}
