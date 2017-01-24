package cn.aiyuedu.bs.dao.mongo.repository;

import cn.aiyuedu.bs.dao.entity.Container;
import cn.aiyuedu.bs.dao.mongo.repository.custom.ContainerRepositoryCustom;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by Thinkpad on 2015/1/6.
 */
public interface ContainerRepository extends CrudRepository<Container, Integer>, ContainerRepositoryCustom {
}
