package cn.aiyuedu.bs.dao.mongo.repository.custom;

import cn.aiyuedu.bs.dao.dto.RoleQueryDto;
import cn.aiyuedu.bs.dao.entity.Role;

import java.util.List;

/**
 * Created by Thinkpad on 2014/12/26.
 */
public interface RoleRepositoryCustom {

    void drop();

    Role persist(Role role);

    List<Role> find(RoleQueryDto roleQueryDto);

    void removeMulti(List<Integer> ids);
}
