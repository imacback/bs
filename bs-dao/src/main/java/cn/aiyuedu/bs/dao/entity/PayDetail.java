package cn.aiyuedu.bs.dao.entity;

import cn.aiyuedu.bs.common.model.PayDetailBase;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author Scott
 */
@Document(collection = "payDetail")
@CompoundIndexes({
        @CompoundIndex(name = "uid_bookId_type", def = "{'uid':1,'bookId':1,'type':1}"),
        @CompoundIndex(name = "uid_bookId_type_chapterId", def = "{'uid':1,'bookId':1,'type':1,'chapterId':1}")
})
public class PayDetail extends PayDetailBase {
}
