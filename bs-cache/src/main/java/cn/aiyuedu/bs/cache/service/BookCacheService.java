package cn.aiyuedu.bs.cache.service;

import cn.aiyuedu.bs.cache.util.RedisKeyUtil;
import com.duoqu.commons.redis.client.RedisClient;
import cn.aiyuedu.bs.common.Constants.Counter;
import cn.aiyuedu.bs.common.model.BookBase;
import com.google.common.collect.Lists;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

/**
 * Description:
 *
 * @author yz.wu
 */
@Service("bookCacheService")
public class BookCacheService {

    @Autowired
    private RedisClient redisClient;
    @Autowired
    private CounterCacheService counterCacheService;

    /**
     * 保存书籍缓存
     * @param bookBase
     */
    public void set(BookBase bookBase) {
        BookBase old = get(bookBase.getId());
        if (old != null) {
            if (CollectionUtils.isNotEmpty(old.getCategoryIds()) && CollectionUtils.isNotEmpty(old.getOperatePlatformIds())) {
                for (Integer operatePlatformId : old.getOperatePlatformIds()) {
                    for (Integer id : old.getCategoryIds()) {
                        redisClient.removeFromSortedSet(
                                RedisKeyUtil.getBookCategoryZSetKey(id, operatePlatformId),
                                old.getId().toString());
                    }
                }
            }
        }

        redisClient.addToHashWithMsgPack(RedisKeyUtil.HASH_BOOK, bookBase.getId().toString(), bookBase);

        if (CollectionUtils.isNotEmpty(bookBase.getCategoryIds())) {
            for (Integer id : bookBase.getCategoryIds()) {
                for (Integer operatePlatformId : bookBase.getOperatePlatformIds()) {
                    redisClient.addToSortedSet(RedisKeyUtil.getBookCategoryZSetKey(id, operatePlatformId),
                            bookBase.getId().toString(),
                            1 / Double.parseDouble(bookBase.getCreateDate().getTime() + bookBase.getId() + ""));
                }
            }
        }

    }

    public void setBook(BookBase bookBase) {
        redisClient.addToHashWithMsgPack(RedisKeyUtil.HASH_BOOK, bookBase.getId().toString(), bookBase);
    }

    /**
     * 获取书籍
     * @param id
     * @return
     */
    public BookBase get(Long id) {
        return redisClient.getFromHashWithMsgPack(RedisKeyUtil.HASH_BOOK, id.toString(), BookBase.class);
    }

    /**
     * 批量获取书籍
     * @param bookIds
     * @return
     */
    public List<BookBase> getBooks(List<String> bookIds) {
        List<Object> ids = Lists.newArrayList(bookIds);
        return redisClient.multiGetFromHashWithMsgPack(
                RedisKeyUtil.HASH_BOOK,
                ids,
                BookBase.class);
    }

    /**
     * 删除书籍
     * @param id
     */
    public void delete(Long id) {
        redisClient.removeFromHash(RedisKeyUtil.HASH_BOOK, false, id.toString());
    }

    /**
     * 批量删除书籍
     * @param ids
     */
    public void delete(List<Long> ids) {
        List<String> list = Lists.newArrayList();
        for (Long id : ids) {
            list.add(id.toString());
        }
        redisClient.removeFromHash(RedisKeyUtil.HASH_BOOK, true, list);
    }

    /**
     * 添加书籍pv
     * @param bookId
     * @return
     */
    public long addPageView(Long bookId) {
        Counter counter = counterCacheService.getPageViewCounter(redisClient);
        return redisClient.incrementToHash(RedisKeyUtil.getPageViewHashKey(counter.getName()), bookId.toString(), 1);
    }

    public int getPageView(Long bookId, Counter counter) {
        Object o = redisClient.getFromHash(RedisKeyUtil.getPageViewHashKey(counter.getName()), bookId.toString());
        if (o != null) {
            return Integer.valueOf(o.toString());
        }

        return 0;
    }
}
