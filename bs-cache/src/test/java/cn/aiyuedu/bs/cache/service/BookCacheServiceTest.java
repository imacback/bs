package cn.aiyuedu.bs.cache.service;

import com.duoqu.commons.redis.client.RedisClient;
import cn.aiyuedu.bs.common.Constants;
import cn.aiyuedu.bs.common.model.BookBase;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by Thinkpad on 2014/9/25.
 */
//@Ignore
public class BookCacheServiceTest extends BaseTest {

    @Autowired
    private BookCacheService bookCacheService;
    @Autowired
    private RedisClient redisClient;
    @Autowired
    private CounterCacheService counterCacheService;

    @Test
    public void testGet() {
        BookBase bookBase = bookCacheService.get(2375l);
        Assert.assertNotNull(bookBase);
        System.out.println(bookBase.getName()+","+bookBase.getProviderId()+","+bookBase.getSmallPic()+","+bookBase.getPublishChapters());
    }

    @Test
    public void testAddPageView() {
        Long bookId = 1276l;
        bookCacheService.addPageView(bookId);

        Constants.Counter counter = counterCacheService.getPageViewCounter(redisClient);
        System.out.println("currentCounter:"+counter.getName());
        int count = bookCacheService.getPageView(bookId, counter);
        System.out.println(count);
    }

    @Test
    public void testChangeCounter() {
        Long bookId = 1276l;
        bookCacheService.addPageView(bookId);

        Constants.Counter counter = counterCacheService.getPageViewCounter(redisClient);
        System.out.println("currentCounter:"+counter.getName());
        int count = bookCacheService.getPageView(bookId, counter);
        System.out.println(count);

        System.out.println("--------------");

        counterCacheService.changeCounter(redisClient);

        bookCacheService.addPageView(bookId);

        counter = counterCacheService.getPageViewCounter(redisClient);
        System.out.println("currentCounter:"+counter.getName());
        count = bookCacheService.getPageView(bookId, counter);
        System.out.println(count);
    }
}
