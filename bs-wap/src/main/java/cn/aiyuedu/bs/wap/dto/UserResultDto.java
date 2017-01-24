package cn.aiyuedu.bs.wap.dto;

import cn.aiyuedu.bs.common.dto.ResultDto;
import cn.aiyuedu.bs.dao.entity.User;

/**
 * Description:
 *
 * @author yz.wu
 */
public class UserResultDto extends ResultDto {

    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
