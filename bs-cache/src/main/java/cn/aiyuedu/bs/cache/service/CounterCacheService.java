package cn.aiyuedu.bs.cache.service;

import cn.aiyuedu.bs.cache.util.RedisKeyUtil;
import com.duoqu.commons.redis.client.RedisClient;
import cn.aiyuedu.bs.common.Constants.Counter;
import cn.aiyuedu.bs.common.dto.CounterDto;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Properties;
import java.util.Set;

/**
 * Description:
 *
 * @author yz.wu
 */
@Service("counterCacheService")
public class CounterCacheService {

    private final Logger logger = LoggerFactory.getLogger(CounterCacheService.class);

    @Autowired
    private Properties redisConfig;

    public CounterDto changeCounter(RedisClient redisClient) {
        CounterDto c = new CounterDto();
        c.setPageViewCounter(change(RedisKeyUtil.HASH_BOOK_PV_COUNTER, redisClient));

        redisClient.publish(redisConfig.getProperty("redis.topic.back.counter"), "reload");

        return c;
    }

    public Counter getPageViewCounter(RedisClient redisClient) {
        return get(RedisKeyUtil.HASH_BOOK_PV_COUNTER, redisClient);
    }

    public void delPageViewCounter(Counter counter, RedisClient redisClient) {
        redisClient.delete(RedisKeyUtil.getPageViewHashKey(counter.getName()), false);
    }

    public Set<Object> getIdsFromPageViewCounter(Counter counter, RedisClient redisClient) {
        return redisClient.getKeysFromHash(RedisKeyUtil.getPageViewHashKey(counter.getName()), false);
    }

    private Counter get(String key, RedisClient redisClient) {
        String name = redisClient.get(key);
        Counter c = null;
        if (StringUtils.isNotEmpty(name)) {
            c = Counter.getByName(name);
            if (c == null) {
                name = null;
            }
        }

        if (StringUtils.isEmpty(name)) {
            c = Counter.getById(0);
            redisClient.set(key, c.getName());
        }

        return c;
    }

    private Counter change(String key, RedisClient redisClient) {
        Counter c = get(key, redisClient);
        if (c != null) {
            Integer id = Math.abs(c.getId() - 1);
            redisClient.set(key, Counter.getById(id).getName());
            if (logger.isDebugEnabled()) {
                logger.debug("counter change, from " + c.getName() + " to " + Counter.getById(id).getName());
            }
        } else {
            c = Counter.getById(0);
            redisClient.set(key, c.getName());
            if (logger.isDebugEnabled()) {
                logger.debug("counter init, current is " + c.getName());
            }
        }

        return c;
    }
}
