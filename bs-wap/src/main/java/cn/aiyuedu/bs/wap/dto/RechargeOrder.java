package cn.aiyuedu.bs.wap.dto;

import cn.aiyuedu.bs.common.Constants.*;
import cn.aiyuedu.bs.wap.Constants;

/**
 * Description:
 *
 * @author yz.wu
 */
public class RechargeOrder {

    private String subject;
    private String notifyUrl;
    private String returnUrl;
    private String tradeNo;
    private String sign;
    private Double totalFee;

    private RechWay rechWay;

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    public String getReturnUrl() {
        return returnUrl;
    }

    public void setReturnUrl(String returnUrl) {
        this.returnUrl = returnUrl;
    }

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public Double getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(Double totalFee) {
        this.totalFee = totalFee;
    }

    public String toString() {
        if (rechWay != null && rechWay.val() == RechWay.zfb.val()) {
            final StringBuilder sb = new StringBuilder()
                    .append("subject=").append(getSubject())
                    .append("&sign_type=").append("RSA")
                    .append("&out_trade_no=").append(getTradeNo())
                    .append("&notify_url=").append(getNotifyUrl())
                    .append("&return_url=").append(getReturnUrl())
                    .append("&sign=").append(getSign())
                    .append("&_input_charset=").append("utf-8")
                    .append("&total_fee=").append(getTotalFee())
                    .append("&service=alipay.wap.create.direct.pay.by.user")
                    .append("&partner=").append(Constants.PARTNER_ID)
                    .append("&seller_id=").append(Constants.PARTNER_ID)
                    .append("&payment_type=").append(1);
            return sb.toString();
        } else {
            final StringBuilder sb = new StringBuilder();
            sb.append("customerid=").append(Constants.ZHIFUKA_ID)
                    .append("&sdcustomno=").append(getTradeNo())
                    .append("&orderAmount=").append(getTotalFee().intValue()*100)
                    .append("&cardno=").append(Constants.ZHIFUKA_CARDNO)
                    .append("&noticeurl=").append(getNotifyUrl())
                    .append("&backurl=").append(getReturnUrl())
                    .append("&sign=").append(getSign())
                    .append("&mark=").append(getSubject());
            return sb.toString();
        }
    }

    public String getSignString() {
        if (rechWay != null && rechWay.val() == RechWay.zfb.val()) {
            final StringBuilder sb = new StringBuilder()
                    .append("_input_charset=").append("utf-8")
                    .append("&notify_url=").append(getNotifyUrl())
                    .append("&out_trade_no=").append(getTradeNo())
                    .append("&partner=").append(Constants.PARTNER_ID)
                    .append("&payment_type=").append(1)
                    .append("&return_url=").append(getReturnUrl())
                    .append("&seller_id=").append(Constants.PARTNER_ID)
                    .append("&service=alipay.wap.create.direct.pay.by.user")
                    .append("&subject=").append(getSubject())
                    .append("&total_fee=").append(getTotalFee());

            return sb.toString();
        } else {
            final StringBuilder sb = new StringBuilder();
            sb.append("customerid=").append(Constants.ZHIFUKA_ID)
                    .append("&sdcustomno=").append(getTradeNo())
                    .append("&orderAmount=").append(getTotalFee().intValue()*100)
                    .append("&cardno=").append(Constants.ZHIFUKA_CARDNO)
                    .append("&noticeurl=").append(getNotifyUrl())
                    .append("&backurl=").append(getReturnUrl())
                    .append(Constants.ZHIFUKA_KEY);
            return sb.toString();
        }
    }

    public RechWay getRechWay() {
        return rechWay;
    }

    public void setRechWay(RechWay rechWay) {
        this.rechWay = rechWay;
    }
}
