package cn.aiyuedu.bs.service;

import cn.aiyuedu.bs.common.Constants;
import cn.aiyuedu.bs.common.Constants.BookModule;
import cn.aiyuedu.bs.common.Constants.BookStatus;
import cn.aiyuedu.bs.dao.entity.Book;
import cn.aiyuedu.bs.index.BookIndexDao;
//import ChapterIndexDao;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;

import java.util.List;

/**
 * Description:
 *
 * @author yz.wu
 */
public class SolrGeneralService {

    @Autowired
    private BookIndexDao bookIndexDao;
//    @Autowired
//    private ChapterIndexDao chapterIndexDao;

    public boolean allowStoreBook(Book book, Book old) {
        if (book != null) {
            if (book.getCheckLevel() < BookModule.Search.getBookLevel() || book.getStatus() == BookStatus.Offline.getId()) {
                bookIndexDao.delete(book.getId());
            } else if (old == null) {
                if (book.getStatus() == BookStatus.Online.getId() &&
                        book.getCheckLevel() != null &&
                        book.getCheckLevel() >= BookModule.Search.getBookLevel()) {
                    return true;
                }
            } else {
                if ((book.getStatus() == BookStatus.Online.getId() ||
                        book.getStatus() == BookStatus.Offline.getId()) &&
                        (!StringUtils.equals(book.getName(), old.getName()) ||
                                !StringUtils.equals(book.getAuthor(), old.getAuthor()) ||
                                !StringUtils.equals(book.getMemo(), old.getMemo()) ||
                                !StringUtils.equals(book.getSmallPic(), old.getSmallPic()) ||
                                book.getCheckLevel() != old.getCheckLevel() ||
                                book.getStatus() != old.getStatus() ||
                                book.getOperatePlatformIds() != old.getOperatePlatformIds())) {
                    return true;
                }
            }
        }

        return false;
    }

    //@Async
    public void saveBook(Book book) {
        if (book.getCheckLevel() != null && book.getCheckLevel() >= Constants.BookModule.Search.getBookLevel()) {
            bookIndexDao.saveBook(book);
        }
    }

    //@Async
    public void deleteBook(Long bookId) {
        bookIndexDao.delete(bookId);
//        chapterIndexDao.delByBookId(bookId);
    }

    @Async
    public void multiDeleteBook(List<Long> bookIds) {
        if (CollectionUtils.isNotEmpty(bookIds)) {
            bookIndexDao.multiDelete(bookIds);
        }
    }
}