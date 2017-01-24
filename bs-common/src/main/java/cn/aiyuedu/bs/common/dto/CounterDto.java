package cn.aiyuedu.bs.common.dto;

import cn.aiyuedu.bs.common.Constants;

import java.io.Serializable;

/**
 * Description:
 *
 * @author yz.wu
 */
public class CounterDto implements Serializable {

    private static final long serialVersionUID = 4051793436997730864L;

    private Constants.Counter pageViewCounter;

    public Constants.Counter getPageViewCounter() {
        return pageViewCounter;
    }

    public void setPageViewCounter(Constants.Counter pageViewCounter) {
        this.pageViewCounter = pageViewCounter;
    }
}
