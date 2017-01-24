package cn.aiyuedu.bs.cache.service;

import cn.aiyuedu.bs.common.model.ChapterBase;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Description:
 *
 * @author yz.wu
 */
@Ignore
public class ChapterCahceServiceTest extends BaseTest {

    @Autowired
    private ChapterCacheService chapterCacheService;

    @Test
    public void testGet() {
        long bookId = 1127l;

        List<ChapterBase> list = chapterCacheService.getChapters(bookId, 0, 10, false);
        for (ChapterBase c : list) {
            System.out.println(c.getName()+", orderId:"+c.getOrderId() + ", sumWords:" + c.getSumWords());
        }
    }

    @Test
    public void testGetChapter() {
        Long bookId = 1564l;
        Long chapterId = 592330l;
        ChapterBase c = chapterCacheService.get(bookId, chapterId);
        System.out.println(c.getPrice());
    }
}
