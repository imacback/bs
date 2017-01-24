package cn.aiyuedu.bs.back.controller;

import com.duoqu.commons.utils.DigestUtils;
import com.duoqu.commons.web.spring.SessionAttribute;
import cn.aiyuedu.bs.back.service.AdminUserService;
import cn.aiyuedu.bs.common.orm.Page;
import cn.aiyuedu.bs.dao.dto.AdminUserDto;
import cn.aiyuedu.bs.dao.dto.AdminUserQueryDto;
import cn.aiyuedu.bs.dao.entity.AdminUser;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
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
 * Description:
 *
 * @author yz.wu
 */
@Controller
@RequestMapping("/adminUser/*")
public class AdminUserController {

    @Autowired
    private AdminUserService adminUserService;


    @RequestMapping("changePassword.do")
    @ResponseBody
    public Map<String, Object> changePassword(
            @RequestParam(value = "oldPassword", required = false) String oldPassword,
            @RequestParam(value = "newPassword", required = false) String newPassword,
            @RequestParam(value = "confirmPassword", required = false) String confirmPassword,
            @SessionAttribute(value = "adminUser", required = false) AdminUser adminUser) {

        Map<String, Object> responseMap = new HashMap<String, Object>();
        responseMap.put("success", "false");

        if (!confirmPassword.equals(newPassword)) {
            responseMap.put("info", "两次输入的新密码不一致！");
        } else {


            if (!DigestUtils.sha1ToBase64UrlSafe(oldPassword).equals( adminUser.getPassword())) {
                responseMap.put("success", "false");
                responseMap.put("info", "原密码错误！");
            } else {
                adminUser.setPassword(DigestUtils.sha1ToBase64UrlSafe(newPassword));
                adminUserService.update(adminUser);
                adminUserService.reload();
                responseMap.put("success", "true");
                responseMap.put("info", "密码修改成功！");
            }
        }

        return responseMap;
    }

    @RequestMapping("list.do")
    @ResponseBody
    public Page<AdminUserDto> list(
            HttpServletRequest request,
            HttpServletResponse response,
            @ModelAttribute("adminUserQueryDto") AdminUserQueryDto adminUserQueryDto){

        if (adminUserQueryDto.getNickname() != null && adminUserQueryDto.getNickname().length() < 1) {
            adminUserQueryDto.setNickname(null);
        }
        if (adminUserQueryDto.getEmail() != null && adminUserQueryDto.getEmail().length() < 1) {
            adminUserQueryDto.setEmail(null);
        }
        return adminUserService.getPage(adminUserQueryDto);
    }

    @RequestMapping("save.do")
    @ResponseBody
    public Map<String, Object> save(@ModelAttribute("AdminUser") AdminUser adminUser,
                                    @SessionAttribute("adminUser") AdminUser admin) {

        Map<String, Object> responseMap = new HashMap<String, Object>();
        responseMap.put("success", "false");

        adminUser.setEditDate(new Date());
        adminUser.setEditorId(admin.getId());

        AdminUserQueryDto queryDto = new AdminUserQueryDto();
        queryDto.setId(adminUser.getId());
        queryDto.setName(adminUser.getName());
        queryDto.setEmail(adminUser.getEmail());

        if (adminUser.getId() != null){
            //排除当前对象进行排重校验
            queryDto.setIsNEId(1);
            if (!adminUserService.isExist(queryDto)) {
                if (adminUserService.update(adminUser)) {
                    responseMap.put("success", "true");
                    responseMap.put("info", "更新成功！");
                } else {
                    responseMap.put("info", "更新失败！");
                }
            } else {
                responseMap.put("info", "用户已存在");
            }
        } else {
            if (!adminUserService.isExist(queryDto)) {
                adminUser.setCreateDate(new Date());
                adminUser.setCreatorId(admin.getId());

                if (adminUserService.add(adminUser)) {
                    responseMap.put("success", "true");
                    responseMap.put("info", "保存成功，初始密码已发送到用户邮箱，请登录后立即修改密码！");
                } else {
                    responseMap.put("info", "保存失败！请检查邮件地址是否正确");
                }
            } else {
                responseMap.put("info", "用户已存在");
            }
        }

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
            List<Integer> list = new ArrayList<Integer>();
            for (String id : data) {
                list.add(Integer.valueOf(id));
            }

            if (adminUserService.delete(list)) {
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
