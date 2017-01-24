package cn.aiyuedu.bs.service;

import cn.aiyuedu.bs.cache.service.PlatformCacheService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by Thinkpad on 2014/9/26.
 */


public class PlatformCeneralService {
    @Autowired
    PlatformCacheService platformCacheService;

    public String get(String id) {
        return platformCacheService.getByid(id);
    }

}
