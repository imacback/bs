package cn.aiyuedu.rs;

import cn.aiyuedu.bs.common.dto.ChapterIndexDto;
import cn.aiyuedu.bs.common.model.ChapterBase;
import cn.aiyuedu.bs.index.ChapterIndexDao;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@Ignore
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/WEB-INF/config/test1/spring/test.xml"})
public class ChapterIndexDaoTest {

    @Autowired
    ChapterIndexDao chapIndexDao;

    @Test
    public void testGet() {
        ChapterIndexDto obj = new ChapterIndexDto();
        obj.setChapterBase(new ChapterBase());
        obj.getChapterBase().setId(System.currentTimeMillis());
        obj.getChapterBase().setBookId(System.currentTimeMillis());
        obj.getChapterBase().setName("章节名称1");
        obj.setText("章节内容1");
//        doc.put(CHAP_KEY_ID, chap.getChapterBase().getId());
//        doc.put(CHAP_KEY_BOOK_ID, chap.getChapterBase().getBookId());
//        doc.put(CHAP_KEY_CHAP_NAME, chap.getChapterBase().getName());
//        doc.put(CHAP_KEY_CHAP_CTN, chap.getText());
//        doc.put(CHAP_KEY_EDATE, new Date());
        chapIndexDao.saveChapter(obj);
    }


}
