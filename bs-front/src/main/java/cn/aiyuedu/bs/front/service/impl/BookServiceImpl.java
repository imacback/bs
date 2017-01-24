package cn.aiyuedu.bs.front.service.impl;

import cn.aiyuedu.bs.cache.service.BookCacheService;
import cn.aiyuedu.bs.common.Constants;
import cn.aiyuedu.bs.common.model.BookBase;
import cn.aiyuedu.bs.common.orm.Page;
import cn.aiyuedu.bs.service.*;
import cn.aiyuedu.bs.front.service.BookService;
import cn.aiyuedu.bs.front.vo.BookVo;
import cn.aiyuedu.bs.index.base.IndexDao;
import org.apache.commons.lang3.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Description:
 *
 * @author Scott
 */
@Service("bookService")
public class BookServiceImpl extends BookService {
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    @Autowired
    TagGeneralService tagGeneralService;
    @Autowired
    CommentGeneralService commentGeneralService;
    @Autowired
    BookCacheService bookCacheService;
    @Autowired
    IndexDao bookSolrImpl;
    @Autowired
    BookGeneralService bookGeneralService;
    @Autowired
    ProviderGeneralService providerGeneralService;
    @Autowired
    UserRecommendGeneralService userRecommendGeneralService;
    @Autowired
    PayService payService;

    @Override
    public List<BookVo> listByType(Integer typeId, Integer platform, Integer pageNum, Integer pageSize) {
        List<BookVo> out = new ArrayList<BookVo>();
        Page<BookBase> page = bookGeneralService.getBooksByCategory(typeId, platform, pageNum, pageSize);
//        int start = (pageNum - 1) * pageSize;
//        int end = start + pageSize - 1;
//        List<BookBase> list = bookCacheService.getBooks(typeId, orderType, start, end);
        if (CollectionUtils.isEmpty(page.getResult()))
            return out;
        for (BookBase bookBase : page.getResult()) {
            BookVo book = makeBookVo(bookBase);
            out.add(book);
        }
        return out;
    }

    @Override
    public BookVo get(Long bookId, Integer platform) {
        BookBase bookBase = bookGeneralService.get(bookId, platform);
        if (Constants.BookStatus.Online.getId() != bookBase.getStatus()) {
            bookBase = null;
        }
        return makeBookFullVo(bookBase, platform);
    }

    public static void main(String[] args) {

        for (; ; ) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(RandomUtils.nextInt(1, 3));
        }
    }

    @Override
    public List<BookVo> rcmdList(Integer platform) {
        List<BookVo> out = new ArrayList<>();
        List<Long> _list = userRecommendGeneralService.get(platform);
        if (CollectionUtils.isEmpty(_list))
            return out;
        List<Long> list = new ArrayList<>(_list);
        for (; ; ) {
            if (CollectionUtils.isEmpty(list) || out.size() >= 4)
                break;
            int random = RandomUtils.nextInt(0, list.size());
            BookBase bookBase = bookGeneralService.get(list.remove(random), platform);
            if (bookBase != null)
                out.add(makeBookVo(bookBase));
        }
        return out;
    }

    @Override
    public List<BookVo> rcmdList(Long bookId, Integer platform) {
        List<BookBase> list = bookGeneralService.getRecommendBooks(bookId, platform);
        List<BookVo> out = new ArrayList<>();
        if (CollectionUtils.isEmpty(list)) {
            return out;
        }
        for (BookBase bookBase : list) {
            out.add(makeBookVo(bookBase));
        }
        return out;
    }

    public BookVo makeBookVo(BookBase book) {
        BookVo vo = new BookVo();
        BeanUtils.copyProperties(book, vo);
        vo.setTinyMemo(book.getMemo().length() <= 150 ? book.getMemo() : book.getMemo().substring(0, 150) + "...");
        return vo;
    }

    public BookVo makeBookFullVo(BookBase book, Integer platform) {
        BookVo vo = makeBookVo(book);
        vo.setCategoryName(tagGeneralService.getName(book.getTagStoryId()));
        vo.setProviderName(providerGeneralService.getName(book.getProviderId()));
        vo.setComments(cmtList(book.getId()));
        int isFee = payService.platformFree(platform, book) || book.getIsFee() == 0 ? 0 : 1;
        vo.setIsFee(isFee);
        return vo;
    }


    @Override
    public List<? extends CommentBaseDto> cmtList(Long bookId) {
        List<CommentBaseDto> list = commentGeneralService.searchNewestComment(bookId, 3);
        return list;
    }

    @Override
    public List<BookVo> listByTag(Integer tagId, Integer platform, Integer pageNum, Integer pageSize) {
        Page<BookBase> page = bookGeneralService.getBooksByTag(tagId, platform, pageNum, pageSize);
        List<BookVo> out = new ArrayList<>();
        if (CollectionUtils.isEmpty(page.getResult())) {
            return out;
        }
        for (BookBase bookBase : page.getResult()) {
            out.add(makeBookVo(bookBase));
        }
        return out;
    }

    @Override
    public List<BookVo> listByRank(String rankId, Integer platform, Integer pageNum, Integer pageSize) {
        Page<BookBase> page = bookGeneralService.getRankingList(Integer.valueOf(rankId), platform, pageNum, pageSize);
        List<BookVo> out = new ArrayList<>();
        if (CollectionUtils.isEmpty(page.getResult())) {
            return out;
        }
        for (BookBase bookBase : page.getResult()) {
            out.add(makeBookVo(bookBase));
        }
        return out;
    }

    @Override
    public List<BookVo> sortList(String kw, Integer platform, Integer p, int i, Object o) {
        String query = buildQuery(kw, platform);
        log.info("solrQuery = " + query);
        List<? extends Map> list = bookSolrImpl.sortList(query, p, 5, null);
        List<BookVo> out = new ArrayList<>();
        for (Map map : list) {
            BookVo vo = _makeBookVo(map);
            out.add(vo);
        }
        return out;
    }

//    @Override
//    public List<BookVo> search(String kw, Integer p, int i) {
//        List<BookVo> list = new
//        String query = "*:*";
//        if (StringUtils.hasText(kw)) {
//            query = "bookName:" + kw;
//
//        }
//        List<? extends Map> list = bookSolrImpl.sortList(query, p, 5, null);
//    }

}
