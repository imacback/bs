package cn.aiyuedu.bs.dao.mongo.repository.custom;

import cn.aiyuedu.bs.common.orm.Page;
import cn.aiyuedu.bs.dao.dto.ComponentQueryDto;
import cn.aiyuedu.bs.dao.entity.Component;

import java.util.List;

/**
 * Created by Thinkpad on 2015/1/6.
 */
public interface ComponentRepositoryCustom {

    Component persist(Component component);

    long count(ComponentQueryDto componentQueryDto);

    List<Component> find(ComponentQueryDto componentQueryDto);

    Page<Component> getPage(ComponentQueryDto componentQueryDto);

    void removeMulti(List<Integer> ids);
}
