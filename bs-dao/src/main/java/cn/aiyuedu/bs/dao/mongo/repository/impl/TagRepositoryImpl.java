package cn.aiyuedu.bs.dao.mongo.repository.impl;

import cn.aiyuedu.bs.common.Constants;
import cn.aiyuedu.bs.common.orm.Page;
import cn.aiyuedu.bs.dao.mongo.SequenceDao;
import cn.aiyuedu.bs.dao.mongo.repository.custom.TagRepositoryCustom;
import cn.aiyuedu.bs.dao.util.QueryLikeUtils;
import cn.aiyuedu.bs.dao.dto.TagQueryDto;
import cn.aiyuedu.bs.dao.entity.Tag;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.*;

/**
 * Created by Thinkpad on 2015/1/7.
 */
public class TagRepositoryImpl implements TagRepositoryCustom {

    @Autowired
    private MongoOperations mongoOperations;
    @Autowired
    private SequenceDao sequenceDao;

    @Override
    public Tag persist(Tag tag) {
        if (tag != null) {
            if (tag.getId() == null) {
                tag.setId(sequenceDao.getSequence(
                        mongoOperations.getCollectionName(Tag.class)));
                mongoOperations.insert(tag);
            } else {
                mongoOperations.save(tag);
            }
        }

        return tag;
    }

    private Query getQuery(TagQueryDto tagQueryDto) {
        Query query = new Query();

        //如果查询对象不为空才增加过滤条件,否则查询所有信息
        if(null!=tagQueryDto){

            if(null!=tagQueryDto.getId()){
                if(null!=tagQueryDto.getIsNEId() && tagQueryDto.getIsNEId()==1){
                    query.addCriteria(Criteria.where(Constants.MONGODB_ID_KEY).ne(tagQueryDto.getId()));
                }else{
                    query.addCriteria(Criteria.where(Constants.MONGODB_ID_KEY).is(tagQueryDto.getId()));
                }
            }

            if(null!=tagQueryDto.getIsUse())
                query.addCriteria(Criteria.where("isUse").is(tagQueryDto.getIsUse()));

            if(StringUtils.isNotBlank(tagQueryDto.getName())){
                if(null!=tagQueryDto.getIsLikeName() && tagQueryDto.getIsLikeName()==1){
                    query.addCriteria(Criteria.where("name").regex(QueryLikeUtils.normalizeKeyword(tagQueryDto.getName())));
                }else{
                    query.addCriteria(Criteria.where("name").is(tagQueryDto.getName()));
                }
            }

            if(null!=tagQueryDto.getParentId())
                query.addCriteria(Criteria.where("parentId").is(tagQueryDto.getParentId()));

            if(null!=tagQueryDto.getIsLeaf())
                query.addCriteria(Criteria.where("isLeaf").is(tagQueryDto.getIsLeaf()));

            if(null!=tagQueryDto.getTypeId())
                query.addCriteria(Criteria.where("typeId").is(tagQueryDto.getTypeId()));

            if(StringUtils.isNotBlank(tagQueryDto.getScope())){
                if(null!=tagQueryDto.getIsNotNullScope() && tagQueryDto.getIsNotNullScope()==1){
                    query.addCriteria(Criteria.where("scope").ne(null));
                }else{
                    query.addCriteria(Criteria.where("scope").is(tagQueryDto.getScope()));
                }
            }
        }

        return query;
    }

    @Override
    public long count(TagQueryDto tagQueryDto) {
        return mongoOperations.count(getQuery(tagQueryDto), Tag.class);
    }

    @Override
    public List<Tag> find(TagQueryDto tagQueryDto) {
        Query query = getQuery(tagQueryDto);

        //如果排序Map有值则优先使用Map
        if(null!=tagQueryDto && MapUtils.isNotEmpty(tagQueryDto.getOrderMap())){
            LinkedHashMap<String, Sort.Direction> orderMap = tagQueryDto.getOrderMap();
            Set<Map.Entry<String, Sort.Direction>> setEntry = orderMap.entrySet();
            Iterator<Map.Entry<String, Sort.Direction>> it = setEntry.iterator();

            Map.Entry<String, Sort.Direction> entry = null;
            while(it.hasNext()){
                entry = it.next();
                query.with(new Sort(entry.getValue(), entry.getKey()));
            }
        }else{
            //默认升序
            Sort.Direction direction = Sort.Direction.ASC;
            if(null!=tagQueryDto && null!=tagQueryDto.getIsDesc() && tagQueryDto.getIsDesc()==1){
                direction = Sort.Direction.DESC;
            }
            //排序
            query.with(new Sort(direction, Constants.MONGODB_ID_KEY));
        }

        return mongoOperations.find(query, Tag.class);
    }

    @Override
    public Page<Tag> getPage(TagQueryDto tagQueryDto) {
        Query query = getQuery(tagQueryDto);

        //总数
        long count = 0l;
        if (null!=tagQueryDto && tagQueryDto.getStart() != null) {
            count = mongoOperations.count(query, Tag.class);
            //分页
            query.skip(tagQueryDto.getStart()).limit(tagQueryDto.getLimit());
        }

        //如果排序Map有值则优先使用Map
        if(null!=tagQueryDto && MapUtils.isNotEmpty(tagQueryDto.getOrderMap())){
            LinkedHashMap<String, Sort.Direction> orderMap = tagQueryDto.getOrderMap();
            Set<Map.Entry<String, Sort.Direction>> setEntry = orderMap.entrySet();
            Iterator<Map.Entry<String, Sort.Direction>> it = setEntry.iterator();

            Map.Entry<String, Sort.Direction> entry = null;
            while(it.hasNext()){
                entry = it.next();
                query.with(new Sort(entry.getValue(), entry.getKey()));
            }
        }else{
            //默认升序
            Sort.Direction direction = Sort.Direction.ASC;
            if(null!=tagQueryDto && null!=tagQueryDto.getIsDesc() && tagQueryDto.getIsDesc()==1){
                direction = Sort.Direction.DESC;
            }
            //排序
            query.with(new Sort(direction, Constants.MONGODB_ID_KEY));
        }

        return new Page<>(mongoOperations.find(query, Tag.class), count);
    }

    @Override
    public void removeMulti(List<Integer> ids) {
        mongoOperations.remove(new Query(Criteria.where("id").in(ids)), Tag.class);
    }
}
