package cn.aiyuedu.bs.back.service;

import cn.aiyuedu.bs.common.orm.Page;
import cn.aiyuedu.bs.dao.dto.RoleQueryDto;
import cn.aiyuedu.bs.dao.entity.Role;
import cn.aiyuedu.bs.dao.mongo.repository.RoleRepository;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;

/**
 * Description:
 *
 * @author yz.wu
 */
@Service("roleService")
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    private List<Role> roleList;
    private Map<Integer, Role> roleMap;

    @PostConstruct
    public synchronized void reload(){

        RoleQueryDto roleQueryDto = new RoleQueryDto();
        roleList = roleRepository.find(roleQueryDto);

        roleMap = Maps.newHashMap();
        for (Role r : roleList){
            roleMap.put(r.getId(), r);
        }
    }

    public List<Role> getRoles() {
        if (roleList == null) {
            reload();
        }

        return roleList;
    }

    public Role getById(Integer id) {
        return roleMap.get(id);
    }

    public Page<Role> getRolePage() {
        Page<Role> page = new Page<>();
        page.setResult(getRoles());
        page.setTotalItems(page.getResult().size());
        return page;
    }

    public void initData() {
        roleRepository.drop();

        Role r = new Role();
        r.setIsUse(1);
        r.setMemo("管理员");
        r.setName("管理员");
        r.setMenuIds(Lists.newArrayList(
                1, 2, 3, 4, 5, 6, 7, 8, 9, 10,
                11, 12, 13, 14, 15, 16, 17));
        roleRepository.persist(r);

        r = new Role();
        r.setIsUse(1);
        r.setMemo("操作员");
        r.setName("操作员");
        r.setMenuIds(Lists.newArrayList(
                3,4,5,6,7,8,9,10,
                11,12,13,14,15,16,17));
        roleRepository.persist(r);

        reload();
    }
}
