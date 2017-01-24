package cn.aiyuedu.bs.wap.service;

import cn.aiyuedu.bs.cache.service.BookCacheService;
import cn.aiyuedu.bs.common.model.BookBase;
import cn.aiyuedu.bs.common.orm.Page;
import cn.aiyuedu.bs.dao.dto.BookQueryDto;
import cn.aiyuedu.bs.dao.entity.Book;
import cn.aiyuedu.bs.dao.mongo.repository.BookRepository;
import cn.aiyuedu.bs.service.BookGeneralService;
import cn.aiyuedu.bs.service.ProviderGeneralService;
import cn.aiyuedu.bs.service.TagGeneralService;
import cn.aiyuedu.bs.wap.dto.BookDto;
import com.google.common.collect.Lists;
import jodd.bean.BeanCopy;
import jodd.bean.BeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Description:
 *
 * @author yz.wu
 */
@Service("bookService")
public class BookService {

    @Autowired
    private BookCacheService bookCacheService;
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private BookGeneralService bookGeneralService;
    @Autowired
    private TagGeneralService tagGeneralService;
    @Autowired
    private ProviderGeneralService providerGeneralService;

    public BookBase get(Long id) {
        return bookGeneralService.get(id);
    }

    public BookDto getBookDto(BookBase bookBase) {
        BookDto bookDto = new BookDto();
        BeanCopy.beans(bookBase, bookDto).ignoreNulls(false).copy();
        bookDto.setCategoryName(tagGeneralService.getName(bookBase.getTagStoryId()));
        bookDto.setProviderName(providerGeneralService.getName(bookBase.getProviderId()));

        return bookDto;
    }

    public List<BookBase> getBooks(List<String> ids) {
        return bookCacheService.getBooks(ids);
    }

    public List<BookBase> getListByTag(Integer tagId, Integer pageNum, Integer pageSize) {
        Page<BookBase> page = bookGeneralService.getBooksByTag(tagId, null, pageNum, pageSize);
        return page.getResult();
    }

    public List<BookBase> getListByCategory(Integer categoryId, Integer pageNum, Integer pageSize) {
        Page<BookBase> page = bookGeneralService.getBooksByCategory(categoryId, null, pageNum, pageSize);
        return page.getResult();
    }

    public List<BookBase> getListByRank(String rankId, Integer pageNum, Integer pageSize) {
        Page<BookBase> page = bookGeneralService.getRankingList(Integer.valueOf(rankId), null, pageNum, pageSize);
        return page.getResult();
    }

    public List<BookBase> getListByRecommend(Long bookId) {
        return bookGeneralService.getRecommendBooks(bookId, null);
    }

    public List<BookBase> getListByRecommend(Long bookId, int count) {
        List<BookBase> listBook = bookGeneralService.getRecommendBooks(bookId, null, count);
        return listBook;
    }
}
