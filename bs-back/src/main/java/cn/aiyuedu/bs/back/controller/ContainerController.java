package cn.aiyuedu.bs.back.controller;

import cn.aiyuedu.bs.back.service.ContainerService;
import com.alibaba.fastjson.JSON;
import com.duoqu.commons.utils.HttpClient;
import com.duoqu.commons.web.spring.SessionAttribute;
import cn.aiyuedu.bs.common.orm.Page;
import cn.aiyuedu.bs.common.dto.ResultDto;
import cn.aiyuedu.bs.dao.dto.ContainerDto;
import cn.aiyuedu.bs.dao.entity.Container;
import cn.aiyuedu.bs.dao.entity.AdminUser;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 */
@Controller
@RequestMapping("/container/*")
public class ContainerController {

    private final Logger logger = LoggerFactory.getLogger(ContainerController.class);

    @Autowired
    private ContainerService containerService;
    @Autowired
    private Properties bsBgConfig;

    @RequestMapping("list.do")
    @ResponseBody
    public Page<ContainerDto> list(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "isUse", required = false) Integer isUse,
            @RequestParam(value = "start", required = false) Integer start,
            @RequestParam(value = "limit", required = false) Integer limit) {

        return containerService.getPage(name, isUse, start, limit);
    }

    @RequestMapping("save.do")
    @ResponseBody
    public Map<String, Object> save(
            @ModelAttribute("Container") Container container,
            @SessionAttribute("adminUser") AdminUser adminUser) {

        Map<String, Object> responseMap = Maps.newHashMap();
        responseMap.put("success", "false");

        ResultDto result = containerService.save(container, adminUser);

        responseMap.put("success", result.getSuccess());
        responseMap.put("info", result.getInfo());

        return responseMap;
    }

    @RequestMapping("del.do")
    @ResponseBody
    public Map<String, Object> del(HttpServletRequest request,
                                   HttpServletResponse response,
                                   @RequestParam(value = "ids", required = false) String ids) {
        Map<String, Object> responseMap = Maps.newHashMap();
        responseMap.put("success", "false");

        if (StringUtils.isNotEmpty(ids)) {
            String[] data = ids.split(",");
            List<Integer> list = Lists.newArrayList();
            for (String id : data) {
                list.add(Integer.valueOf(id));
            }

            if (containerService.delete(list)) {
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
            @RequestParam(value = "containerId", required = false) Integer containerId) {

        ResultDto r = containerService.publish(containerId);
        Map<String, Object> responseMap = Maps.newHashMap();
        responseMap.put("success", r.getSuccess()? "true": "false");
        responseMap.put("info", r.getInfo());
        return responseMap;
    }

    @RequestMapping("preview.do")
    public ModelAndView preview(
            HttpServletResponse response,
            @RequestParam(value = "containerId", required = false) Integer containerId) {

        Container c = containerService.getPreviewContainer(containerId);
        String json = JSON.toJSONString(c);

        logger.debug(json);

        String html = HttpClient.post(bsBgConfig.getProperty("front.domain") + bsBgConfig.getProperty("container.preview"), json);
        if (StringUtils.isNotEmpty(html)) {
            html = html.replaceAll("\"/static", "\"" + bsBgConfig.getProperty("front.domain") + "static");
        } else {
            html = "data error!";
        }

        try {
            response.getWriter().write(html);
            response.getWriter().flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
