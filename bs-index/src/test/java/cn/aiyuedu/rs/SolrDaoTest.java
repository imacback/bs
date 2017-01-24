package cn.aiyuedu.rs;

import cn.aiyuedu.bs.common.model.BookBase;
import cn.aiyuedu.bs.index.BookIndexDao;
import cn.aiyuedu.bs.index.base.IndexDao;
import com.google.common.collect.Lists;
import junit.framework.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;
import java.util.List;
import java.util.Map;
@Ignore
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/WEB-INF/config/test1/spring/test.xml"})
public class SolrDaoTest {
    @Autowired
    IndexDao bookSolrImpl;
    @Autowired
    IndexDao chapSolrImpl;
    @Autowired
    BookIndexDao bookIndexDao;
//    @Autowired
//    ChapterIndexDao chapIndexDao;

    @Test
    public void testGet() {
        List<? extends Map> list = bookSolrImpl.sortList("dqyd22:中文", 0, 10, null);
        Assert.assertEquals(true, list.size() > 0);
        for (Map<String, Object> map : list) {
            System.out.println(map);
        }
    }

    @Test
    public void testBook() {
        BookBase book = new BookBase();
        book.setId(99999L);
        book.setName("书名");
        book.setAuthor("作者");
        book.setMemo("作者");
        book.setSmallPic("http://s1.dwstatic.com/group1/M00/FB/F9/4094a35252da01094f7cfa90771e7f5d.jpg");
        book.setCreateDate(new Date());
        book.setOperatePlatformIds(Lists.newArrayList(1,2,3));
        bookIndexDao.saveBook(book);
        List<? extends Map> list = bookSolrImpl.sortList("id:" + book.getId(), 1, 10, null);
        Assert.assertEquals(1, list.size());
//        bookSolrImpl.delete("id:" + book.getId());
    }

//    @Test
//    public void testChap() {
//        ChapterIndexDto c = new ChapterIndexDto();
//        c.setChapterBase(new ChapterBase());
//        c.setText("章节内容");
//        c.getChapterBase().setId(System.currentTimeMillis());
//        c.getChapterBase().setName("书名");
//        c.getChapterBase().setBookId(System.currentTimeMillis());
//        chapIndexDao.saveChapter(c);
//        List<? extends Map> list = chapSolrImpl.sortList(
//                "id:" + c.getChapterBase().getId(), 0, 10, null);
//        Assert.assertEquals(1, list.size());
//        list = chapSolrImpl.sortList("content:内容", 0, 10, null);
//        Assert.assertEquals(true, list.size() >0);
//        chapSolrImpl.delete("id:" + c.getChapterBase().getId());
//    }

    @Test
    public void testMultiSearch() {
        BookBase book = new BookBase();
        book.setId(System.currentTimeMillis());
        book.setName("书名");
        book.setAuthor("作者");
        book.setMemo("作者");
        book.setSmallPic("http://s1.dwstatic.com/group1/M00/FB/F9/4094a35252da01094f7cfa90771e7f5d.jpg");
        book.setCreateDate(new Date());
        bookIndexDao.saveBook(book);
        List<? extends Map> list = bookSolrImpl.sortList("type3:3 AND type3:1", 1, 10, null);
        Assert.assertEquals(1, list.size());
        bookSolrImpl.delete("id:" + book.getId());
    }

}
