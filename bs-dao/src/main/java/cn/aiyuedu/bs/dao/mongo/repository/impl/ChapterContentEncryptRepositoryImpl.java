package cn.aiyuedu.bs.dao.mongo.repository.impl;

import cn.aiyuedu.bs.dao.entity.ChapterContentEncrypt;
import cn.aiyuedu.bs.dao.mongo.repository.custom.ChapterContentEncryptRepositoryCustom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

/**
 * Description:
 *
 * @author yz.wu
 */
public class ChapterContentEncryptRepositoryImpl implements ChapterContentEncryptRepositoryCustom {
    @Autowired
    private MongoOperations mongoOperations;

    public void delete(List<Long> chapterIds) {
        mongoOperations.remove(new Query(Criteria.where("chapterId").in(chapterIds)), ChapterContentEncrypt.class);
    }

    public void deleteByBookId(Long bookId) {
        mongoOperations.remove(new Query(Criteria.where("bookId").is(bookId)), ChapterContentEncrypt.class);
    }

    public List<ChapterContentEncrypt> find(List<Long> chapterIds) {
        return mongoOperations.find(new Query(Criteria.where("chapterId").in(chapterIds)), ChapterContentEncrypt.class);
    }
}
