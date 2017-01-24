package cn.aiyuedu.bs.dao.mongo.repository;

import cn.aiyuedu.bs.dao.entity.User;
import cn.aiyuedu.bs.dao.mongo.repository.custom.UserRepositoryCustom;
import org.springframework.data.repository.CrudRepository;


public interface UserRepository extends CrudRepository<User, Integer>,UserRepositoryCustom {
}
