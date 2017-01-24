package cn.aiyuedu.bs.back.controller;

import cn.aiyuedu.bs.back.service.ProviderService;
import com.duoqu.commons.web.spring.SessionAttribute;
import cn.aiyuedu.bs.common.dto.ResultDto;
import cn.aiyuedu.bs.common.orm.Page;
import cn.aiyuedu.bs.dao.dto.ProviderDto;
import cn.aiyuedu.bs.dao.entity.AdminUser;
import cn.aiyuedu.bs.dao.entity.Provider;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 */
@Controller
@RequestMapping("/provider/*")
public class ProviderController {

    @Autowired
    private ProviderService providerService;

    @RequestMapping("list.do")
    @ResponseBody
    public Page<ProviderDto> list(
            @RequestParam(value = "id", required = false) Integer id,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "start", required = false) Integer start,
            @RequestParam(value = "limit", required = false) Integer limit) {

        return providerService
                .getPage(id, name, start, limit);
    }

    @RequestMapping("save.do")
    @ResponseBody
    public Map<String, Object> save(
            @ModelAttribute("Provider") Provider provider,
            @SessionAttribute("adminUser") AdminUser adminUser) {

        Map<String, Object> responseMap = Maps.newHashMap();
        responseMap.put("success", "false");

        ResultDto result = providerService.save(provider, adminUser);

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
            List<Integer> list = new ArrayList<>();
            for (String id : data) {
                list.add(Integer.valueOf(id));
            }

            if (providerService.delete(list)) {
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

    @RequestMapping("publish.do")
    @ResponseBody
    public Map<String, Object> publish(
            @SessionAttribute("adminUser") AdminUser adminUser) {
        Map<String, Object> responseMap = Maps.newHashMap();
        responseMap.put("success", "false");

        ResultDto result = providerService.publish();

        responseMap.put("success", result.getSuccess());
        responseMap.put("info", result.getInfo());

        return responseMap;
    }
}
