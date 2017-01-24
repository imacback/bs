package cn.aiyuedu.bs.dao.mongo.repository;

import cn.aiyuedu.bs.dao.mongo.repository.custom.ChapterContentFilterRepositoryCustom;
import cn.aiyuedu.bs.dao.entity.ChapterContentFilter;
import org.springframework.data.repository.CrudRepository;

/**
 * Description:
 *
 * @author yz.wu
 */
public interface ChapterContentFilterRepository extends CrudRepository<ChapterContentFilter, Long>, ChapterContentFilterRepositoryCustom {
}
