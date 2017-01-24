package cn.aiyuedu.bs.dao.mongo.repository.custom;

import cn.aiyuedu.bs.common.Constants;
import cn.aiyuedu.bs.common.model.RechargeBase;
import cn.aiyuedu.bs.common.model.RechargeCode;
import cn.aiyuedu.bs.common.orm.Page;
import cn.aiyuedu.bs.dao.dto.RechargeQueryDto;

import java.util.List;


public interface RechargeRepositoryCustom {
    public <T extends RechargeBase> T persist(T recharge);

    public <T extends RechargeBase> T createOrder(T recharge);

    public <T extends RechargeBase> T findByCode(String code, Class<T> cls);

    public <T extends RechargeBase> void paySuccess(T recharge);

    public long count(Integer userId, String uid, Integer type, Integer status);

    public <T extends RechargeBase> List<T> find(Integer userId, String uid, Integer type, Integer status, Integer skip, Integer limit);

    public <T extends RechargeBase> Page<T> getPage(RechargeQueryDto rechargeQueryDto, Class<T> clz);

    boolean updateStatus(String tradeCode, Constants.RechStatus s);

    void updateThirdMsg(RechargeCode recharge);

    public void updateUserId(String uid, Integer userId);
}
