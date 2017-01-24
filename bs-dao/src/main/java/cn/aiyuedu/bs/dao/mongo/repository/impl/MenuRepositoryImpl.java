package cn.aiyuedu.bs.dao.mongo.repository.impl;

import cn.aiyuedu.bs.common.Constants;
import cn.aiyuedu.bs.dao.entity.AdminUser;
import cn.aiyuedu.bs.dao.mongo.SequenceDao;
import cn.aiyuedu.bs.dao.mongo.repository.custom.MenuRepositoryCustom;
import cn.aiyuedu.bs.dao.dto.MenuQueryDto;
import cn.aiyuedu.bs.dao.entity.Menu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

/**
 * Created by Thinkpad on 2014/12/29.
 */
public class MenuRepositoryImpl implements MenuRepositoryCustom {

    @Autowired
    private MongoOperations mongoOperations;
    @Autowired
    private SequenceDao sequenceDao;

    @Override
    public void drop() {
        mongoOperations.dropCollection(Menu.class);
        sequenceDao.delete(mongoOperations.getCollectionName(Menu.class));
    }

    @Override
    public Menu persist(Menu menu) {
        if (menu != null) {
            if (menu.getId() == null) {
                menu.setId(sequenceDao.getSequence(
                        mongoOperations.getCollectionName(Menu.class)));
                mongoOperations.insert(menu);
            } else {
                mongoOperations.save(menu);
            }
        }

        return menu;
    }

    private Query getQuery(MenuQueryDto menuQueryDto) {
        Query query = new Query();

        //如果查询对象不为空才增加过滤条件,否则查询所有信息
        if(null!=menuQueryDto){
            //筛选
            if(null!=menuQueryDto.getParentId()) query.addCriteria(Criteria.where("parentId").is(menuQueryDto.getParentId()));
            if(null!=menuQueryDto.getIsUse()) query.addCriteria(Criteria.where("isUse").is(menuQueryDto.getIsUse()));
        }

        return query;
    }

    @Override
    public List<Menu> find(MenuQueryDto menuQueryDto) {
        Query query = getQuery(menuQueryDto);

        //默认升序
        Sort.Direction direction = Sort.Direction.ASC;
        if(null!=menuQueryDto && null!=menuQueryDto.getIsDesc() && menuQueryDto.getIsDesc()==1){
            direction = Sort.Direction.DESC;
        }
        //排序
        query.with(new Sort(direction, Constants.MONGODB_ID_KEY));

        return mongoOperations.find(query, Menu.class);
    }

    @Override
    public void removeMulti(List<Integer> ids){
        mongoOperations.remove(new Query(Criteria.where("id").in(ids)), Menu.class);
    }
}
