
package cn.aiyuedu.bs.front.controller.resp;


/**
 * Description:
 *
 * @author Scott
 */
public class UserUpdateResp extends RespBase{
    private User user;
    private Integer status=1;//1：成功，2：失败，3：昵称重复

    public UserUpdateResp() {
        setMsg("操作成功");
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