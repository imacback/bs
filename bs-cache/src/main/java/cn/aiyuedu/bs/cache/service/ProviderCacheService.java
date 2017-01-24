package cn.aiyuedu.bs.cache.service;

import cn.aiyuedu.bs.cache.util.RedisKeyUtil;
import com.duoqu.commons.redis.client.RedisClient;
import cn.aiyuedu.bs.common.model.ProviderBase;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Description:
 *
 * @author yz.wu
 */
@Service("providerCahceService")
public class ProviderCacheService {

    @Autowired
    private RedisClient redisClient;

    public void set(List<? extends ProviderBase> list) {
        Map<String, ProviderBase> map = Maps.newHashMap();
        for (ProviderBase o : list) {
            map.put(o.getId().toString(), o);
        }
        redisClient.delete(RedisKeyUtil.HASH_PROVIDER, true);
        redisClient.addToHashWithMsgPack(RedisKeyUtil.HASH_PROVIDER, map);
    }

    public List<ProviderBase> get() {
        return redisClient.getAllFromHashWithMsgPack(RedisKeyUtil.HASH_PROVIDER, ProviderBase.class);
    }
}
