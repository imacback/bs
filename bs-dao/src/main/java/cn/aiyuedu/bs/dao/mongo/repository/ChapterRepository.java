package cn.aiyuedu.bs.dao.mongo.repository;

import cn.aiyuedu.bs.dao.entity.Chapter;
import cn.aiyuedu.bs.dao.mongo.repository.custom.ChapterRepositoryCustom;
import org.springframework.data.repository.CrudRepository;

/**
 * Description:
 *
 * @author yz.wu
 */
public interface ChapterRepository extends CrudRepository<Chapter, Long>, ChapterRepositoryCustom {
}
