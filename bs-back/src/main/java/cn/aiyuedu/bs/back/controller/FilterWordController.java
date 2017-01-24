package cn.aiyuedu.bs.back.controller;

import cn.aiyuedu.bs.back.dto.UploadResultDto;
import com.duoqu.commons.web.spring.SessionAttribute;
import cn.aiyuedu.bs.back.service.FileService;
import cn.aiyuedu.bs.back.service.FilterWordService;
import cn.aiyuedu.bs.common.Constants;
import cn.aiyuedu.bs.common.orm.Page;
import cn.aiyuedu.bs.common.dto.ResultDto;
import cn.aiyuedu.bs.common.util.StringUtil;
import cn.aiyuedu.bs.dao.dto.FilterWordDto;
import cn.aiyuedu.bs.dao.dto.FilterWordQueryDto;
import cn.aiyuedu.bs.dao.entity.FilterWord;
import cn.aiyuedu.bs.dao.entity.AdminUser;
import org.apache.commons.lang3.StringUtils;
import com.google.common.collect.Maps;
import org.apache.poi.ss.usermodel.Workbook;
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
import java.io.OutputStream;
import java.util.*;

/**
 */
@Controller
@RequestMapping("/filterWord/*")
public class FilterWordController {

    @Autowired
    private FilterWordService filterWordService;
    @Autowired
    private FileService fileService;

    @RequestMapping("list.do")
    @ResponseBody
    public Page<FilterWordDto> list(
            @ModelAttribute("filterWordQueryDto") FilterWordQueryDto filterWordQueryDto) {
        return filterWordService.getPage(filterWordQueryDto);
    }

    @RequestMapping("save.do")
    @ResponseBody
    public Map<String, Object> save(
            @ModelAttribute("FilterWord") FilterWord filterWord,
            @SessionAttribute("adminUser") AdminUser adminUser) {

        Map<String, Object> responseMap = Maps.newHashMap();
        responseMap.put("success", "false");

        ResultDto result = filterWordService.save(filterWord, adminUser);

        responseMap.put("success", result.getSuccess());
        responseMap.put("info", result.getInfo());

        return responseMap;
    }

    @RequestMapping("multiAdd.do")
    @ResponseBody
    public Map<String, Object> multiAdd(
            HttpServletResponse response,
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "level", required = false) Integer level,
            @RequestParam(value = "replaceStrategyType", required = false) Integer replaceStrategyType,
            @RequestParam(value = "words", required = false) String words,
            @SessionAttribute("adminUser") AdminUser adminUser) {

        response.setContentType("text/html; charset=utf-8");
        String data = "{success:false,info:'文件上传失败'}";

        ResultDto result;
        if (file != null && StringUtils.isNotBlank(file.getOriginalFilename())) {
            UploadResultDto u = fileService.save(file, Constants.UploadFileType.Excel);
            if (u != null && u.getSuccess()) {
                result = filterWordService.multiAdd(u.getFile(), adminUser);
            } else {
                result = new ResultDto();
                result.setSuccess(false);
                result.setInfo("文件上传失");
            }
        } else {
            result = filterWordService.multiAdd(level, replaceStrategyType, words, adminUser);
        }

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
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("success", "false");

        if (StringUtils.isNotEmpty(ids)) {
            if (filterWordService.delete(StringUtil.split2Int(ids))) {
                responseMap.put("success", "true");
                responseMap.put("info", "成功删除敏感词！");
            } else {
                responseMap.put("info", "删除敏感词失败！");
            }
        } else {
            responseMap.put("info", "请选择要删除的敏感词！");
        }

        return responseMap;
    }

    @RequestMapping("publish.do")
    @ResponseBody
    public Map<String, Object> publish(
            @SessionAttribute("adminUser") AdminUser adminUser) {
        Map<String, Object> responseMap = Maps.newHashMap();
        responseMap.put("success", "false");

        filterWordService.reload();

        responseMap.put("success", true);
        responseMap.put("info", "发布成功");

        return responseMap;
    }

    @RequestMapping("export.do")
    public ModelAndView export(
            HttpServletResponse response,
            @ModelAttribute("filterWordQueryDto") FilterWordQueryDto filterWordQueryDto,
            @SessionAttribute("adminUser") AdminUser adminUser) {

        Workbook wb = filterWordService.export(filterWordQueryDto, adminUser);
        if (wb != null) {
            response.reset();
            response.setContentType("application/msexcel;charset=UTF-8");
            try {
                response.addHeader("Content-Disposition", "attachment;filename=filterWords.xls");
                OutputStream out = response.getOutputStream();
                wb.write(out);
                out.flush();
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            String data = "{success:false,info:'文件上传失败'}";
            try {
                response.getWriter().write(data);
                response.getWriter().flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
