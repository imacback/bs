package cn.aiyuedu.bs.service;


import cn.aiyuedu.bs.cache.service.ClientTabCacheService;
import cn.aiyuedu.bs.common.model.ClientTabBase;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

public class ClientTabGeneralService {

    Map<String, List<ClientTabBase>> clientTabMap;

    @Autowired
    ClientTabCacheService tabCacheService;

    public void reload() {
        if (clientTabMap == null) {
            clientTabMap = Maps.newHashMap();
        }
        List<ClientTabBase> list = tabCacheService.get();
        clientTabMap.put("key", list);
    }

    /**
     * *
     *
     * @return List <ClientTabBase>
     */
    public List<ClientTabBase> queryClientTab() {
        if (clientTabMap.get("key") != null) {
            return clientTabMap.get("key");
        }
        return null;
    }

}
