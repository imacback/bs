package cn.aiyuedu.bs.dao.mongo.repository.impl;

import cn.aiyuedu.bs.common.Constants;
import cn.aiyuedu.bs.common.orm.Page;
import cn.aiyuedu.bs.dao.mongo.SequenceDao;
import cn.aiyuedu.bs.dao.util.QueryLikeUtils;
import cn.aiyuedu.bs.dao.dto.CategoryQueryDto;
import cn.aiyuedu.bs.dao.entity.Category;
import cn.aiyuedu.bs.dao.mongo.repository.custom.CategoryRepositoryCustom;
import com.google.common.collect.Lists;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.List;

/**
 * Description:
 *
 * @author yz.wu
 */
public class CategoryRepositoryImpl implements CategoryRepositoryCustom {

    @Autowired
    private MongoOperations mongoOperations;
    @Autowired
    private SequenceDao sequenceDao;

    @Override
    public Category persist(Category category) {
        if (category != null) {
            if (category.getId() == null) {
                category.setId(sequenceDao.getSequence(
                        mongoOperations.getCollectionName(Category.class)));
                mongoOperations.insert(category);
            } else {
                mongoOperations.save(category);
            }
        }

        return category;
    }

    @Override
    public List<Category> persist(List<Category> categories) {
        List<Category> insert = null;
        if (CollectionUtils.isNotEmpty(categories)) {
            insert = Lists.newArrayList();
            for (Category o : categories) {
                if (o != null) {
                    if (o.getId() == null) {
                        o.setId(sequenceDao.getSequence(
                                mongoOperations.getCollectionName(Category.class)));
                        insert.add(o);
                    } else {
                        mongoOperations.save(o);
                    }
                }
            }

            if (CollectionUtils.isNotEmpty(insert)) {
                mongoOperations.insertAll(insert);
            }
        }

        return categories;
    }

    @Override
    public boolean exist(Integer id, String name) {
        Query query = new Query();
        if (id != null) query.addCriteria(Criteria.where(Constants.MONGODB_ID_KEY).lt(id));
        if (StringUtils.isNotEmpty(name)) {
            query.addCriteria(Criteria.where("name").is(name));
        }
        return mongoOperations.exists(query, Category.class);
    }

    @Override
    public List<Category> find(CategoryQueryDto categoryQueryDto) {
        if (categoryQueryDto != null) {
            Query query = new Query();
            //筛选
            if (categoryQueryDto.getId() != null)
                query.addCriteria(Criteria.where(Constants.MONGODB_ID_KEY).is(categoryQueryDto.getId()));
            if (categoryQueryDto.getIsUse() != null)
                query.addCriteria(Criteria.where("isUse").is(categoryQueryDto.getIsUse()));
            if (StringUtils.isNotBlank(categoryQueryDto.getName()))
                query.addCriteria(Criteria.where("name").regex(QueryLikeUtils.normalizeKeyword(categoryQueryDto.getName())));
            if (categoryQueryDto.getParentId() != null)
                query.addCriteria(Criteria.where("parentId").is(categoryQueryDto.getParentId()));
            if (categoryQueryDto.getIsLeaf() != null)
                query.addCriteria(Criteria.where("isLeaf").is(categoryQueryDto.getIsLeaf()));
            if (categoryQueryDto.getDistributeId() != null)
                query.addCriteria(Criteria.where("distributeIds").in(categoryQueryDto.getDistributeId()));

            if (categoryQueryDto.getStart() != null) {
                //分页
                query.skip(categoryQueryDto.getStart()).limit(categoryQueryDto.getLimit());
            }

            //默认排序
            Sort.Direction direction = Sort.Direction.DESC;
            if (categoryQueryDto.getIsDesc() != null && categoryQueryDto.getIsDesc() == 0) {
                direction = Sort.Direction.ASC;
            }
            String orderBy = Constants.MONGODB_ID_KEY;
            if (categoryQueryDto.getOrderType() != null && categoryQueryDto.getOrderType() == 2) {
                orderBy = "orderId";
            }
            query.with(new Sort(direction, orderBy));

            return mongoOperations.find(query, Category.class);
        }

        return null;
    }

    @Override
    public Page<Category> getPage(CategoryQueryDto categoryQueryDto) {
        Query query = new Query();
        //总数
        long count = 0l;
        if (categoryQueryDto != null) {
            //筛选
            if (categoryQueryDto.getId() != null)
                query.addCriteria(Criteria.where(Constants.MONGODB_ID_KEY).is(categoryQueryDto.getId()));
            if (categoryQueryDto.getIsUse() != null)
                query.addCriteria(Criteria.where("isUse").is(categoryQueryDto.getIsUse()));
            if (StringUtils.isNotBlank(categoryQueryDto.getName()))
                query.addCriteria(Criteria.where("name").regex(QueryLikeUtils.normalizeKeyword(categoryQueryDto.getName())));
            if (categoryQueryDto.getParentId() != null)
                query.addCriteria(Criteria.where("parentId").is(categoryQueryDto.getParentId()));
            if (categoryQueryDto.getIsLeaf() != null)
                query.addCriteria(Criteria.where("isLeaf").is(categoryQueryDto.getIsLeaf()));
            if (categoryQueryDto.getDistributeId() != null)
                query.addCriteria(Criteria.where("distributeIds").in(categoryQueryDto.getDistributeId()));

            if (categoryQueryDto.getStart() != null) {
                count = mongoOperations.count(query, Category.class);

                //分页
                query.skip(categoryQueryDto.getStart()).limit(categoryQueryDto.getLimit());
            }

            //默认排序
            Sort.Direction direction = Sort.Direction.DESC;
            if (categoryQueryDto.getIsDesc() != null && categoryQueryDto.getIsDesc() == 0) {
                direction = Sort.Direction.ASC;
            }
            String orderBy = Constants.MONGODB_ID_KEY;
            if (categoryQueryDto.getOrderType() != null && categoryQueryDto.getOrderType() == 2) {
                orderBy = "orderId";
            }
            query.with(new Sort(direction, orderBy));
        }

        return new Page<>(mongoOperations.find(query, Category.class), count);
    }

    @Override
    public void updateOrderId(Integer id, Integer orderId) {
        mongoOperations.findAndModify(new Query(Criteria.where(Constants.MONGODB_ID_KEY).is(id)),
                new Update().set("orderId", orderId),
                FindAndModifyOptions.options().returnNew(true),
                Category.class);
    }

    @Override
    public void updateBookCount(Integer id, Integer bookCount) {
        mongoOperations.findAndModify(new Query(Criteria.where(Constants.MONGODB_ID_KEY).is(id)),
                new Update().set("bookCount", bookCount),
                FindAndModifyOptions.options().returnNew(true),
                Category.class);
    }

    @Override
    public void removeMulti(List<Integer> ids) {
        mongoOperations.remove(new Query(Criteria.where("id").in(ids)), Category.class);
    }
}
