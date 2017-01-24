package cn.aiyuedu.bs.dao.mongo.repository.custom;

import cn.aiyuedu.bs.common.orm.Page;
import cn.aiyuedu.bs.dao.entity.Site;

/**
 * Description:
 *
 * @author yz.wu
 */
public interface SiteRepositoryCustom {

    Site persist(Site site);

    Page<Site> getPage(Integer start, Integer limit);
}
