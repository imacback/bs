package cn.aiyuedu.bs.dao.mongo.repository.impl;

import cn.aiyuedu.bs.common.Constants;
import cn.aiyuedu.bs.common.orm.Page;
import cn.aiyuedu.bs.dao.entity.ChapterContentFilter;
import cn.aiyuedu.bs.dao.mongo.repository.custom.ChapterContentFilterRepositoryCustom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

/**
 * Description:
 *
 * @author yz.wu
 */
public class ChapterContentFilterRepositoryImpl implements ChapterContentFilterRepositoryCustom {

    @Autowired
    private MongoOperations mongoOperations;

    public void delete(List<Long> chapterIds) {
        mongoOperations.remove(new Query(Criteria.where("chapterId").in(chapterIds)), ChapterContentFilter.class);
    }

    public void deleteByBookId(Long bookId) {
        mongoOperations.remove(new Query(Criteria.where("bookId").is(bookId)), ChapterContentFilter.class);
    }

    public List<ChapterContentFilter> find(List<Long> chapterIds) {
        return mongoOperations.find(new Query(Criteria.where("chapterId").in(chapterIds)), ChapterContentFilter.class);
    }

    public Page<ChapterContentFilter> getPage(Integer start, Integer limit) {
        Query query = new Query();

        //分页
        query.skip(start).limit(limit);

        //默认排序
        query.with(new Sort(Sort.Direction.ASC, Constants.MONGODB_ID_KEY));

        return new Page<>(mongoOperations.find(query, ChapterContentFilter.class), 0);
    }
}