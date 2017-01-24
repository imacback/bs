package cn.aiyuedu.bs.back.controller;

import cn.aiyuedu.bs.back.service.ComponentDataService;
import cn.aiyuedu.bs.back.dto.UploadResultDto;
import com.duoqu.commons.web.spring.SessionAttribute;
import cn.aiyuedu.bs.back.service.FileService;
import cn.aiyuedu.bs.common.Constants;
import cn.aiyuedu.bs.common.dto.ResultDto;
import cn.aiyuedu.bs.common.orm.Page;
import cn.aiyuedu.bs.dao.dto.ComponentDataDto;
import cn.aiyuedu.bs.dao.entity.AdminUser;
import cn.aiyuedu.bs.dao.entity.ComponentData;
import cn.aiyuedu.bs.service.ImageUploadService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 */
@Controller
@RequestMapping("/componentData/*")
public class ComponentDataController {

    @Autowired
    private ComponentDataService componentDataService;
    @Autowired
    private FileService fileService;
    @Autowired
    private ImageUploadService imageUploadService;

    @RequestMapping("list.do")
    @ResponseBody
    public Page<ComponentDataDto> list(
            @RequestParam(value = "componentId", required = false) Integer componentId,
            @RequestParam(value = "groupId", required = false) Integer groupId,
            @RequestParam(value = "start", required = false) Integer start,
            @RequestParam(value = "limit", required = false) Integer limit) {

        return componentDataService.getPage(componentId, groupId, start, limit);
    }

    @RequestMapping("save.do")
    public ModelAndView save(
            HttpServletResponse response,
            @RequestParam("file") MultipartFile file,
            @ModelAttribute("componentData") ComponentData componentData,
            @SessionAttribute("adminUser") AdminUser adminUser) {

        response.setContentType("text/html; charset=utf-8");
        String data = "{success:false,info:'文件上传失败'}";

        if (file != null & StringUtils.isNotEmpty(file.getOriginalFilename())) {
            UploadResultDto u = fileService.save(file, Constants.UploadFileType.ComponentImg);
            if (u != null && u.getSuccess()) {
                componentData.setLogo(imageUploadService.uploadComponentImage(u.getFile()));
            }
        }

        ResultDto result = componentDataService.save(componentData, adminUser);
        if (!result.getSuccess()) {
            data = "{success:false,info:'" + result.getInfo() + "'}";
        } else {
            data = "{success:true,info:'" + result.getInfo() + "'}";
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
            String[] data = ids.split(",");
            List<Integer> list = Lists.newArrayList();
            for (String id : data) {
                list.add(Integer.valueOf(id));
            }

            if (componentDataService.delete(list)) {
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
