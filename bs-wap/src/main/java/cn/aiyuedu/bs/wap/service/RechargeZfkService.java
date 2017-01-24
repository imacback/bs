package cn.aiyuedu.bs.wap.service;

import cn.aiyuedu.bs.common.Constants;
import cn.aiyuedu.bs.common.Constants.RechStatus;
import cn.aiyuedu.bs.common.Constants.RechWay;
import cn.aiyuedu.bs.common.model.RechargeZfb;
import cn.aiyuedu.bs.dao.mongo.repository.RechargeRepository;
import cn.aiyuedu.bs.wap.dto.ZhiFukaNotifyDto;
import cn.aiyuedu.bs.wap.util.SignUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Description:
 *
 * @author yz.wu
 */
@Service("rechargeZfkService")
public class RechargeZfkService extends RechargeService {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private RechargeRepository rechargeRepository;

    public boolean callbackUpdate(ZhiFukaNotifyDto notify) {
        log.info("callbackUpdate paramMap: " + notify.toString());
        //验证sign
        String sign = SignUtils.sign(notify.getSignString(), RechWay.zfk);
        if (StringUtils.equals(sign, notify.getSign())) {
            RechargeZfb recharge = rechargeRepository.findByCode(notify.getSdcustomno(), RechargeZfb.class);
            if (recharge == null) {
                return false;
            }

            recharge.setTradeNo(notify.getSd51no());
            recharge.setZfbTradeStatus(notify.getState() != null ? notify.getState().toString() : null);

            log.info(recharge.getMerTradeCode() + " callbackUpdate third : " + recharge.getZfbTradeStatus());
            if (StringUtils.isBlank(recharge.getZfbTradeStatus())) {
                rechargeRepository.persist(recharge);
                return false;
            } else if (StringUtils.equals("1", recharge.getZfbTradeStatus())) {
                if (recharge.getTradeStatus() != Constants.RechStatus.serverDone.val()) {
                    recharge.setTradeStatus(Constants.RechStatus.serverDone.val());
                    rechargeRepository.paySuccess(recharge);

                    return true;
                }
            } else if (StringUtils.equals("0", recharge.getZfbTradeStatus())) {
                if (recharge.getTradeStatus() != RechStatus.serverFail.val()) {
                    recharge.setTradeStatus(Constants.RechStatus.serverFail.val());
                    rechargeRepository.persist(recharge);

                    return false;
                }
            } else {
                if (recharge.getTradeStatus() != RechStatus.serverFail.val()) {
                    recharge.setTradeStatus(Constants.RechStatus.serverFail.val());
                    rechargeRepository.persist(recharge);

                    return false;
                }
            }
        } else {
            log.info("callbackUpdate valid sign fail, tradeNo:" + notify.getSdcustomno());
            return false;
        }

        return false;
    }
}
