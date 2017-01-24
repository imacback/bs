package cn.aiyuedu.bs.dao.mongo.repository.impl;

import cn.aiyuedu.bs.common.Constants;
import cn.aiyuedu.bs.common.orm.Page;
import cn.aiyuedu.bs.dao.dto.BatchQueryDto;
import cn.aiyuedu.bs.dao.entity.Batch;
import cn.aiyuedu.bs.dao.mongo.SequenceDao;
import cn.aiyuedu.bs.dao.mongo.repository.custom.BatchRepositoryCustom;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

/**
 * Created by Thinkpad on 2014/12/29.
 */
public class BatchRepositoryImpl implements BatchRepositoryCustom {

    @Autowired
    private MongoOperations mongoOperations;
    @Autowired
    private SequenceDao sequenceDao;

    @Override
    public Batch persist(Batch batch) {
        if (batch != null) {
            if (batch.getId() == null) {
                batch.setId(sequenceDao.getSequence(
                        mongoOperations.getCollectionName(Batch.class)));
                mongoOperations.insert(batch);
            } else {
                mongoOperations.save(batch);
            }
        }

        return batch;
    }

    private Query getQuery(BatchQueryDto batchQueryDto) {
        Query query = new Query();

        //如果查询对象不为空才增加过滤条件,否则查询所有信息
        if(null!=batchQueryDto){

            if(null!=batchQueryDto.getId()){
                if(null!=batchQueryDto.getIsNEId() && batchQueryDto.getIsNEId()==1){
                    query.addCriteria(Criteria.where(Constants.MONGODB_ID_KEY).ne(batchQueryDto.getId()));
                }else{
                    query.addCriteria(Criteria.where(Constants.MONGODB_ID_KEY).is(batchQueryDto.getId()));
                }
            }

            if(null!=batchQueryDto.getProviderId())
                query.addCriteria(Criteria.where("providerId").is(batchQueryDto.getProviderId()));
            if(null!=batchQueryDto.getIsUse())
                query.addCriteria(Criteria.where("isUse").is(batchQueryDto.getIsUse()));
            if(StringUtils.isNotBlank(batchQueryDto.getContractId()))
                query.addCriteria(Criteria.where("contractId").is(batchQueryDto.getContractId()));
        }

        return query;
    }

    @Override
    public long count(BatchQueryDto batchQueryDto) {
        return mongoOperations.count(getQuery(batchQueryDto), Batch.class);
    }

    @Override
    public List<Batch> find(BatchQueryDto batchQueryDto) {
        Query query = getQuery(batchQueryDto);

        //默认升序
        Sort.Direction direction = Sort.Direction.ASC;
        if(null!=batchQueryDto && null!=batchQueryDto.getIsDesc() && batchQueryDto.getIsDesc()==1){
            direction = Sort.Direction.DESC;
        }
        //排序
        query.with(new Sort(direction, Constants.MONGODB_ID_KEY));

        return mongoOperations.find(query, Batch.class);
    }

    @Override
    public Page<Batch> getPage(BatchQueryDto batchQueryDto) {
        Query query = getQuery(batchQueryDto);

        //总数
        long count = 0l;
        if (null!=batchQueryDto && batchQueryDto.getStart() != null) {
            count = mongoOperations.count(query, Batch.class);
            //分页
            query.skip(batchQueryDto.getStart()).limit(batchQueryDto.getLimit());
        }

        //默认升序
        Sort.Direction direction = Sort.Direction.ASC;
        if(null!=batchQueryDto && null!=batchQueryDto.getIsDesc() && batchQueryDto.getIsDesc()==1){
            direction = Sort.Direction.DESC;
        }
        //排序
        query.with(new Sort(direction, Constants.MONGODB_ID_KEY));

        return new Page<>(mongoOperations.find(query, Batch.class), count);
    }

    @Override
    public void removeMulti(List<Integer> ids) {
        mongoOperations.remove(new Query(Criteria.where("id").in(ids)), Batch.class);
    }
}
