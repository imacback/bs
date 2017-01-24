package cn.aiyuedu.bs.dao.mongo.repository.impl;

import cn.aiyuedu.bs.common.Constants;
import cn.aiyuedu.bs.common.orm.Page;
import cn.aiyuedu.bs.dao.mongo.SequenceDao;
import cn.aiyuedu.bs.dao.dto.ComponentDataQueryDto;
import cn.aiyuedu.bs.dao.entity.ComponentData;
import cn.aiyuedu.bs.dao.mongo.repository.custom.ComponentDataRepositoryCustom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

/**
 * Created by Thinkpad on 2015/1/6.
 */
public class ComponentDataRepositoryImpl implements ComponentDataRepositoryCustom {

    @Autowired
    private MongoOperations mongoOperations;
    @Autowired
    private SequenceDao sequenceDao;

    @Override
    public ComponentData persist(ComponentData componentData) {
        if (componentData != null) {
            if (componentData.getId() == null) {
                componentData.setId(sequenceDao.getSequence(
                        mongoOperations.getCollectionName(ComponentData.class)));
                mongoOperations.insert(componentData);
            } else {
                mongoOperations.save(componentData);
            }
        }

        return componentData;
    }

    private Query getQuery(ComponentDataQueryDto componentDataQueryDto) {
        Query query = new Query();

        //如果查询对象不为空才增加过滤条件,否则查询所有信息
        if(null!=componentDataQueryDto){
            if(null!=componentDataQueryDto.getComponentId())
                query.addCriteria(Criteria.where("componentId").is(componentDataQueryDto.getComponentId()));

            if(null!=componentDataQueryDto.getOrderId())
                query.addCriteria(Criteria.where("orderId").is(componentDataQueryDto.getOrderId()));

            if(null!=componentDataQueryDto.getGroupId())
                query.addCriteria(Criteria.where("groupId").is(componentDataQueryDto.getGroupId()));

        }

        return query;
    }

    @Override
    public long count(ComponentDataQueryDto componentDataQueryDto) {
        return mongoOperations.count(getQuery(componentDataQueryDto), ComponentData.class);
    }

    @Override
    public List<ComponentData> find(ComponentDataQueryDto componentDataQueryDto) {
        Query query = getQuery(componentDataQueryDto);

        //默认升序
        Sort.Direction direction = Sort.Direction.ASC;
        if(null!=componentDataQueryDto && null!=componentDataQueryDto.getIsDesc() && componentDataQueryDto.getIsDesc()==1){
            direction = Sort.Direction.DESC;
        }

        //默认排序的字段
        String order = Constants.MONGODB_ID_KEY;
        if(null!=componentDataQueryDto && null!=componentDataQueryDto.getOrderType() && componentDataQueryDto.getOrderType()==2){
            order = "orderId";
        }

        //排序
        query.with(new Sort(direction, order));

        return mongoOperations.find(query, ComponentData.class);
    }

    @Override
    public Page<ComponentData> getPage(ComponentDataQueryDto componentDataQueryDto) {
        Query query = getQuery(componentDataQueryDto);

        //总数
        long count = 0l;
        if (null!=componentDataQueryDto && componentDataQueryDto.getStart() != null) {
            count = mongoOperations.count(query, ComponentData.class);
            //分页
            query.skip(componentDataQueryDto.getStart()).limit(componentDataQueryDto.getLimit());
        }

        //默认升序
        Sort.Direction direction = Sort.Direction.ASC;
        if(null!=componentDataQueryDto && null!=componentDataQueryDto.getIsDesc() && componentDataQueryDto.getIsDesc()==1){
            direction = Sort.Direction.DESC;
        }

        //默认排序的字段
        String order = Constants.MONGODB_ID_KEY;
        if(null!=componentDataQueryDto && null!=componentDataQueryDto.getOrderType() && componentDataQueryDto.getOrderType()==2){
            order = "orderId";
        }

        //排序
        query.with(new Sort(direction, order));

        return new Page<>(mongoOperations.find(query, ComponentData.class), count);
    }

    @Override
    public void removeMulti(List<Integer> ids) {
        mongoOperations.remove(new Query(Criteria.where("id").in(ids)), ComponentData.class);
    }
}
