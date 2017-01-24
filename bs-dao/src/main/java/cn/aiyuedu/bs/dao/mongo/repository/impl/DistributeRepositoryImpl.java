package cn.aiyuedu.bs.dao.mongo.repository.impl;

import cn.aiyuedu.bs.common.Constants;
import cn.aiyuedu.bs.common.orm.Page;
import cn.aiyuedu.bs.dao.mongo.SequenceDao;
import cn.aiyuedu.bs.dao.util.QueryLikeUtils;
import cn.aiyuedu.bs.dao.entity.Distribute;
import cn.aiyuedu.bs.dao.dto.DistributeDto;
import cn.aiyuedu.bs.dao.mongo.repository.custom.DistributeRepositoryCustom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.Date;
import java.util.List;

/**
 * Created by Thinkpad on 2014/11/18.
 */
public class DistributeRepositoryImpl implements DistributeRepositoryCustom {


    @Autowired
    private MongoOperations mongoOperations;
    @Autowired
    private SequenceDao sequenceDao;

    public void update(Distribute distribute) {
        Update update = new Update().set("status", distribute.getStatus())
                .set("editorId", distribute.getEditorId())
                .set("editDate", new Date()).set("name", distribute.getName()).set("isCategory",distribute.getIsCategory());
        mongoOperations.updateFirst(Query.query(Criteria.where(Constants.MONGODB_ID_KEY).is(distribute.getId())),
                update,
                Distribute.class);
    }


    public Distribute persist(Distribute distribute) {
        if (distribute.getId() == null) {
            distribute.setId(sequenceDao.getSequence(
                    mongoOperations.getCollectionName(Distribute.class)));
            mongoOperations.insert(distribute);
        } else {
            mongoOperations.save(distribute);
        }
        return distribute;
    }

    public List<Distribute> findAll(Integer status,Integer isCategory) {
        Query query=new Query();
        if(status!=null){
            query.addCriteria(Criteria.where("status").in(status));
        }
        if(isCategory !=null){
            query.addCriteria(Criteria.where("isCategory").in(isCategory));
        }
        return mongoOperations.find(query, Distribute.class);
    }

    public Distribute getDistrbuteOne(Integer id) {
        return mongoOperations.findOne(new Query(Criteria.where("id").is(id)), Distribute.class);
    }

    public Page<Distribute> getPage(DistributeDto distributeDto) {
        Query query = new Query();
        long count = 0l;
        if (distributeDto != null) {
            if (distributeDto.getDisName() != null)
                query.addCriteria(Criteria.where("name").regex(QueryLikeUtils.normalizeKeyword(distributeDto.getDisName())));
            if (distributeDto.getStatus() != null)
                query.addCriteria(Criteria.where("status").in(distributeDto.getStatus()));
            if (distributeDto.getIsCategory() != null)
                query.addCriteria(Criteria.where("isCategory").in(distributeDto.getIsCategory()));
            if (distributeDto.getStart() != null) {
                count = mongoOperations.count(query, Distribute.class);
                //分页
                query.skip(distributeDto.getStart()).limit(distributeDto.getLimit());
            }
            //默认排序
            Sort.Direction direction = Sort.Direction.DESC;
            query.with(new Sort(direction, "id"));
        }
        return new Page<>(mongoOperations.find(query, Distribute.class), count);
    }

}
