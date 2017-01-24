package cn.aiyuedu.bs.wap.service;

import cn.aiyuedu.bs.common.Constants;
import cn.aiyuedu.bs.common.model.RechargeZfb;
import cn.aiyuedu.bs.dao.mongo.repository.RechargeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Map;

/**
 * Description:
 *
 * @author
 */
@Service("RechargeMobilepayService")
public class RechargeMobilepayService extends RechargeService {

    private static Logger log = LoggerFactory.getLogger(RechargeMobilepayService.class);

    @Autowired
    private RechargeRepository rechargeRepository;

    public boolean callbackUpdate(Map<String, String> paramMap) {
        try {
            //https://mapi.alipay.com/gateway.do?service=notify_verify&partner=2088002396712354&notify_id=RqPnCoPT3K9%252Fvwbh3I%252BFioE227%252BPfNMl8jwyZqMIiXQWxhOCmQ5MQO%252FWd93rvCB%252BaiGg
            log.info("callbackUpdate paramMap: " + paramMap);
            //验证sign
            //TODO

            RechargeZfb recharge = rechargeRepository.findByCode(paramMap.get("app_orderid"), RechargeZfb.class);
            log.info("callbackUpdate recharge: " + recharge);
            if (recharge == null) {
                return false;
            }

            // recharge.setNotifyId(paramMap.get("notify_id"));///???
            recharge.setPayAccount(paramMap.get("phone"));///？？？？
            recharge.setTradeNo(paramMap.get("orderid"));
            recharge.setZfbTradeStatus(paramMap.get("status"));

            log.info(recharge.getMerTradeCode() + " callbackUpdate third : " + recharge.getZfbTradeStatus());

            if (!StringUtils.hasText(recharge.getZfbTradeStatus())) {
                rechargeRepository.persist(recharge);
                return false;
            } else if ("1".equals(recharge.getZfbTradeStatus())) {
                if (recharge.getTradeStatus() != Constants.RechStatus.serverDone.val()) {
                    recharge.setTradeStatus(Constants.RechStatus.serverDone.val());
                    rechargeRepository.paySuccess(recharge);
                }
                return true;
            } else {
                rechargeRepository.persist(recharge);

                return false;
            }
        } catch (Throwable e) {
            log.error("callbackUpdate Exception", e);
            return false;
        }
    }
}
