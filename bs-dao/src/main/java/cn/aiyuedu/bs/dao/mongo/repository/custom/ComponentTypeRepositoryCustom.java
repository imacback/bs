package cn.aiyuedu.bs.dao.mongo.repository.custom;

import cn.aiyuedu.bs.common.orm.Page;
import cn.aiyuedu.bs.dao.dto.ComponentTypeQueryDto;
import cn.aiyuedu.bs.dao.entity.ComponentType;

import java.util.List;

/**
 * Created by Thinkpad on 2015/1/5.
 */
public interface ComponentTypeRepositoryCustom {

    ComponentType persist(ComponentType componentType);

    long count(ComponentTypeQueryDto componentTypeQueryDto);

    List<ComponentType> find(ComponentTypeQueryDto componentTypeQueryDto);

    Page<ComponentType> getPage(ComponentTypeQueryDto componentTypeQueryDto);

    void removeMulti(List<Integer> ids);
}
