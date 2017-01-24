package cn.aiyuedu.bs.front.service;


import cn.aiyuedu.bs.common.Constants;
import cn.aiyuedu.bs.common.model.BookBase;
import cn.aiyuedu.bs.common.model.CommentBase;
import cn.aiyuedu.bs.front.vo.BookVo;
import cn.aiyuedu.bs.index.BookIndexDao;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * Description:
 *
 * @author Scott
 */
public abstract class BookService {
    abstract public List<BookVo> listByType(Integer typeId,Integer platform, Integer pageNum, Integer pageSize);

    //TO DO ___________________________________________________书籍查询 @param 书籍ID     @return 书籍对象
    abstract public BookVo get(Long bookId,Integer platform);

    abstract public List<BookVo> rcmdList(Integer platform);

    abstract public List<BookVo> rcmdList(Long bookId,Integer platform);

    abstract public BookVo makeBookVo(BookBase book);

    //TO DO ______________________________________________标签查询 @param 书籍ID     @return 该书标签列表
//    public abstract List<Tag> getTags(Long bookId);

    //TO DO ______________________________________________评论查询 @param 书籍ID     @return 该书评论列表
//    public abstract List<CommentVo> cmtList(Long bookId);
    public abstract List<? extends CommentBase> cmtList(Long bookId);

    public abstract List<BookVo> listByTag(Integer tagId,Integer platform, Integer pageNum, Integer pageSize);

    public abstract List<BookVo> listByRank(String rankId,Integer platform, Integer pageNum, Integer pageSize);

    public abstract List<BookVo> sortList(String kw, Integer platform, Integer p, int i, Object o);

    protected String buildQuery(String kw,Integer platform) {
        String query = "(*:*)";
        if (StringUtils.hasText(kw)) {
            query = "(bookName:(" + kw + ")^10" +
                    " OR author:(" + kw + ")^5" +
                    " OR memo:(" + kw + "))";
        }
        query = query + " AND chkLevel:[ " + Constants.BookModule.Search.getBookLevel() + " TO *]";
        query = query + " AND pubChaps:[ 1 TO *]";
        query = query + " AND platform:"+platform;
        return query;
    }

    protected BookVo _makeBookVo(Map map) {
        BookVo book = new BookVo();
        book.setId(Long.valueOf(map.get(BookIndexDao.BOOK_KEY_ID).toString()));
        book.setName((String) map.get(BookIndexDao.BOOK_KEY_NAME));
        book.setAuthor((String) map.get(BookIndexDao.BOOK_KEY_AUTHOR));
        book.setSmallPic((String) map.get(BookIndexDao.BOOK_KEY_SMALLPIC));
        book.setTinyMemo((String) map.get(BookIndexDao.BOOK_KEY_MEMO));
        book.setTinyMemo(book.getTinyMemo().length() <= 150 ? book.getTinyMemo() : book.getTinyMemo().substring(0, 150) + "...");
        return book;
    }
}
