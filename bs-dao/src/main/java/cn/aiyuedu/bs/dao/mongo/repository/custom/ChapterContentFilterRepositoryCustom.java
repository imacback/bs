package cn.aiyuedu.bs.dao.mongo.repository.custom;

import cn.aiyuedu.bs.common.orm.Page;
import cn.aiyuedu.bs.dao.entity.ChapterContentFilter;

import java.util.List;

/**
 * Description:
 *
 * @author yz.wu
 */
public interface ChapterContentFilterRepositoryCustom {

    void delete(List<Long> chapterIds);

    void deleteByBookId(Long bookId);

    List<ChapterContentFilter> find(List<Long> chapterIds);

    Page<ChapterContentFilter> getPage(Integer start, Integer limit);
}
