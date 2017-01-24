package cn.aiyuedu.bs.dao.mongo.repository.impl;

import cn.aiyuedu.bs.common.Constants;
import cn.aiyuedu.bs.common.orm.Page;
import cn.aiyuedu.bs.dao.dto.ProviderQueryDto;
import cn.aiyuedu.bs.dao.mongo.SequenceDao;
import cn.aiyuedu.bs.dao.mongo.repository.custom.ProviderRepositoryCustom;
import cn.aiyuedu.bs.dao.util.QueryLikeUtils;
import cn.aiyuedu.bs.dao.entity.Provider;
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
public class ProviderRepositoryImpl implements ProviderRepositoryCustom {

    @Autowired
    private MongoOperations mongoOperations;
    @Autowired
    private SequenceDao sequenceDao;

    @Override
    public Provider persist(Provider provider) {
        if (provider != null) {
            if (provider.getId() == null) {
                provider.setId(sequenceDao.getSequence(
                        mongoOperations.getCollectionName(Provider.class)));
                mongoOperations.insert(provider);
            } else {
                mongoOperations.save(provider);
            }
        }

        return provider;
    }

    private Query getQuery(ProviderQueryDto providerQueryDto) {
        Query query = new Query();

        //如果查询对象不为空才增加过滤条件,否则查询所有信息
        if(null!=providerQueryDto){
            if(null!=providerQueryDto.getId()){
                if(null!=providerQueryDto.getIsNEId() && providerQueryDto.getIsNEId()==1){
                    query.addCriteria(Criteria.where(Constants.MONGODB_ID_KEY).ne(providerQueryDto.getId()));
                }else{
                    query.addCriteria(Criteria.where(Constants.MONGODB_ID_KEY).is(providerQueryDto.getId()));
                }
            }

            if(StringUtils.isNotBlank(providerQueryDto.getName())){
                if(null!=providerQueryDto.getIsLikeName() && providerQueryDto.getIsLikeName()==1){
                    query.addCriteria(Criteria.where("name").regex(QueryLikeUtils.normalizeKeyword(providerQueryDto.getName())));
                }else{
                    query.addCriteria(Criteria.where("name").is(providerQueryDto.getName()));
                }
            }
        }

        return query;
    }

    @Override
    public long count(ProviderQueryDto providerQueryDto) {
        return mongoOperations.count(getQuery(providerQueryDto), Provider.class);
    }

    @Override
    public List<Provider> find(ProviderQueryDto providerQueryDto) {
        Query query = getQuery(providerQueryDto);

        //默认升序
        Sort.Direction direction = Sort.Direction.ASC;
        if(null!=providerQueryDto && null!=providerQueryDto.getIsDesc() && providerQueryDto.getIsDesc()==1){
            direction = Sort.Direction.DESC;
        }
        //排序
        query.with(new Sort(direction, Constants.MONGODB_ID_KEY));

        return mongoOperations.find(query, Provider.class);
    }

    @Override
    public Page<Provider> getPage(ProviderQueryDto providerQueryDto) {
        Query query = getQuery(providerQueryDto);

        //总数
        long count = 0l;
        if (null!=providerQueryDto && providerQueryDto.getStart() != null) {
            count = mongoOperations.count(query, Provider.class);
            //分页
            query.skip(providerQueryDto.getStart()).limit(providerQueryDto.getLimit());
        }

        //默认升序
        Sort.Direction direction = Sort.Direction.ASC;
        if(null!=providerQueryDto && null!=providerQueryDto.getIsDesc() && providerQueryDto.getIsDesc()==1){
            direction = Sort.Direction.DESC;
        }
        //排序
        query.with(new Sort(direction, Constants.MONGODB_ID_KEY));

        return new Page<>(mongoOperations.find(query, Provider.class), count);
    }

    @Override
    public void removeMulti(List<Integer> ids) {
        mongoOperations.remove(new Query(Criteria.where("id").in(ids)), Provider.class);
    }
}
