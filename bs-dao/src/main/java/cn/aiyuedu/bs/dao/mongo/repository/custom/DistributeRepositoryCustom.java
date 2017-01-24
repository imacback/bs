package cn.aiyuedu.bs.dao.mongo.repository.custom;

import cn.aiyuedu.bs.common.orm.Page;
import cn.aiyuedu.bs.dao.dto.DistributeDto;
import cn.aiyuedu.bs.dao.entity.Distribute;

import java.util.List;

/**
 * Created by Thinkpad on 2014/11/18.
 */
public interface DistributeRepositoryCustom {

    Distribute persist(Distribute distribute);

    List<Distribute> findAll(Integer status,Integer isCategory);

    Distribute getDistrbuteOne(Integer id);

    Page<Distribute> getPage(DistributeDto map);

    void update(Distribute distribute);


}
