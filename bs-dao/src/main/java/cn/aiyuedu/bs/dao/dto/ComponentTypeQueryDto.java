package cn.aiyuedu.bs.dao.dto;

/**
 * Created by Thinkpad on 2015/1/5.
 */
public class ComponentTypeQueryDto {

    private Integer id;
    //标示主键ID是否按"!="在数据库中查询
    private Integer isNEId;
    private String name;
    //标示name是否按like在数据库中查询
    private Integer isLikeName;

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
