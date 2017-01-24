package cn.aiyuedu.bs.dao.mongo.repository.custom;

import cn.aiyuedu.bs.common.orm.Page;
import cn.aiyuedu.bs.dao.entity.ComponentDataGroup;
import cn.aiyuedu.bs.dao.dto.ComponentDataGroupQueryDto;

import java.util.List;

/**
 * Created by Thinkpad on 2015/1/6.
 */
public interface ComponentDataGroupRepositoryCustom {

    ComponentDataGroup persist(ComponentDataGroup componentDataGroup);

    long count(ComponentDataGroupQueryDto componentDataGroupQueryDto);

    List<ComponentDataGroup> find(ComponentDataGroupQueryDto componentDataGroupQueryDto);

    Page<ComponentDataGroup> getPage(ComponentDataGroupQueryDto componentDataGroupQueryDto);

    void removeMulti(List<Integer> ids);
}
