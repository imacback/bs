package cn.aiyuedu.bs.dao.mongo.repository;

import cn.aiyuedu.bs.dao.mongo.repository.custom.SiteRepositoryCustom;
import cn.aiyuedu.bs.dao.entity.Site;
import org.springframework.data.repository.CrudRepository;

/**
 * Description:
 *
 * @author yz.wu
 */
public interface SiteRepository extends CrudRepository<Site, Integer>, SiteRepositoryCustom {
}
