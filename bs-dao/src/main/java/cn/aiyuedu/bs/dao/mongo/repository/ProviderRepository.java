package cn.aiyuedu.bs.dao.mongo.repository;

import cn.aiyuedu.bs.dao.entity.Provider;
import cn.aiyuedu.bs.dao.mongo.repository.custom.ProviderRepositoryCustom;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by Thinkpad on 2015/1/6.
 */
public interface ProviderRepository extends CrudRepository<Provider, Integer>, ProviderRepositoryCustom {
}
