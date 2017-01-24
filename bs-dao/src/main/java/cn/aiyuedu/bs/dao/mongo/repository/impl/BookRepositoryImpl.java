package cn.aiyuedu.bs.dao.mongo.repository.impl;

import cn.aiyuedu.bs.common.Constants;
import cn.aiyuedu.bs.common.Constants.*;
import cn.aiyuedu.bs.common.orm.Page;
import cn.aiyuedu.bs.dao.mongo.SequenceDao;
import cn.aiyuedu.bs.dao.mongo.repository.custom.BookRepositoryCustom;
import cn.aiyuedu.bs.dao.util.QueryLikeUtils;
import cn.aiyuedu.bs.dao.dto.BookQueryDto;
import cn.aiyuedu.bs.dao.entity.Book;
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
public class BookRepositoryImpl implements BookRepositoryCustom {

    @Autowired
    private MongoOperations mongoOperations;
    @Autowired
    private SequenceDao sequenceDao;

    @Override
    public Book persist(Book book) {
        if (book != null) {
            if (book.getId() == null) {
                book.setId((long) sequenceDao.getSequence(
                        mongoOperations.getCollectionName(Book.class)));
                mongoOperations.insert(book);
            } else {
                mongoOperations.save(book);
            }
        }

        return book;
    }

    @Override
    public List<Book> persist(List<Book> books) {
        if (CollectionUtils.isNotEmpty(books)) {
            List<Book> insert = Lists.newArrayList();
            for (Book o : books) {
                if (o != null) {
                    if (o.getId() == null) {
                        o.setId((long) sequenceDao.getSequence(
                                mongoOperations.getCollectionName(Book.class)));
                    } else {
                        mongoOperations.save(o);
                    }
                    insert.add(o);
                }
            }

            if (CollectionUtils.isNotEmpty(insert)) {
                mongoOperations.insertAll(insert);
            }
        }

        return books;
    }

    @Override
    public boolean exist(Long id, String name, String author, Integer status) {
        Query query = new Query();
        if (id != null) query.addCriteria(Criteria.where(Constants.MONGODB_ID_KEY).lt(id));
        if (StringUtils.isNotEmpty(name) && StringUtils.isNotEmpty(author)) {
            query.addCriteria(Criteria.where("name").is(name));
            query.addCriteria(Criteria.where("author").is(author));
        }
        if (status != null) query.addCriteria(Criteria.where("status").is(status));
        return mongoOperations.exists(query, Book.class);
    }

    @Override
    public Book findOne(String cpBookId, Integer providerId) {
        if (StringUtils.isNotEmpty(cpBookId) && providerId != null) {
            Query query = new Query();
            query.addCriteria(Criteria.where("cpBookId").is(cpBookId));
            query.addCriteria(Criteria.where("providerId").is(providerId));

            return mongoOperations.findOne(query, Book.class);
        }

        return null;
    }

    private Query getQuery(BookQueryDto bookQueryDto) {
        Query query = new Query();
        //筛选
        if (bookQueryDto.getId() != null) {
            query.addCriteria(Criteria.where(Constants.MONGODB_ID_KEY).is(bookQueryDto.getId()));
        } else if (CollectionUtils.isNotEmpty(bookQueryDto.getExcludeIds())) {
            query.addCriteria(Criteria.where(Constants.MONGODB_ID_KEY).nin(bookQueryDto.getExcludeIds()));
        } else if (CollectionUtils.isNotEmpty(bookQueryDto.getIds())) {
            query.addCriteria(Criteria.where(Constants.MONGODB_ID_KEY).in(bookQueryDto.getIds()));
        }
        if (StringUtils.isNotBlank(bookQueryDto.getName())) query.addCriteria(Criteria.where("name").regex(QueryLikeUtils.normalizeKeyword(bookQueryDto.getName())));
        if (StringUtils.isNotBlank(bookQueryDto.getAuthor())) query.addCriteria(Criteria.where("author").regex(QueryLikeUtils.normalizeKeyword(bookQueryDto.getAuthor())));
        if (bookQueryDto.getIsSerial() != null) query.addCriteria(Criteria.where("isSerial").is(bookQueryDto.getIsSerial()));
        if (bookQueryDto.getIsFee() != null) query.addCriteria(Criteria.where("isFee").is(bookQueryDto.getIsFee()));
        if (bookQueryDto.getProviderId() != null) query.addCriteria(Criteria.where("providerId").is(bookQueryDto.getProviderId()));
        if (bookQueryDto.getBatchId() != null) query.addCriteria(Criteria.where("batchId").is(bookQueryDto.getBatchId()));
        if (bookQueryDto.getStatus() != null) query.addCriteria(Criteria.where("status").is(bookQueryDto.getStatus()));
        if (bookQueryDto.getTagSexId() != null) query.addCriteria(Criteria.where("tagSexId").is(bookQueryDto.getTagSexId()));
        if (bookQueryDto.getTagClassifyId() != null) query.addCriteria(Criteria.where("tagClassifyId").is(bookQueryDto.getTagClassifyId()));
        if (bookQueryDto.getTagStoryId() != null) query.addCriteria(Criteria.where("tagStoryId").is(bookQueryDto.getTagStoryId()));
        if (StringUtils.isNotBlank(bookQueryDto.getSmallPic())) query.addCriteria(Criteria.where("smallPic").regex(QueryLikeUtils.normalizeKeyword(bookQueryDto.getSmallPic())));

        //包含
        if (bookQueryDto.getTagContentId() != null) query.addCriteria(Criteria.where("tagContentIds").in(bookQueryDto.getTagContentId()));
        if (bookQueryDto.getTagSupplyId() != null) query.addCriteria(Criteria.where("tagSupplyIds").in(bookQueryDto.getTagSupplyId()));
        if (bookQueryDto.getCategoryId() != null) query.addCriteria(Criteria.where("categoryIds").in(bookQueryDto.getCategoryId()));
        if (bookQueryDto.getOperatePlatformId() != null) query.addCriteria(Criteria.where("operatePlatformIds").in(bookQueryDto.getOperatePlatformId()));

        //区间
        if (bookQueryDto.getCheckLevel() != null) query.addCriteria(Criteria.where("checkLevel").gte(bookQueryDto.getCheckLevel()));
        if (bookQueryDto.getChapters() != null) query.addCriteria(Criteria.where("chapters").gt(bookQueryDto.getChapters()));
        if (bookQueryDto.getPublishChapters() != null) query.addCriteria(Criteria.where("publishChapters").gt(bookQueryDto.getPublishChapters()));
        if (bookQueryDto.getDayPublishChapters() != null) query.addCriteria(Criteria.where("dayPublishChapters").gt(bookQueryDto.getDayPublishChapters()));
        if (bookQueryDto.getUpdateChapterDate() != null) query.addCriteria(Criteria.where("updateChapterDate").gt(bookQueryDto.getUpdateChapterDate()));
        if (bookQueryDto.getStartEditDate() != null || bookQueryDto.getEndEditDate() != null) {
            Criteria dateCriteria = Criteria.where("editDate");
            if (bookQueryDto.getStartEditDate() != null) dateCriteria.gte(bookQueryDto.getStartEditDate());
            if (bookQueryDto.getEndEditDate() != null) dateCriteria.lte(bookQueryDto.getEndEditDate());
            query.addCriteria(dateCriteria);
        }

        return query;
    }

    @Override
    public long count(BookQueryDto bookQueryDto) {
        return mongoOperations.count(getQuery(bookQueryDto), Book.class);
    }

    @Override
    public List<Book> find(BookQueryDto bookQueryDto) {
        Query query = getQuery(bookQueryDto);

        //分页
        if (bookQueryDto.getStart() != null) {
            query.skip(bookQueryDto.getStart()).limit(bookQueryDto.getLimit());
        }

        //默认排序
        Sort.Direction direction = Sort.Direction.DESC;
        if (bookQueryDto.getIsDesc() != null && bookQueryDto.getIsDesc() == 0) {
            direction = Sort.Direction.ASC;
        }
        if (bookQueryDto.getOrderType() != null && bookQueryDto.getOrderType() == 2) {
            query.with(new Sort(direction, new String[]{"viewCount", Constants.MONGODB_ID_KEY}));
        } else {
            query.with(new Sort(direction, Constants.MONGODB_ID_KEY));
        }

        return mongoOperations.find(query, Book.class);
    }

    @Override
    public Page<Book> getPage(BookQueryDto bookQueryDto) {
        Query query = getQuery(bookQueryDto);

        //总数
        long count = 0l;
        if (bookQueryDto.getStart() != null) {
            count = mongoOperations.count(query, Book.class);
        }

        //分页
        query.skip(bookQueryDto.getStart()).limit(bookQueryDto.getLimit());

        //默认排序
        Sort.Direction direction = Sort.Direction.DESC;
        if (bookQueryDto.getIsDesc() != null && bookQueryDto.getIsDesc() == 0) {
            direction = Sort.Direction.ASC;
        }
        if (bookQueryDto.getOrderType() != null && bookQueryDto.getOrderType() == 2) {
            query.with(new Sort(direction, new String[]{"viewCount", Constants.MONGODB_ID_KEY}));
        } else {
            query.with(new Sort(direction, Constants.MONGODB_ID_KEY));
        }

        return new Page<>(mongoOperations.find(query, Book.class), count);
    }

    @Override
    public Book addChapters(Long id, int chapters) {
        return mongoOperations.findAndModify(new Query(Criteria.where(Constants.MONGODB_ID_KEY).is(id)),
                new Update().inc("chapters", chapters),
                FindAndModifyOptions.options().returnNew(true),
                Book.class);
    }

    @Override
    public void removeMulti(List<Long> ids) {
        mongoOperations.remove(new Query(Criteria.where("id").in(ids)), Book.class);
    }

    @Override
    public void updateStatus(List<Long> ids, Integer status, Integer adminUserId, boolean updateOnlineDate) {
        Update update = new Update().set("status", status)
                .set("editorId", adminUserId)
                .set("editDate", new Date());
        if (updateOnlineDate) {
            update.set("onlineDate", new Date());
        }
        mongoOperations.updateMulti(Query.query(Criteria.where(Constants.MONGODB_ID_KEY).in(ids)),
                update,
                Book.class);
    }

    @Override
    public void updateChapters(Long id, Integer chapters) {
        mongoOperations.findAndModify(new Query(Criteria.where(Constants.MONGODB_ID_KEY).is(id)),
                new Update().set("chapters", chapters),
                FindAndModifyOptions.options().returnNew(true),
                Book.class);
    }

    @Override
    public void updateChapters(Long id, Integer chapters, Integer publishChapters) {
        if (chapters != null || publishChapters != null) {
            Update update = new Update();
            if (chapters != null) {
                update.set("chapters", chapters);
            }
            if (publishChapters != null) {
                update.set("publishChapters", publishChapters);
            }

            mongoOperations.findAndModify(new Query(Criteria.where(Constants.MONGODB_ID_KEY).is(id)),
                    update,
                    FindAndModifyOptions.options().returnNew(true),
                    Book.class);
        }
    }

    @Override
    public void updatePublishChapters(Long id, Integer publishChapters) {
        mongoOperations.findAndModify(new Query(Criteria.where(Constants.MONGODB_ID_KEY).is(id)),
                new Update().set("publishChapters", publishChapters),
                FindAndModifyOptions.options().returnNew(true),
                Book.class);
    }

    @Override
    public void updatePublishChapterDate(Long id) {
        mongoOperations.findAndModify(new Query(Criteria.where(Constants.MONGODB_ID_KEY).is(id)),
                new Update().set("updateChapterDate", new Date()),
                FindAndModifyOptions.options().returnNew(true),
                Book.class);
    }

    @Override
    public void updateViewCount(Long id, Integer viewCount) {
        mongoOperations.findAndModify(new Query(Criteria.where(Constants.MONGODB_ID_KEY).is(id)),
                new Update().set("viewCount", viewCount),
                FindAndModifyOptions.options().returnNew(true),
                Book.class);
    }

    @Override
    public void updateWords(Long id, Integer words) {
        mongoOperations.findAndModify(new Query(Criteria.where(Constants.MONGODB_ID_KEY).is(id)),
                new Update().set("words", words),
                FindAndModifyOptions.options().returnNew(true),
                Book.class);
    }

    @Override
    public void updateMemo(Long id, String originMemo, String memo) {
        mongoOperations.findAndModify(new Query(Criteria.where(Constants.MONGODB_ID_KEY).is(id)),
                new Update().set("originMemo", originMemo).set("memo", memo),
                FindAndModifyOptions.options().returnNew(true),
                Book.class);
    }

    @Override
    public Book updateCover(Long id, String smallPic, String largePic) {
        Update update = new Update();
        if (StringUtils.isNotBlank(smallPic)) {
            update.set("smallPic", smallPic);
        }
        if (StringUtils.isNotBlank(largePic)) {
            update.set("largePic", largePic);
        }
        return mongoOperations.findAndModify(new Query(Criteria.where(Constants.MONGODB_ID_KEY).is(id)),
                update,
                FindAndModifyOptions.options().returnNew(true),
                Book.class);
    }

    @Override
    public void updateTags(BookQueryDto queryDto) {
        mongoOperations.findAndModify(new Query(Criteria.where(Constants.MONGODB_ID_KEY).is(queryDto.getId())),
                new Update().set("tagSexId", queryDto.getTagSexId())
                        .set("tagClassifyId", queryDto.getTagClassifyId())
                        .set("tagStoryId", queryDto.getTagStoryId())
                        .set("tagContentIds", queryDto.getTagContentIds())
                        .set("tagSupplyIds", queryDto.getTagSupplyIds())
                        .set("categoryIds", queryDto.getCategoryIds())
                        .set("checkLevel", queryDto.getCheckLevel()),
                FindAndModifyOptions.options().returnNew(true),
                Book.class);
    }

    private Query getFrontListQuery() {
        return new Query().addCriteria(Criteria.where("checkLevel").gte(BookModule.Ranking.getBookLevel())
                .and("publishChapters").gt(0).and("status").is(BookStatus.Online.getId()));
    }

    private Query getRankingListQuery(Integer rankingId) {
        Query query = getFrontListQuery();

        if (rankingId == 1) {//男生原创玄幻榜
            Criteria criteria = new Criteria().andOperator(
                    Criteria.where("tagSexId").is(105),                             //男频
                    Criteria.where("tagClassifyId").is(103),                        //原创小说
                    Criteria.where("tagContentIds").in(108,109,110,111,112),    //洪荒,历史,现代,架空,未来
                    Criteria.where("tagContentIds").in(117,118,119,121)          //奇幻,玄幻,异能,异世大陆
            );
            query.addCriteria(criteria);
        } else if (rankingId == 2) {//男生原创都市榜
            Criteria criteria = new Criteria().andOperator(
                    Criteria.where("tagSexId").is(105),                             //男频
                    Criteria.where("tagClassifyId").is(103),                        //原创小说
                    Criteria.where("tagContentIds").in(110),                        //现代
                    Criteria.where("tagContentIds").in(113,114,130,131,132,134,135,133)//爱情婚姻,青春校园,商场,官场,职场,娱乐风云,乡土百态,都市生活
            );
            query.addCriteria(criteria);
        } else if (rankingId == 3) {//女生原创古言穿越榜
            Criteria c1 = new Criteria().andOperator(
                    Criteria.where("tagSexId").is(106),                             //女频
                    Criteria.where("tagClassifyId").is(103),                        //原创小说
                    Criteria.where("tagContentIds").in(109),                        //历史
                    Criteria.where("tagContentIds").in(113),                        //爱情婚姻
                    Criteria.where("tagSupplyIds").in(176,177,185),                 //宫斗+宅斗+种田文
                    Criteria.where("tagSupplyIds").in(198,199),                     //女强+女尊
                    Criteria.where("tagSupplyIds").in(226,227,228,235,239,240,241)  //嫡女+庶女+弃妇+王妃+皇后+皇帝+将军
            );

            Criteria c2 = new Criteria().andOperator(
                    Criteria.where("tagSexId").is(106),                             //女频
                    Criteria.where("tagClassifyId").is(103),                        //原创小说
                    Criteria.where("tagSupplyIds").in(173,174,175,192)              //穿越,重生,反穿越,转世
            );

            query.addCriteria(new Criteria().orOperator(c1, c2));
        } else if (rankingId == 4) {//女生原创都市言情榜
            Criteria criteria = new Criteria().andOperator(
                    Criteria.where("tagSexId").is(106),                             //女频
                    Criteria.where("tagClassifyId").is(103),                        //原创小说
                    Criteria.where("tagContentIds").in(110),                        //现代
                    Criteria.where("tagContentIds").in(113,114,130,131,132,134,135,133)//爱情婚姻,青春校园,商场,官场,职场,娱乐风云,乡土百态,都市生活
            );
            query.addCriteria(criteria);
        } else if (rankingId == 5) {//最热出版古典小说榜
            Criteria criteria = new Criteria().andOperator(
                    Criteria.where("tagClassifyId").is(101),                        //出版小说
                    Criteria.where("tagContentIds").in(108,109,111),                //洪荒,历史,架空
                    Criteria.where("tagContentIds").in(139)                         //爱情婚姻,青春校园,商场,官场,职场,娱乐风云,乡土百态,都市生活
            );
            query.addCriteria(criteria);
        } else if (rankingId == 6) {//最热出版两性文学榜
            Criteria c1 = new Criteria().andOperator(
                    Criteria.where("tagClassifyId").is(101),                        //出版小说
                    Criteria.where("tagContentIds").in(108,109,110,111,112),        //洪荒,历史,现代,架空,未来
                    Criteria.where("tagContentIds").in(113,114,133,134)             //爱情婚姻,青春校园,都市生活,娱乐风云
            );

            Criteria c2 = new Criteria().andOperator(
                    Criteria.where("tagClassifyId").is(102),                        //出版非小说
                    Criteria.where("tagContentIds").in(108,109,110,111,112),        //洪荒,历史,现代,架空,未来
                    Criteria.where("tagContentIds").in(143)                         //两性关系
            );

            query.addCriteria(new Criteria().orOperator(c1, c2));
        }

        return query;
    }

    private Page<Book> getPageOrderByViewCount(Query query, Integer start, Integer limit) {
        //总数
        long count = 0l;
        if (start != null) {
            count = mongoOperations.count(query, Book.class);
        }

        //分页
        query.skip(start).limit(limit);

        //默认排序
        query.with(new Sort(Sort.Direction.DESC, new String[]{"viewCount", Constants.MONGODB_ID_KEY}));

        return new Page<>(mongoOperations.find(query, Book.class), count);
    }

    @Override
    public Page<Book> getRankingPage(Integer rankingId, Integer operatePlatformId, Integer start, Integer limit) {
        Query query = getRankingListQuery(rankingId);
        if (operatePlatformId != null) {
            query.addCriteria(Criteria.where("operatePlatformIds").in(operatePlatformId));
        }
        return getPageOrderByViewCount(query, start, limit);
    }

    @Override
    public Page<Book> getPageByCategory(Integer categoryId, Integer operatePlatformId, Integer start, Integer limit) {
        Query query = getFrontListQuery();
        if (categoryId != null) {
            query.addCriteria(Criteria.where("categoryIds").in(categoryId));
        }
        if (operatePlatformId != null) {
            query.addCriteria(Criteria.where("operatePlatformIds").in(operatePlatformId));
        }

        return getPageOrderByViewCount(query, start, limit);
    }

    @Override
    public Page<Book> getPageByTagContentId(Long bookId, Integer tagContentId, Integer operatePlatformId, Integer start, Integer limit) {
        Query query = getFrontListQuery();
        if (tagContentId != null) {
            query.addCriteria(Criteria.where("tagContentIds").in(tagContentId));
        }
        if (operatePlatformId != null) {
            query.addCriteria(Criteria.where("operatePlatformIds").in(operatePlatformId));
        }

        if (bookId != null) {
            query.addCriteria(Criteria.where(Constants.MONGODB_ID_KEY).ne(bookId));
        }

        return getPageOrderByViewCount(query, start, limit);
    }
}
