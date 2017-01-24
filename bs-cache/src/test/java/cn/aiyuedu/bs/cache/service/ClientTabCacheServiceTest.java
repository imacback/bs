package cn.aiyuedu.bs.cache.service;

import cn.aiyuedu.bs.cache.util.RedisKeyUtil;
import com.duoqu.commons.redis.client.RedisClient;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

@Ignore
public class ClientTabCacheServiceTest extends BaseTest {
@Autowired
ClientTabCacheService clientTabCacheService;
    
    @Autowired
    private RedisClient redisClient;

    @Test
    public void get(){
        redisClient.delete(RedisKeyUtil.HASH_WAP_BOOKLIST ,true);
    }
}
