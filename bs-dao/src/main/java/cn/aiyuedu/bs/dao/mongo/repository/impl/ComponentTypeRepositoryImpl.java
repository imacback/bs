package cn.aiyuedu.bs.dao.mongo.repository.impl;

import cn.aiyuedu.bs.common.Constants;
import cn.aiyuedu.bs.common.orm.Page;
import cn.aiyuedu.bs.dao.dto.ComponentTypeQueryDto;
import cn.aiyuedu.bs.dao.entity.ComponentType;
import cn.aiyuedu.bs.dao.mongo.SequenceDao;
import cn.aiyuedu.bs.dao.mongo.repository.custom.ComponentTypeRepositoryCustom;
import cn.aiyuedu.bs.dao.util.QueryLikeUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

/**
 * Created by Thinkpad on 2015/1/5.
 */
public class ComponentTypeRepositoryImpl implements ComponentTypeRepositoryCustom {

    @Autowired
    private MongoOperations mongoOperations;
    @Autowired
    private SequenceDao sequenceDao;

    @Override
    public ComponentType persist(ComponentType componentType) {
        if (componentType != null) {
            if (componentType.getId() == null) {
                componentType.setId(sequenceDao.getSequence(
                        mongoOperations.getCollectionName(ComponentType.class)));
                mongoOperations.insert(componentType);
            } else {
                mongoOperations.save(componentType);
            }
        }

        return componentType;
    }

    private Query getQuery(ComponentTypeQueryDto componentTypeQueryDto) {
        Query query = new Query();

        //如果查询对象不为空才增加过滤条件,否则查询所有信息
        if(null!=componentTypeQueryDto){
            if(null!=componentTypeQueryDto.getId()){
                if(null!=componentTypeQueryDto.getIsNEId() && componentTypeQueryDto.getIsNEId()==1){
                    query.addCriteria(Criteria.where(Constants.MONGODB_ID_KEY).ne(componentTypeQueryDto.getId()));
                }else{
                    query.addCriteria(Criteria.where(Constants.MONGODB_ID_KEY).is(componentTypeQueryDto.getId()));
                }
            }

            if(StringUtils.isNotBlank(componentTypeQueryDto.getName())){
                if(null!=componentTypeQueryDto.getIsLikeName() && componentTypeQueryDto.getIsLikeName()==1){
                    query.addCriteria(Criteria.where("name").regex(QueryLikeUtils.normalizeKeyword(componentTypeQueryDto.getName())));
                }else{
                    query.addCriteria(Criteria.where("name").is(componentTypeQueryDto.getName()));
                }
            }

        }

        return query;
    }

    @Override
    public long count(ComponentTypeQueryDto componentTypeQueryDto) {
        return mongoOperations.count(getQuery(componentTypeQueryDto), ComponentType.class);
    }

    @Override
    public List<ComponentType> find(ComponentTypeQueryDto componentTypeQueryDto) {
        Query query = getQuery(componentTypeQueryDto);

        //默认升序
        Sort.Direction direction = Sort.Direction.ASC;
        if(null!=componentTypeQueryDto && null!=componentTypeQueryDto.getIsDesc() && componentTypeQueryDto.getIsDesc()==1){
            direction = Sort.Direction.DESC;
        }
        //排序
        query.with(new Sort(direction, Constants.MONGODB_ID_KEY));

        return mongoOperations.find(query, ComponentType.class);
    }

    @Override
    public Page<ComponentType> getPage(ComponentTypeQueryDto componentTypeQueryDto) {
        Query query = getQuery(componentTypeQueryDto);

        //总数
        long count = 0l;
        if (null!=componentTypeQueryDto && componentTypeQueryDto.getStart() != null) {
            count = mongoOperations.count(query, ComponentType.class);
            //分页
            query.skip(componentTypeQueryDto.getStart()).limit(componentTypeQueryDto.getLimit());
        }

        //默认升序
        Sort.Direction direction = Sort.Direction.ASC;
        if(null!=componentTypeQueryDto && null!=componentTypeQueryDto.getIsDesc() && componentTypeQueryDto.getIsDesc()==1){
            direction = Sort.Direction.DESC;
        }
        //排序
        query.with(new Sort(direction, Constants.MONGODB_ID_KEY));

        return new Page<>(mongoOperations.find(query, ComponentType.class), count);
    }

    @Override
    public void removeMulti(List<Integer> ids) {
        mongoOperations.remove(new Query(Criteria.where("id").in(ids)), ComponentType.class);
    }
}
