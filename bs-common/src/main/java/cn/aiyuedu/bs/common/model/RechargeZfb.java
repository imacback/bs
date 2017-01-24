package cn.aiyuedu.bs.common.model;

import cn.aiyuedu.bs.common.util.CDataAdapter;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * 1.2 added
 *
 * @author Scott
 */
@XmlAccessorType(XmlAccessType.FIELD)
@Document(collection = "recharge")
public class RechargeZfb extends RechargeBase {
    @XmlJavaTypeAdapter(CDataAdapter.class)
    String payInfo;
    // 服务接口名称， 固定值
//    orderInfo += "&service=\"mobile.securitypay.pay\"";

    // 支付类型， 固定值
//    orderInfo += "&payment_type=\"1\"";

    // 参数编码， 固定值
//    orderInfo += "&_input_charset=\"utf-8\"";

    // 设置未付款交易的超时时间
    // 默认30分钟，一旦超时，该笔交易就会自动被关闭。
    // 取值范围：1m～15d。
    // m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
    // 该参数数值不接受小数点，如1.5h，可转换为90m。
    String timeOut;

    // extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
    // orderInfo += "&extern_token=" + "\"" + extern_token + "\"";

    // 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
//    returnUrl=\"m.alipay.com\";

    // 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
//    String orderInfo += "&paymethod=\"expressGateway\"";
    String paymethod;

    ////////////////////////////////////////////////////
//    String notify_type;  // 通知类型 String 通知的类型。 不可空 trade_status_sync
    String notifyId;   //通知校验 ID    String 通知校验 ID。 不可空    64ce1b6ab92d00ede0e e56ade98fdf2f4c
    //    String sign_type; //签名方式 String 固定取值为 RSA。 不可空 RSA
//    String sign;//签名 String 请参见“9  签名机制”。 不可空    1glihU9DPWee+UJ82u 3+mw3Bdnr9u01at0M/x JnPsGuHh+JA5bk3zb WaoWhU6GmLab3dIM 4JNdktTcEUI9/FBGhgf LO39BKX/eBCFQ3bXA mIZn4l26fiwoO613BptT 44GTEtnPiQ6+tnLsGlV SrFZaLB9FVhrGfipH2S WJcnwYs=
    // 业务参数
//    String out_trade_no;  //商户网站唯 一订单号    String(64 )    对应商户网站的订单系统中的 唯一订单号，非支付宝交易号。    需保证在商户网站中的唯一性。 是请求时对应的参数，原样返 回。可空 082215222612710
    String subject;//商品名称    String(12 8)    商品的标题/交易标题/订单标题 /订单关键字等。 它在支付宝的交易明细中排在 第一列，对于财务对账尤为重 要。是请求时对应的参数，原样 通知回来。  可空 测试
    //    String payment_type;  // 支付类型 String(4)    支付类型。默认值为：1（商品 购买）。    可空 1
    String tradeNo;  // 支付宝交易 号    String(64 )    该交易在支付宝系统中的交易 流水号。 最短 16 位，最长 64 位。    可空 2013082244524842
    String zfbTradeStatus; // 交易状态 String    交易状态，取值范围请参见 “11.3  交易状态”。    可空 TRADE_SUCCESS
    String sellerId;   //卖家支付宝 用户号    String(30 )    卖家支付宝账号对应的支付宝 唯一用户号。 以 2088 开头的纯 16 位数字。    可空 2088501624816263
    String sellerEmail; //  卖家支付宝 账号    String(10 0)    卖家支付宝账号，可以是 email 和手机号码。    可空 xxx@alipay.com
    String buyerId;  //买家支付宝 用户号    String(30 )    买家支付宝账号对应的支付宝 唯一用户号。 以 2088 开头的纯 16 位数字。    可空 2088602315385429
    String buyerEmail; // 买家支付宝 账号    String(10 0)    买家支付宝账号，可以是 Email 或手机号码。    可空 dlwdgl@gmail.com
    String totalFee; //交易金额 Number    该笔订单的总金额。 请求时对应的参数，原样通知回 来。    可空 1.00
//    String quantity;//购买数量 Number    购买数量，固定取值为 1（请求 时使用的是 total_fee）。    可空 1
//    String price; //商品单价 Number    price 等于 total_fee（请求时使 用的是 total_fee）。    可空 1.00
//    String body; //商品描述   // String(51 2)    该笔订单的备注、描述、明细等。 对应请求时的 body 参数，原样 通知回来。    可空 测试测试
//    String gmt_create; //交易创建时 间    Date    该笔交易创建的时间。 格式为 yyyy-MM-dd HH:mm:ss。    可空 2013-08-22 14:45:23
//    String gmt_payment;  //交易付款时 间    Date    该笔交易的买家付款时间。 格式为 yyyy-MM-dd HH:mm:ss。    可空 2013-08-22 14:45:24
//    String is_total_fee_adjust; // 是否调整总 价    String(1) 该交易是否调整过价格。 可空 N
//    String use_coupon; //是否使用红 包买家    String(1) 是否在交易过程中使用了红包。 可空 N
//    String discount;//折扣 String    //支付宝系统会把 discount 的值 加到交易金额上，如果有折扣， 本参数为负数，单位为元。    可空 0.00
//    String refund_status;   //退款状态 String    取值范围请参见“11.4  退款状 态”。    可空 REFUND_SUCCESS
//    String gmt_refund;   //退款时间 Date    卖家退款的时间，退款通知时会 发送。 格式为 yyyy-MM-dd HH:mm:ss。    可空

    public String getPayInfo() {
        return payInfo;
    }

    public void setPayInfo(String payInfo) {
        this.payInfo = payInfo;
    }

    public String getTimeOut() {
        return timeOut;
    }

    public void setTimeOut(String timeOut) {
        this.timeOut = timeOut;
    }

    public String getPaymethod() {
        return paymethod;
    }

    public void setPaymethod(String paymethod) {
        this.paymethod = paymethod;
    }

    public String getNotifyId() {
        return notifyId;
    }

    public void setNotifyId(String notifyId) {
        this.notifyId = notifyId;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    public String getZfbTradeStatus() {
        return zfbTradeStatus;
    }

    public void setZfbTradeStatus(String zfbTradeStatus) {
        this.zfbTradeStatus = zfbTradeStatus;
    }

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public String getSellerEmail() {
        return sellerEmail;
    }

    public void setSellerEmail(String sellerEmail) {
        this.sellerEmail = sellerEmail;
    }

    public String getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(String buyerId) {
        this.buyerId = buyerId;
    }

    public String getBuyerEmail() {
        return buyerEmail;
    }

    public void setBuyerEmail(String buyerEmail) {
        this.buyerEmail = buyerEmail;
    }

    public String getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(String totalFee) {
        this.totalFee = totalFee;
    }
}
