package cn.aiyuedu.bs.dao.mongo.repository.custom;

import cn.aiyuedu.bs.common.orm.Page;
import cn.aiyuedu.bs.dao.dto.BookQueryDto;
import cn.aiyuedu.bs.dao.entity.Book;

import java.util.List;

/**
 * Description:
 *
 * @author yz.wu
 */
public interface BookRepositoryCustom {

    Book persist(Book book);

    List<Book> persist(List<Book> books);

    boolean exist(Long id, String name, String author, Integer status);

    Book findOne(String cpBookId, Integer providerId);

    long count(BookQueryDto bookQueryDto);

    List<Book> find(BookQueryDto bookQueryDto);

    Page<Book> getPage(BookQueryDto bookQueryDto);

    Book addChapters(Long id, int chapters);

    void removeMulti(List<Long> ids);

    void updateStatus(List<Long> ids, Integer status, Integer adminUserId, boolean updateOnlineDate);

    void updateChapters(Long id, Integer chapters);

    void updateChapters(Long id, Integer chapters, Integer publishChapters);

    void updatePublishChapters(Long id, Integer chapters);

    void updatePublishChapterDate(Long id);

    void updateViewCount(Long id, Integer viewCount);

    void updateWords(Long id, Integer words);

    Book updateCover(Long id, String smallPic, String largePic);

    void updateMemo(Long id, String originMemo, String memo);

    void updateTags(BookQueryDto queryDto);

    /**
     * 分页取排行榜
     * @param rankingId
     * @param start
     * @param limit
     * @return
     */
    Page<Book> getRankingPage(Integer rankingId, Integer operatePlatformId, Integer start, Integer limit);

    /**
     * 分页取分类书籍
     * @param categoryId
     * @param start
     * @param limit
     * @return
     */
    Page<Book> getPageByCategory(Integer categoryId, Integer operatePlatformId, Integer start, Integer limit);

    /**
     * 分页取相同标签书籍
     * @param bookId 忽略书籍
     * @param tagContentId
     * @param start
     * @param limit
     * @return
     */
    Page<Book> getPageByTagContentId(Long bookId, Integer tagContentId, Integer operatePlatformId, Integer start, Integer limit);
}
