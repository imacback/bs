package cn.aiyuedu.bs.dao.mongo.repository;

import cn.aiyuedu.bs.dao.entity.Role;
import cn.aiyuedu.bs.dao.mongo.repository.custom.RoleRepositoryCustom;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by Thinkpad on 2014/12/26.
 */
public interface RoleRepository extends CrudRepository<Role, Integer>, RoleRepositoryCustom {
}
