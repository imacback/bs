package cn.aiyuedu.bs.back.controller;

import com.duoqu.commons.web.spring.SessionAttribute;
import cn.aiyuedu.bs.common.dto.ResultDto;
import cn.aiyuedu.bs.common.orm.Page;

import cn.aiyuedu.bs.dao.dto.ClientShelfDto;
import cn.aiyuedu.bs.dao.dto.ClientShelfQueryDto;
import cn.aiyuedu.bs.dao.entity.ClientShelf;
import cn.aiyuedu.bs.dao.entity.AdminUser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.*;

@Controller
@RequestMapping("/clientShelf/*")
public class ClientShelfController {

    @Autowired
    private cn.aiyuedu.bs.back.service.ClientShelfService ClientShelfService;

    @RequestMapping("list.do")
    @ResponseBody
    public Page<ClientShelfDto> list(
            HttpServletRequest request,
            HttpServletResponse response,
            @ModelAttribute("clientShelfQueryDto") ClientShelfQueryDto clientShelfQueryDto){

        return ClientShelfService.getPage(clientShelfQueryDto);
    }

    @RequestMapping("saveInfo.do")
    @ResponseBody
    public ResultDto saveInfo(
    		@RequestParam(value = "id", required = false) int id,
            @RequestParam(value = "chapters", required = false) int chapters,
            @RequestParam(value = "platformId", required = false) int platformId,
            @RequestParam(value = "bookIds", required = false) String bookIds,
            @RequestParam(value = "is_use_ditch", required = false) int is_use_ditch,
            @RequestParam(value = "status", required = false) int status,
            @RequestParam(value = "version", required = false) String version,
            @RequestParam(value = "ditch_ids", required = false) String ditch_ids,
            @SessionAttribute(value = "adminUser", required = false) AdminUser adminUser) {
        Map<String, Object> responseMap = new HashMap<String, Object>();
        ClientShelf shelf  = new ClientShelf();
        shelf.setBookIds(bookIds);
        shelf.setChapters(chapters);
        shelf.setDitchIds(ditch_ids);
        shelf.setEditDate(new Date());
        shelf.setEditorId(adminUser.getId());
        shelf.setIsUseDitch(is_use_ditch);
        shelf.setPlatformId(platformId);
        shelf.setStatus(status);
        shelf.setVersion(version);
        ResultDto resultDto =new ResultDto();
        if(is_use_ditch==0   || ditch_ids.equals("")){
        	shelf.setDitchIds("0");
        }
        if(bookIds.split(";").length>9){

            resultDto.setSuccess(false);
            resultDto.setInfo("书籍编号最多填写九个！");

        	return resultDto;
        }

        if(id==0){//add
            shelf.setCreatorId(adminUser.getId());
            shelf.setCreateDate(new Date());

            resultDto=ClientShelfService.insert(shelf);
        }else {//update
            shelf.setId(id);
            resultDto = ClientShelfService.upate(shelf);
        }
        return resultDto;
    }
    
    /***
     * 
     * 是否上线
     * @param id
     * @param adminUser
     * @return
     */
    @RequestMapping("changStatus.do")
    @ResponseBody
    public Map<String, Object> changStatus(
            @RequestParam(value = "id", required = false) int id,
            @RequestParam(value = "status", required = false) int status,
            @SessionAttribute(value = "adminUser", required = false) AdminUser adminUser) {
    	
            Map<String, Object> responseMap = new HashMap<String, Object>();
            boolean k = ClientShelfService.upateStatus(id, status, adminUser.getId());
            if(k){
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
     * 
     * @param id
     * @param adminUser
     * @return
     */
    @RequestMapping("delete.do")
    @ResponseBody
    public Map<String, Object> delete(
            @RequestParam(value = "id", required = false) String id,
            @SessionAttribute(value = "adminUser", required = false) AdminUser adminUser) {
    	Map<String, Object> responseMap = new HashMap<String, Object>();
        boolean k = ClientShelfService.delete(id);
        if(k){
        	 responseMap.put("success", "true");
        	 responseMap.put("info", "删除成功！");
        }else{
        	responseMap.put("success", "false");
        	 responseMap.put("info", "删除失败！");
    	}
        return responseMap;
    }
}
