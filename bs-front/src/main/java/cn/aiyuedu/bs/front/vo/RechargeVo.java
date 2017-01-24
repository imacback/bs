package cn.aiyuedu.bs.front.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.duoqu.commons.utils.DateUtils;
import cn.aiyuedu.bs.common.Constants;

/**
 * /**
 * 页面拼接数据
 * <p/>
 * json数组，格式：
 * 1.充值记录：[
 * {"title":"消费书籍标题","date":"充值时间","orderNum": ""}
 * ]
 * 2. 消费记录：[
 * {"title":"消费书籍标题","date":"消费时间"},
 * {"title":"消费书籍标题","date":"消费时间"}
 * ]
 */

public class RechargeVo extends RechargeBase {
    String title;
    @JSONField(name = "date")
    String dateStr;
    String orderNum;
    String productName;

    public RechargeVo(String productName) {
        this.productName = productName;
    }

    public String getTitle() {
        if (getType() == Constants.RechType.rmb.val())
            return "充值" + (getProductUnitNum() + getFreeProductUnitNum()) + productName;
        else
            return "签到奖励" + getFreeProductUnitNum() + productName;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDateStr() {
        return DateUtils.getDateString(getCreateTime(), "yyyy-MM-dd HH:mm");
    }

    public void setDateStr(String dateStr) {
        this.dateStr = dateStr;
    }

    public String getOrderNum() {
        if (getMerTradeCode() == null)
            return null;
        else
            return "订单号：" + getMerTradeCode();
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }
}
