package cn.aiyuedu.bs.cache.service;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import cn.aiyuedu.bs.common.model.ChapterBase;

/**
 * Description:
 *
 * @author yz.wu
 */
//@Ignore
public class ChapterCacheServiceTest extends BaseTest {

    @Autowired
    private ChapterCacheService chapterCacheService;

    @Test
    public void test() {
        ChapterBase c = chapterCacheService.get(2666l, 955154l);
        if (c != null) {
            System.out.println(c.getName()+",price:"+c.getPrice());
        }
    }

    @Test
    public void testGetAll() {
        List<ChapterBase> chapters = chapterCacheService.getChapters(2375l, 0, 10, false);
        System.out.println(chapters.size());
        for (ChapterBase c : chapters) {
            System.out.println(c.getName());
        }
    }


}
