package cn.aiyuedu.bs.dao.mongo.repository.impl;

import cn.aiyuedu.bs.common.Constants;
import cn.aiyuedu.bs.common.orm.Page;
import cn.aiyuedu.bs.dao.dto.ComponentQueryDto;
import cn.aiyuedu.bs.dao.entity.Component;
import cn.aiyuedu.bs.dao.mongo.SequenceDao;
import cn.aiyuedu.bs.dao.mongo.repository.custom.ComponentRepositoryCustom;
import cn.aiyuedu.bs.dao.util.QueryLikeUtils;
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
public class ComponentRepositoryImpl implements ComponentRepositoryCustom {

    @Autowired
    private MongoOperations mongoOperations;
    @Autowired
    private SequenceDao sequenceDao;

    @Override
    public Component persist(Component component) {
        if (component != null) {
            if (component.getId() == null) {
                component.setId(sequenceDao.getSequence(
                        mongoOperations.getCollectionName(Component.class)));
                mongoOperations.insert(component);
            } else {
                mongoOperations.save(component);
            }
        }

        return component;
    }

    private Query getQuery(ComponentQueryDto componentQueryDto) {
        Query query = new Query();

        //如果查询对象不为空才增加过滤条件,否则查询所有信息
        if(null!=componentQueryDto){
            if(null!=componentQueryDto.getId()){
                if(null!=componentQueryDto.getIsNEId() && componentQueryDto.getIsNEId()==1){
                    query.addCriteria(Criteria.where(Constants.MONGODB_ID_KEY).ne(componentQueryDto.getId()));
                }else{
                    query.addCriteria(Criteria.where(Constants.MONGODB_ID_KEY).is(componentQueryDto.getId()));
                }
            }

            if(StringUtils.isNotBlank(componentQueryDto.getName())){
                if(null!=componentQueryDto.getIsLikeName() && componentQueryDto.getIsLikeName()==1){
                    query.addCriteria(Criteria.where("name").regex(QueryLikeUtils.normalizeKeyword(componentQueryDto.getName())));
                }else{
                    query.addCriteria(Criteria.where("name").is(componentQueryDto.getName()));
                }
            }

            if(null!=componentQueryDto.getIsUse())
                query.addCriteria(Criteria.where("isUse").is(componentQueryDto.getIsUse()));
            if(null!=componentQueryDto.getContainerId())
                query.addCriteria(Criteria.where("containerId").is(componentQueryDto.getContainerId()));

        }

        return query;
    }

    @Override
    public long count(ComponentQueryDto componentQueryDto) {
        return mongoOperations.count(getQuery(componentQueryDto), Component.class);
    }

    @Override
    public List<Component> find(ComponentQueryDto componentQueryDto) {
        Query query = getQuery(componentQueryDto);

        //默认升序
        Sort.Direction direction = Sort.Direction.ASC;
        if(null!=componentQueryDto && null!=componentQueryDto.getIsDesc() && componentQueryDto.getIsDesc()==1){
            direction = Sort.Direction.DESC;
        }

        //默认排序的字段
        String order = Constants.MONGODB_ID_KEY;
        if(null!=componentQueryDto && null!=componentQueryDto.getOrderType() && componentQueryDto.getOrderType()==2){
            order = "orderId";
        }

        //排序
        query.with(new Sort(direction, order));

        return mongoOperations.find(query, Component.class);
    }

    @Override
    public Page<Component> getPage(ComponentQueryDto componentQueryDto) {
        Query query = getQuery(componentQueryDto);

        //总数
        long count = 0l;
        if (null!=componentQueryDto && componentQueryDto.getStart() != null) {
            count = mongoOperations.count(query, Component.class);
            //分页
            query.skip(componentQueryDto.getStart()).limit(componentQueryDto.getLimit());
        }

        //默认升序
        Sort.Direction direction = Sort.Direction.ASC;
        if(null!=componentQueryDto && null!=componentQueryDto.getIsDesc() && componentQueryDto.getIsDesc()==1){
            direction = Sort.Direction.DESC;
        }

        //默认排序的字段
        String order = Constants.MONGODB_ID_KEY;
        if(null!=componentQueryDto && null!=componentQueryDto.getOrderType() && componentQueryDto.getOrderType()==2){
            order = "orderId";
        }

        //排序
        query.with(new Sort(direction, order));

        return new Page<>(mongoOperations.find(query, Component.class), count);
    }

    @Override
    public void removeMulti(List<Integer> ids) {
        mongoOperations.remove(new Query(Criteria.where("id").in(ids)), Component.class);
    }
}
