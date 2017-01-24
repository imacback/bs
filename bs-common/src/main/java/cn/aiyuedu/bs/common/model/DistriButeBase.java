package cn.aiyuedu.bs.common.model;

import org.msgpack.annotation.Message;

/**
 * Created by Thinkpad on 2014/11/18.
 */
@Message
public class DistriButeBase {
    private  Integer id;
    private  String name;

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
