package cn.aiyuedu.bs.dao.mongo.repository;

import cn.aiyuedu.bs.dao.mongo.repository.custom.ComponentRepositoryCustom;
import cn.aiyuedu.bs.dao.entity.Component;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by Thinkpad on 2015/1/6.
 */
public interface ComponentRepository extends CrudRepository<Component, Integer>, ComponentRepositoryCustom {
}
