package cn.aiyuedu.bs.dao.repository;

import cn.aiyuedu.bs.dao.BaseTest;
import cn.aiyuedu.bs.dao.entity.Book;
import com.duoqu.commons.utils.DateUtils;
import org.junit.Ignore;

import java.util.Date;

/**
* Description:
*
* @author yz.wu
*/
@Ignore
public class BookDaoTest extends BaseTest {

   // @Test
    public void testAdd() {
        Book b = null;
        Date now = new Date();
        Date date = null;
        for (int i=0;i<100;i++) {
            date = DateUtils.addDay2Date(-i, now);
            b = new Book();
            b.setBatchId(1);
            b.setCheckLevel(10);
            b.setCreatorId(1);
            b.setCreateDate(date);
            b.setEditorId(1);
            b.setEditDate(date);
            b.setIsbn("ISBN");
            b.setOnlineDate(date);
            b.setTagClassifyId(103);
            b.setTagSexId(105);
            //b.setTagContentIds("109,115,165");
            //b.setTagSupplyIds("174,196,218,247");
            b.setAuthor("作者_"+i);
            //b.setCategoryIds("1");
            b.setChapters(100);
            b.setFeeChapter(10);
            //b.setFeePlatformIds("1,2");
            //b.setFeeTypeIds("1");
            b.setIsFee(1);
            b.setIsSerial(1);
            b.setLargePic("http://p0.qhimg.com/t0106c4e91d9af6b6c8.jpg");
            b.setLongRecommend("长推荐");
            b.setMemo("简介");
            b.setName("书名_"+i);
            b.setProviderId(2);
            b.setPublishChapters(50);
            b.setShortRecommend("短推荐");
            b.setSmallPic("http://p0.qhimg.com/t01d1634db32cc77f88.jpg");
            b.setStatus(1);
            b.setWholePrice(1);
            b.setWords(10000);
            b.setUpdateChapterId(0l);

            //bookDao.insert(b);
        }
    }

}
