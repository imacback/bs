package cn.aiyuedu.bs.wap.function;

import cn.aiyuedu.bs.common.Constants.*;
import org.apache.commons.lang3.StringUtils;
import org.beetl.core.Context;
import org.beetl.core.Function;

/**
 * Description:
 *
 * @author yz.wu
 */
public class ComponentTargetFunction implements Function {

    /**
     *
     * @param paras 1,dataType; 2,dataId; 3,prefix
     * @param ctx
     * @return
     */
    public String call(Object[] paras, Context ctx) {
        ComponentDataType type = ComponentDataType.getByValue(Integer.valueOf(paras[0].toString()));
        if (type != null) {
            String url = type.getUrl();
            if (StringUtils.isNotBlank(url)) {
                if (type.getValue() == 2) {
                    return url + "?id=" + paras[1];
                } else {
                    return url + "?bid=" + paras[1];
                }
            }
        }

        return "";
    }
}
