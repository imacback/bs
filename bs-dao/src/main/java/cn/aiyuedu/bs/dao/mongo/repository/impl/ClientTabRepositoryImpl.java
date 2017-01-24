package cn.aiyuedu.bs.dao.mongo.repository.impl;

import cn.aiyuedu.bs.common.Constants;
import cn.aiyuedu.bs.common.orm.Page;
import cn.aiyuedu.bs.dao.dto.ClientTabQueryDto;
import cn.aiyuedu.bs.dao.mongo.SequenceDao;
import cn.aiyuedu.bs.dao.mongo.repository.custom.ClientTabRepositoryCustom;
import cn.aiyuedu.bs.dao.entity.ClientTab;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

/**
 * Created by Thinkpad on 2015/1/5.
 */
public class ClientTabRepositoryImpl implements ClientTabRepositoryCustom {

    @Autowired
    private MongoOperations mongoOperations;
    @Autowired
    private SequenceDao sequenceDao;

    @Override
    public ClientTab persist(ClientTab clientTab) {
        if (clientTab != null) {
            if (clientTab.getId() == null) {
                clientTab.setId(sequenceDao.getSequence(
                        mongoOperations.getCollectionName(ClientTab.class)));
                mongoOperations.insert(clientTab);
            } else {
                mongoOperations.save(clientTab);
            }
        }

        return clientTab;
    }

    private Query getQuery(ClientTabQueryDto clientTabQueryDto) {
        Query query = new Query();

        //如果查询对象不为空才增加过滤条件,否则查询所有信息
        if(null!=clientTabQueryDto){
        }

        return query;
    }

    @Override
    public List<ClientTab> find(ClientTabQueryDto clientTabQueryDto) {
        Query query = getQuery(clientTabQueryDto);

        //默认升序
        Sort.Direction direction = Sort.Direction.ASC;
        if(null!=clientTabQueryDto && null!=clientTabQueryDto.getIsDesc() && clientTabQueryDto.getIsDesc()==1){
            direction = Sort.Direction.DESC;
        }
        //排序
        query.with(new Sort(direction, Constants.MONGODB_ID_KEY));

        return mongoOperations.find(query, ClientTab.class);
    }

    @Override
    public Page<ClientTab> getPage(ClientTabQueryDto clientTabQueryDto) {
        Query query = getQuery(clientTabQueryDto);

        //总数
        long count = 0l;
        if (null!=clientTabQueryDto && clientTabQueryDto.getStart() != null) {
            count = mongoOperations.count(query, ClientTab.class);
            //分页
            query.skip(clientTabQueryDto.getStart()).limit(clientTabQueryDto.getLimit());
        }

        //默认升序
        Sort.Direction direction = Sort.Direction.ASC;
        if(null!=clientTabQueryDto && null!=clientTabQueryDto.getIsDesc() && clientTabQueryDto.getIsDesc()==1){
            direction = Sort.Direction.DESC;
        }
        //排序
        query.with(new Sort(direction, Constants.MONGODB_ID_KEY));

        return new Page<>(mongoOperations.find(query, ClientTab.class), count);
    }

    @Override
    public void removeMulti(List<Integer> ids) {
        mongoOperations.remove(new Query(Criteria.where("id").in(ids)), ClientTab.class);
    }
}
