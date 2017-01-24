package cn.aiyuedu.bs.dao.mongo.repository.custom;

import cn.aiyuedu.bs.common.orm.Page;
import cn.aiyuedu.bs.dao.dto.ChapterQueryDto;
import cn.aiyuedu.bs.dao.entity.Chapter;

import java.util.List;

/**
 * Description:
 *
 * @author yz.wu
 */
public interface ChapterRepositoryCustom {

    Chapter persist(Chapter chapter);

    List<Chapter> persist(List<Chapter> chapters);

    boolean contain(Long id, String name);

    boolean exist(Long bookId, String cpChapterId);

    boolean exist(Long bookId, Long chapterId, Integer orderId);

    Chapter findOne(Long bookId, String cpChapterId);

    Chapter findOne(Long bookId, Integer charIndex);

    Chapter findOne(Long bookId, Integer status, Integer withoutStatus, Integer isMaxOrder);

    long count(ChapterQueryDto chapterQueryDto);

    List<Chapter> find(ChapterQueryDto chapterQueryDto);

    Page<Chapter> getPage(ChapterQueryDto chapterQueryDto);

    void updateStatus(List<Long> chapterIds, Integer status, Integer adminUserId);

    void updateSumWord(Long id, Integer sumWords);

    void updateOrderId(Long id, Integer orderId);

    void updateOrderId(Long id, Integer orderId, Integer sumWords);

    void updatePrice(Long id, Integer fee, Integer price, Integer adminUserId);

    void updateFilterWords(Long id, String filterWords, Integer words);

    void updateName(Long id, String originName, String name);

    void delete(List<Long> ids);

    void removeMultiByBookId(Long bookId);

    void removeMultiByBookId(List<Long> bookIds);
}
