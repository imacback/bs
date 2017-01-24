package cn.aiyuedu.bs.back.controller;

import com.duoqu.commons.web.spring.SessionAttribute;
import cn.aiyuedu.bs.back.dto.UploadResultDto;
import cn.aiyuedu.bs.back.service.CategoryService;
import cn.aiyuedu.bs.back.service.FileService;
import cn.aiyuedu.bs.common.Constants;
import cn.aiyuedu.bs.common.dto.ResultDto;
import cn.aiyuedu.bs.common.orm.Page;
import cn.aiyuedu.bs.common.util.StringUtil;
import cn.aiyuedu.bs.dao.dto.CategoryDto;
import cn.aiyuedu.bs.dao.dto.CategoryQueryDto;
import cn.aiyuedu.bs.dao.entity.AdminUser;
import cn.aiyuedu.bs.dao.entity.Category;
import cn.aiyuedu.bs.service.ImageUploadService;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
 * Description:
 * @author yz.wu
 */
@Controller
@RequestMapping("/category/*")
public class CategoryController {

    private final Logger logger = LoggerFactory.getLogger(CategoryController.class);

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private FileService fileService;
    @Autowired
    private ImageUploadService imageUploadService;

    @RequestMapping("list.do")
    @ResponseBody
    public Page<CategoryDto> list(
            @ModelAttribute("categoryQueryDto") CategoryQueryDto categoryQueryDto) {
        if (StringUtils.isBlank(categoryQueryDto.getName())) {
            categoryQueryDto.setName(null);
        }
        categoryQueryDto.setIsDesc(0);
        categoryQueryDto.setOrderType(2);
        return categoryService.getPage(categoryQueryDto);
    }

    @RequestMapping("save.do")
    public ModelAndView save(
            HttpServletResponse response,
            @RequestParam("file") MultipartFile file,
            @ModelAttribute("category") Category category,
            @SessionAttribute("adminUser") AdminUser adminUser) {

        response.setContentType("text/html;charset=utf-8");
        String data;

        if (file != null & StringUtils.isNotEmpty(file.getOriginalFilename())) {
            UploadResultDto u = fileService.save(file, Constants.UploadFileType.CategoryImg);
            if (u != null && u.getSuccess()) {
                category.setLogo(imageUploadService.uploadComponentImage(u.getFile()));
            }
        }

        ResultDto result = categoryService.save(category, adminUser);
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

    @RequestMapping("updateOrder.do")
    @ResponseBody
    public Map<String, Object> updateOrder(
            @ModelAttribute("category") Category category,
            @SessionAttribute("adminUser") AdminUser adminUser) {

        Map<String, Object> responseMap = Maps.newHashMap();
        responseMap.put("success", "false");

        ResultDto result = categoryService.save(category, adminUser);

        responseMap.put("success", result.getSuccess());
        responseMap.put("info", result.getInfo());

        return responseMap;
    }

    @RequestMapping("publish.do")
    @ResponseBody
    public Map<String, Object> publish(
            @SessionAttribute("adminUser") AdminUser adminUser) {
        Map<String, Object> responseMap = Maps.newHashMap();
        responseMap.put("success", "false");

        ResultDto result = categoryService.publish();

        responseMap.put("success", result.getSuccess());
        responseMap.put("info", result.getInfo());

        return responseMap;
    }

    @RequestMapping("del.do")
    @ResponseBody
    public Map<String, Object> del(
            @RequestParam(value = "ids", required = false) String ids,
            @SessionAttribute("adminUser") AdminUser adminUser) {
        Map<String, Object> responseMap = Maps.newHashMap();
        responseMap.put("success", "false");

        if (StringUtils.isNotBlank(ids)) {
            List<Integer> list = StringUtil.split2Int(ids);

            ResultDto resultDto = categoryService.delete(list);

            responseMap.put("success", resultDto.getSuccess()? "true": "false");
            responseMap.put("info", resultDto.getInfo());
        } else {
            responseMap.put("info", "请选择要删除的分类！");
        }

        return responseMap;
    }
}

