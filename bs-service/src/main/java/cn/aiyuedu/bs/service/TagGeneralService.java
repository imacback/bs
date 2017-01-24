package cn.aiyuedu.bs.service;

import cn.aiyuedu.bs.cache.service.TagCacheService;
import cn.aiyuedu.bs.common.model.TagBase;
import com.google.common.collect.Maps;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

/**
 * Description:
 *
 * @author yz.wu
 */
public class TagGeneralService {

    @Autowired
    private TagCacheService tagCacheService;

    private Map<Integer, TagBase> tagMap = Maps.newHashMap();

    public synchronized void reload() {
        tagMap.clear();
        List<TagBase> list = tagCacheService.get();
        if (CollectionUtils.isNotEmpty(list)) {
            for (TagBase t : list) {
                tagMap.put(t.getId(), t);
            }
        }
    }

    public TagBase get(Integer id) {
        return tagMap.get(id);
    }

    public String getName(Integer id) {
        TagBase t = tagMap.get(id);
        if (t != null) {
            return t.getName();
        }

        return null;
    }
}
