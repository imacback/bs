package cn.aiyuedu.bs.dao.mongo.repository;

import cn.aiyuedu.bs.dao.entity.AdminUser;
import cn.aiyuedu.bs.dao.mongo.repository.custom.AdminUserRepositoryCustom;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by Thinkpad on 2014/12/28.
 */
public interface AdminUserRepository extends CrudRepository<AdminUser, Integer>, AdminUserRepositoryCustom {
}
