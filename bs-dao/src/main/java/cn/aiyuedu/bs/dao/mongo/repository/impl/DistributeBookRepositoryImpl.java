package cn.aiyuedu.bs.dao.mongo.repository.impl;

import cn.aiyuedu.bs.common.orm.Page;
import cn.aiyuedu.bs.dao.dto.DistributeBookQueryDto;
import cn.aiyuedu.bs.dao.mongo.SequenceDao;
import cn.aiyuedu.bs.dao.dto.BookQueryDto;
import cn.aiyuedu.bs.dao.entity.Book;
import cn.aiyuedu.bs.dao.entity.DistributeBook;
import cn.aiyuedu.bs.dao.mongo.repository.BookRepository;
import cn.aiyuedu.bs.dao.mongo.repository.custom.DistributeBookRepositoryCustom;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Thinkpad on 2014/11/18.
 */
public class DistributeBookRepositoryImpl implements DistributeBookRepositoryCustom {

    private final Logger logger = LoggerFactory.getLogger(DistributeBookRepositoryImpl.class);

    @Autowired
    BookRepository bookRepository;
    @Autowired
    private MongoOperations mongoOperations;
    @Autowired
    private SequenceDao sequenceDao;

    @Override
    public int count(Integer disId) {
        return (int) mongoOperations.count(new Query(Criteria.where("disId").is(disId)), DistributeBook.class);
    }

    @Override
    public List<DistributeBook> queryBookList(Integer disId) {
        return mongoOperations.find(new Query(Criteria.where("disId").is(disId)), DistributeBook.class);
    }

    @Override
    public List<DistributeBook> queryBookList(DistributeBookQueryDto distributeBookQueryDto) {
        return mongoOperations.find(getQuery(distributeBookQueryDto), DistributeBook.class);
    }

    @Override
    public boolean exist(Long bookId, Integer distributeId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("disId").is(distributeId));
        query.addCriteria(Criteria.where("bookId").is(bookId));
        return mongoOperations.count(query, DistributeBook.class) > 0;

    }

    public DistributeBook persist(DistributeBook distributeBook) {
        if (distributeBook != null) {
            if (distributeBook.getId() == null) {
                distributeBook.setId(sequenceDao.getSequence(
                        mongoOperations.getCollectionName(DistributeBook.class)));
                mongoOperations.insert(distributeBook);
            } else {
                mongoOperations.save(distributeBook);
            }

        }

        return distributeBook;
    }

    public List<DistributeBook> persist(List<DistributeBook> list) {
        List<DistributeBook> insert = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(list)) {
            for (DistributeBook distributeBook : list) {
                if (distributeBook != null) {
                    if (distributeBook.getId() == null) {
                        distributeBook.setId(sequenceDao.getSequence(
                                mongoOperations.getCollectionName(DistributeBook.class)));
                        insert.add(distributeBook);
                    } else {
                        mongoOperations.save(distributeBook);
                    }
                }
            }
        }
        if (CollectionUtils.isNotEmpty(insert)) {
            mongoOperations.insertAll(insert);
        }
        return list;
    }


    public void deleteAll() {
        mongoOperations.remove(new Query(), DistributeBook.class);
    }

    private Query getQuery(DistributeBookQueryDto dto) {
        Query query = new Query();
        if (dto != null) {
            if (dto.getDistributeId() != null)
                query.addCriteria(Criteria.where("disId").is(dto.getDistributeId()));
            if (dto.getBookId() != null)
                query.addCriteria(Criteria.where("bookId").is(dto.getBookId()));
            if (dto.getCreateDate() != null) query.addCriteria(Criteria.where("createDate").gt(dto.getCreateDate()));
        }

        return query;
    }


    public Page<DistributeBook> getPage(DistributeBookQueryDto dto) {
        Query query = new Query();
        long count = 0l;
        if (dto != null) {
            if (dto.getDistributeId() != null)
                query.addCriteria(Criteria.where("disId").is(dto.getDistributeId()));
            if (dto.getBookId() != null)
                query.addCriteria(Criteria.where("bookId").is(dto.getBookId()));
            if (dto.getStart() != null) {
                count = mongoOperations.count(query, DistributeBook.class);
                //分页
                query.skip(dto.getStart()).limit(dto.getLimit());
            }
            if (dto.getCreateDate() != null) query.addCriteria(Criteria.where("createDate").gt(dto.getCreateDate()));

            //默认排序
            Sort.Direction direction = Sort.Direction.DESC;
            query.with(new Sort(direction, "disId"));
        }
        return new Page<>(mongoOperations.find(query, DistributeBook.class), count);
    }

    public void update(DistributeBook distribute) {
        logger.info("update distributeid = " + distribute.getDisId() + "editorId = " + distribute.getEditorId());
        Update update = new Update().set("disId", distribute.getDisId())
                .set("editorId", distribute.getEditorId())
                .set("editDate", new Date());
        mongoOperations.updateFirst(Query.query(Criteria.where("id").is(distribute.getId())),
                update,
                DistributeBook.class);
    }

    public List<Long> findBookListByDisId(Integer id) {
        List<Long> bookList = new ArrayList<>();
        List<DistributeBook> list = mongoOperations.find(new Query(Criteria.where("disId").in(id)), DistributeBook.class);
        for (DistributeBook book : list) {
            bookList.add(book.getBookId());
        }
        return bookList;
    }


    private List<Long> getBookIdByname(String name) {
        List<Long> list_id = new ArrayList<>();
        BookQueryDto bookQueryDto = new BookQueryDto();
        bookQueryDto.setName(name);
        List<Book> list = bookRepository.find(bookQueryDto);
        for (Book b : list) {
            list_id.add(b.getId());
        }
        return list_id;
    }

    public Long getBookCountBydisId(Integer id) {
        return mongoOperations.count(new Query(Criteria.where("disId").in(id)), DistributeBook.class);
    }

}
