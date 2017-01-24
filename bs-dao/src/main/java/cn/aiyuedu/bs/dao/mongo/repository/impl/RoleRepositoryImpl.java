package cn.aiyuedu.bs.dao.mongo.repository.impl;

import cn.aiyuedu.bs.common.Constants;
import cn.aiyuedu.bs.dao.entity.Menu;
import cn.aiyuedu.bs.dao.mongo.SequenceDao;
import cn.aiyuedu.bs.dao.dto.RoleQueryDto;
import cn.aiyuedu.bs.dao.entity.Role;
import cn.aiyuedu.bs.dao.mongo.repository.custom.RoleRepositoryCustom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

/**
 * Created by Thinkpad on 2014/12/26.
 */
public class RoleRepositoryImpl implements RoleRepositoryCustom {
    @Autowired
    private MongoOperations mongoOperations;
    @Autowired
    private SequenceDao sequenceDao;

    @Override
    public void drop() {
        mongoOperations.dropCollection(Role.class);
        sequenceDao.delete(mongoOperations.getCollectionName(Role.class));
    }

    @Override
    public Role persist(Role role) {
        if (role != null) {
            if (role.getId() == null) {
                role.setId(sequenceDao.getSequence(
                        mongoOperations.getCollectionName(Role.class)));
                mongoOperations.insert(role);
            } else {
                mongoOperations.save(role);
            }
        }

        return role;
    }

    private Query getQuery(RoleQueryDto roleQueryDto) {

        Query query = new Query();

        //添加过滤条件
        if(null!=roleQueryDto){
        }

        return query;
    }

    @Override
    public List<Role> find(RoleQueryDto roleQueryDto) {
        Query query = getQuery(roleQueryDto);

        //默认升序
        Sort.Direction direction = Sort.Direction.ASC;
        if(null!=roleQueryDto && null!=roleQueryDto.getIsDesc() && roleQueryDto.getIsDesc()==1){
            direction = Sort.Direction.DESC;
        }
        //排序
        query.with(new Sort(direction, Constants.MONGODB_ID_KEY));

        return mongoOperations.find(query, Role.class);
    }

    @Override
    public void removeMulti(List<Integer> ids){
        mongoOperations.remove(new Query(Criteria.where("id").in(ids)), Role.class);
    }
}
