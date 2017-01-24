package cn.aiyuedu.bs.dao.mongo.repository.impl;

import cn.aiyuedu.bs.common.Constants;
import cn.aiyuedu.bs.common.orm.Page;
import cn.aiyuedu.bs.dao.dto.FilterWordQueryDto;
import cn.aiyuedu.bs.dao.entity.FilterWord;
import cn.aiyuedu.bs.dao.mongo.SequenceDao;
import cn.aiyuedu.bs.dao.util.QueryLikeUtils;
import cn.aiyuedu.bs.dao.mongo.repository.custom.FilterWordRepositoryCustom;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.Date;
import java.util.List;

/**
 * Description:
 *
 * @author yz.wu
 */
public class FilterWordRepositoryImpl implements FilterWordRepositoryCustom {

    @Autowired
    private MongoOperations mongoOperations;
    @Autowired
    private SequenceDao sequenceDao;

    @Override
    public FilterWord persist(FilterWord filterWord) {
        if (filterWord != null) {
            if (filterWord.getId() == null) {
                filterWord.setId(sequenceDao.getSequence(
                        mongoOperations.getCollectionName(FilterWord.class)));
                mongoOperations.insert(filterWord);
            } else {
                mongoOperations.save(filterWord);
            }
        }

        return filterWord;
    }

    @Override
    public boolean exist(Integer id, String word) {
        Query query = new Query();
        if (id != null) query.addCriteria(Criteria.where(Constants.MONGODB_ID_KEY).lt(id));
        if (StringUtils.isNotEmpty(word)) {
            query.addCriteria(Criteria.where("wrd").is(word));
        }
        return mongoOperations.exists(query, FilterWord.class);
    }

    private Query getListQuery(FilterWordQueryDto filterWordQueryDto) {
        Query query = new Query();
        if (filterWordQueryDto != null) {
            //筛选
            if (filterWordQueryDto.getStatus() != null)
                query.addCriteria(Criteria.where("sts").is(filterWordQueryDto.getStatus()));
            if (StringUtils.isNotBlank(filterWordQueryDto.getWord()))
                query.addCriteria(Criteria.where("wrd").regex(QueryLikeUtils.normalizeKeyword(filterWordQueryDto.getWord())));
            if (filterWordQueryDto.getLevel() != null)
                query.addCriteria(Criteria.where("lev").is(filterWordQueryDto.getLevel()));
        }

        return query;
    }

    @Override
    public FilterWord findOne(String word) {
        if (StringUtils.isNotBlank(word)) {
            return mongoOperations.findOne(new Query().addCriteria(Criteria.where("wrd").is(word)), FilterWord.class);
        }

        return null;
    }

    @Override
    public List<FilterWord> find(FilterWordQueryDto filterWordQueryDto) {
        Query query = getListQuery(filterWordQueryDto);
        query.with(new Sort(Sort.Direction.DESC, "len"));
        return mongoOperations.find(query, FilterWord.class);
    }

    @Override
    public Page<FilterWord> getPage(FilterWordQueryDto filterWordQueryDto) {
        Query query = getListQuery(filterWordQueryDto);
        //总数
        long count = 0l;
        if (filterWordQueryDto != null) {
            if (filterWordQueryDto.getStart() != null) {
                count = mongoOperations.count(query, FilterWord.class);

                //分页
                query.skip(filterWordQueryDto.getStart()).limit(filterWordQueryDto.getLimit());
            }
        }
        query.with(new Sort(Sort.Direction.DESC, Constants.MONGODB_ID_KEY));

        return new Page<>(mongoOperations.find(query, FilterWord.class), count);
    }

    @Override
    public void updateStatus(List<Integer> ids, Integer status, Integer adminUserId) {
        Update update = new Update();
        Date now = new Date();
        update.set("status", status).set("editDate", now);
        if (adminUserId != null) {
            update.set("editorId", adminUserId);
        }

        mongoOperations.updateMulti(Query.query(Criteria.where(Constants.MONGODB_ID_KEY).in(ids)),
                update,
                FilterWord.class);
    }

    @Override
    public void updateLength(Integer id, Integer length) {
        mongoOperations.findAndModify(new Query(Criteria.where(Constants.MONGODB_ID_KEY).is(id)),
                new Update().set("len", length),
                FindAndModifyOptions.options().returnNew(true),
                FilterWord.class);
    }

    @Override
    public void delete(List<Integer> ids) {
        mongoOperations.remove(new Query(Criteria.where(Constants.MONGODB_ID_KEY).in(ids)), FilterWord.class);
    }
}
