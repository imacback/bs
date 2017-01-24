package cn.aiyuedu.bs.back.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Properties;

public class RedisMessageDelegate {
    private static Logger logger = LoggerFactory.getLogger(RedisMessageDelegate.class);
    @Autowired
    private Properties redisConfig;

    public void handleMessage(String topic, String message) {
        logger.info("RedisMessageDelegate: " + topic + " = " + message);
    }

}
