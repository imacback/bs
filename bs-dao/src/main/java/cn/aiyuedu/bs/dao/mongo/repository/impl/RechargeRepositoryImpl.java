package cn.aiyuedu.bs.dao.mongo.repository.impl;

import cn.aiyuedu.bs.common.Constants;
import cn.aiyuedu.bs.common.Constants.RechargeAmount;
import cn.aiyuedu.bs.common.model.RechargeBase;
import cn.aiyuedu.bs.common.model.RechargeCode;
import cn.aiyuedu.bs.common.orm.Page;
import cn.aiyuedu.bs.dao.dto.RechargeQueryDto;
import cn.aiyuedu.bs.dao.entity.Recharge;
import cn.aiyuedu.bs.dao.entity.User;
import cn.aiyuedu.bs.dao.mongo.SequenceDao;
import cn.aiyuedu.bs.dao.mongo.repository.UserRepository;
import cn.aiyuedu.bs.dao.mongo.repository.custom.RechargeRepositoryCustom;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;


public class RechargeRepositoryImpl<T extends RechargeBase> implements RechargeRepositoryCustom {

    @Autowired
    private MongoOperations mongoOperations;
    @Autowired
    private SequenceDao sequenceDao;
    @Autowired
    private UserRepository userRepository;

    public <T extends RechargeBase> T persist(T recharge) {

        if (recharge != null) {
            recharge.setEditTime(new Date());
            if (recharge.getId() == null) {
                recharge.setId(sequenceDao.getSequence(
                        mongoOperations.getCollectionName(recharge.getClass())));
                mongoOperations.insert(recharge);
            } else {
                mongoOperations.save(recharge);
            }
        }
        return recharge;
    }

    //    id;
    //    平台
    //            版本
    //    渠道
    //            tradeStatus;
    //    uid;
    //    type;//1=充值，2=签到，3=其他
    //    recAmount;
    //    productUnitNum;//兑换的虚拟币
    //    freeProductUnitNum;//赠送的虚拟币
    //    recSum;//recAmout+freeRecAmount
    //    ---------------------
    //    token;
    //    seckey;
    //    resultCode;
    //    resultMsg;
    public <T extends RechargeBase> T createOrder(T recharge) {

//    public Recharge createOrder(Recharge recharge) {
//        if (recharge != null) {
//            recharge.setId(sequenceDao.getSequence(
//                    mongoOperations.getCollectionName(Recharge.class)));
//            mongoOperations.insert(recharge);
//        }
        recharge.setCreateTime(new Date());
        recharge.setEditTime(new Date());
        return persist(recharge);
    }

    @Override
    public <T extends RechargeBase> T findByCode(String code, Class<T> cls) {
        Query query = new Query();
        query.addCriteria(Criteria.where("merTradeCode").is(code));
        return mongoOperations.findOne(query, cls);
    }

    @Override
    @Transactional
    public <T extends RechargeBase> void paySuccess(T recharge) {
        persist(recharge);
        if (recharge.getUserId() != null) {
            Integer amount = recharge.getRecAmount().intValue();
            int bonus = 0;
            RechargeAmount rechargeAmount = RechargeAmount.getByAmount(amount);
            if (rechargeAmount != null) {
                bonus = rechargeAmount.getBonus();
            }
            amount = bonus + amount;
            userRepository.update(recharge.getUserId(), recharge.getUid(), amount * Constants.CORN, null);
        }
    }

    public long count(Integer userId, String uid, Integer type, Integer status) {
        Query query = new Query();
        if (userId != null) {
            query.addCriteria(Criteria.where("userId").is(userId));
        } else if (StringUtils.isNotBlank(uid)) {
            query.addCriteria(Criteria.where("uid").is(uid));
        }
        if (type != null)
            query.addCriteria(Criteria.where("type").is(type));
        if (status != null)
            query.addCriteria(Criteria.where("tradeStatus").is(status));
        return mongoOperations.count(query, RechargeBase.class);
    }

    public List<RechargeBase> find(Integer userId, String uid, Integer type, Integer status, Integer skip, Integer limit) {
        Query query = new Query();
        if (userId != null) {
            query.addCriteria(Criteria.where("userId").is(userId));
        } else if (StringUtils.isNotBlank(uid)) {
            query.addCriteria(Criteria.where("uid").is(uid));
        }
        if (type != null)
            query.addCriteria(Criteria.where("type").is(type));
        if (status != null)
            query.addCriteria(Criteria.where("tradeStatus").is(status));
        query.skip(skip).limit(limit);
        query.with(new Sort(Sort.Direction.DESC, "createTime"));
        return mongoOperations.find(query, RechargeBase.class);
    }


    private Query getQuery(RechargeQueryDto rechargeQueryDto) {
        Query query = new Query();

        if (null != rechargeQueryDto) {
            //筛选
            if (null != rechargeQueryDto.getUserId()) {
                query.addCriteria(Criteria.where("userId").is(rechargeQueryDto.getUserId()));
            } else if (StringUtils.isNotBlank(rechargeQueryDto.getUid())) {
                query.addCriteria(Criteria.where("uid").is(rechargeQueryDto.getUid()));
            }

        /* 处理后台用户充值信息查询的充值状态,后台状态有：6成功 -1未成功.
           数据库中的真正状态值是：1创建订单成功 2签名验证失败 3创建订单失败
           4客户端支付成功 5客户端支付失败 6服务端支付成功 7后台通知签名验证失败.
           所以在RechargeQueryDto.rechargeStatus的值为0时得进行特殊处理
         */
            if (null != rechargeQueryDto.getRechargeStatus()) {
                if (rechargeQueryDto.getRechargeStatus() == -1) {
                    query.addCriteria(Criteria.where("tradeStatus").ne(Constants.RechStatus.serverDone.val()));
                } else {
                    query.addCriteria(Criteria.where("tradeStatus").is(rechargeQueryDto.getRechargeStatus()));
                }
            }

            //区间
            if (null != rechargeQueryDto.getStartCreateTime() || null != rechargeQueryDto.getEndCreateTime()) {
                Criteria dateCriteria = Criteria.where("createTime");
                if (rechargeQueryDto.getStartCreateTime() != null)
                    dateCriteria.gte(rechargeQueryDto.getStartCreateTime());
                if (rechargeQueryDto.getEndCreateTime() != null) dateCriteria.lte(rechargeQueryDto.getEndCreateTime());
                query.addCriteria(dateCriteria);
            }

            //支付类型
            if (CollectionUtils.isNotEmpty(rechargeQueryDto.getPayTypeList()))
                query.addCriteria(Criteria.where("payType").in(rechargeQueryDto.getPayTypeList()));

            //订单号
            if (StringUtils.isNotBlank(rechargeQueryDto.getMerTradeCode()))
                query.addCriteria(Criteria.where("merTradeCode").is(rechargeQueryDto.getMerTradeCode()));
        }

        return query;
    }

    @Override
    public <T extends RechargeBase> Page<T> getPage(RechargeQueryDto rechargeQueryDto, Class<T> clz) {
        Query query = getQuery(rechargeQueryDto);

        //总数
        long count = 0l;
        if (rechargeQueryDto.getStart() != null) {
            count = mongoOperations.count(query, Recharge.class);
        }

        //分页
        if (null != rechargeQueryDto.getStart() && null != rechargeQueryDto.getLimit()) {
            query.skip(rechargeQueryDto.getStart()).limit(rechargeQueryDto.getLimit());
        }

        //默认排序
        Sort.Direction direction = Sort.Direction.DESC;
        if (rechargeQueryDto.getIsDesc() != null && rechargeQueryDto.getIsDesc() == 0) {
            direction = Sort.Direction.ASC;
        }
        String orderBy = Constants.MONGODB_ID_KEY;
        if (rechargeQueryDto.getOrderType() != null && rechargeQueryDto.getOrderType() == 1) {
            orderBy = "merTradeCode";
        } else if (rechargeQueryDto.getOrderType() != null && rechargeQueryDto.getOrderType() == 2) {
            orderBy = "createTime";
        }

        query.with(new Sort(direction, orderBy));

        return new Page<T>(mongoOperations.find(query, clz), count);
    }

    @Override
    public boolean updateStatus(String tradeCode, Constants.RechStatus s) {
        RechargeBase rech = mongoOperations.findAndModify(new Query(Criteria.where("merTradeCode").is(tradeCode)),
                new Update().
                        set("tradeStatus", s.val()).
                        set("clientDoneTime", new Date()).
                        set("editDate", new Date()),
                FindAndModifyOptions.options().returnNew(true),
                RechargeBase.class);
        return s.val() == rech.getTradeStatus();
    }

    @Override
    public void updateThirdMsg(RechargeCode recharge) {
        mongoOperations.findAndModify(new Query(Criteria.where("merTradeCode").is(recharge.getMerTradeCode())),
                new Update().
                        set("resultCode", recharge.getResultCode()).
                        set("resultMsg", recharge.getResultMsg()).
                        set("editDate", new Date()),
                FindAndModifyOptions.options().returnNew(false),
                RechargeBase.class);
    }

    @Override
    public void updateUserId(String uid, Integer userId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("uid").is(uid));
        query.addCriteria(Criteria.where("userId").exists(false));
        Update update = new Update();
        update.set("userId", userId);
        mongoOperations.findAndModify(
                query,
                update,
                FindAndModifyOptions.options().returnNew(true),
                Recharge.class);
    }

}
