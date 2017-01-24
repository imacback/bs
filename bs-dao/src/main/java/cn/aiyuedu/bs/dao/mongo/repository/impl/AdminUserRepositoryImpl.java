package cn.aiyuedu.bs.dao.mongo.repository.impl;

import cn.aiyuedu.bs.common.Constants;
import cn.aiyuedu.bs.common.orm.Page;
import cn.aiyuedu.bs.dao.dto.AdminUserQueryDto;
import cn.aiyuedu.bs.dao.mongo.SequenceDao;
import cn.aiyuedu.bs.dao.mongo.repository.custom.AdminUserRepositoryCustom;
import cn.aiyuedu.bs.dao.util.QueryLikeUtils;
import cn.aiyuedu.bs.dao.entity.AdminUser;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

/**
 * Created by Thinkpad on 2014/12/28.
 */
public class AdminUserRepositoryImpl implements AdminUserRepositoryCustom {

    @Autowired
    private MongoOperations mongoOperations;
    @Autowired
    private SequenceDao sequenceDao;

    @Override
    public void drop() {
        mongoOperations.dropCollection(AdminUser.class);
        sequenceDao.delete(mongoOperations.getCollectionName(AdminUser.class));
    }

    @Override
    public AdminUser persist(AdminUser adminUser) {
        if (adminUser != null) {
            if (adminUser.getId() == null) {
                adminUser.setId(sequenceDao.getSequence(
                        mongoOperations.getCollectionName(AdminUser.class)));
                mongoOperations.insert(adminUser);
            } else {
                mongoOperations.save(adminUser);
            }
        }

        return adminUser;
    }

    private Query getQuery(AdminUserQueryDto adminUserQueryDto) {
        Query query = new Query();

        //如果查询对象不为空才增加过滤条件,否则查询所有信息
        if(null!=adminUserQueryDto){
            if(null!=adminUserQueryDto.getId()){
                if(null!=adminUserQueryDto.getIsNEId() && adminUserQueryDto.getIsNEId()==1){
                    query.addCriteria(Criteria.where(Constants.MONGODB_ID_KEY).ne(adminUserQueryDto.getId()));
                }else{
                    query.addCriteria(Criteria.where(Constants.MONGODB_ID_KEY).is(adminUserQueryDto.getId()));
                }
            }

            if(StringUtils.isNotBlank(adminUserQueryDto.getName())){
                if(null!=adminUserQueryDto.getIsLikeName() && adminUserQueryDto.getIsLikeName()==1){
                    query.addCriteria(Criteria.where("name").regex(QueryLikeUtils.normalizeKeyword(adminUserQueryDto.getName())));
                }else{
                    query.addCriteria(Criteria.where("name").is(adminUserQueryDto.getName()));
                }
            }

            if(StringUtils.isNotBlank(adminUserQueryDto.getEmail())){
                if(null!=adminUserQueryDto.getIsLikeEmail() && adminUserQueryDto.getIsLikeEmail()==1){
                    query.addCriteria(Criteria.where("email").regex(QueryLikeUtils.normalizeKeyword(adminUserQueryDto.getEmail())));
                }else{
                    query.addCriteria(Criteria.where("email").is(adminUserQueryDto.getEmail()));
                }
            }

            if(StringUtils.isNotBlank(adminUserQueryDto.getNickname())){
                if(null!=adminUserQueryDto.getIsLikeNickname() && adminUserQueryDto.getIsLikeNickname()==1){
                    query.addCriteria(Criteria.where("nickname").regex(QueryLikeUtils.normalizeKeyword(adminUserQueryDto.getNickname())));
                }else{
                    query.addCriteria(Criteria.where("nickname").is(adminUserQueryDto.getNickname()));
                }
            }

            if(StringUtils.isNotBlank(adminUserQueryDto.getPassword())) query.addCriteria(Criteria.where("password").is(adminUserQueryDto.getPassword()));
            if(null!=adminUserQueryDto.getIsUse()) query.addCriteria(Criteria.where("isUse").is(adminUserQueryDto.getIsUse()));
            if(null!=adminUserQueryDto.getRoleId()) query.addCriteria(Criteria.where("roleId").is(adminUserQueryDto.getRoleId()));

        }

        return query;
    }

    @Override
    public long count(AdminUserQueryDto adminUserQueryDto) {
        return mongoOperations.count(getQuery(adminUserQueryDto), AdminUser.class);
    }

    @Override
    public List<AdminUser> find(AdminUserQueryDto adminUserQueryDto) {
        Query query = getQuery(adminUserQueryDto);

        //默认升序
        Sort.Direction direction = Sort.Direction.ASC;
        if(null!=adminUserQueryDto && null!=adminUserQueryDto.getIsDesc() && adminUserQueryDto.getIsDesc()==1){
            direction = Sort.Direction.DESC;
        }
        //排序
        query.with(new Sort(direction, Constants.MONGODB_ID_KEY));

        return mongoOperations.find(query, AdminUser.class);
    }

    @Override
    public Page<AdminUser> getPage(AdminUserQueryDto adminUserQueryDto) {
        Query query = getQuery(adminUserQueryDto);

        //总数
        long count = 0l;
        if (null!=adminUserQueryDto && adminUserQueryDto.getStart() != null) {
            count = mongoOperations.count(query, AdminUser.class);
            //分页
            query.skip(adminUserQueryDto.getStart()).limit(adminUserQueryDto.getLimit());
        }

        //默认升序
        Sort.Direction direction = Sort.Direction.ASC;
        if(null!=adminUserQueryDto && null!=adminUserQueryDto.getIsDesc() && adminUserQueryDto.getIsDesc()==1){
            direction = Sort.Direction.DESC;
        }
        //排序
        query.with(new Sort(direction, Constants.MONGODB_ID_KEY));

        return new Page<>(mongoOperations.find(query, AdminUser.class), count);
    }

    @Override
    public void removeMulti(List<Integer> ids) {
        mongoOperations.remove(new Query(Criteria.where("id").in(ids)), AdminUser.class);
    }
}
