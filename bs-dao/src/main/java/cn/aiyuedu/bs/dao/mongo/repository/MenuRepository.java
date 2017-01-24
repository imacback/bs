package cn.aiyuedu.bs.dao.mongo.repository;

import cn.aiyuedu.bs.dao.mongo.repository.custom.MenuRepositoryCustom;
import cn.aiyuedu.bs.dao.entity.Menu;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by Thinkpad on 2014/12/29.
 */
public interface MenuRepository extends CrudRepository<Menu, Integer>, MenuRepositoryCustom {
}
