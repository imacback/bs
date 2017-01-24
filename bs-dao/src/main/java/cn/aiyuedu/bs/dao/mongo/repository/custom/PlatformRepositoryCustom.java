package cn.aiyuedu.bs.dao.mongo.repository.custom;

import cn.aiyuedu.bs.common.orm.Page;
import cn.aiyuedu.bs.dao.dto.PlatformQueryDto;
import cn.aiyuedu.bs.dao.entity.Platform;

import java.util.List;

/**
 * Created by Thinkpad on 2015/1/6.
 */
public interface PlatformRepositoryCustom {

    Platform persist(Platform platform);

    long count(PlatformQueryDto platformQueryDto);

    List<Platform> find(PlatformQueryDto platformQueryDto);

    Page<Platform> getPage(PlatformQueryDto platformQueryDto);

    void removeMulti(List<Integer> ids);
}
