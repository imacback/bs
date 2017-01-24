package cn.aiyuedu.bs.cache.service;

import cn.aiyuedu.bs.cache.util.RedisKeyUtil;
import com.duoqu.commons.redis.client.RedisClient;
import cn.aiyuedu.bs.common.model.ChapterBase;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Description:
 *
 * @author yz.wu
 */
@Service("chapterCacheService")
public class ChapterCacheService {

    @Autowired
    private RedisClient redisClient;

    /**
     * 保存章节
     * @param chapterBase
     */
    public void set(ChapterBase chapterBase) {
        //章节对象
        redisClient.addToHashWithMsgPack(
                RedisKeyUtil.getBookChaptersHashKey(chapterBase.getBookId()),
                chapterBase.getId().toString(), chapterBase);
        //章节id列表,以orderId排序
        redisClient.addToSortedSet(
                RedisKeyUtil.getBookChapterIdsZSetKey(chapterBase.getBookId()),
                chapterBase.getId().toString(), chapterBase.getOrderId());
        //章节orderId列表
        redisClient.addToHash(
                RedisKeyUtil.getBookChapterIdsByOrderIdHashKey(chapterBase.getBookId()),
                chapterBase.getOrderId().toString(),
                chapterBase.getId().toString()
        );
    }

    /**
     * 批量保存章节
     * @param bookId
     * @param list
     */
    public void set(Long bookId, List<? extends ChapterBase> list) {
        Map<String, ChapterBase> map = Maps.newHashMap();
        Map<String, String> orderIdMap = Maps.newHashMap();
        Map<Double, String> idsMap = Maps.newHashMap();
        for (ChapterBase c : list) {
            map.put(c.getId().toString(), c);
            orderIdMap.put(c.getOrderId().toString(), c.getId().toString());
            idsMap.put(c.getOrderId().doubleValue(), c.getId().toString());
        }
        redisClient.addToHashWithMsgPack(RedisKeyUtil.getBookChaptersHashKey(bookId), map);
        redisClient.addToSortedSet(RedisKeyUtil.getBookChapterIdsZSetKey(bookId), idsMap);
        redisClient.addToHash(RedisKeyUtil.getBookChapterIdsByOrderIdHashKey(bookId), orderIdMap);
    }

    public void setBookChapter(ChapterBase chapterBase) {
        redisClient.addToHashWithMsgPack(
                RedisKeyUtil.getBookChaptersHashKey(chapterBase.getBookId()),
                chapterBase.getId().toString(), chapterBase);
    }

    public void setBookChapters(Long bookId, List<? extends ChapterBase> list) {
        Map<String, ChapterBase> map = Maps.newHashMap();
        for (ChapterBase c : list) {
            map.put(c.getId().toString(), c);
        }
        redisClient.addToHashWithMsgPack(RedisKeyUtil.getBookChaptersHashKey(bookId), map);
    }

    /**
     * 获取章节
     * @param bookId
     * @param chapterId
     * @return
     */
    public ChapterBase get(Long bookId, Long chapterId) {
        return redisClient.getFromHashWithMsgPack(
                RedisKeyUtil.getBookChaptersHashKey(bookId),
                chapterId.toString(),
                ChapterBase.class);
    }

    /**
     * 批量取章节
     * @param chapterIds
     * @param bookId
     * @return
     */
    public List<ChapterBase> getChapters(Collection<String> chapterIds, Long bookId) {
        List<Object> list = Lists.newArrayList();
        for (String s : chapterIds) {
            list.add(s);
        }
        return redisClient.multiGetFromHashWithMsgPack(
                RedisKeyUtil.getBookChaptersHashKey(bookId),
                list,
                ChapterBase.class);
    }

    /**
     * 批量取章节
     * @param bookId
     * @param start
     * @param end
     * @return
     */
    public List<ChapterBase> getChapters(Long bookId, long start, long end) {
        Set<String> ids = redisClient.getPageFromSortedSet(
                RedisKeyUtil.getBookChapterIdsZSetKey(bookId), start, end, false);
        return getChapters(ids, bookId);
    }

    public Set<String> getChapterIds(Long bookId, double start, double end, boolean isDesc) {
        return redisClient.getPageFromSortedSetByScore(
                RedisKeyUtil.getBookChapterIdsZSetKey(bookId), start, end, isDesc);
    }

    /**
     * 批量取章节 以 score 排序
     * @param bookId
     * @param start
     * @param end
     * @return
     */
    public List<ChapterBase> getChapters(Long bookId, double start, double end, boolean isDesc) {
        Set<String> ids = redisClient.getPageFromSortedSetByScore(
                RedisKeyUtil.getBookChapterIdsZSetKey(bookId), start, end, isDesc);
        return getChapters(ids, bookId);
    }

    /**
     * 通过章节排序号批量取章节
     * @param bookId
     * @param orderIds
     * @return
     */
    public List<ChapterBase> getChapters(Long bookId, List<Integer> orderIds) {
        if (CollectionUtils.isNotEmpty(orderIds)) {
            List<Object> list = Lists.newArrayList();
            for (Integer id : orderIds) {
                list.add(id.toString());
            }
            List<Object> chapterIds = redisClient.multiGetFromHash(
                    RedisKeyUtil.getBookChapterIdsByOrderIdHashKey(bookId), list);
            chapterIds.removeAll(Collections.singleton(null));
            if (CollectionUtils.isNotEmpty(chapterIds)) {
                return redisClient.multiGetFromHashWithMsgPack(
                        RedisKeyUtil.getBookChaptersHashKey(bookId), chapterIds, ChapterBase.class);
            }
        }

        return null;
    }

    /**
     * 删除书籍下所有章节
     * @param bookId
     */
    public void remove(Long bookId) {
        redisClient.delete(RedisKeyUtil.getBookChapterIdsByOrderIdHashKey(bookId), false);
        redisClient.delete(RedisKeyUtil.getBookChapterIdsZSetKey(bookId), false);
        redisClient.delete(RedisKeyUtil.getBookChaptersHashKey(bookId), true);
    }

    /**
     * 批量删除书籍下所有章节
     * @param bookIds
     */
    public void remove(List<Long> bookIds) {
        List<String> bookChapterIdsByOrderIdKeyList = Lists.newArrayList();
        List<String> bookChapterIdsKeyList = Lists.newArrayList();
        List<String> bookChaptersKeyList = Lists.newArrayList();
        for (Long bookId : bookIds) {
            bookChapterIdsByOrderIdKeyList.add(RedisKeyUtil.getBookChapterIdsByOrderIdHashKey(bookId));
            bookChapterIdsKeyList.add(RedisKeyUtil.getBookChapterIdsZSetKey(bookId));
            bookChaptersKeyList.add(RedisKeyUtil.getBookChaptersHashKey(bookId));
        }

        redisClient.delete(bookChapterIdsByOrderIdKeyList, false);
        redisClient.delete(bookChapterIdsKeyList, false);
        redisClient.delete(bookChaptersKeyList, true);
    }

    /**
     *
     * @param chapterBase
     */
    public void remove(ChapterBase chapterBase) {
        Long bookId = chapterBase.getBookId();
        Long chapterId = chapterBase.getId();
        redisClient.removeFromHash(RedisKeyUtil.getBookChapterIdsByOrderIdHashKey(bookId), false, chapterBase.getOrderId().toString());
        redisClient.removeFromSortedSet(RedisKeyUtil.getBookChapterIdsZSetKey(bookId), chapterId.toString());
        redisClient.removeFromHash(RedisKeyUtil.getBookChaptersHashKey(bookId), true, chapterId.toString());
    }
}
