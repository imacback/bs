package cn.aiyuedu.bs.dao.entity;

import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * Description:CMS后台管理员角色
 * @author yz.wu
 */
@Document(collection = "role")
public class Role {
    private Integer id;
    private String name;
    private String memo;
    private Integer isUse;

    private List<Integer> menuIds;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public Integer getIsUse() {
        return isUse;
    }

    public void setIsUse(Integer isUse) {
        this.isUse = isUse;
    }

    public List<Integer> getMenuIds() {
        return menuIds;
    }

    public void setMenuIds(List<Integer> menuIds) {
        this.menuIds = menuIds;
    }
}
