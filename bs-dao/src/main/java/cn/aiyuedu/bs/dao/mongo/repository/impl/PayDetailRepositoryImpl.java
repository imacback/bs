package cn.aiyuedu.bs.dao.mongo.repository.impl;

import cn.aiyuedu.bs.common.Constants;
import cn.aiyuedu.bs.common.orm.Page;
import cn.aiyuedu.bs.dao.dto.PayDetailQueryDto;
import cn.aiyuedu.bs.dao.entity.Book;
import cn.aiyuedu.bs.dao.entity.PayDetail;
import cn.aiyuedu.bs.dao.mongo.SequenceDao;
import cn.aiyuedu.bs.dao.mongo.repository.custom.PayDetailRepositoryCustom;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;


public class PayDetailRepositoryImpl implements PayDetailRepositoryCustom {

    @Autowired
    private MongoOperations mongoOperations;
    @Autowired
    private SequenceDao sequenceDao;

    @Override
    public List<PayDetail> boughtChapters(Integer uid, Long bookId, List<Long> chapList) {
        Criteria c1 = Criteria.where("uid").is(uid).and("bookId").is(bookId).and("type").is(Constants.PayType.book.val());
        Criteria c2 = Criteria.where("uid").is(uid).and("bookId").is(bookId).and("type").is(Constants.PayType.chapter.val())
                .and("chapterId").in(chapList);
        Query query = new Query(new Criteria().orOperator(c1, c2));
        return mongoOperations.find(query, PayDetail.class);
    }

    @Override
    public PayDetail persist(PayDetail payDetail) {
        if (payDetail != null) {
            if (payDetail.getId() == null) {
                payDetail.setId(sequenceDao.getSequence(
                        mongoOperations.getCollectionName(PayDetail.class)));
                mongoOperations.insert(payDetail);
            } else {
                mongoOperations.save(payDetail);
            }
        }
        return payDetail;
    }

    @Override
    public boolean exist(Long bookId, Long chapterId, Integer uid) {
        Query query = new Query();
        if (bookId != null) query.addCriteria(Criteria.where("bookId").is(bookId));
        if (chapterId != null) query.addCriteria(Criteria.where("chapterId").is(chapterId));
        if (uid != null) query.addCriteria(Criteria.where("uid").is(uid));
        return mongoOperations.exists(query, PayDetail.class);
    }

    private Query getQuery(PayDetailQueryDto payDetailQueryDto) {
        Query query = new Query();

        //筛选
        if (payDetailQueryDto.getUserId() != null)
            query.addCriteria(Criteria.where("uid").is(payDetailQueryDto.getUserId()));
        if (payDetailQueryDto.getPayType() != null)
            query.addCriteria(Criteria.where("type").is(payDetailQueryDto.getPayType()));

        //区间
        if (payDetailQueryDto.getStartCreateTime() != null || payDetailQueryDto.getEndCreateTime() != null) {
            Criteria dateCriteria = Criteria.where("createTime");
            if (payDetailQueryDto.getStartCreateTime() != null)
                dateCriteria.gte(payDetailQueryDto.getStartCreateTime());
            if (payDetailQueryDto.getEndCreateTime() != null) dateCriteria.lte(payDetailQueryDto.getEndCreateTime());
            query.addCriteria(dateCriteria);
        }

        return query;
    }

    @Override
    public Page<PayDetail> getPage(PayDetailQueryDto payDetailQueryDto) {
        Query query = getQuery(payDetailQueryDto);

        //总数
        long count = 0l;
        if (payDetailQueryDto.getStart() != null) {
            count = mongoOperations.count(query, PayDetail.class);
        }

        //分页
        if (null != payDetailQueryDto.getStart() && null != payDetailQueryDto.getLimit()) {
            query.skip(payDetailQueryDto.getStart()).limit(payDetailQueryDto.getLimit());
        }

        //默认排序
        Sort.Direction direction = Sort.Direction.DESC;
        if (payDetailQueryDto.getIsDesc() != null && payDetailQueryDto.getIsDesc() == 0) {
            direction = Sort.Direction.ASC;
        }
        String orderBy = Constants.MONGODB_ID_KEY;
        if (payDetailQueryDto.getOrderType() != null && payDetailQueryDto.getOrderType() == 1) {
            orderBy = "createTime";
        }
        query.with(new Sort(direction, orderBy));

        return new Page<>(mongoOperations.find(query, PayDetail.class), count);
    }


}
