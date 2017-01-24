package cn.aiyuedu.bs.dao.mongo.repository;

import cn.aiyuedu.bs.dao.entity.ComponentData;
import cn.aiyuedu.bs.dao.mongo.repository.custom.ComponentDataRepositoryCustom;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by Thinkpad on 2015/1/6.
 */
public interface ComponentDataRepository extends CrudRepository<ComponentData, Integer>, ComponentDataRepositoryCustom {
}
