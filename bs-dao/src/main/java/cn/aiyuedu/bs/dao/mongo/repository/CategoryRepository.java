package cn.aiyuedu.bs.dao.mongo.repository;

import cn.aiyuedu.bs.dao.entity.Category;
import cn.aiyuedu.bs.dao.mongo.repository.custom.CategoryRepositoryCustom;
import org.springframework.data.repository.CrudRepository;

/**
 * Description:
 *
 * @author yz.wu
 */
public interface CategoryRepository extends CrudRepository<Category, Integer>, CategoryRepositoryCustom {
}
