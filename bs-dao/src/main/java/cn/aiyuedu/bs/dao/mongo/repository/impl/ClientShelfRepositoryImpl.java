package cn.aiyuedu.bs.dao.mongo.repository.impl;

import cn.aiyuedu.bs.common.Constants;
import cn.aiyuedu.bs.common.orm.Page;
import cn.aiyuedu.bs.dao.dto.ClientShelfQueryDto;
import cn.aiyuedu.bs.dao.entity.ClientShelf;
import cn.aiyuedu.bs.dao.mongo.SequenceDao;
import cn.aiyuedu.bs.dao.mongo.repository.custom.ClientShelfRepositoryCustom;
import cn.aiyuedu.bs.dao.util.QueryLikeUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.*;

/**
 * Created by Thinkpad on 2015/1/4.
 */
public class ClientShelfRepositoryImpl implements ClientShelfRepositoryCustom {

    @Autowired
    private MongoOperations mongoOperations;
    @Autowired
    private SequenceDao sequenceDao;

    @Override
    public ClientShelf persist(ClientShelf clientShelf) {
        if (clientShelf != null) {
            if (clientShelf.getId() == null) {
                clientShelf.setId(sequenceDao.getSequence(
                        mongoOperations.getCollectionName(ClientShelf.class)));
                mongoOperations.insert(clientShelf);
            } else {
                mongoOperations.save(clientShelf);
            }
        }

        return clientShelf;
    }

    private Query getQuery(ClientShelfQueryDto clientShelfQueryDto) {
        Query query = new Query();

        //如果查询对象不为空才增加过滤条件,否则查询所有信息
        if(null!=clientShelfQueryDto){
            //这里使用platformId为0表示"全部"
            if(null!=clientShelfQueryDto.getPlatformId() && clientShelfQueryDto.getPlatformId()!=0)
                query.addCriteria(Criteria.where("platformId").is(clientShelfQueryDto.getPlatformId()));

            if(StringUtils.isNotBlank(clientShelfQueryDto.getBookIds())){
                if(null!=clientShelfQueryDto.getIsLikeBookId() && clientShelfQueryDto.getIsLikeBookId()==1){
                    query.addCriteria(Criteria.where("bookIds").regex(QueryLikeUtils.normalizeKeyword(clientShelfQueryDto.getBookIds())));
                }else{
                    query.addCriteria(Criteria.where("bookIds").is(clientShelfQueryDto.getBookIds()));
                }
            }

            if(StringUtils.isNotBlank(clientShelfQueryDto.getDitchIds())){
                if(null!=clientShelfQueryDto.getIsLikeDitchId() && clientShelfQueryDto.getIsLikeDitchId()==1){
                    query.addCriteria(Criteria.where("ditchIds").regex(QueryLikeUtils.normalizeKeyword(clientShelfQueryDto.getDitchIds())));
                }else{
                    query.addCriteria(Criteria.where("ditchIds").is(clientShelfQueryDto.getDitchIds()));
                }
            }

            if(StringUtils.isNotBlank(clientShelfQueryDto.getVersion())){
                if(null!=clientShelfQueryDto.getIsLikeVersion() && clientShelfQueryDto.getIsLikeVersion()==1){
                    query.addCriteria(Criteria.where("version").regex(QueryLikeUtils.normalizeKeyword(clientShelfQueryDto.getVersion())));
                }else{
                    query.addCriteria(Criteria.where("version").is(clientShelfQueryDto.getVersion()));
                }
            }

            if(null!=clientShelfQueryDto.getStatus())
                query.addCriteria(Criteria.where("status").is(clientShelfQueryDto.getStatus()));

            //区间
            if (clientShelfQueryDto.getStartCreateDate()!= null || clientShelfQueryDto.getEndCreateDate() != null) {
                Criteria dateCriteria = Criteria.where("createDate");
                if (clientShelfQueryDto.getStartCreateDate() != null) dateCriteria.gte(clientShelfQueryDto.getStartCreateDate());
                if (clientShelfQueryDto.getEndCreateDate() != null) dateCriteria.lte(clientShelfQueryDto.getEndCreateDate());
                query.addCriteria(dateCriteria);
            }

        }

        return query;
    }

    @Override
    public long count(ClientShelfQueryDto clientShelfQueryDto) {
        return mongoOperations.count(getQuery(clientShelfQueryDto), ClientShelf.class);
    }

    @Override
    public List<ClientShelf> find(ClientShelfQueryDto clientShelfQueryDto) {
        Query query = getQuery(clientShelfQueryDto);

        //如果排序Map有值则优先使用Map
        if(null!=clientShelfQueryDto && MapUtils.isNotEmpty(clientShelfQueryDto.getOrderMap())){
            LinkedHashMap<String, Sort.Direction> orderMap = clientShelfQueryDto.getOrderMap();
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
            if(null!=clientShelfQueryDto && null!=clientShelfQueryDto.getIsDesc() && clientShelfQueryDto.getIsDesc()==1){
                direction = Sort.Direction.DESC;
            }
            //排序
            query.with(new Sort(direction, Constants.MONGODB_ID_KEY));
        }

        return mongoOperations.find(query, ClientShelf.class);
    }

    @Override
    public Page<ClientShelf> getPage(ClientShelfQueryDto clientShelfQueryDto) {
        Query query = getQuery(clientShelfQueryDto);

        //总数
        long count = 0l;
        if (null!=clientShelfQueryDto && clientShelfQueryDto.getStart() != null) {
            count = mongoOperations.count(query, ClientShelf.class);
            //分页
            query.skip(clientShelfQueryDto.getStart()).limit(clientShelfQueryDto.getLimit());
        }

        //如果排序Map有值则优先使用Map
        if(MapUtils.isNotEmpty(clientShelfQueryDto.getOrderMap())){
            LinkedHashMap<String, Sort.Direction> orderMap = clientShelfQueryDto.getOrderMap();
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
            if(null!=clientShelfQueryDto && null!=clientShelfQueryDto.getIsDesc() && clientShelfQueryDto.getIsDesc()==1){
                direction = Sort.Direction.DESC;
            }
            //排序
            query.with(new Sort(direction, Constants.MONGODB_ID_KEY));
        }

        return new Page<>(mongoOperations.find(query, ClientShelf.class), count);
    }

    @Override
    public void removeMulti(List<Integer> ids) {
        mongoOperations.remove(new Query(Criteria.where("id").in(ids)), ClientShelf.class);
    }
}
