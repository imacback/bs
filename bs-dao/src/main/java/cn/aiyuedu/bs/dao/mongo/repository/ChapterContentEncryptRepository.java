package cn.aiyuedu.bs.dao.mongo.repository;

import cn.aiyuedu.bs.dao.entity.ChapterContentEncrypt;
import cn.aiyuedu.bs.dao.mongo.repository.custom.ChapterContentEncryptRepositoryCustom;
import org.springframework.data.repository.CrudRepository;

/**
 * Description:
 *
 * @author yz.wu
 */
public interface ChapterContentEncryptRepository extends CrudRepository<ChapterContentEncrypt, Long>, ChapterContentEncryptRepositoryCustom {
}
