package cn.aiyuedu.bs.dao.repository;

import cn.aiyuedu.bs.dao.entity.Chapter;
import cn.aiyuedu.bs.dao.BaseTest;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.junit.Ignore;
import org.junit.Test;

import java.util.List;
import java.util.Map;

/**
 * Description:
 *
 * @author yz.wu
 */
@Ignore
public class ChapterDaoTest extends BaseTest {

    //@Test
    public void testUpdateStatus() {
        List<Long> list = Lists.newArrayList();
        list.add(1l);
        list.add(2l);

        Map<String, Object> parameters = Maps.newHashMap();
        parameters.put("list", list);
        parameters.put("status", 2);
        //chapterDao.updateStatus(parameters);

    }

    @Test
    public void testGet() {
        Long chapterId = 137l;
        Long bookId = 108l;
        Map<String, Object> parameters = Maps.newHashMap();
        //parameters.put("id", chapterId);
        parameters.put("bookId", bookId);
        parameters.put("isMaxOrder", 1);
        Chapter c = null;
        System.out.println(c.getName());
    }
}
