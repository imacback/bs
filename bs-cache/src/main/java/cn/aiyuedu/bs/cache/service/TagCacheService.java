package cn.aiyuedu.bs.cache.service;

import cn.aiyuedu.bs.cache.util.RedisKeyUtil;
import com.duoqu.commons.redis.client.RedisClient;
import cn.aiyuedu.bs.common.model.TagBase;
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
@Service("tagCacheService")
public class TagCacheService {

    @Autowired
    private RedisClient redisClient;

    public void set(List<? extends TagBase> list) {
        Map<String, TagBase> map = Maps.newHashMap();
        for (TagBase t : list) {
            map.put(t.getId().toString(), t);
        }
        redisClient.addToHashWithMsgPack(RedisKeyUtil.HASH_TAG, map);
    }

    public List<TagBase> get() {
        return redisClient.getAllFromHashWithMsgPack(RedisKeyUtil.HASH_TAG, TagBase.class);
    }

}
