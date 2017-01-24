package cn.aiyuedu.bs.back.controller;

import cn.aiyuedu.bs.back.dto.UploadResultDto;
import cn.aiyuedu.bs.back.service.BatchBookService;
import cn.aiyuedu.bs.back.service.FileService;
import cn.aiyuedu.bs.common.Constants;
import cn.aiyuedu.bs.common.dto.ResultDto;
import cn.aiyuedu.bs.common.orm.Page;
import cn.aiyuedu.bs.dao.dto.BatchBookDto;
import cn.aiyuedu.bs.dao.entity.AdminUser;
import com.duoqu.commons.web.spring.SessionAttribute;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description:
 *
 * @author yz.wu
 */
@Controller
@RequestMapping("/batchBook/*")
public class BatchBookController {

    @Autowired
    private BatchBookService batchBookService;
    @Autowired
    private FileService fileService;

    @RequestMapping("list.do")
    @ResponseBody
    public Page<BatchBookDto> list(
            @RequestParam(value = "providerId", required = false) Integer providerId,
            @RequestParam(value = "batchId", required = false) Integer batchId,
            @RequestParam(value = "bookName", required = false) String bookName,
            @RequestParam(value = "cpBookId", required = false) String cpBookId,
            @RequestParam(value = "start", required = false) Integer start,
            @RequestParam(value = "limit", required = false) Integer limit) {

        return batchBookService.getPage(providerId, batchId, bookName, cpBookId, start, limit);
    }

    @RequestMapping("save.do")
    @ResponseBody
    public Map<String, Object> save(
            HttpServletResponse response,
            @RequestParam(value = "providerId", required = false) Integer providerId,
            @RequestParam(value = "batchId", required = false) Integer batchId,
            @RequestParam("file") MultipartFile file,
            @SessionAttribute("adminUser") AdminUser adminUser) {

        response.setContentType("text/html; charset=utf-8");
        String data = "{success:false,info:'文件上传失败'}";

        if (file != null && StringUtils.isNotEmpty(file.getOriginalFilename())) {
            UploadResultDto u = fileService.save(file, Constants.UploadFileType.Excel);
            if (u != null && u.getSuccess()) {
                ResultDto result = batchBookService.multiAdd(u.getFile(), providerId, batchId, adminUser);
                if (!result.getSuccess()) {
                    data = "{success:false,info:'" + result.getInfo() + "'}";
                } else {
                    data = "{success:true,info:'" + result.getInfo() + "'}";
                }
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
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("success", "false");

        if (StringUtils.isNotEmpty(ids)) {
            String[] data = ids.split(",");
            List<Long> list = new ArrayList<>();
            for (String id : data) {
                list.add(Long.valueOf(id));
            }

            if (batchBookService.delete(list)) {
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
