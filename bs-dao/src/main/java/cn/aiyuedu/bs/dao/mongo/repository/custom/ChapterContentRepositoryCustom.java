package cn.aiyuedu.bs.dao.mongo.repository.custom;

import cn.aiyuedu.bs.common.orm.Page;
import cn.aiyuedu.bs.dao.entity.ChapterContent;

import java.util.List;

/**
 * Description:
 *
 * @author yz.wu
 */
public interface ChapterContentRepositoryCustom {

    void delete(List<Long> chapterIds);

    void deleteByBookId(Long bookId);

    List<ChapterContent> find(List<Long> chapterIds);

    Page<ChapterContent> getPage(Integer start, Integer limit);
}
