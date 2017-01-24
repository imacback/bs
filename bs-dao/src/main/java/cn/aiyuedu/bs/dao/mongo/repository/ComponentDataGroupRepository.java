package cn.aiyuedu.bs.dao.mongo.repository;

import cn.aiyuedu.bs.dao.entity.ComponentDataGroup;
import cn.aiyuedu.bs.dao.mongo.repository.custom.ComponentDataGroupRepositoryCustom;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by Thinkpad on 2015/1/6.
 */
public interface ComponentDataGroupRepository extends CrudRepository<ComponentDataGroup, Integer>, ComponentDataGroupRepositoryCustom {
}
