package cn.aiyuedu.bs.dao.mongo.repository.impl;

import cn.aiyuedu.bs.common.Constants;
import cn.aiyuedu.bs.common.orm.Page;
import cn.aiyuedu.bs.dao.dto.ComponentDataGroupQueryDto;
import cn.aiyuedu.bs.dao.entity.ComponentDataGroup;
import cn.aiyuedu.bs.dao.mongo.SequenceDao;
import cn.aiyuedu.bs.dao.mongo.repository.custom.ComponentDataGroupRepositoryCustom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

/**
 * Created by Thinkpad on 2015/1/6.
 */
public class ComponentDataGroupRepositoryImpl implements ComponentDataGroupRepositoryCustom {

    @Autowired
    private MongoOperations mongoOperations;
    @Autowired
    private SequenceDao sequenceDao;

    @Override
    public ComponentDataGroup persist(ComponentDataGroup componentDataGroup) {
        if (componentDataGroup != null) {
            if (componentDataGroup.getId() == null) {
                componentDataGroup.setId(sequenceDao.getSequence(
                        mongoOperations.getCollectionName(ComponentDataGroup.class)));
                mongoOperations.insert(componentDataGroup);
            } else {
                mongoOperations.save(componentDataGroup);
            }
        }

        return componentDataGroup;
    }

    private Query getQuery(ComponentDataGroupQueryDto componentDataGroupQueryDto) {
        Query query = new Query();

        //如果查询对象不为空才增加过滤条件,否则查询所有信息
        if(null!=componentDataGroupQueryDto){

            if(null!=componentDataGroupQueryDto.getComponentId())
                query.addCriteria(Criteria.where("componentId").is(componentDataGroupQueryDto.getComponentId()));

        }

        return query;
    }

    @Override
    public long count(ComponentDataGroupQueryDto componentDataGroupQueryDto) {
        return mongoOperations.count(getQuery(componentDataGroupQueryDto), ComponentDataGroup.class);
    }

    @Override
    public List<ComponentDataGroup> find(ComponentDataGroupQueryDto componentDataGroupQueryDto) {
        Query query = getQuery(componentDataGroupQueryDto);

        //默认升序
        Sort.Direction direction = Sort.Direction.ASC;
        if(null!=componentDataGroupQueryDto && null!=componentDataGroupQueryDto.getIsDesc() && componentDataGroupQueryDto.getIsDesc()==1){
            direction = Sort.Direction.DESC;
        }

        //默认排序的字段
        String order = Constants.MONGODB_ID_KEY;
        if(null!=componentDataGroupQueryDto && null!=componentDataGroupQueryDto.getOrderType() && componentDataGroupQueryDto.getOrderType()==2){
            order = "orderId";
        }

        //排序
        query.with(new Sort(direction, order));

        return mongoOperations.find(query, ComponentDataGroup.class);
    }

    @Override
    public Page<ComponentDataGroup> getPage(ComponentDataGroupQueryDto componentDataGroupQueryDto) {
        Query query = getQuery(componentDataGroupQueryDto);

        //总数
        long count = 0l;
        if (null!=componentDataGroupQueryDto && componentDataGroupQueryDto.getStart() != null) {
            count = mongoOperations.count(query, ComponentDataGroup.class);
            //分页
            query.skip(componentDataGroupQueryDto.getStart()).limit(componentDataGroupQueryDto.getLimit());
        }

        //默认升序
        Sort.Direction direction = Sort.Direction.ASC;
        if(null!=componentDataGroupQueryDto && null!=componentDataGroupQueryDto.getIsDesc() && componentDataGroupQueryDto.getIsDesc()==1){
            direction = Sort.Direction.DESC;
        }

        //默认排序的字段
        String order = Constants.MONGODB_ID_KEY;
        if(null!=componentDataGroupQueryDto && null!=componentDataGroupQueryDto.getOrderType() && componentDataGroupQueryDto.getOrderType()==2){
            order = "orderId";
        }

        //排序
        query.with(new Sort(direction, order));

        return new Page<>(mongoOperations.find(query, ComponentDataGroup.class), count);
    }

    @Override
    public void removeMulti(List<Integer> ids) {
        mongoOperations.remove(new Query(Criteria.where("id").in(ids)), ComponentDataGroup.class);
    }
}
