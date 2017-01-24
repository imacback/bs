package cn.aiyuedu.bs.dao.mongo.repository.impl;

import cn.aiyuedu.bs.common.Constants;
import cn.aiyuedu.bs.common.Constants.ChapterStatus;
import cn.aiyuedu.bs.common.orm.Page;
import cn.aiyuedu.bs.dao.mongo.SequenceDao;
import cn.aiyuedu.bs.dao.dto.ChapterQueryDto;
import cn.aiyuedu.bs.dao.entity.Chapter;
import cn.aiyuedu.bs.dao.mongo.repository.custom.ChapterRepositoryCustom;
import cn.aiyuedu.bs.dao.util.QueryLikeUtils;
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

import java.util.Date;
import java.util.List;

/**
 * Description:
 *
 * @author yz.wu
 */
public class ChapterRepositoryImpl implements ChapterRepositoryCustom {

    @Autowired
    private MongoOperations mongoOperations;
    @Autowired
    private SequenceDao sequenceDao;

    @Override
    public Chapter persist(Chapter chapter) {
        if (chapter != null) {
            if (chapter.getId() == null) {
                chapter.setId((long) sequenceDao.getSequence(
                        Constants.MongoCollectionName.Chapter.getName()));
                mongoOperations.insert(chapter);
            } else {
                mongoOperations.save(chapter);
            }
        }

        return chapter;
    }

    @Override
    public List<Chapter> persist(List<Chapter> chapters) {
        List<Chapter> insert = null;
        if (CollectionUtils.isNotEmpty(chapters)) {
            insert = Lists.newArrayList();
            for (Chapter o : chapters) {
                if (o != null) {
                    if (o.getId() == null) {
                        o.setId((long) sequenceDao.getSequence(
                                Constants.MongoCollectionName.Chapter.getName()));
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

        return chapters;
    }

    @Override
    public Chapter findOne(Long bookId, String cpChapterId) {
        if (StringUtils.isNotEmpty(cpChapterId) && bookId != null) {
            Query query = new Query();
            query.addCriteria(Criteria.where("bookId").is(bookId));
            query.addCriteria(Criteria.where("cpChapterId").is(cpChapterId));

            return mongoOperations.findOne(query, Chapter.class);
        }

        return null;
    }

    @Override
    public Chapter findOne(Long bookId, Integer charIndex) {
        if (bookId != null && charIndex != null) {
            Query query = new Query();
            query.addCriteria(Criteria.where("bookId").is(bookId));
            query.addCriteria(Criteria.where("sumWords").gte(charIndex));
            query.with(new Sort(Sort.Direction.ASC, "orderId"));

            return mongoOperations.findOne(query, Chapter.class);
        }

        return null;
    }

    @Override
    public Chapter findOne(Long bookId, Integer status, Integer withoutStatus, Integer isMaxOrder) {
        if (bookId != null && (status != null || withoutStatus != null) && isMaxOrder != null) {
            Query query = new Query();
            query.addCriteria(Criteria.where("bookId").is(bookId));
            if (status != null) {
                query.addCriteria(Criteria.where("status").is(status));
            } else {
                query.addCriteria(Criteria.where("status").ne(withoutStatus));
            }

            Sort.Direction sort = isMaxOrder == 1? Sort.Direction.DESC: Sort.Direction.ASC;

            query.with(new Sort(sort, new String[]{"orderId", Constants.MONGODB_ID_KEY})).limit(1);

            return mongoOperations.findOne(query, Chapter.class);
        }

        return null;
    }

    @Override
    public boolean contain(Long id, String name) {
        Query query = new Query();
        if (id != null) query.addCriteria(Criteria.where(Constants.MONGODB_ID_KEY).ne(id));
        if (StringUtils.isNotBlank(name)) query.addCriteria(Criteria.where("name").is(name));
        return mongoOperations.exists(query, Chapter.class);
    }

    @Override
    public boolean exist(Long bookId, String cpChapterId) {
        if (bookId != null && StringUtils.isNotBlank(cpChapterId)) {
            Query query = new Query();
            query.addCriteria(Criteria.where("bookId").is(bookId));
            query.addCriteria(Criteria.where("cpChapterId").is(cpChapterId));
            return mongoOperations.exists(query, Chapter.class);
        }

        return false;
    }

    @Override
    public boolean exist(Long bookId, Long chapterId, Integer orderId) {
        if (bookId != null && chapterId != null && orderId != null) {
            Query query = new Query();
            query.addCriteria(Criteria.where("bookId").is(bookId));
            query.addCriteria(Criteria.where(Constants.MONGODB_ID_KEY).ne(chapterId));
            query.addCriteria(Criteria.where("orderId").is(orderId));
            return mongoOperations.exists(query, Chapter.class);
        }

        return false;
    }

    private Query getQuery(ChapterQueryDto chapterQueryDto) {
        Query query = new Query();
        if (chapterQueryDto != null) {
            //筛选
            if (CollectionUtils.isNotEmpty(chapterQueryDto.getIds()))
                query.addCriteria(Criteria.where("id").in(chapterQueryDto.getIds()));
            if (chapterQueryDto.getBookId() != null)
                query.addCriteria(Criteria.where("bookId").is(chapterQueryDto.getBookId()));
            if (StringUtils.isNotBlank(chapterQueryDto.getOriginName())) {
                Criteria c = new Criteria().orOperator(
                        Criteria.where("originName").regex(QueryLikeUtils.normalizeKeyword(chapterQueryDto.getOriginName())),
                        Criteria.where("name").regex(QueryLikeUtils.normalizeKeyword(chapterQueryDto.getOriginName())));

                query.addCriteria(c);
            }
            if (CollectionUtils.isNotEmpty(chapterQueryDto.getStatuses())) {
                List<Integer> list = chapterQueryDto.getStatuses();
                int size = list.size();
                Criteria[] array = new Criteria[size];
                for (int i=0; i<size; i++) {
                    array[i] = Criteria.where("status").is(list.get(i));
                }
                query.addCriteria(new Criteria().orOperator(array));
            } else if (chapterQueryDto.getStatus() != null) {
                query.addCriteria(Criteria.where("status").is(chapterQueryDto.getStatus()));
            }
            if (chapterQueryDto.getStartOrderId() != null) {
                query.addCriteria(Criteria.where("orderId").gte(chapterQueryDto.getStartOrderId()));
            }
        }

        return query;
    }

    @Override
    public long count(ChapterQueryDto chapterQueryDto) {
        return mongoOperations.count(getQuery(chapterQueryDto), Chapter.class);
    }

    @Override
    public List<Chapter> find(ChapterQueryDto chapterQueryDto) {
        Query query = getQuery(chapterQueryDto);

        if (chapterQueryDto.getStart() != null) {
            query.skip(chapterQueryDto.getStart()).limit(chapterQueryDto.getLimit());
        }

        //默认排序
        Sort.Direction direction = Sort.Direction.DESC;
        if (chapterQueryDto.getIsDesc() != null && chapterQueryDto.getIsDesc() == 0) {
            direction = Sort.Direction.ASC;
        }
        String orderBy = Constants.MONGODB_ID_KEY;
        if (chapterQueryDto.getOrderType() != null && chapterQueryDto.getOrderType() == 2) {
            orderBy = "orderId";
        }
        query.with(new Sort(direction, new String[]{"bookId", orderBy}));

        return mongoOperations.find(query, Chapter.class);
    }

    @Override
    public Page<Chapter> getPage(ChapterQueryDto chapterQueryDto) {
        Query query = getQuery(chapterQueryDto);

        //总数
        long count = 0l;
        if (chapterQueryDto.getStart() != null) {
            count = mongoOperations.count(query, Chapter.class);
            //分页
            query.skip(chapterQueryDto.getStart()).limit(chapterQueryDto.getLimit());
        }

        //默认排序
        Sort.Direction direction = Sort.Direction.DESC;
        if (chapterQueryDto.getIsDesc() != null && chapterQueryDto.getIsDesc() == 0) {
            direction = Sort.Direction.ASC;
        }
        String orderBy = Constants.MONGODB_ID_KEY;
        if (chapterQueryDto.getOrderType() != null && chapterQueryDto.getOrderType() == 2) {
            orderBy = "orderId";
        }
        query.with(new Sort(direction, new String[]{"bookId", orderBy}));

        return new Page<>(mongoOperations.find(query, Chapter.class), count);
    }

    @Override
    public void updateStatus(List<Long> chapterIds, Integer status, Integer adminUserId) {
        Update update = new Update();
        Date now = new Date();
        update.set("status", status).set("editDate", now);
        if (adminUserId != null) {
            update.set("editorId", adminUserId);
        }
        if (status == ChapterStatus.Online.getId()) {
            update.set("publishDate", now);
        }

        mongoOperations.updateMulti(Query.query(Criteria.where(Constants.MONGODB_ID_KEY).in(chapterIds)),
                update,
                Chapter.class);
    }

    @Override
    public void updateSumWord(Long id, Integer sumWords) {
        mongoOperations.findAndModify(new Query(Criteria.where(Constants.MONGODB_ID_KEY).is(id)),
                new Update().set("sumWords", sumWords),
                FindAndModifyOptions.options().returnNew(true),
                Chapter.class);
    }

    @Override
    public void updateOrderId(Long id, Integer orderId) {
        mongoOperations.findAndModify(new Query(Criteria.where(Constants.MONGODB_ID_KEY).is(id)),
                new Update().set("orderId", orderId),
                FindAndModifyOptions.options().returnNew(true),
                Chapter.class);
    }

    @Override
    public void updateOrderId(Long id, Integer orderId, Integer sumWords) {
        mongoOperations.findAndModify(new Query(Criteria.where(Constants.MONGODB_ID_KEY).is(id)),
                new Update().set("orderId", orderId)
                        .set("sumWords", sumWords),
                FindAndModifyOptions.options().returnNew(true),
                Chapter.class);
    }

    @Override
    public void updatePrice(Long id, Integer isFee, Integer price, Integer adminUserId) {
        mongoOperations.findAndModify(new Query(Criteria.where(Constants.MONGODB_ID_KEY).is(id)),
                new Update().set("isFee", isFee)
                        .set("price", price)
                        .set("editorId", adminUserId)
                        .set("editDate", new Date()),
                FindAndModifyOptions.options().returnNew(true),
                Chapter.class);
    }

    @Override
    public void updateFilterWords(Long id, String filterWords, Integer filteredWords) {
        mongoOperations.findAndModify(new Query(Criteria.where(Constants.MONGODB_ID_KEY).is(id)),
                new Update().set("filterWords", filterWords)
                        .set("filteredWords", filteredWords),
                FindAndModifyOptions.options().returnNew(true),
                Chapter.class);
    }

    @Override
    public void updateName(Long id, String originName, String name) {
        mongoOperations.findAndModify(new Query(Criteria.where(Constants.MONGODB_ID_KEY).is(id)),
                new Update().set("originName", originName)
                        .set("name", name),
                FindAndModifyOptions.options().returnNew(true),
                Chapter.class);
    }

    @Override
    public void delete(List<Long> ids) {
        if (CollectionUtils.isNotEmpty(ids)) {
            mongoOperations.remove(new Query().addCriteria(Criteria.where(Constants.MONGODB_ID_KEY).in(ids)), Chapter.class);
        }
    }

    @Override
    public void removeMultiByBookId(Long bookId) {
        Query query = new Query();
        if (bookId != null) {
            query.addCriteria(Criteria.where("bookId").is(bookId));
        }
        mongoOperations.remove(query, Chapter.class);
    }

    @Override
    public void removeMultiByBookId(List<Long> bookIds) {
        if (CollectionUtils.isNotEmpty(bookIds)) {
            mongoOperations.remove(new Query().addCriteria(Criteria.where("bookId").in(bookIds)), Chapter.class);
        }

    }
}
