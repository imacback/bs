package cn.aiyuedu.bs.common.model;

import org.springframework.data.mongodb.core.index.Indexed;

/**
 * Description:
 *
 * @author yz.wu
 */

public class UserBase {

    private Integer id;
    @Indexed
    private String uid;
    @Indexed
    private String userName;
    private String password;
    private Integer platformId;
    private Integer dictId;
    private Integer sex;
    private String mobile;
    @Indexed
    private String email;
    private String nickname;
    private Integer virtualCorn;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getPlatformId() {
        return platformId;
    }

    public void setPlatformId(Integer platformId) {
        this.platformId = platformId;
    }

    public Integer getDictId() {
        return dictId;
    }

    public void setDictId(Integer dictId) {
        this.dictId = dictId;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public Integer getVirtualCorn() {
        return virtualCorn;
    }

    public void setVirtualCorn(Integer virtualCorn) {
        this.virtualCorn = virtualCorn;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
