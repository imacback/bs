package cn.aiyuedu.bs.common.model;

import com.alibaba.fastjson.annotation.JSONField;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 用户充值记录
 *
 * @author Scott
 */
@Document(collection = "recharge")
public class RechargeBase {
    Integer id;
    Integer tradeStatus;//1创建订单成功 2签名验证失败 3创建订单失败 4客户端支付成功 5客户端支付失败 6服务端支付成功 7后台通知签名验证失败

    Integer type;//充值类型 ，花钱/签到
    Integer userId;//用户ID
    String uid;

    Integer platform;//平台
    String version;//版本
    Integer channelId;//渠道
    Date createTime;//订单创建时间
    Date clientDoneTime;//客户端支付完成时间

    //创建订单阶段
    String token;//	交易SESSION编号	String(32)		不可空	如：token=0EA4B3FE419AEAAD09CF963EE928F3ED
    String seckey;//	一次性签名密钥	String(32)		不可空	如：seckey=FD8C6BBC083CBB221326B0010D8BC6E1
    String resultCode;//		应答码	String(4)		不可空	应答码，0000为成功，其他为失败
    String resultMsg;//		应答信息	String(100)		可空	应答信息
    @JSONField(name = "mer_trade_code")
    String merTradeCode; //订单号
    BigDecimal recAmount;//订单金额（RMB）
    String productName; //虚拟产品名称
    Integer busAmount;//扣款金额，短信支付必输

    //支付阶段
    String gatewayTradeCode;//网关交易编号
    String innerTradeCode;    //支付网关流水号	String(32)	支付网关送给银行的流水号	不可空	支付网关完成支持后，为了方便商户运维
    String bankCode;    //银行编号	String(20)	银行编号	可空	所选银行的编码
    String payTime;    //支付时间	String(16)	银行或第三方支付公司提供的实际付款时间	可空	合作商户可以根据该字段确定银行或第三方支付公司的实际付款时间，如果没有成功支付，该字段为空，格式：YYYYMMDDHHMISS
    String bankPayFlag;    //银行付款状态(支付状态)	String(20)	银行或第三方支付公司提供的支付状态	可空	如果银行或第三方支付公司成功支付，返回success。如果支付过程断路，该字段为空。如果支付失败，返回failed:机构应答码:机构应答信息
    String bankTradeCode;//	第三方流水号	string(32)	银行或者第三方支付公司提供的支付流水号	可空	商户可以根据这个流水号在日常的运维中使用
    String payAmount;    //实际支付金额	Number		可空	当订单金额与实际支付金额不一致时，返回。在充值卡业务中会出现这种场景
    //String md_time;//防钓鱼时间戳	String(64)			请求参数原样带回
    //String ret_params;//回传参数列表	String(500)			请求参数原样带回
    //String product_price;//商品单价	Number			请求参数原样带回
    Integer productUnitNum = 0;//购买数量	Integer			请求参数原样带回
    Integer freeProductUnitNum = 0;//免费虚拟币购买数量
    //String sign;    //交易签名			不可空
    //1.3 added
    Integer payType;//充值流程类型
    Date editTime;//修改时间
    String payAccount;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Date getEditTime() {
        return editTime;
    }

    public void setEditTime(Date editTime) {
        this.editTime = editTime;
    }

    public Integer getPayType() {
        return payType;
    }

    public void setPayType(Integer payType) {
        this.payType = payType;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getClientDoneTime() {
        return clientDoneTime;
    }

    public void setClientDoneTime(Date clientDoneTime) {
        this.clientDoneTime = clientDoneTime;
    }

    public Integer getFreeProductUnitNum() {
        return freeProductUnitNum;
    }

    public void setFreeProductUnitNum(Integer freeProductUnitNum) {
        this.freeProductUnitNum = freeProductUnitNum;
    }

    public Integer getPlatform() {
        return platform;
    }

    public void setPlatform(Integer platform) {
        this.platform = platform;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Integer getChannelId() {
        return channelId;
    }

    public void setChannelId(Integer channelId) {
        this.channelId = channelId;
    }

    public Integer getProductUnitNum() {
        return productUnitNum;
    }

    public void setProductUnitNum(Integer productUnitNum) {
        this.productUnitNum = productUnitNum;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }


    public Integer getTradeStatus() {
        return tradeStatus;
    }

    public void setTradeStatus(Integer tradeStatus) {
        this.tradeStatus = tradeStatus;
    }

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public String getResultMsg() {
        return resultMsg;
    }

    public void setResultMsg(String resultMsg) {
        this.resultMsg = resultMsg;
    }

    public String getSeckey() {
        return seckey;
    }

    public void setSeckey(String seckey) {
        this.seckey = seckey;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMerTradeCode() {
        return merTradeCode;
    }

    public void setMerTradeCode(String merTradeCode) {
        this.merTradeCode = merTradeCode;
    }

    public BigDecimal getRecAmount() {
        return recAmount;
    }

    public void setRecAmount(BigDecimal recAmount) {
        this.recAmount = recAmount;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Integer getBusAmount() {
        return busAmount;
    }

    public void setBusAmount(Integer busAmount) {
        this.busAmount = busAmount;
    }

    public String getGatewayTradeCode() {
        return gatewayTradeCode;
    }

    public void setGatewayTradeCode(String gatewayTradeCode) {
        this.gatewayTradeCode = gatewayTradeCode;
    }

    public String getInnerTradeCode() {
        return innerTradeCode;
    }

    public void setInnerTradeCode(String innerTradeCode) {
        this.innerTradeCode = innerTradeCode;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getPayTime() {
        return payTime;
    }

    public void setPayTime(String payTime) {
        this.payTime = payTime;
    }

    public String getBankPayFlag() {
        return bankPayFlag;
    }

    public void setBankPayFlag(String bankPayFlag) {
        this.bankPayFlag = bankPayFlag;
    }

    public String getBankTradeCode() {
        return bankTradeCode;
    }

    public void setBankTradeCode(String bankTradeCode) {
        this.bankTradeCode = bankTradeCode;
    }

    public String getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(String payAmount) {
        this.payAmount = payAmount;
    }

    public String getPayAccount() {
        return payAccount;
    }

    public void setPayAccount(String payAccount) {
        this.payAccount = payAccount;
    }
}
