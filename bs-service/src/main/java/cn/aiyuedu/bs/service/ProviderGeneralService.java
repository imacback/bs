package cn.aiyuedu.bs.service;

import cn.aiyuedu.bs.cache.service.ProviderCacheService;
import cn.aiyuedu.bs.common.model.ProviderBase;
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
public class ProviderGeneralService {

    @Autowired
    private ProviderCacheService providerCacheService;

    private Map<Integer, ProviderBase> map = Maps.newHashMap();

    public synchronized void reload() {
        map.clear();
        List<ProviderBase> list = providerCacheService.get();
        if (CollectionUtils.isNotEmpty(list)) {
            for (ProviderBase o : list) {
                map.put(o.getId(), o);
            }
        }
    }

    public String getName(Integer id) {
        ProviderBase o = map.get(id);
        if (o != null) {
            return o.getName();
        }

        return null;
    }
}
