package cn.aiyuedu.bs.dao.mongo.repository;

import cn.aiyuedu.bs.dao.entity.ClientTab;
import cn.aiyuedu.bs.dao.mongo.repository.custom.ClientTabRepositoryCustom;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by Thinkpad on 2015/1/5.
 */
public interface ClientTabRepository extends CrudRepository<ClientTab, Integer>, ClientTabRepositoryCustom {
}
