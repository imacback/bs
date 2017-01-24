package cn.aiyuedu.bs.dao.mongo.repository;

import cn.aiyuedu.bs.dao.entity.Batch;
import cn.aiyuedu.bs.dao.mongo.repository.custom.BatchRepositoryCustom;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by Thinkpad on 2014/12/29.
 */
public interface BatchRepository extends CrudRepository<Batch, Integer>, BatchRepositoryCustom {
}
