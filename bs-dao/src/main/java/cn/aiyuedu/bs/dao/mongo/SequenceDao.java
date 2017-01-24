package cn.aiyuedu.bs.dao.mongo;

import cn.aiyuedu.bs.dao.entity.Sequence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

/**
 * Description:
 * @author yz.wu
 */
public class SequenceDao {

    @Autowired
    private MongoTemplate mongoTemplate;

    public Integer getSequence(String collectionName){
        Sequence s = mongoTemplate.findAndModify(new Query(Criteria.where("id").is(collectionName)),
                new Update().inc("sequence", Sequence.STEP),
                FindAndModifyOptions.options().returnNew(true).upsert(true),
                Sequence.class);
        if (s == null) {
            s = new Sequence();
            s.setId(collectionName);
            s.setSequence(1);
            mongoTemplate.save(s);
        }

        return s.getSequence();
    }

    public void delete(String collectionName) {
        mongoTemplate.remove(new Query(Criteria.where("id").is(collectionName)), Sequence.class);
    }
}