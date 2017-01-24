package cn.aiyuedu.bs.dao.entity;

import cn.aiyuedu.bs.common.model.ComponentDataBase;
import org.msgpack.annotation.Message;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "componentData")
@Message
public class ComponentData extends ComponentDataBase {

    public ComponentData(){};

    public ComponentData(Integer id, Integer orderId) {
        setId(id);
        setOrderId(orderId);
    }

}
