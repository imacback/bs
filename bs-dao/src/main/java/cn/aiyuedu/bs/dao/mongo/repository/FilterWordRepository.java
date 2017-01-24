package cn.aiyuedu.bs.dao.mongo.repository;

import cn.aiyuedu.bs.dao.entity.FilterWord;
import cn.aiyuedu.bs.dao.mongo.repository.custom.FilterWordRepositoryCustom;
import org.springframework.data.repository.CrudRepository;

/**
 * Description:
 *
 * @author yz.wu
 */
public interface FilterWordRepository extends CrudRepository<FilterWord, Integer>, FilterWordRepositoryCustom {
}
