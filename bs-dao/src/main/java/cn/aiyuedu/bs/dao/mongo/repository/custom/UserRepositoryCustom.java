package cn.aiyuedu.bs.dao.mongo.repository.custom;

import cn.aiyuedu.bs.common.orm.Page;
import cn.aiyuedu.bs.dao.dto.UserQueryDto;
import cn.aiyuedu.bs.dao.dto.UserQueryDto;
import cn.aiyuedu.bs.dao.entity.User;
import cn.aiyuedu.bs.dao.entity.User;

import java.util.List;


public interface UserRepositoryCustom {

    User persist(User user);

    long count(UserQueryDto userQueryDto);

    boolean exist(String userName, String email);

    List<User> find(UserQueryDto userQueryDto);

    Page<User> getPage(UserQueryDto userQueryDto);

    void removeMulti(List<Integer> ids);

    User findOne(UserQueryDto userQueryDto);

    User update(Integer id, String uid, Integer virtualCorn, Integer status);

    User update(Integer id, String nickname);
}
