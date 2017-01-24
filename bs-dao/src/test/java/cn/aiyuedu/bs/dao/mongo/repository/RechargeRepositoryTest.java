package cn.aiyuedu.bs.dao.mongo.repository;

import cn.aiyuedu.bs.common.util.IdUtil;
import cn.aiyuedu.bs.dao.BaseTest;
import cn.aiyuedu.bs.dao.entity.Recharge;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.Date;

/**
 * Description:
 *
 * @author yz.wu
 */
public class RechargeRepositoryTest extends BaseTest {

    private final Logger logger = LoggerFactory.getLogger(RechargeRepositoryTest.class);

    @Autowired
    private MongoOperations mongoOperations;
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private RechargeRepository rechargeRepository;

    @Test
    public void testInsert() {
        String uid = "123456789";
        Recharge r = new Recharge();
        r.setChannelId(1);
        r.setPayAccount("20");
        r.setCreateTime(new Date());
        r.setMerTradeCode(IdUtil.uuid());
        r.setPlatform(1);
        r.setProductName("test");
        r.setUid(uid);

        rechargeRepository.persist(r);
    }

    @Test
    public void testUpdateUserId() {
        String uid = "123456789";
        Integer userId = 100;

        rechargeRepository.updateUserId(uid, userId);
    }
}
