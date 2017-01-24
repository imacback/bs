package cn.aiyuedu.bs.dao.mongo.repository;

import cn.aiyuedu.bs.common.Constants;
import cn.aiyuedu.bs.common.orm.Page;
import cn.aiyuedu.bs.dao.BaseTest;
import cn.aiyuedu.bs.dao.dto.ChapterDto;
import cn.aiyuedu.bs.dao.dto.ChapterQueryDto;
import cn.aiyuedu.bs.dao.entity.Book;
import cn.aiyuedu.bs.dao.entity.Chapter;
import cn.aiyuedu.bs.dao.mongo.SequenceDao;
import cn.aiyuedu.bs.dao.dto.BookQueryDto;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.beans.BeanCopier;

import java.util.List;
import java.util.Map;

/**
 * Description:
 *
 * @author yz.wu
 */
@Ignore
public class ChapterRepositoryTest extends BaseTest {

    @Autowired
    private ChapterRepository chapterRepository;
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private SequenceDao sequenceDao;

    private BeanCopier copier = BeanCopier.create(Chapter.class, ChapterDto.class, false);

    //@Test
    public void testSave() {
        Long chapterId = 137l;
        Long bookId = 108l;
        Map<String, Object> parameters = Maps.newHashMap();
        //parameters.put("id", chapterId);
        parameters.put("bookId", bookId);
        parameters.put("isMaxOrder", 1);
        Chapter c = null;

        chapterRepository.persist(c);

        Chapter chapter = chapterRepository.findOne(1l);
        ChapterDto cd = new ChapterDto();
        copier.copy(chapter, cd, null);

        System.out.println(cd.getId());
    }

    @Test
    public void testGet() {
        Long chapterId = 1l;
        Chapter chapter = chapterRepository.findOne(906151l);
        System.out.println(chapter.getOriginName());
        //chapter.setIsFee(0);
        //chapterRepository.save(chapter);
    }

    @Test
    public void testFind() {
        ChapterQueryDto query = new ChapterQueryDto();
        query.setBookId(2089l);
        query.setIsDesc(0);
        Page<Chapter> page = chapterRepository.getPage(query);
        if (page != null && CollectionUtils.isNotEmpty(page.getResult())) {
            for (Chapter c : page.getResult()) {
                System.out.println(c.getName()+", filteredWords:"+c.getFilteredWords()+", orderId:"+c.getOrderId() + ", isFee:"+c.getIsFee()+", price:"+c.getPrice());
            }
        }
    }

    @Test
    public void testUpdate() {
        BookQueryDto bookQueryDto = new BookQueryDto();
        List<Book> books = bookRepository.find(bookQueryDto);

        ChapterQueryDto query = null;
        for (Book b : books) {
            query = new ChapterQueryDto();
            query.setBookId(b.getId());
            query.setIsDesc(0);
            Page<Chapter> page = chapterRepository.getPage(query);
            if (page != null && CollectionUtils.isNotEmpty(page.getResult())) {
                List<Long> chapterIds = Lists.newArrayList();
                int words = 0;
                int orderId = 1;
                for (Chapter c : page.getResult()) {
                    c.setOrderId(orderId++);
                    words = words + c.getFilteredWords();
                    c.setSumWords(words);
                    chapterRepository.persist(c);
                    chapterIds.add(c.getId());
                }

                if (CollectionUtils.isNotEmpty(chapterIds)) {
                    b.setChapters(chapterIds.size());
                    b.setWords(words);
                    bookRepository.persist(b);
                }
            }
        }
    }

    @Test
    public void testFindOneByCharIndex() {
        Long bookId = 10l;
        Integer charIndex = 3000;
        Chapter c = chapterRepository.findOne(bookId, charIndex);
        if (c != null) {
            System.out.println(c.getOrderId());
        } else {
            System.out.println("null");
        }
    }

    @Test
    public void testIsExist() {
        Long bookId = 20l;
        Long chapterId = 6757l;

        Chapter c = chapterRepository.findOne(chapterId);

        //System.out.println(chapterRepository.exist(chapterId, bookId, null, c.getName()));
    }

    @Test
    public void testUpdateOrderId() {
        BookQueryDto bookQueryDto = new BookQueryDto();
        List<Book> list = bookRepository.find(bookQueryDto);
        ChapterQueryDto chapterQueryDto;
        List<Chapter> chapters;
        Chapter c;
        for (Book b : list) {
            chapterQueryDto = new ChapterQueryDto();
            chapterQueryDto.setBookId(b.getId());
            chapterQueryDto.setIsDesc(0);
            chapterQueryDto.setOrderId(2);
            chapters = chapterRepository.find(chapterQueryDto);
            if (CollectionUtils.isNotEmpty(chapters) && chapters.size() > 1) {
                c = chapters.get(0);
                chapterRepository.updateOrderId(c.getId(), 1);
                c = chapters.get(1);
                chapterRepository.updateOrderId(c.getId(), 2);
            }
        }
    }

    @Test
    public void testUpdateStatus() {
        Long chapterId = 687942l;
        chapterRepository.updateStatus(Lists.newArrayList(chapterId), Constants.ChapterStatus.Online.getId(), 1);
        Chapter c = chapterRepository.findOne(chapterId);
        System.out.println(c.getPublishDate());
    }

    public static void main(String[] args) {
        List<Long> list = Lists.newArrayList();
        list.add(1l);
        list.add(2l);
        list.add(3l);

        list.add(2, 6l);

        list.remove(2);

        for (Long i : list) {
            System.out.println(i);
        }
    }
}
