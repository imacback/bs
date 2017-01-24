package cn.aiyuedu.bs.back.controller;

import cn.aiyuedu.bs.back.dto.DistributeBookTagDto;
import cn.aiyuedu.bs.back.dto.UploadResultDto;
import cn.aiyuedu.bs.back.service.DistributeBookService;
import cn.aiyuedu.bs.back.service.FileService;
import com.duoqu.commons.web.spring.SessionAttribute;
import cn.aiyuedu.bs.common.Constants;
import cn.aiyuedu.bs.common.orm.Page;
import cn.aiyuedu.bs.dao.dto.DistributeBookQueryDto;
import cn.aiyuedu.bs.dao.entity.AdminUser;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Thinkpad on 2014/11/19.
 */
@Controller
@RequestMapping("/distributeBook/*")
public class DistributeBookController {

    private final Logger logger = LoggerFactory.getLogger(DistributeBookController.class);
    @Autowired
    private FileService fileService;

    @Autowired
    private DistributeBookService distributeBookService;


    @RequestMapping("list.do")
    @ResponseBody
    public Page<DistributeBookTagDto> list(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestParam(value = "bookId", required = false) Long bookId,
            @RequestParam(value = "disId", required = false) Integer disId,
            @RequestParam(value = "start", required = false) Integer start,
            @RequestParam(value = "limit", required = false) Integer limit) {
        DistributeBookQueryDto dto = new DistributeBookQueryDto();
        dto.setBookId(bookId);
        dto.setDistributeId(disId);
        dto.setLimit(limit);
        dto.setStart(start);
        return distributeBookService.find(dto);


    }


    @RequestMapping("save.do")
    @ResponseBody
    public Map<String, Object> save(
            HttpServletResponse response,
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "disId", required = false) Integer disId,
            @RequestParam(value = "id", required = false) Integer id,
            @RequestParam(value = "bookId", required = false) String bookId,
            @SessionAttribute("adminUser") AdminUser adminUser) {

        response.setContentType("text/html; charset=utf-8");
        String data = "{success:false,info:'文件上传失败'}";
        //未上传文件
        if (file == null || StringUtils.isBlank(file.getOriginalFilename())) {
            if (id == null) {
                Map map = distributeBookService.insertBook(null, disId, bookId, adminUser);
                data = "{success:" + (Boolean) map.get("success") + ",info:'" + map.get("info") + "'}";
            } else {

                int k = distributeBookService.update(id, disId, Long.valueOf(bookId), adminUser);
                if (k == 1) {
                    data = "{success:true,info:'保存成功'}";
                } else if (k == 2) {
                    data = "{success:false,info:'该渠道存在相同的书籍'}";
                } else {
                    data = "{success:false,info:'保存失败'}";
                }
            }

        } else {//有上传文件
            UploadResultDto u = fileService.save(file, Constants.UploadFileType.Excel);
            if (u != null && u.getSuccess()) {
                Map map = distributeBookService.insertBook(u.getFile(), disId, bookId, adminUser);
                data = "{success:" + (Boolean) map.get("success") + ",info:'" + map.get("info") + "'}";
            }

        }
        try {
            response.getWriter().write(data);
            response.getWriter().flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (file != null) {
                file = null;
            }
        }

        return null;
    }


    @RequestMapping("delete.do")
    @ResponseBody
    public Map<String, Object> delete(
            @RequestParam(value = "id", required = false) Integer id,
            @RequestParam(value = "disId", required = false) Integer disId,
            @SessionAttribute(value = "adminUser", required = false) AdminUser adminUser) {
        Map<String, Object> responseMap = new HashMap<String, Object>();
        logger.info("disId" + disId);
        distributeBookService.delete(id, disId);
        responseMap.put("success", "true");
        responseMap.put("info", "删除成功！");
        return responseMap;
    }

}
