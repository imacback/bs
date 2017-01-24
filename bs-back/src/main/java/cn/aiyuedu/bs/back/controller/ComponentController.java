package cn.aiyuedu.bs.back.controller;

import cn.aiyuedu.bs.back.service.ComponentService;
import cn.aiyuedu.bs.back.dto.UploadResultDto;
import com.duoqu.commons.web.spring.SessionAttribute;
import cn.aiyuedu.bs.back.service.FileService;
import cn.aiyuedu.bs.common.Constants;
import cn.aiyuedu.bs.common.dto.ResultDto;
import cn.aiyuedu.bs.common.orm.Page;
import cn.aiyuedu.bs.common.util.StringUtil;
import cn.aiyuedu.bs.dao.dto.ComponentDto;
import cn.aiyuedu.bs.dao.entity.AdminUser;
import cn.aiyuedu.bs.dao.entity.Component;
import cn.aiyuedu.bs.service.ImageUploadService;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 */
@Controller
@RequestMapping("/component/*")
public class ComponentController {

    @Autowired
    private ComponentService componentService;
    @Autowired
    private FileService fileService;
    @Autowired
    private ImageUploadService imageUploadService;

    @RequestMapping("list.do")
    @ResponseBody
    public Page<ComponentDto> list(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "isUse", required = false) Integer isUse,
            @RequestParam(value = "containerId", required = false) Integer containerId,
            @RequestParam(value = "start", required = false) Integer start,
            @RequestParam(value = "limit", required = false) Integer limit) {

        if (StringUtils.isBlank(name)) {
            name = null;
        }

        return componentService.getPage(name, isUse, containerId, start, limit);
    }

    @RequestMapping("save.do")
    @ResponseBody
    public Map<String, Object> save(
            HttpServletResponse response,
            @RequestParam("file") MultipartFile file,
            @ModelAttribute("component") Component component,
            @SessionAttribute("adminUser") AdminUser adminUser) {

        response.setContentType("text/html; charset=utf-8");
        String data = "{success:false,info:'文件上传失败'}";

        //未上传文件
        if(file == null || StringUtils.isBlank(file.getOriginalFilename())){
            //update,执行更新操作.如果是insert时则会返回"新增必须上传标题图标"的错误提示
            if(null!=component && null!=component.getId()){
                //更新
                ResultDto r = componentService.save(component, adminUser);
                data = "{success:" + (r.getSuccess()? "true": "false") + ",info:'" + r.getInfo() + "'}";
            }else{
                //组件类型为[广告组1、小图导航2、文字导航3、通栏单广告7、通栏双广告8、分类组件9]中一个时,图片不是必填项
                Integer typeId = component.getTypeId();
                if(null!=typeId && (typeId==1 || typeId==2 || typeId==3
                        || typeId==7 || typeId==8 || typeId==9)){

                    ResultDto rt = componentService.save(component, adminUser);
                    data = "{success:" + (rt.getSuccess()? "true": "false") + ",info:'" + rt.getInfo() + "'}";

                }else{
                    data = "{success:false,info:'新增必须上传标题图标'}";
                }
            }
        }else {//有上传文件
            UploadResultDto u = fileService.save(file, Constants.UploadFileType.ComponentImg);
            if (u != null && u.getSuccess()) {
                component.setIcon(imageUploadService.uploadComponentImage(u.getFile()));
                ResultDto rt = componentService.save(component, adminUser);
                data = "{success:" + (rt.getSuccess()? "true": "false") + ",info:'" + rt.getInfo() + "'}";
            }else{
                data = "{success:false,info:'"+u.getInfo()+"'}";
            }
        }

        try {
            response.getWriter().write(data);
            response.getWriter().flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;

    }

    @RequestMapping("del.do")
    @ResponseBody
    public Map<String, Object> del(
            @RequestParam(value = "ids", required = false) String ids) {
        Map<String, Object> responseMap = Maps.newHashMap();
        responseMap.put("success", "false");

        if (StringUtils.isNotEmpty(ids)) {
            if (componentService.delete(StringUtil.split2Int(ids))) {
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
