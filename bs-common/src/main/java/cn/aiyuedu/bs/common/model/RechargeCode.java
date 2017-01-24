package cn.aiyuedu.bs.common.model;

import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 1.2 added
 * Integer rechargeType;//充值类型
 * Integer mobile;//电话
 *
 * @author Scott
 */
@Document(collection = "recharge")
public class RechargeCode extends RechargeBase{
    String mobile;//电话
    String pid;//商户ID
    String codeGetUrl;//
    String codeSubmitUrl;//
    String validMobile;//电话
    String validFee;//电话

    public String getValidMobile() {
        return validMobile;
    }

    public void setValidMobile(String validMobile) {
        this.validMobile = validMobile;
    }

    public String getValidFee() {
        return validFee;
    }

    public void setValidFee(String validFee) {
        this.validFee = validFee;
    }

    public String getCodeGetUrl() {
        return codeGetUrl;
    }

    public void setCodeGetUrl(String codeGetUrl) {
        this.codeGetUrl = codeGetUrl;
    }

    public String getCodeSubmitUrl() {
        return codeSubmitUrl;
    }

    public void setCodeSubmitUrl(String codeSubmitUrl) {
        this.codeSubmitUrl = codeSubmitUrl;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
