package cn.aiyuedu.bs.cache.service;

import cn.aiyuedu.bs.cache.util.RedisKeyUtil;
import com.duoqu.commons.redis.client.RedisClient;
import cn.aiyuedu.bs.common.model.ComponentBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Description:
 *
 * @author yz.wu
 */
@Service("componentCacheService")
public class ComponentCacheService {

    @Autowired
    RedisClient redisClient;

    public void publish(ComponentBase c) {
        redisClient.addToHashWithMsgPack(RedisKeyUtil.HASH_COMPONENT, c.getId().toString(), c);
    }

    /**
     * 取组件
     * @param componentId
     * @return
     */
    public ComponentBase get(Integer componentId) {
        return redisClient.getFromHashWithMsgPack(RedisKeyUtil.HASH_COMPONENT, componentId.toString(), ComponentBase.class);
    }

    /**
     * 取所有组件
     */
    public void getAll() {
        redisClient.getAllFromHashWithMsgPack(RedisKeyUtil.HASH_COMPONENT, ComponentBase.class);
    }
}
