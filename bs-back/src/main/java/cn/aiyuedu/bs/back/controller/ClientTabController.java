package cn.aiyuedu.bs.back.controller;

import cn.aiyuedu.bs.back.service.ClientTabService;
import com.duoqu.commons.web.spring.SessionAttribute;
import cn.aiyuedu.bs.common.orm.Page;

import cn.aiyuedu.bs.dao.entity.AdminUser;
import cn.aiyuedu.bs.dao.entity.ClientTab;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.*;

@Controller
@RequestMapping("/clientTab/*")
public class ClientTabController {

    @Autowired
    private ClientTabService clientTabService;

    @RequestMapping("list.do")
    @ResponseBody
    public Page<ClientTab> list(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestParam(value = "start", required = false) Integer start,
            @RequestParam(value = "limit", required = false) Integer limit) {
        return clientTabService.getPage(start, limit);
    }

    @RequestMapping("changeOrder.do")
    @ResponseBody
    public Map<String, Object> changeOrderid(
            @RequestParam(value = "id", required = false) int id,
            @RequestParam(value = "orderId", required = false) int orderId,
            @RequestParam(value = "url", required = false) String url,
            @RequestParam(value = "name", required = false) String name,
            @SessionAttribute(value = "adminUser", required = false) AdminUser adminUser) {

        Map<String, Object> responseMap = new HashMap<String, Object>();
        
      int k =   clientTabService.updateOrderid(id, orderId, adminUser.getId(),  url,name);
        if(k==1){
        	 responseMap.put("success", "true");
        	 responseMap.put("info", "修改成功！");
        }else{
        	responseMap.put("success", "false");
        	 responseMap.put("info", "修改失败！");
    	}
       
       
        return responseMap;
    }
    /***
     * 
     * 发布
     * @param adminUser
     * @return
     */
    @RequestMapping("release.do")
    @ResponseBody
    public Map<String, Object> Release( 
            @SessionAttribute(value = "adminUser", required = false) AdminUser adminUser) {

      Map<String, Object> responseMap = new HashMap<String, Object>();
        
      int k = clientTabService.release();
        if(k==1){
        	 responseMap.put("success", "true");
        	 responseMap.put("info", "发布成功！");
        }else{
        	responseMap.put("success", "false");
        	 responseMap.put("info", "发布失败！");
    	}

        return responseMap;
    }
   
}
