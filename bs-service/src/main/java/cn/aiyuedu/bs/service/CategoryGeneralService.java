package cn.aiyuedu.bs.service;

import cn.aiyuedu.bs.cache.service.CategoryCacheService;
import cn.aiyuedu.bs.common.model.CategoryBase;
import com.google.common.collect.Maps;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Description:
 *
 * @author yz.wu
 */
public class CategoryGeneralService {

    @Autowired
    private CategoryCacheService categoryCacheService;

    private Map<Integer, CategoryBase> categoryBaseMap;
    private List<CategoryBase> categoryBases;

    public synchronized void reload() {
        categoryBaseMap = Maps.newHashMap();
        categoryBases = categoryCacheService.getAll();
        if (CollectionUtils.isNotEmpty(categoryBases)) {
            for (CategoryBase c : categoryBases) {
                categoryBaseMap.put(c.getId(), c);
                if (CollectionUtils.isNotEmpty(c.getChildren())) {
                    for (CategoryBase o : c.getChildren()) {
                        categoryBaseMap.put(o.getId(), o);
                    }
                }
            }
        }
    }



    public CategoryBase get(Integer id) {
        if (categoryBaseMap == null) {
            reload();
        }
        return categoryBaseMap.get(id);
    }

    public List<CategoryBase> getCategoryBases() {
        if (CollectionUtils.isEmpty(categoryBases)) {
            reload();
        }
        return categoryBases;
    }
}
