package cn.aiyuedu.bs.back.controller;

import cn.aiyuedu.bs.back.service.RoleService;
import cn.aiyuedu.bs.common.orm.Page;
import cn.aiyuedu.bs.dao.entity.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Description:
 *
 * @author yz.wu
 */
@Controller
@RequestMapping("/role/*")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @RequestMapping("list.do")
    @ResponseBody
    public Page<Role> list() {
        return roleService.getRolePage();
    }
}
