package cn.aiyuedu.bs.common.dto;

import org.msgpack.annotation.Message;

import java.util.List;

/**
 * Description:
 *
 * @author yz.wu
 */
@Message
public class ChildrenDto<T> {

    private long totalItems;
    private List<T> items;

    public List<T> getItems() {
        return items;
    }

    public void setItems(List<T> items) {
        this.items = items;
    }

    public long getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(long totalItems) {
        this.totalItems = totalItems;
    }
}
