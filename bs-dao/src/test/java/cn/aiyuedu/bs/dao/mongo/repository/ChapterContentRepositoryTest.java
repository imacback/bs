package cn.aiyuedu.bs.dao.mongo.repository;

import cn.aiyuedu.bs.dao.BaseTest;
import cn.aiyuedu.bs.dao.entity.ChapterContentEncrypt;
import cn.aiyuedu.bs.dao.entity.ChapterContent;
import com.google.common.collect.Lists;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Description:
 *
 * @author yz.wu
 */
@Ignore
public class ChapterContentRepositoryTest extends BaseTest {

    @Autowired
    private ChapterContentRepository chapterContentRepository;
    @Autowired
    private ChapterContentEncryptRepository chapterContentEncryptRepository;

    @Test
    public void test() {
        ChapterContent c = new ChapterContent();
        c.setBookId(1l);
        c.setChapterId(1l);
        c.setContent("测试");
        chapterContentRepository.save(c);

        c = new ChapterContent();
        c.setBookId(2l);
        c.setChapterId(2l);
        c.setContent("测试2");
        chapterContentRepository.save(c);

        c = new ChapterContent();
        c.setBookId(3l);
        c.setChapterId(3l);
        c.setContent("测试3");
        chapterContentRepository.save(c);

        c = chapterContentRepository.findOne(1l);
        assertNotNull(c);
        assertEquals(c.getChapterId(), Long.valueOf(1l));
        assertEquals(c.getContent(), "测试");

        List<ChapterContent> list = chapterContentRepository.find(Lists.newArrayList(2l, 3l));
        assertNotNull(list);
        assertEquals(2, list.size());

        chapterContentRepository.delete(Lists.newArrayList(2l, 3l));
        c = chapterContentRepository.findOne(2l);
        assertNull(c);

        c = chapterContentRepository.findOne(1l);
        assertNotNull(c);
        chapterContentRepository.deleteByBookId(1l);
        c = chapterContentRepository.findOne(2l);
        assertNull(c);

    }

    @Test
    public void testGet() {
        Long chapterId = 112486l;
        ChapterContent s = chapterContentRepository.findOne(chapterId);
        System.out.println(s.getContent());
    }

    @Test
    public void testGetEncrypt() {
        Long chapterId = 903838l;
        ChapterContentEncrypt c = chapterContentEncryptRepository.findOne(chapterId);
        if (c != null) {
            System.out.println(c.getContent());
        } else {
            System.out.println("null");
        }
    }
}
