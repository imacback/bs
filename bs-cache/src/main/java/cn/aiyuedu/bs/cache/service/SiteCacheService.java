package cn.aiyuedu.bs.cache.service;

import com.duoqu.commons.redis.client.RedisClient;
import cn.aiyuedu.bs.common.model.SiteBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Properties;

/**
 * Description:
 *
 * @author yz.wu
 */
@Service("siteCacheService")
public class SiteCacheService {

    @Autowired
    private RedisClient redisClient;
    @Autowired
    private Properties redisConfig;

    public void publish(List<SiteBase> list) {

    }

    public List<SiteBase> getAll() {
        return null;
    }
}
