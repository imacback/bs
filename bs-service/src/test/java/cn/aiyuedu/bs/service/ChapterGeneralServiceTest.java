package cn.aiyuedu.bs.service;

import com.duoqu.commons.redis.client.RedisClient;
import cn.aiyuedu.bs.cache.service.BookCacheService;
import cn.aiyuedu.bs.cache.service.ChapterCacheService;
import cn.aiyuedu.bs.cache.util.RedisKeyUtil;
import cn.aiyuedu.bs.common.dto.ChapterBaseDto;
import cn.aiyuedu.bs.common.dto.ResultDto;
import cn.aiyuedu.bs.common.model.BookBase;
import cn.aiyuedu.bs.common.model.ChapterBase;
import cn.aiyuedu.bs.common.orm.Page;
import cn.aiyuedu.bs.dao.dto.ChapterQueryDto;
import cn.aiyuedu.bs.dao.entity.Book;
import cn.aiyuedu.bs.dao.entity.Chapter;
import cn.aiyuedu.bs.dao.mongo.repository.BookRepository;
import cn.aiyuedu.bs.dao.mongo.repository.ChapterRepository;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Description:
 *
 * @author yz.wu
 */
//@Ignore
public class ChapterGeneralServiceTest extends BaseTest {

    @Autowired
    private ChapterGeneralService chapterGeneralService;
    @Autowired
    private BookGeneralService bookGeneralService;
    @Autowired
    private ChapterContentService chapterContentService;
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private ChapterRepository chapterRepository;
    @Autowired
    private BookCacheService bookCacheService;
    @Autowired
    private ChapterCacheService chapterCacheService;
    @Autowired
    private RedisClient redisClient;

    //@Test
    public void testSaveWithApi() {
        Chapter c = new Chapter();
        c.setCpChapterId("cp0011-0003");
        c.setBookId(105l);
        c.setName("章节3");
        c.setVolume("卷3");
        c.setStatus(0);

        String text = "没多久，盖板下的“哔啵”裂响声渐渐小了，取而代之的是阅读币,一种无数鱼儿在水里拍打发出的声音，或者说，9a色色,像是无数泥鳅装在一个小桶里相互钻挤发出的声音。虽然盖板盖着，但是从盖板的边缘还是能看到，下面的黑色蛹壳已经看不到了，变成了许多白皙如玉的小肉虫，它们刚刚从沉睡中苏醒，仿佛受到卓木强巴血液的吸引，争先恐后向池子的下层钻去，你推我搡，谁也不让谁。";

        ResultDto r = chapterGeneralService.saveWithApi(c, text);
        System.out.println(r.getSuccess()+","+r.getInfo());

    }

    @Test
    public void testGetChapterContent() {
//        Long bookId = 10l;
//        Long chapterId = 3304l;
        List<Integer> list = new ArrayList<>();
        list.add(12); list.add(13);
        List<ChapterBaseDto> date =  chapterGeneralService.getChaptersWithOrderIdList(2312L,list);
        System.out.println( date.get(0).getText());
        //System.out.println(chapterContentService.getChapterContent(bookId, chapterId, true));
    }

    //@Test
    public void testSave() {
        Long bookId = 108l;
        Book book = bookRepository.findOne(bookId);
        bookCacheService.set(book);
    }

    @Test
    public void testSaveChapters() {
        Long bookId = 9l;
        Map<String, Object> parameters = Maps.newHashMap();
        if (bookId != null) {
            parameters.put("bookId", bookId);
        }

        String key = RedisKeyUtil.getBookChapterIdsZSetKey(bookId);
        redisClient.delete(key, true);
        redisClient.delete(key, false);

        key = RedisKeyUtil.getBookChaptersHashKey(bookId);
        redisClient.delete(key, true);
        redisClient.delete(key, false);

        key = RedisKeyUtil.getBookChapterIdsByOrderIdHashKey(bookId);
        redisClient.delete(key, true);
        redisClient.delete(key, false);

        ChapterQueryDto query = new ChapterQueryDto();
        query.setBookId(bookId);
        Page<Chapter> page = chapterRepository.getPage(query);
        chapterCacheService.set(bookId, page.getResult());

        List<ChapterBase> chapterBases = chapterGeneralService.getChaptersByOrderId(bookId, 50d, 100d, true);

        for (ChapterBase c : chapterBases) {
            System.out.println(c.getName());
        }


    }

    //@Test
    public void testGetBookChapterIdsByOrderId() {
        String key = RedisKeyUtil.getBookChapterIdsByOrderIdHashKey(108l);
        List<Object> list = redisClient.getAllFromHash(key);
        if (CollectionUtils.isNotEmpty(list)) {
            for (Object o : list) {
                System.out.println(o);
            }
        }
    }

    @Test
    public void testGetChapters() {
        Long bookId = 2375l;
        Integer operatePlatformId = 1;
        BookBase bookBase = bookGeneralService.get(bookId, operatePlatformId);
        System.out.println(bookBase.getPublishChapters()+","+bookBase.getWords());
        List<Integer> orderIds = Lists.newArrayList();
        //orderIds.add(100);
//        //List<ChapterBaseDto> chapterBaseDtos = chapterGeneralService.getChaptersByOrderIds(108l, orderIds);
//        for (ChapterBaseDto c : chapterBaseDtos) {
//            System.out.println(c.getText());
//        }

        List<ChapterBase> list = null;
        //list = chapterGeneralService.getChaptersByOrderId(bookId, 0d, 20d, true);
//        List<ChapterBase> list = chapterGeneralService.getChaptersByOrderId(bookId, 1d, 10d, true);
        list = chapterGeneralService.getChapters(bookId, 0, 20, false);

        for (ChapterBase c : list) {
            System.out.println(c.getName()+"||"+c.getId()+"||"+c.getOrderId()+"||"+c.getSumWords());
        }

    }

    @Test
    public void testGetChaptersWithContent() {
        Long bookId = 10L;
        List<ChapterBaseDto> list = chapterGeneralService.getChaptersWithContentByCharIndex(bookId, 3000, 1, 3);
        ChapterBase c = null;
        for (ChapterBaseDto base : list) {
            c = base.getChapterBase();
            System.out.println(c.getName()+", orderId:"+c.getOrderId());
        }
    }
}
