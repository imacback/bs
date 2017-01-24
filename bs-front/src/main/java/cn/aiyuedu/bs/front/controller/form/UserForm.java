package cn.aiyuedu.bs.front.controller.form;

import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.Date;


public class UserForm implements Serializable {

    private Integer id;
    private String nickName;
    private Integer sex;
    private String mobile;
    private Date birthday;

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        if (StringUtils.hasText(nickName))
            this.nickName = nickName;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    //    @JsonSerialize(using = DateYMDHMSJsonSerializer.class)
    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
