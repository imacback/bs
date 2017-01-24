package cn.aiyuedu.bs.common.model;

import org.msgpack.annotation.Message;

/**
 * Description:
 *
 * @author yz.wu
 */
@Message
public class ProviderBase {

    private Integer id;
    private String name;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
