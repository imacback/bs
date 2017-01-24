package cn.aiyuedu.bs.service;

import com.duoqu.commons.redis.client.RedisClient;
import cn.aiyuedu.bs.cache.service.BookCacheService;
import cn.aiyuedu.bs.cache.util.RedisKeyUtil;
import cn.aiyuedu.bs.common.Constants;
import cn.aiyuedu.bs.common.model.BookBase;
import cn.aiyuedu.bs.common.orm.Page;
import cn.aiyuedu.bs.dao.dto.BookQueryDto;
import cn.aiyuedu.bs.dao.entity.Book;
import cn.aiyuedu.bs.dao.mongo.repository.BookRepository;
import cn.aiyuedu.bs.index.BookIndexDao;
import com.google.common.collect.Lists;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * Description:
 *
 * @author yz.wu
 */
@Ignore
public class BookGeneralServiceTest extends BaseTest {

    @Autowired
    private BookGeneralService bookGeneralService;
    @Autowired
    private BookCacheService bookCacheService;
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private RedisClient redisClient;
    @Autowired
    private BookIndexDao bookIndexDao;

    //@Test
    public void testSaveWithApi() {
        Book b = new Book();
        b.setId(5l);
        b.setBatchId(5);
        b.setCpBookId("cp0011");
        b.setIsbn("isbn");
        b.setAuthor("作者");
        b.setIsSerial(1);
        b.setLargePic("largePic");
        b.setMemo("简介");
        b.setName("书名0011");
        b.setProviderId(3);
        b.setSmallPic("smallPic");

        boolean result = bookGeneralService.saveWithApi(b);
        System.out.println(result+","+b.getId());
    }

    //@Test
    public void testGetBookFromDbByIds(){
//        Book b = bookGeneralService.getBookFromDbById(new Long(4));
//        System.out.println("======="+b.getName());
//        Assert.assertNotNull(b);
        List<Long> ids = new ArrayList<Long>();
        ids.add(new Long(0));
        ids.add(new Long(1));
        ids.add(new Long(2));
        ids.add(new Long(2));
        ids.add(new Long(3));
        ids.add(new Long(4));
        ids.add(null);

        //List<Book> ll = bookGeneralService.getBookFromDbByIds(ids);
        //System.out.println("======="+ll.size());
        //for(Book b : ll){
            //.out.println("=============="+b.getName());
        //}
    }

    @Test
    public void testGetBook() {
        long bookId = 1516;
        Integer operatePlatformId = 1;
        //Book book = bookRepository.findOne(bookId);
        //bookCacheService.set(book);
        BookBase bookBase = bookGeneralService.get(bookId, operatePlatformId);
        if (bookBase.getStatus() == Constants.BookStatus.Online.getId()){
            System.out.println(bookBase.getName()+", words:"+bookBase.getWords()+", publishChapters:"+bookBase.getPublishChapters());

        }
        System.out.println(bookBase.getStatus()+", Constants.BookStatus.Online.getId():"+Constants.BookStatus.Online.getId()+", publishChapters:"+bookBase.getPublishChapters());

    }

    @Test
    public void testPublish() {
        Book b = bookRepository.findOne(10l);
        b.setStatus(Constants.BookStatus.Online.getId());
        //bookGeneralService.save(b);
    }

    @Test
    public void testGetCategoryPage() {
        Integer categoryId = 9;
        Integer operatePlatformId = 1;
        Page<BookBase> page = bookGeneralService.getBooksByCategory(categoryId, operatePlatformId, 1, 10);
        System.out.println(page.getTotalItems());
        if (CollectionUtils.isNotEmpty(page.getResult())) {
            for (BookBase b : page.getResult()) {
                System.out.println(b.getName()+","+b.getId());
            }
        }

    }

    @Test
    public void testRestore() {
        Long bookId = 1266l;
        bookGeneralService.restoreBook(bookId);
    }

    @Test
    public void testReloadBookChapterList() {
        bookGeneralService.reloadBookChapterList();
    }

    @Test
    public void testBatchOnline() {
        List<Long> bookIds = Lists.newArrayList(2100l);
        bookGeneralService.batchOnline(Lists.newArrayList(bookIds),null,null, true);
    }

    @Test
    public void testRestore2Cache() {
        bookGeneralService.restoreBook();
    }

    @Test
    public void testGetRecommend() {
        Long bookId = 1221l;
        Integer operatePlatformId = 1;
        List<BookBase> list = bookGeneralService.getRecommendBooks(bookId, operatePlatformId);
        if (CollectionUtils.isNotEmpty(list)) {
            for (BookBase b : list) {
                System.out.println(b.getName()+", tagStoryId:"+b.getTagStoryId()+", id:"+b.getId()+", viewCount:"+b.getViewCount());
            }
        }
    }

    @Test
    public void testGetRankingPage() {
        Page<BookBase> page = bookGeneralService.getRankingList(1, 1, 1, 10);
        if (page != null && CollectionUtils.isNotEmpty(page.getResult())) {
            for (BookBase b : page.getResult()) {
                System.out.println(b.getName()+","+b.getId());
            }
        }
    }

    @Test
    public void testReload() {
        BookQueryDto queryDto = new BookQueryDto();
        queryDto.setStatus(Constants.BookStatus.Online.getId());
        queryDto.setPublishChapters(0);
        queryDto.setCheckLevel(Constants.BookCheckLevel.Normal.getValue());
        List<Book> list = bookRepository.find(queryDto);

        for (Book b : list) {
            redisClient.addToHashWithMsgPack(RedisKeyUtil.HASH_BOOK, b.getId().toString(), b);
            if (CollectionUtils.isNotEmpty(b.getCategoryIds()) &&
                    CollectionUtils.isNotEmpty(b.getOperatePlatformIds())) {
                for (Integer id : b.getCategoryIds()) {
                    for (Integer operatePlatformId : b.getOperatePlatformIds()) {
                        redisClient.addToSortedSet(RedisKeyUtil.getBookCategoryZSetKey(id, operatePlatformId),
                                b.getId().toString(),
                                1 / Double.parseDouble(b.getCreateDate().getTime() + b.getId() + ""));
                    }
                }
            }
        }

        bookIndexDao.saveBook(list);
    }

    @Test
    public void testDeleteBookAllChapters() {
        Long bookId = 2741l;
        bookGeneralService.deleteBookAllChapters(bookId);
    }

    @Test
    public void testUpdateFeePlatformIds() {
        bookGeneralService.updateFeePlatformIds();
    }

    @Test
    public void testRestoreChapters() {
        bookGeneralService.restoreChaptersWithAllBooks();
    }
}
