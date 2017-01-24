package cn.aiyuedu.bs.dao.mongo.repository;

import cn.aiyuedu.bs.dao.mongo.repository.custom.ComponentTypeRepositoryCustom;
import cn.aiyuedu.bs.dao.entity.ComponentType;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by Thinkpad on 2015/1/5.
 */
public interface ComponentTypeRepository extends CrudRepository<ComponentType, Integer>, ComponentTypeRepositoryCustom {
}
