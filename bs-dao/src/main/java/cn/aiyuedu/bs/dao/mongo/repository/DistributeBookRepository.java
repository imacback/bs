package cn.aiyuedu.bs.dao.mongo.repository;

import cn.aiyuedu.bs.dao.entity.DistributeBook;
import cn.aiyuedu.bs.dao.mongo.repository.custom.DistributeBookRepositoryCustom;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by Thinkpad on 2014/11/18. Repository
 */
public interface DistributeBookRepository extends CrudRepository<DistributeBook, Integer>,DistributeBookRepositoryCustom {
}
