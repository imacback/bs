package cn.aiyuedu.bs.wap.service;

import cn.aiyuedu.bs.common.Constants;
import cn.aiyuedu.bs.common.model.RechargeZfb;
import cn.aiyuedu.bs.dao.mongo.repository.RechargeRepository;
import com.duoqu.commons.utils.HttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Map;

/**
 * Description:
 *
 * @author yz.wu
 */
@Service("rechargeZfbService")
public class RechargeZfbService extends RechargeService {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private RechargeRepository rechargeRepository;

    public boolean callbackUpdate(Map<String, String> paramMap) {
        try {
            //https://mapi.alipay.com/gateway.do?service=notify_verify&partner=2088002396712354&notify_id=RqPnCoPT3K9%252Fvwbh3I%252BFioE227%252BPfNMl8jwyZqMIiXQWxhOCmQ5MQO%252FWd93rvCB%252BaiGg
            log.info("callbackUpdate paramMap: " + paramMap);
            //验证sign
            String sign = paramMap.remove("sign");
            paramMap.remove("&sign_type");

            String result = HttpClient.get("https://mapi.alipay.com/gateway.do?service=notify_verify&" +
                    "partner=" + Constants.PARTNER_ID +
                    "&notify_id=" + paramMap.get("notify_id"));
            log.info(paramMap.get("notify_id") + "callbackUpdate notifyVerify: " + result);
            if ("false".equals(result)) {
                return false;
            }

            RechargeZfb recharge = rechargeRepository.findByCode(paramMap.get("out_trade_no"), RechargeZfb.class);
            log.info("callbackUpdate recharge: " + recharge);
            if (recharge == null) {
                return false;
            }

            recharge.setNotifyId(paramMap.get("notify_id"));
            recharge.setPayAccount(paramMap.get("buyer_email"));
            recharge.setTradeNo(paramMap.get("trade_no"));
            recharge.setZfbTradeStatus(paramMap.get("trade_status"));

            log.info(recharge.getMerTradeCode() + " callbackUpdate third : " + recharge.getZfbTradeStatus());
            if (!StringUtils.hasText(recharge.getZfbTradeStatus())) {
                rechargeRepository.persist(recharge);
                return false;
            } else if ("TRADE_SUCCESS".equals(recharge.getZfbTradeStatus()) || "TRADE_FINISHED".equals(recharge.getZfbTradeStatus())) {
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
