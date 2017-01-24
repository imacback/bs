package cn.aiyuedu.bs.dao.mongo.repository;

import cn.aiyuedu.bs.dao.entity.ClientShelf;
import cn.aiyuedu.bs.dao.mongo.repository.custom.ClientShelfRepositoryCustom;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by Thinkpad on 2015/1/4.
 */
public interface ClientShelfRepository extends CrudRepository<ClientShelf, Integer>, ClientShelfRepositoryCustom {
}
