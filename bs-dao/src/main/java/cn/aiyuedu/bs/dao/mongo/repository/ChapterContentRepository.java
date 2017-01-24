package cn.aiyuedu.bs.dao.mongo.repository;

import cn.aiyuedu.bs.dao.mongo.repository.custom.ChapterContentRepositoryCustom;
import cn.aiyuedu.bs.dao.entity.ChapterContent;
import org.springframework.data.repository.CrudRepository;

/**
 * Description:
 *
 * @author yz.wu
 */
public interface ChapterContentRepository extends CrudRepository<ChapterContent, Long>, ChapterContentRepositoryCustom {
}
