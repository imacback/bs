package cn.aiyuedu.bs.dao.mongo.repository;

import cn.aiyuedu.bs.dao.entity.Platform;
import cn.aiyuedu.bs.dao.mongo.repository.custom.PlatformRepositoryCustom;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by Thinkpad on 2015/1/6.
 */
public interface PlatformRepository extends CrudRepository<Platform, Integer>, PlatformRepositoryCustom {
}
