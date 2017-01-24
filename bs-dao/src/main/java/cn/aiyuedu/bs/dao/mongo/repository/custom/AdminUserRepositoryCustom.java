package cn.aiyuedu.bs.dao.mongo.repository.custom;

import cn.aiyuedu.bs.common.orm.Page;
import cn.aiyuedu.bs.dao.dto.AdminUserQueryDto;
import cn.aiyuedu.bs.dao.entity.AdminUser;

import java.util.List;

/**
 * Created by Thinkpad on 2014/12/28.
 */
public interface AdminUserRepositoryCustom {

    void drop();

    AdminUser persist(AdminUser adminUser);

    long count(AdminUserQueryDto adminUserQueryDto);

    List<AdminUser> find(AdminUserQueryDto adminUserQueryDto);

    Page<AdminUser> getPage(AdminUserQueryDto adminUserQueryDto);

    void removeMulti(List<Integer> ids);
}
