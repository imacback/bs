package cn.aiyuedu.bs.dao.dto;

/**
 * Created by Thinkpad on 2014/12/28.
 */
public class AdminUserQueryDto {

    private Integer id;
    //标示主键ID是否按"!="在数据库中查询
    private Integer isNEId;
    private String name;
    //标识名称是否按Like在数据库中查询
    private Integer isLikeName;
    private String email;
    //标识电子邮件是否按Like在数据库中查询
    private Integer isLikeEmail;
    private String password;
    private String nickname;
    //标识昵称是否按Like在数据库中查询
    private Integer isLikeNickname;
    private Integer roleId;
    private Integer isUse;

    private Integer start;
    private Integer limit;
    private Integer isDesc;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIsNEId() {
        return isNEId;
    }

    public void setIsNEId(Integer isNEId) {
        this.isNEId = isNEId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getIsLikeName() {
        return isLikeName;
    }

    public void setIsLikeName(Integer isLikeName) {
        this.isLikeName = isLikeName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getIsLikeEmail() {
        return isLikeEmail;
    }

    public void setIsLikeEmail(Integer isLikeEmail) {
        this.isLikeEmail = isLikeEmail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Integer getIsLikeNickname() {
        return isLikeNickname;
    }

    public void setIsLikeNickname(Integer isLikeNickname) {
        this.isLikeNickname = isLikeNickname;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public Integer getIsUse() {
        return isUse;
    }

    public void setIsUse(Integer isUse) {
        this.isUse = isUse;
    }

    public Integer getStart() {
        return start;
    }

    public void setStart(Integer start) {
        this.start = start;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public Integer getIsDesc() {
        return isDesc;
    }

    public void setIsDesc(Integer isDesc) {
        this.isDesc = isDesc;
    }
}
