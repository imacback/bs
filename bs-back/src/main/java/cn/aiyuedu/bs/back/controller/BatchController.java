package cn.aiyuedu.bs.back.controller;

import cn.aiyuedu.bs.back.dto.UploadExcelResultDto;
import cn.aiyuedu.bs.back.dto.UploadResultDto;
import cn.aiyuedu.bs.back.service.BatchBookService;
import cn.aiyuedu.bs.back.service.BatchService;
import cn.aiyuedu.bs.back.service.FileService;
import cn.aiyuedu.bs.common.Constants;
import cn.aiyuedu.bs.common.dto.ResultDto;
import cn.aiyuedu.bs.common.orm.Page;
import cn.aiyuedu.bs.dao.dto.BatchDto;
import cn.aiyuedu.bs.dao.entity.AdminUser;
import cn.aiyuedu.bs.dao.entity.Batch;
import com.duoqu.commons.web.spring.SessionAttribute;
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
import java.util.HashMap;
import java.util.Map;

/**
 * Description:
 *
 * @author yz.wu
 */
@Controller
@RequestMapping("/batch/*")
public class BatchController {

    @Autowired
    private BatchService batchService;
    @Autowired
    private FileService fileService;
    @Autowired
    private BatchBookService batchBookService;

    @RequestMapping("list.do")
    @ResponseBody
    public Page<BatchDto> list(
            @RequestParam(value = "providerId", required = false) Integer providerId,
            @RequestParam(value = "start", required = false) Integer start,
            @RequestParam(value = "limit", required = false) Integer limit) {

        return batchService.getPage(providerId, start, limit);
    }

    @RequestMapping("save.do")
    @ResponseBody
    public Map<String, Object> save(
            HttpServletResponse response,
            @RequestParam("file") MultipartFile file,
            @ModelAttribute("batch") Batch batch,
            @SessionAttribute("adminUser") AdminUser adminUser) {

        response.setContentType("text/html; charset=utf-8");
        String data = "{success:false,info:'文件上传失败'}";

        //未上传文件
        if (file == null || StringUtils.isBlank(file.getOriginalFilename())) {
            //保存批次,有可能是批次的add或edit
            ResultDto r = batchService.save(batch, adminUser);
            data = "{success:" + (r.getSuccess() ? "true" : "false") + ",info:'" + r.getInfo() + "'}";
        } else {//有上传文件
            UploadResultDto u = fileService.save(file, Constants.UploadFileType.Excel);
            if (u != null && u.getSuccess()) {
                //读取并校验Excel文件
                UploadExcelResultDto excelResultDto = batchService.checkCpBook(u.getFile(), batch.getProviderId());

                if (null != excelResultDto && !excelResultDto.getSuccess()) {//校验不通过
                    data = "{success:false,info:'" + excelResultDto.getInfo() + "'}";
                } else {//校验通过,不存在相同的书籍
                    //保存批次
                    ResultDto r = batchService.save(batch, adminUser);

                    //批次保存成功后则保存对应的书单
                    if (r.getSuccess()) {
                        ResultDto result = batchBookService.saveMultiBatchBook(excelResultDto.getContentList(), batch.getId(), adminUser);
                        if (!result.getSuccess()) {
                            data = "{success:false,info:'" + result.getInfo() + "'}";
                        } else {
                            data = "{success:true,info:'" + result.getInfo() + "'}";
                        }
                    } else {
                        data = "{success:false,info:'" + r.getInfo() + "'}";
                    }
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

    @RequestMapping("load.do")
    @ResponseBody
    public Map<String, Object> load(
            @RequestParam(value = "id", required = false) Integer id) {

        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("success", "true");
        responseMap.put("object", batchService.get(id));
        return responseMap;
    }
}
