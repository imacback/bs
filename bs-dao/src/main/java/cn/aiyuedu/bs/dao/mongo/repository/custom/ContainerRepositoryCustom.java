package cn.aiyuedu.bs.dao.mongo.repository.custom;

import cn.aiyuedu.bs.common.orm.Page;
import cn.aiyuedu.bs.dao.dto.ContainerQueryDto;
import cn.aiyuedu.bs.dao.entity.Container;

import java.util.List;

/**
 * Created by Thinkpad on 2015/1/6.
 */
public interface ContainerRepositoryCustom {

    Container persist(Container container);

    long count(ContainerQueryDto containerQueryDto);

    List<Container> find(ContainerQueryDto containerQueryDto);

    Page<Container> getPage(ContainerQueryDto containerQueryDto);

    void removeMulti(List<Integer> ids);
}
