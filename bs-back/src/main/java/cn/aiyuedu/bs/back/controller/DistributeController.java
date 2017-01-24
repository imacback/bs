package cn.aiyuedu.bs.back.controller;

import cn.aiyuedu.bs.back.dto.DistributeTagDto;
import cn.aiyuedu.bs.back.service.DistributeService;
import com.duoqu.commons.web.spring.SessionAttribute;
import cn.aiyuedu.bs.common.orm.Page;
import cn.aiyuedu.bs.dao.entity.AdminUser;
import cn.aiyuedu.bs.dao.entity.Distribute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Thinkpad on 2014/11/18.
 */
@Controller
@RequestMapping("/distribute/*")
public class DistributeController {
    private final Logger logger = LoggerFactory.getLogger(DistributeController.class);

    @Autowired
    DistributeService distributeService;


    @RequestMapping("list.do")
    @ResponseBody
    public Page<DistributeTagDto> list(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "status", required = false) Integer status,
            @RequestParam(value = "start", required = false) Integer start,
            @RequestParam(value = "isCategory", required = false) Integer isCategory,
            @RequestParam(value = "limit", required = false) Integer limit) {
        return distributeService.getPage(name, status, start, limit,isCategory);
    }


    @RequestMapping("save.do")
    @ResponseBody
    public Map<String, Object> inster(
            @ModelAttribute("Push") Distribute distribute,
            @SessionAttribute(value = "adminUser", required = false) AdminUser adminUser) {
        Map<String, Object> responseMap = distributeService.save(distribute, adminUser);
        return responseMap;
    }


    @RequestMapping("delete.do")
    @ResponseBody
    public Map<String, Object> delete(
            @RequestParam(value = "id", required = false) Integer id) {
        Map<String, Object> responseMap = new HashMap<String, Object>();
        distributeService.deleteDistribute(id);
        responseMap.put("success", "true");
        responseMap.put("info", "删除成功！");
        return responseMap;
    }


    /**
     * @param adminUser
     * @return
     */
    @RequestMapping("query.do")
    @ResponseBody
    public Page<Distribute> queryDistrbute(
            @RequestParam(value = "start", required = false) Integer start,
            @RequestParam(value = "isCategory", required = false) Integer isCategory,
            @SessionAttribute(value = "adminUser", required = false) AdminUser adminUser) {
        Page<Distribute> page = new Page<>();
        page.setTotalItems(100);
        page.setResult(distributeService.findAll(start,isCategory));
        return page;
    }


}
