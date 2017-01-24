package cn.aiyuedu.bs.back.controller;

import cn.aiyuedu.bs.back.service.PlatformService;
import cn.aiyuedu.bs.common.orm.Page;
import cn.aiyuedu.bs.dao.dto.PlatformDto;
import cn.aiyuedu.bs.dao.entity.Platform;

import com.google.common.collect.Maps;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

/**
 */
@Controller
@RequestMapping("/platform/*")
public class PlatformController {

    @Autowired
    private PlatformService platformService;

    @RequestMapping("list.do")
    @ResponseBody
    public Map<String, Object> list(
            @RequestParam(value = "isUse", required = false) Integer isUse) {
        Map<String, Object> responseMap = Maps.newHashMap();
        List<Platform> list = platformService.getPlatforms(isUse);
        responseMap.put("success", "true");
        responseMap.put("result", list);
        return responseMap;
    }
    
    @RequestMapping("platformDtoList.do")
    @ResponseBody
    public Page<PlatformDto> list(
    		@RequestParam(value = "id", required = false) Integer id,
            @RequestParam(value = "isUse", required = false) Integer isUse,
            @RequestParam(value = "start", required = false) Integer start,
            @RequestParam(value = "limit", required = false) Integer limit) {
    	
        return platformService.getPage(id, isUse, start, limit);
    }
}
