package cn.aiyuedu.bs.back.controller;

import cn.aiyuedu.bs.back.service.ComponentDataGroupService;
import com.duoqu.commons.web.spring.SessionAttribute;
import cn.aiyuedu.bs.common.orm.Page;
import cn.aiyuedu.bs.common.dto.ResultDto;
import cn.aiyuedu.bs.dao.dto.ComponentDataGroupDto;
import cn.aiyuedu.bs.dao.entity.ComponentDataGroup;
import cn.aiyuedu.bs.dao.entity.AdminUser;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

/**
 */
@Controller
@RequestMapping("/componentDataGroup/*")
public class ComponentDataGroupController {

    @Autowired
    private ComponentDataGroupService componentDataGroupService;

    @RequestMapping("list.do")
    @ResponseBody
    public Page<ComponentDataGroupDto> list(
            @RequestParam(value = "componentId", required = false) Integer componentId,
            @RequestParam(value = "start", required = false) Integer start,
            @RequestParam(value = "limit", required = false) Integer limit) {

        return componentDataGroupService.getPage(componentId, start, limit);
    }

    @RequestMapping("save.do")
    @ResponseBody
    public Map<String, Object> save(
            @ModelAttribute("componentDataGroup") ComponentDataGroup componentDataGroup,
            @SessionAttribute("adminUser") AdminUser adminUser) {

        Map<String, Object> responseMap = Maps.newHashMap();
        responseMap.put("success", "false");

        ResultDto result = componentDataGroupService.save(componentDataGroup, adminUser);

        responseMap.put("success", result.getSuccess());
        responseMap.put("info", result.getInfo());

        return responseMap;
    }

    @RequestMapping("del.do")
    @ResponseBody
    public Map<String, Object> del(
            @RequestParam(value = "ids", required = false) String ids) {
        Map<String, Object> responseMap = Maps.newHashMap();
        responseMap.put("success", "false");

        if (StringUtils.isNotEmpty(ids)) {
            String[] data = ids.split(",");
            List<Integer> list = Lists.newArrayList();
            for (String id : data) {
                list.add(Integer.valueOf(id));
            }

            if (componentDataGroupService.delete(list)) {
                responseMap.put("success", "true");
                responseMap.put("info", "成功删除数据！");
            } else {
                responseMap.put("info", "删除数据失败！");
            }
        } else {
            responseMap.put("info", "请选择要删除的数据！");
        }

        return responseMap;
    }
}
