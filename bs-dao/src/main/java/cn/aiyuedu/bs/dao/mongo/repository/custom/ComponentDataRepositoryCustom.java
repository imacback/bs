package cn.aiyuedu.bs.dao.mongo.repository.custom;

import cn.aiyuedu.bs.common.orm.Page;
import cn.aiyuedu.bs.dao.dto.ComponentDataQueryDto;
import cn.aiyuedu.bs.dao.entity.ComponentData;

import java.util.List;

/**
 * Created by Thinkpad on 2015/1/6.
 */
public interface ComponentDataRepositoryCustom {

    ComponentData persist(ComponentData componentData);

    long count(ComponentDataQueryDto componentDataQueryDto);

    List<ComponentData> find(ComponentDataQueryDto componentDataQueryDto);

    Page<ComponentData> getPage(ComponentDataQueryDto componentDataQueryDto);

    void removeMulti(List<Integer> ids);
}
