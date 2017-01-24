package cn.aiyuedu.bs.dao.mongo.repository.impl;

import cn.aiyuedu.bs.common.Constants;
import cn.aiyuedu.bs.common.orm.Page;
import cn.aiyuedu.bs.dao.dto.ContainerQueryDto;
import cn.aiyuedu.bs.dao.mongo.SequenceDao;
import cn.aiyuedu.bs.dao.util.QueryLikeUtils;
import cn.aiyuedu.bs.dao.entity.Container;
import cn.aiyuedu.bs.dao.mongo.repository.custom.ContainerRepositoryCustom;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

/**
 * Created by Thinkpad on 2015/1/6.
 */
public class ContainerRepositoryImpl implements ContainerRepositoryCustom {

    @Autowired
    private MongoOperations mongoOperations;
    @Autowired
    private SequenceDao sequenceDao;

    @Override
    public Container persist(Container container) {
        if (container != null) {
            if (container.getId() == null) {
                container.setId(sequenceDao.getSequence(
                        mongoOperations.getCollectionName(Container.class)));
                mongoOperations.insert(container);
            } else {
                mongoOperations.save(container);
            }
        }

        return container;
    }

    private Query getQuery(ContainerQueryDto containerQueryDto) {
        Query query = new Query();

        //如果查询对象不为空才增加过滤条件,否则查询所有信息
        if(null!=containerQueryDto){
            if(null!=containerQueryDto.getId()){
                if(null!=containerQueryDto.getIsNEId() && containerQueryDto.getIsNEId()==1){
                    query.addCriteria(Criteria.where(Constants.MONGODB_ID_KEY).ne(containerQueryDto.getId()));
                }else{
                    query.addCriteria(Criteria.where(Constants.MONGODB_ID_KEY).is(containerQueryDto.getId()));
                }
            }

            if(StringUtils.isNotBlank(containerQueryDto.getName())){
                if(null!=containerQueryDto.getIsLikeName() && containerQueryDto.getIsLikeName()==1){
                    query.addCriteria(Criteria.where("name").regex(QueryLikeUtils.normalizeKeyword(containerQueryDto.getName())));
                }else{
                    query.addCriteria(Criteria.where("name").is(containerQueryDto.getName()));
                }
            }

            if(null!=containerQueryDto.getIsUse())
                query.addCriteria(Criteria.where("isUse").is(containerQueryDto.getIsUse()));

        }

        return query;
    }

    @Override
    public long count(ContainerQueryDto containerQueryDto) {
        return mongoOperations.count(getQuery(containerQueryDto), Container.class);
    }

    @Override
    public List<Container> find(ContainerQueryDto containerQueryDto) {
        Query query = getQuery(containerQueryDto);

        //默认升序
        Sort.Direction direction = Sort.Direction.ASC;
        if(null!=containerQueryDto && null!=containerQueryDto.getIsDesc() && containerQueryDto.getIsDesc()==1){
            direction = Sort.Direction.DESC;
        }
        //排序
        query.with(new Sort(direction, Constants.MONGODB_ID_KEY));

        return mongoOperations.find(query, Container.class);
    }

    @Override
    public Page<Container> getPage(ContainerQueryDto containerQueryDto) {
        Query query = getQuery(containerQueryDto);

        //总数
        long count = 0l;
        if (null!=containerQueryDto && containerQueryDto.getStart() != null) {
            count = mongoOperations.count(query, Container.class);
            //分页
            query.skip(containerQueryDto.getStart()).limit(containerQueryDto.getLimit());
        }

        //默认升序
        Sort.Direction direction = Sort.Direction.ASC;
        if(null!=containerQueryDto && null!=containerQueryDto.getIsDesc() && containerQueryDto.getIsDesc()==1){
            direction = Sort.Direction.DESC;
        }
        //排序
        query.with(new Sort(direction, Constants.MONGODB_ID_KEY));

        return new Page<>(mongoOperations.find(query, Container.class), count);
    }

    @Override
    public void removeMulti(List<Integer> ids) {
        mongoOperations.remove(new Query(Criteria.where("id").in(ids)), Container.class);
    }
}
