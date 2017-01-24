package cn.aiyuedu.bs.back.controller;

import com.duoqu.commons.web.spring.SessionAttribute;
import cn.aiyuedu.bs.back.service.MenuService;
import cn.aiyuedu.bs.dao.entity.AdminUser;
import cn.aiyuedu.bs.dao.entity.Menu;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * Description:
 * 
 * @author yz.wu
 */
@Controller
@RequestMapping("/menu/*")
public class MenuController {

	@Autowired
	private MenuService menuService;

	@RequestMapping("list.do")
	@ResponseBody
	public Map<String, Object> list(
            @SessionAttribute("adminUser") AdminUser adminUser) {
		Map<String, Object> responseMap = Maps.newHashMap();
		List<Menu> list = menuService.getMenusByRoleId(adminUser.getRoleId());
		responseMap.put("success", "true");
		responseMap.put("data", list);
		return responseMap;
	}
}
