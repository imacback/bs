package cn.aiyuedu.bs.dao.mongo.repository.impl;

import cn.aiyuedu.bs.common.Constants;
import cn.aiyuedu.bs.common.orm.Page;
import cn.aiyuedu.bs.dao.dto.PlatformQueryDto;
import cn.aiyuedu.bs.dao.mongo.SequenceDao;
import cn.aiyuedu.bs.dao.mongo.repository.custom.PlatformRepositoryCustom;
import cn.aiyuedu.bs.dao.entity.Platform;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

/**
 * Created by Thinkpad on 2015/1/6.
 */
public class PlatformRepositoryImpl implements PlatformRepositoryCustom {

    @Autowired
    private MongoOperations mongoOperations;
    @Autowired
    private SequenceDao sequenceDao;

    @Override
    public Platform persist(Platform platform) {
        if (platform != null) {
            if (platform.getId() == null) {
                platform.setId(sequenceDao.getSequence(
                        mongoOperations.getCollectionName(Platform.class)));
                mongoOperations.insert(platform);
            } else {
                mongoOperations.save(platform);
            }
        }

        return platform;
    }

    private Query getQuery(PlatformQueryDto platformQueryDto) {
        Query query = new Query();

        //如果查询对象不为空才增加过滤条件,否则查询所有信息
        if(null!=platformQueryDto){
            if(null!=platformQueryDto.getId())
                query.addCriteria(Criteria.where("id").is(platformQueryDto.getId()));
            if(null!=platformQueryDto.getIsUse())
                query.addCriteria(Criteria.where("isUse").is(platformQueryDto.getIsUse()));

        }

        return query;
    }

    @Override
    public long count(PlatformQueryDto platformQueryDto) {
        return mongoOperations.count(getQuery(platformQueryDto), Platform.class);
    }

    @Override
    public List<Platform> find(PlatformQueryDto platformQueryDto) {
        Query query = getQuery(platformQueryDto);

        //默认升序
        Sort.Direction direction = Sort.Direction.ASC;
        if(null!=platformQueryDto && null!=platformQueryDto.getIsDesc() && platformQueryDto.getIsDesc()==1){
            direction = Sort.Direction.DESC;
        }
        //排序
        query.with(new Sort(direction, Constants.MONGODB_ID_KEY));

        return mongoOperations.find(query, Platform.class);
    }

    @Override
    public Page<Platform> getPage(PlatformQueryDto platformQueryDto) {
        Query query = getQuery(platformQueryDto);

        //总数
        long count = 0l;
        if (null!=platformQueryDto && platformQueryDto.getStart() != null) {
            count = mongoOperations.count(query, Platform.class);
            //分页
            query.skip(platformQueryDto.getStart()).limit(platformQueryDto.getLimit());
        }

        //默认升序
        Sort.Direction direction = Sort.Direction.ASC;
        if(null!=platformQueryDto && null!=platformQueryDto.getIsDesc() && platformQueryDto.getIsDesc()==1){
            direction = Sort.Direction.DESC;
        }
        //排序
        query.with(new Sort(direction, Constants.MONGODB_ID_KEY));

        return new Page<>(mongoOperations.find(query, Platform.class), count);
    }

    @Override
    public void removeMulti(List<Integer> ids) {
        mongoOperations.remove(new Query(Criteria.where("id").in(ids)), Platform.class);
    }
}
