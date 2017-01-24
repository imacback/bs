package cn.aiyuedu.bs.dao.dto;

import cn.aiyuedu.bs.dao.entity.ComponentData;
import org.msgpack.annotation.Message;

/**
 * Description:
 *
 * @author yz.wu
 */
@Message
public class ComponentDataDto extends ComponentData {

    private String dataName;

    public String getDataName() {
        return dataName;
    }

    public void setDataName(String dataName) {
        this.dataName = dataName;
    }
}
