package cn.aiyuedu.bs.wap.service;

import cn.aiyuedu.bs.common.Constants;
import cn.aiyuedu.bs.common.Constants.RechWay;
import cn.aiyuedu.bs.common.model.RechargeZfb;
import cn.aiyuedu.bs.common.orm.Page;
import cn.aiyuedu.bs.dao.dto.RechargeQueryDto;
import cn.aiyuedu.bs.dao.entity.Recharge;
import cn.aiyuedu.bs.dao.entity.User;
import cn.aiyuedu.bs.dao.mongo.repository.RechargeRepository;
import cn.aiyuedu.bs.wap.dto.ParamDto;
import cn.aiyuedu.bs.wap.dto.RechargeOrder;
import cn.aiyuedu.bs.wap.util.SignUtils;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.concurrent.TimeUnit;

/**
 * Description:
 *
 * @author yz.wu
 */
@Service("rechargeService")
public class RechargeService {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    private Cache<String, String> tradeNoCache = CacheBuilder.newBuilder()
            .maximumSize(10000)
            .expireAfterWrite(30l, TimeUnit.SECONDS).build();

    public String getTradeNo(String uid) {
        return tradeNoCache.getIfPresent(uid);
    }

    public void setTraceNo2Cache(String uid, String tradeNo) {
        tradeNoCache.put(uid, tradeNo);
    }

    public void removeTradeNo(String uid) {
        tradeNoCache.put(uid, "0");
    }

    @Autowired
    private RechargeRepository rechargeRepository;

    public String getUrl(RechargeOrder rechargeOrder) {
        RechWay rechWay = rechargeOrder.getRechWay();
        if (rechWay.val() == RechWay.zfb.val()) {
            try {
                rechargeOrder.setSign(URLEncoder.encode(SignUtils.sign(rechargeOrder.getSignString(), rechWay), "utf-8"));
                rechargeOrder.setNotifyUrl(URLEncoder.encode(rechargeOrder.getNotifyUrl(), "utf-8"));
                rechargeOrder.setReturnUrl(URLEncoder.encode(rechargeOrder.getReturnUrl(), "utf-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } else {
            rechargeOrder.setSign(SignUtils.sign(rechargeOrder.getSignString(), rechWay));
        }

        return rechWay.getSubmitUrl() + rechargeOrder.toString();
    }

    public void createOrder(ParamDto param, RechargeOrder rechargeOrder) {
        RechargeZfb recharge = new RechargeZfb();
        recharge.setPayInfo(rechargeOrder.getSignString());

        recharge.setPayType(rechargeOrder.getRechWay().val());
        recharge.setTradeStatus(Constants.RechStatus.createDone.val());
        recharge.setUid(param.getUid());
        if (param.getUserId() != null) {
            recharge.setUserId(param.getUserId());
        }
        recharge.setPlatform(param.getPlatformId());
        recharge.setChannelId(param.getDistributeId());
        recharge.setMerTradeCode(rechargeOrder.getTradeNo());
        recharge.setRecAmount(new BigDecimal(rechargeOrder.getTotalFee().toString()));
        recharge.setProductUnitNum(1);
        recharge.setProductName(rechargeOrder.getSubject());
        recharge.setType(Constants.RechType.rmb.val());

        setTraceNo2Cache(recharge.getUid(), recharge.getMerTradeCode());

        rechargeRepository.createOrder(recharge);
    }

    public static int getChapterFee(int words) {
        double value = (words * Constants.THOUSAND_WORDS_PRICE) / 1000.00;
        double temp = Math.pow(10, 2);
        return (int) (((int) Math.round(temp * value)) / temp);
    }

    public Page<Recharge> getList(Integer userId, Integer start, Integer limit) {
        RechargeQueryDto queryDto = new RechargeQueryDto();
        queryDto.setUserId(userId);
        queryDto.setLimit(limit);
        queryDto.setStart(start);
        return rechargeRepository.getPage(queryDto, Recharge.class);
    }

    public void postRecharge(HttpServletRequest request, User user) {
        String tradeNo = getTradeNo(user.getUid());
        if (StringUtils.isNotBlank(tradeNo)) {
            removeTradeNo(user.getUid());
            RechargeZfb recharge = rechargeRepository.findByCode(tradeNo, RechargeZfb.class);

            if (recharge != null && recharge.getTradeStatus() == Constants.RechStatus.serverDone.val()) {
                Integer amount = recharge.getRecAmount().intValue();
                int bonus = 0;
                Constants.RechargeAmount rechargeAmount = Constants.RechargeAmount.getByAmount(amount);
                if (rechargeAmount != null) {
                    bonus = rechargeAmount.getBonus();
                }
                amount = bonus + amount;

                user.setVirtualCorn(user.getVirtualCorn() != null ? user.getVirtualCorn() + amount * Constants.CORN : amount * Constants.CORN);
                request.getSession().setAttribute("user", user);
            }
        }
    }

    public void updateUserId(String uid, Integer userId) {
        rechargeRepository.updateUserId(uid, userId);
    }
}
