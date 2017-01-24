package cn.aiyuedu.bs.index;

import cn.aiyuedu.bs.common.model.BookBase;
import cn.aiyuedu.bs.index.base.IndexDao;

import java.util.*;

/**
 * 书籍索引接口
 */
public class BookIndexDao {

    public static final String BOOK_KEY_ID = "id";
    public static final String BOOK_KEY_NAME = "bookName";
    public static final String BOOK_KEY_AUTHOR = "author";
    public static final String BOOK_KEY_MEMO = "memo";
    public static final String BOOK_KEY_CDATE = "createDate";
    public static final String BOOK_KEY_EDATE = "editeDate";
    public static final String BOOK_KEY_SMALLPIC = "smallPic";
    public static final String BOOK_KEY_TYPE1 = "type1";
    public static final String BOOK_KEY_TYPE2 = "type2";
    public static final String BOOK_KEY_TYPE3 = "type3";
    public static final String BOOK_KEY_TYPE4 = "type4";
    public static final String BOOK_KEY_CATEGORY = "category";
    public static final String BOOK_KEY_CHKLEVEL = "chkLevel";
    public static final String BOOK_KEY_PUBCHAPS = "pubChaps";
    public static final String BOOK_KEY_PLATFORM= "platform";

    IndexDao bookSolrImpl;

    public IndexDao getBookSolrImpl() {
        return bookSolrImpl;
    }

    public void setBookSolrImpl(IndexDao bookSolrImpl) {
        this.bookSolrImpl = bookSolrImpl;
    }

    public  void saveBook(BookBase book) {
        bookSolrImpl.addIndex(Collections.singletonList(buildBookDoc(book)));
    }

    public void saveBook(List<? extends BookBase> list) {
        List<Map<String, Object>> mapList = new ArrayList<>();
        for (BookBase book : list) {
            mapList.add(buildBookDoc(book));
        }
        bookSolrImpl.addIndex(mapList);
    }

    public void delete(Long bookId) {
        String query = BOOK_KEY_ID+":"+bookId;
        bookSolrImpl.delete(query);
    }
    public void multiDelete(List<Long> ids) {
        for (Long id:ids){
            delete(id);
        }
    }

    private Map<String, Object> buildBookDoc(BookBase book) {
        Map<String, Object> doc = new HashMap<>();
        doc.put(BOOK_KEY_ID, book.getId());
        doc.put(BOOK_KEY_NAME, book.getName());
        doc.put(BOOK_KEY_AUTHOR, book.getAuthor());
        doc.put(BOOK_KEY_MEMO, book.getMemo());
        doc.put(BOOK_KEY_CDATE, book.getCreateDate());
        doc.put(BOOK_KEY_SMALLPIC, book.getSmallPic());
        doc.put(BOOK_KEY_EDATE, new Date());
        doc.put(BOOK_KEY_TYPE1,book.getTagClassifyId());
        doc.put(BOOK_KEY_TYPE2,book.getTagSexId());
        doc.put(BOOK_KEY_TYPE3,book.getTagContentIds());
        doc.put(BOOK_KEY_TYPE4,book.getTagSupplyIds());
        doc.put(BOOK_KEY_CATEGORY,book.getCategoryIds());
        doc.put(BOOK_KEY_CHKLEVEL,book.getCheckLevel());
        doc.put(BOOK_KEY_PUBCHAPS,book.getPublishChapters());
        //1.0.3 added
        doc.put(BOOK_KEY_PLATFORM, book.getOperatePlatformIds());
        return doc;
    }

}
