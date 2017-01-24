package cn.aiyuedu.bs.back.dto;

import java.util.Map;
import java.util.Set;

/**
 * Description:
 *
 * @author yz.wu
 */
public class CategoryTagDto {
    Map<Integer, Set<Integer>> tagMap;
    Integer categoryId;

    public Map<Integer, Set<Integer>> getTagMap() {
        return tagMap;
    }

    public void setTagMap(Map<Integer, Set<Integer>> tagMap) {
        this.tagMap = tagMap;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }
}
