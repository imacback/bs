package cn.aiyuedu.bs.back.schedule;

import com.duoqu.commons.redis.client.RedisClient;
import cn.aiyuedu.bs.cache.service.BookCacheService;
import cn.aiyuedu.bs.cache.service.CounterCacheService;
import cn.aiyuedu.bs.common.dto.CounterDto;
import cn.aiyuedu.bs.dao.mongo.repository.BookRepository;
import com.google.common.collect.Maps;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.Map;
import java.util.Set;

/**
 * Description:
 *
 * @author yz.wu
 */
public class BookCounterJob {

    private final Logger logger = LoggerFactory.getLogger(BookCounterJob.class);

    @Autowired
    private BookCacheService bookCacheService;
    @Autowired
    private CounterCacheService counterCacheService;
    @Autowired
    private RedisClient redisClient;
    @Autowired
    private BookRepository bookRepository;

    public void execute() {
        Date day = new Date();
        Map<Long, Integer> map = Maps.newHashMap();
        process(map);

        if (map != null && map.size() > 0) {
            storeData(map, day);
        }
    }

    public void process(Map<Long, Integer> map) {
        CounterDto cd = counterCacheService.changeCounter(redisClient);
        Set<Object> ids = counterCacheService.getIdsFromPageViewCounter(cd.getPageViewCounter(), redisClient);
        if (logger.isDebugEnabled()) {
            logger.debug("paveView, book count:" + (CollectionUtils.isNotEmpty(ids)? ids.size(): "0"));
        }
        Long id = null;
        if (CollectionUtils.isNotEmpty(ids)) {
            for (Object o : ids) {
                if(o == null) continue;
                id = Long.valueOf(o.toString());
                Integer pv = bookCacheService.getPageView(id, cd.getPageViewCounter());
                if (map.get(id) == null) {
                    map.put(id, pv);
                } else {
                    map.put(id, map.get(id) + pv);
                }
            }

            counterCacheService.delPageViewCounter(cd.getPageViewCounter(), redisClient);
        }
    }

    private boolean storeData(Map<Long, Integer> map, Date day) {
        for (Map.Entry<Long, Integer> entry : map.entrySet()) {
            bookRepository.updateViewCount(entry.getKey(), entry.getValue());
        }

        return true;
    }

}
