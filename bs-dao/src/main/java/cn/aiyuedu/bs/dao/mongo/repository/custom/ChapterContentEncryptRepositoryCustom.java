package cn.aiyuedu.bs.dao.mongo.repository.custom;

import cn.aiyuedu.bs.dao.entity.ChapterContentEncrypt;

import java.util.List;

/**
 * Description:
 *
 * @author yz.wu
 */
public interface ChapterContentEncryptRepositoryCustom {

    void delete(List<Long> chapterIds);

    void deleteByBookId(Long bookId);

    List<ChapterContentEncrypt> find(List<Long> chapterIds);
}
