package cn.aiyuedu.bs.dao.mongo.repository;

import cn.aiyuedu.bs.dao.entity.Distribute;
import cn.aiyuedu.bs.dao.mongo.repository.custom.DistributeRepositoryCustom;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by Thinkpad on 2014/11/18.
 */
public interface DistributeRepository extends CrudRepository<Distribute, Integer> ,DistributeRepositoryCustom {
}
