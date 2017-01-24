package cn.aiyuedu.bs.cache.service;

import cn.aiyuedu.bs.cache.util.RedisKeyUtil;
import com.duoqu.commons.redis.client.RedisClient;
import com.duoqu.commons.utils.DateUtils;
import cn.aiyuedu.bs.common.Constants.DistributeName;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Description:
 *
 * @author yz.wu
 */
@Service("distributeCacheService")
public class DistributeCacheService {

    @Autowired
    private RedisClient redisClient;

    public void setLastAccessDate(DistributeName distributeName, Date date) {
        redisClient.addToHash(RedisKeyUtil.HASH_DISTRIBUTE_LASTDATE,
                distributeName.getName(), DateUtils.getFullTimeString(date));
    }

    public Date getLastAccessDate(DistributeName distributeName) {
        String date = redisClient.getFromHash(RedisKeyUtil.HASH_DISTRIBUTE_LASTDATE, distributeName.getName());
        if (StringUtils.isNotBlank(date)) {
            return DateUtils.string2Date(date, "yyyyMMddHHmmss");
        }

        return null;
    }
}
