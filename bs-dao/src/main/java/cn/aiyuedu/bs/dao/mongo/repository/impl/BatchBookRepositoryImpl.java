package cn.aiyuedu.bs.dao.mongo.repository.impl;

import cn.aiyuedu.bs.common.Constants;
import cn.aiyuedu.bs.common.orm.Page;
import cn.aiyuedu.bs.dao.dto.BatchBookQueryDto;
import cn.aiyuedu.bs.dao.entity.BatchBook;
import cn.aiyuedu.bs.dao.mongo.SequenceDao;
import cn.aiyuedu.bs.dao.mongo.repository.custom.BatchBookRepositoryCustom;
import cn.aiyuedu.bs.dao.util.QueryLikeUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

/**
 * Created by Thinkpad on 2015/1/1.
 */
public class BatchBookRepositoryImpl implements BatchBookRepositoryCustom {

    @Autowired
    private MongoOperations mongoOperations;
    @Autowired
    private SequenceDao sequenceDao;

    @Override
    public BatchBook persist(BatchBook batchBook) {
        if (batchBook != null) {
            if (batchBook.getId() == null) {
                batchBook.setId(new Long(sequenceDao.getSequence(
                        mongoOperations.getCollectionName(BatchBook.class))));
                mongoOperations.insert(batchBook);
            } else {
                mongoOperations.save(batchBook);
            }
        }

        return batchBook;
    }

    private Query getQuery(BatchBookQueryDto batchBookQueryDto) {
        Query query = new Query();

        //如果查询对象不为空才增加过滤条件,否则查询所有信息
        if(null!=batchBookQueryDto){
            if(null!=batchBookQueryDto.getId()){
                if(null!=batchBookQueryDto.getIsNEId() && batchBookQueryDto.getIsNEId()==1){
                    query.addCriteria(Criteria.where(Constants.MONGODB_ID_KEY).ne(batchBookQueryDto.getId()));
                }else{
                    query.addCriteria(Criteria.where(Constants.MONGODB_ID_KEY).is(batchBookQueryDto.getId()));
                }
            }

            if(null!=batchBookQueryDto.getBatchId())
                query.addCriteria(Criteria.where("batchId").is(batchBookQueryDto.getBatchId()));

            if(CollectionUtils.isNotEmpty(batchBookQueryDto.getBatchIdList()))
                query.addCriteria(Criteria.where("batchId").in(batchBookQueryDto.getBatchIdList()));

            if(StringUtils.isNotBlank(batchBookQueryDto.getCpBookId()))
                query.addCriteria(Criteria.where("cpBookId").is(batchBookQueryDto.getCpBookId()));

            if(null!=batchBookQueryDto.getProviderId())
                query.addCriteria(Criteria.where("providerId").is(batchBookQueryDto.getProviderId()));

            if(StringUtils.isNotBlank(batchBookQueryDto.getBookName())){
                if(null!=batchBookQueryDto.getIsLikeBookName() && batchBookQueryDto.getIsLikeBookName()==1){
                    query.addCriteria(Criteria.where("bookName").regex(QueryLikeUtils.normalizeKeyword(batchBookQueryDto.getBookName())));
                }else{
                    query.addCriteria(Criteria.where("bookName").is(batchBookQueryDto.getBookName()));
                }
            }

            if(StringUtils.isNotBlank(batchBookQueryDto.getAuthor()))
                query.addCriteria(Criteria.where("author").is(batchBookQueryDto.getAuthor()));

        }

        return query;
    }

    @Override
    public long count(BatchBookQueryDto batchBookQueryDto) {
        return mongoOperations.count(getQuery(batchBookQueryDto), BatchBook.class);
    }

    @Override
    public List<BatchBook> find(BatchBookQueryDto batchBookQueryDto) {
        Query query = getQuery(batchBookQueryDto);

        //默认升序
        Sort.Direction direction = Sort.Direction.ASC;
        if(null!=batchBookQueryDto && null!=batchBookQueryDto.getIsDesc() && batchBookQueryDto.getIsDesc()==1){
            direction = Sort.Direction.DESC;
        }
        //排序
        query.with(new Sort(direction, Constants.MONGODB_ID_KEY));

        return mongoOperations.find(query, BatchBook.class);
    }

    @Override
    public Page<BatchBook> getPage(BatchBookQueryDto batchBookQueryDto) {
        Query query = getQuery(batchBookQueryDto);

        //总数
        long count = 0l;
        if (null!=batchBookQueryDto && batchBookQueryDto.getStart() != null) {
            count = mongoOperations.count(query, BatchBook.class);
            //分页
            query.skip(batchBookQueryDto.getStart()).limit(batchBookQueryDto.getLimit());
        }

        //默认升序
        Sort.Direction direction = Sort.Direction.ASC;
        if(null!=batchBookQueryDto && null!=batchBookQueryDto.getIsDesc() && batchBookQueryDto.getIsDesc()==1){
            direction = Sort.Direction.DESC;
        }
        //排序
        query.with(new Sort(direction, Constants.MONGODB_ID_KEY));

        return new Page<>(mongoOperations.find(query, BatchBook.class), count);
    }

    @Override
    public void removeMulti(List<Long> ids) {
        mongoOperations.remove(new Query(Criteria.where("id").in(ids)), BatchBook.class);
    }
}
