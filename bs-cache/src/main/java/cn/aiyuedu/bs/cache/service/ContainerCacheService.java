package cn.aiyuedu.bs.cache.service;

import cn.aiyuedu.bs.cache.util.RedisKeyUtil;
import com.duoqu.commons.redis.client.RedisClient;
import cn.aiyuedu.bs.common.model.ContainerBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Description:
 *
 * @author yz.wu
 */
@Service("containerCacheService")
public class ContainerCacheService {

    @Autowired
    private RedisClient redisClient;

    public void publish(ContainerBase c) {
        redisClient.addToHashWithMsgPack(RedisKeyUtil.HASH_CONTAINER, c.getId().toString(), c);
    }

    /**
     * 取单个页面
     * @param containerId
     * @return
     */
    public ContainerBase get(Integer containerId) {
        return redisClient.getFromHashWithMsgPack(RedisKeyUtil.HASH_CONTAINER, containerId.toString(), ContainerBase.class);
    }

    /**
     * 取所有页面
     * @return
     */
    public List<ContainerBase> getAll() {
        return redisClient.getAllFromHashWithMsgPack(RedisKeyUtil.HASH_CONTAINER, ContainerBase.class);
    }
}
