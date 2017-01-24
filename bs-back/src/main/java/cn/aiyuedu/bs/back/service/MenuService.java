package cn.aiyuedu.bs.back.service;

import cn.aiyuedu.bs.dao.dto.MenuQueryDto;
import cn.aiyuedu.bs.dao.entity.Menu;
import cn.aiyuedu.bs.dao.entity.Role;
import cn.aiyuedu.bs.dao.mongo.repository.MenuRepository;
import com.google.common.collect.Maps;
import org.apache.commons.collections4.CollectionUtils;
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
@Service("menuService")
public class MenuService {

    @Autowired
    private MenuRepository menuRepository;
    @Autowired
    private RoleService roleService;

    private Map<Integer, List<Menu>> menuMap;

    @PostConstruct
    public synchronized void reload() {
        menuMap = Maps.newHashMap();
        List<Role> roles = roleService.getRoles();
        List<Menu> menus;
        for (Role r : roles) {
            menus = getMenusByRoleId(0, r.getId());
            menuMap.put(r.getId(), menus);
        }
    }

    public List<Menu> getMenusByRoleId(Integer roleId) {
        if (menuMap != null) {
            return menuMap.get(roleId);
        } else {
            return null;
        }
    }

    /**
     * Description 根据菜单父ID获取其下的子菜单
     * @param parentId
     * @return
     */
    public List<Menu> getMenus(Integer parentId) {
        MenuQueryDto queryDto = new MenuQueryDto();
        queryDto.setParentId(parentId);
        queryDto.setIsUse(1);

        List<Menu> list = menuRepository.find(queryDto);
        for (Menu m : list) {
            if (m.getIsLeaf() == 0) {
                m.setLeaf(false);
                m.setChildren(getMenus(m.getId()));
            } else {
                m.setLeaf(true);
            }
        }

        return list;
    }

    /**
     * Description 根据角色获取对应的菜单,目前的版本是不按角色获取菜单,而是获取所有菜单
     * @param parentId
     * @param roleId
     * @return
     */
    public List<Menu> getMenusByRoleId(Integer parentId, Integer roleId) {

        MenuQueryDto queryDto = new MenuQueryDto();
        queryDto.setParentId(parentId);
        queryDto.setIsUse(1);

        if (roleId != null) {
            Role r = roleService.getById(roleId);
            if (r != null && CollectionUtils.isNotEmpty(r.getMenuIds())) {
                queryDto.setIds(r.getMenuIds());
            }
        }

        List<Menu> list = menuRepository.find(queryDto);

        for (Menu m : list) {
            if (m.getIsLeaf() == 0) {
                m.setLeaf(false);
                m.setChildren(getMenusByRoleId(m.getId(), roleId));
            } else {
                m.setLeaf(true);
            }
        }

        return list;
    }

    public void initData() {
        menuRepository.drop();

        Menu m = new Menu();
        m.setMemo("后台用户管理");
        m.setIsUse(1);
        m.setIsLeaf(0);
        m.setOrderId(1);
        m.setParentId(0);
        m.setText(m.getMemo());
        menuRepository.persist(m);//1
        m = new Menu();
        m.setMemo("后台用户管理");
        m.setIsUse(1);
        m.setIsLeaf(1);
        m.setOrderId(1);
        m.setParentId(1);
        m.setText(m.getMemo());
        m.setUrl("adminUserList");
        menuRepository.persist(m);//2

        m = new Menu();
        m.setMemo("资源管理");
        m.setIsUse(1);
        m.setIsLeaf(0);
        m.setOrderId(2);
        m.setParentId(0);
        m.setText(m.getMemo());
        menuRepository.persist(m);//3
        m = new Menu();
        m.setMemo("书籍管理");
        m.setIsUse(1);
        m.setIsLeaf(1);
        m.setOrderId(1);
        m.setParentId(3);
        m.setText(m.getMemo());
        m.setUrl("bookList");
        menuRepository.persist(m);//4
        m = new Menu();
        m.setMemo("章节管理");
        m.setIsUse(1);
        m.setIsLeaf(1);
        m.setOrderId(2);
        m.setParentId(3);
        m.setText(m.getMemo());
        m.setUrl("chapterList");
        menuRepository.persist(m);//5
        m = new Menu();
        m.setMemo("标签管理");
        m.setIsUse(1);
        m.setIsLeaf(1);
        m.setOrderId(3);
        m.setParentId(3);
        m.setText(m.getMemo());
        m.setUrl("tagList");
        menuRepository.persist(m);//6
        m = new Menu();
        m.setMemo("分类管理");
        m.setIsUse(1);
        m.setIsLeaf(1);
        m.setOrderId(4);
        m.setParentId(3);
        m.setText(m.getMemo());
        m.setUrl("categoryList");
        menuRepository.persist(m);//7
        m = new Menu();
        m.setMemo("敏感词管理");
        m.setIsUse(1);
        m.setIsLeaf(1);
        m.setOrderId(5);
        m.setParentId(3);
        m.setText(m.getMemo());
        m.setUrl("filterWordList");
        menuRepository.persist(m);//8

        m = new Menu();
        m.setMemo("版权管理");
        m.setIsUse(1);
        m.setIsLeaf(0);
        m.setOrderId(3);
        m.setParentId(0);
        m.setText(m.getMemo());
        menuRepository.persist(m);//9
        m = new Menu();
        m.setMemo("版权方管理");
        m.setIsUse(1);
        m.setIsLeaf(1);
        m.setOrderId(1);
        m.setParentId(9);
        m.setText(m.getMemo());
        m.setUrl("providerList");
        menuRepository.persist(m);//10
        m = new Menu();
        m.setMemo("批次管理");
        m.setIsUse(1);
        m.setIsLeaf(1);
        m.setOrderId(2);
        m.setParentId(9);
        m.setText(m.getMemo());
        m.setUrl("batchList");
        menuRepository.persist(m);//11

        m = new Menu();
        m.setMemo("组件管理");
        m.setIsUse(1);
        m.setIsLeaf(0);
        m.setOrderId(4);
        m.setParentId(0);
        m.setText(m.getMemo());
        menuRepository.persist(m);//12
        m = new Menu();
        m.setMemo("组件类型管理");
        m.setIsUse(1);
        m.setIsLeaf(1);
        m.setOrderId(1);
        m.setParentId(12);
        m.setText(m.getMemo());
        m.setUrl("componentTypeList");
        menuRepository.persist(m);//13
        m = new Menu();
        m.setMemo("组件管理");
        m.setIsUse(1);
        m.setIsLeaf(1);
        m.setOrderId(2);
        m.setParentId(12);
        m.setText(m.getMemo());
        m.setUrl("componentList");
        menuRepository.persist(m);//14

        m = new Menu();
        m.setMemo("运营管理");
        m.setIsUse(1);
        m.setIsLeaf(0);
        m.setOrderId(5);
        m.setParentId(0);
        m.setText(m.getMemo());
        menuRepository.persist(m);//15
        m = new Menu();
        m.setMemo("页面管理");
        m.setIsUse(1);
        m.setIsLeaf(1);
        m.setOrderId(1);
        m.setParentId(15);
        m.setText(m.getMemo());
        m.setUrl("containerList");
        menuRepository.persist(m);//16
        m = new Menu();
        m.setMemo("Tab管理");
        m.setIsUse(1);
        m.setIsLeaf(1);
        m.setOrderId(2);
        m.setParentId(15);
        m.setText(m.getMemo());
        m.setUrl("tabList");
        menuRepository.persist(m);//17

        reload();
    }

}
