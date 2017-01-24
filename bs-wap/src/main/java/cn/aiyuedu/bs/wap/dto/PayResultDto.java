package cn.aiyuedu.bs.wap.dto;

import cn.aiyuedu.bs.dao.entity.User;

/**
 * Description:
 *
 * @author yz.wu
 */
public class PayResultDto {

    public static final int BUY_RESULT_SUCCESS = 1;
    public static final int BUY_RESULT_NO_MONEY = 2;
    public static final int BUY_RESULT_DUPLICATE = 3;
    private User user;
    private Integer price;//需要支付的钱
    private Integer status = -1;

    public PayResultDto(User user, Integer price, int status) {
        setUser(user);
        setStatus(status);
        setPrice(price);
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
