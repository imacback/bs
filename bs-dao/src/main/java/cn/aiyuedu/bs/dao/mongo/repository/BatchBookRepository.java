package cn.aiyuedu.bs.dao.mongo.repository;

import cn.aiyuedu.bs.dao.entity.BatchBook;
import cn.aiyuedu.bs.dao.mongo.repository.custom.BatchBookRepositoryCustom;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by Thinkpad on 2015/1/1.
 */
public interface BatchBookRepository extends CrudRepository<BatchBook, Long>, BatchBookRepositoryCustom {
}
