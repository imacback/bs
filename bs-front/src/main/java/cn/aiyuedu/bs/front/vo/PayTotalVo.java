package cn.aiyuedu.bs.front.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.duoqu.commons.utils.DateUtils;
import cn.aiyuedu.bs.common.Constants;

/**
 * Description:
 *
 * @author Scott
 */
public class PayTotalVo extends PayTotalBase {
    String bookName;
    String title;
    @JSONField(name = "date")
    String dateStr;
    String productName;

    public PayTotalVo(String productName) {
        this.productName = productName;
    }


    public String getTitle() {
        return "《" + bookName + "》消费" + getCost() + productName;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDateStr() {
        String dateStr = DateUtils.getDateString(getEditTime(), "yyyy-MM-dd HH:mm");
        if (getType() == Constants.PayType.chapter.val())
            return "单章末次购买时间：" + dateStr;
        else
            return "全本购买时间：" + dateStr;
    }

    public void setDateStr(String dateStr) {
        this.dateStr = dateStr;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }
}
