package cn.aiyuedu.bs.dao.dto;

import org.springframework.data.domain.Sort;

import java.util.LinkedHashMap;

/**
 * Created by Thinkpad on 2015/1/7.
 */
public class TagQueryDto {

    private Integer id;
    //标示主键ID是否按"!="在数据库中查询
    private Integer isNEId;
    private Integer isUse;
    private String name;
    //标示name是否按like在数据库中查询
    private Integer isLikeName;
    private Integer parentId;
    private Integer isLeaf;
    private Integer typeId;
    private String scope;
    private Integer isNotNullScope;

    private Integer start;
    private Integer limit;
    private Integer isDesc;
    //按put的顺序对相应的字段进行排序
    private LinkedHashMap<String, Sort.Direction> orderMap;

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

    public Integer getIsUse() {
        return isUse;
    }

    public void setIsUse(Integer isUse) {
        this.isUse = isUse;
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

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public Integer getIsLeaf() {
        return isLeaf;
    }

    public void setIsLeaf(Integer isLeaf) {
        this.isLeaf = isLeaf;
    }

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public Integer getIsNotNullScope() {
        return isNotNullScope;
    }

    public void setIsNotNullScope(Integer isNotNullScope) {
        this.isNotNullScope = isNotNullScope;
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

    public LinkedHashMap<String, Sort.Direction> getOrderMap() {
        return orderMap;
    }

    public void setOrderMap(LinkedHashMap<String, Sort.Direction> orderMap) {
        this.orderMap = orderMap;
    }
}
