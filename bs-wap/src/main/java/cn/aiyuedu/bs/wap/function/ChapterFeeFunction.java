package cn.aiyuedu.bs.wap.function;

import cn.aiyuedu.bs.wap.service.RechargeService;
import org.apache.commons.lang3.StringUtils;
import org.beetl.core.Context;
import org.beetl.core.Function;

/**
 * Description:
 *
 * @author yz.wu
 */
public class ChapterFeeFunction implements Function {

    public Integer call(Object[] paras, Context ctx) {
        if (paras != null && paras.length > 0 &&
                StringUtils.isNumeric(paras[0].toString())) {
            int price = RechargeService.getChapterFee(Integer.valueOf(paras[0].toString()));
            return price;
        }

        return 0;
    }
}
