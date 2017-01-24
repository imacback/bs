package cn.aiyuedu.bs.front.redis;

import cn.aiyuedu.bs.service.*;
import com.duoqu.commons.redis.client.RedisClient;
import cn.aiyuedu.bs.cache.service.CounterCacheService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Properties;

public class RedisMessageDelegate {
    private static Logger logger = LoggerFactory.getLogger(RedisMessageDelegate.class);

    @Autowired
    private Properties redisConfig;
    @Autowired
    private CategoryGeneralService categoryGeneralService;
    @Autowired
    private RankingGeneralService rankingGeneralService;
    @Autowired
    private TagGeneralService tagGeneralService;
    @Autowired
    CommentGeneralService commentGeneralService;
    @Autowired
    ProviderGeneralService providerGeneralService;
    @Autowired
    private CounterCacheService counterCacheService;
    @Autowired
    private RedisClient redisClient;
    @Autowired
    private PaySwitchGeneralService paySwitchGeneralService;
    @Autowired
    private SubConfigGeneralService subConfigGeneralService;
    @Autowired
    private UserRecommendGeneralService userRecommendGeneralService;

    public void handleMessage(String topic, String message) {
        logger.info("RedisMessageDelegate: " + topic + " = " + message);
        if (redisConfig.getProperty("redis.topic.back.category").equals(topic)){
            categoryGeneralService.reload();
        } else if (redisConfig.getProperty("redis.topic.back.provider").equals(topic)){
            providerGeneralService.reload();
        } else if (redisConfig.getProperty("redis.topic.back.ranking").equals(topic)){
            rankingGeneralService.reload();
        } else if (redisConfig.getProperty("redis.topic.back.tag").equals(topic)){
            tagGeneralService.reload();
        } else if (redisConfig.getProperty("redis.topic.back.bookComment").equals(topic)){
            commentGeneralService.reload();
        } else if(redisConfig.getProperty("redis.topic.back.paySwitch").equals(topic)){
            paySwitchGeneralService.reload();
        } else if(redisConfig.getProperty("redis.topic.back.subConfig").equals(topic)){
            subConfigGeneralService.reload();
        } else if(redisConfig.getProperty("redis.topic.back.userrecommend").equals(topic)){
            userRecommendGeneralService.reload();
        }
    }
}
