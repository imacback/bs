package cn.aiyuedu.bs.dao.mongo.repository.impl;

import cn.aiyuedu.bs.common.Constants;
import cn.aiyuedu.bs.common.orm.Page;
import cn.aiyuedu.bs.dao.dto.ProviderQueryDto;
import cn.aiyuedu.bs.dao.entity.Provider;
import cn.aiyuedu.bs.dao.mongo.SequenceDao;
import cn.aiyuedu.bs.dao.entity.Site;
import cn.aiyuedu.bs.dao.mongo.repository.custom.SiteRepositoryCustom;
import cn.aiyuedu.bs.dao.util.QueryLikeUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

/**
 * Description:
 *
 * @author yz.wu
 */
public class SiteRepositoryImpl implements SiteRepositoryCustom {

    @Autowired
    private MongoOperations mongoOperations;
    @Autowired
    private SequenceDao sequenceDao;

    @Override
    public Site persist(Site site) {
        if (site != null) {
            if (site.getId() == null) {
                site.setId(sequenceDao.getSequence(
                        mongoOperations.getCollectionName(Site.class)));
                mongoOperations.insert(site);
            } else {
                mongoOperations.save(site);
            }
        }

        return site;
    }

    @Override
    public Page<Site> getPage(Integer start, Integer limit) {
        Query query = new Query();

        //总数
        long count = 0l;
        if (start != null) {
            count = mongoOperations.count(query, Provider.class);
            //分页
            query.skip(start).limit(limit);
        }

        //默认升序
        Sort.Direction direction = Sort.Direction.ASC;

        //排序
        query.with(new Sort(direction, Constants.MONGODB_ID_KEY));

        return new Page<>(mongoOperations.find(query, Site.class), count);
    }
}
