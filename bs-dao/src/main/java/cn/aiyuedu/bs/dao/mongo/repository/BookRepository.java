package cn.aiyuedu.bs.dao.mongo.repository;

import cn.aiyuedu.bs.dao.entity.Book;
import cn.aiyuedu.bs.dao.mongo.repository.custom.BookRepositoryCustom;
import org.springframework.data.repository.CrudRepository;

/**
 * Description:
 *
 * @author yz.wu
 */
public interface BookRepository extends CrudRepository<Book, Long>, BookRepositoryCustom {
}
