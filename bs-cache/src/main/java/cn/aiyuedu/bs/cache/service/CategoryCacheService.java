package cn.aiyuedu.bs.cache.service;

import com.duoqu.commons.redis.client.RedisClient;
import cn.aiyuedu.bs.cache.util.RedisKeyUtil;
import cn.aiyuedu.bs.common.model.CategoryBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Properties;

/**
 * Description:
 *
 * @author yz.wu
 */
@Service("categoryCacheService")
public class CategoryCacheService {

    @Autowired
    private RedisClient redisClient;
    @Autowired
    private Properties redisConfig;

    public void publish(List<? extends CategoryBase> categoryList) {
        redisClient.delete(RedisKeyUtil.LIST_CATEGORY, true);
        redisClient.addToListWithMsgPack(RedisKeyUtil.LIST_CATEGORY, false, categoryList);
        redisClient.publish(redisConfig.getProperty("redis.topic.back.category"), "reload");
        redisClient.publish(redisConfig.getProperty("redis.topic.wap.category"), "reload");
    }

    public List<CategoryBase> getAll() {
        return redisClient.getPageFromListWithMsgPack(RedisKeyUtil.LIST_CATEGORY, CategoryBase.class, 0, 100);
    }
}
