package cn.aiyuedu.bs.back.controller;

import cn.aiyuedu.bs.back.service.ComponentTypeService;
import com.duoqu.commons.web.spring.SessionAttribute;
import cn.aiyuedu.bs.common.orm.Page;
import cn.aiyuedu.bs.common.dto.ResultDto;
import cn.aiyuedu.bs.common.util.StringUtil;
import cn.aiyuedu.bs.dao.entity.ComponentType;
import cn.aiyuedu.bs.dao.entity.AdminUser;
import org.apache.commons.lang3.StringUtils;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 */
@Controller
@RequestMapping("/componentType/*")
public class ComponentTypeController {

    @Autowired
    private ComponentTypeService componentTypeService;

    @RequestMapping("list.do")
    @ResponseBody
    public Page<ComponentType> list(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "start", required = false) Integer start,
            @RequestParam(value = "limit", required = false) Integer limit) {

        return componentTypeService
                .getPage(name, start, limit);
    }

    @RequestMapping("save.do")
    @ResponseBody
    public Map<String, Object> save(
            @ModelAttribute("componentType") ComponentType componentType,
            @SessionAttribute("adminUser") AdminUser adminUser) {

        Map<String, Object> responseMap = Maps.newHashMap();
        responseMap.put("success", "false");

        ResultDto result = componentTypeService.save(componentType, adminUser);

        responseMap.put("success", result.getSuccess());
        responseMap.put("info", result.getInfo());

        return responseMap;
    }

    @RequestMapping("del.do")
    @ResponseBody
    public Map<String, Object> del(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestParam(value = "ids", required = false) String ids) {
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("success", "false");

        if (StringUtils.isNoneBlank(ids)) {
            if (componentTypeService.delete(StringUtil.split2Int(ids))) {
                responseMap.put("success", "true");
                responseMap.put("info", "成功删除用户！");
            } else {
                responseMap.put("info", "删除用户失败！");
            }
        } else {
            responseMap.put("info", "请选择要删除的用户！");
        }

        return responseMap;
    }
}
