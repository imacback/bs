package cn.aiyuedu.bs.back.controller;

import com.duoqu.commons.web.spring.SessionAttribute;
import cn.aiyuedu.bs.back.dto.UploadResultDto;
import cn.aiyuedu.bs.back.service.ChapterService;
import cn.aiyuedu.bs.back.service.FileService;
import cn.aiyuedu.bs.common.Constants;
import cn.aiyuedu.bs.common.dto.ResultDto;
import cn.aiyuedu.bs.common.orm.Page;
import cn.aiyuedu.bs.dao.dto.ChapterDto;
import cn.aiyuedu.bs.dao.dto.ChapterQueryDto;
import cn.aiyuedu.bs.dao.dto.FilterResultDto;
import cn.aiyuedu.bs.dao.entity.AdminUser;
import cn.aiyuedu.bs.dao.entity.Chapter;
import cn.aiyuedu.bs.service.FilterWordGeneralService;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 */
@Controller
@RequestMapping("/chapter/*")
public class ChapterController {

    @Autowired
    private FileService fileService;
    @Autowired
    private ChapterService chapterService;
    @Autowired
    private FilterWordGeneralService filterWordGeneralService;

    @RequestMapping("list.do")
    @ResponseBody
    public Page<ChapterDto> list(
            @ModelAttribute("chapterQueryDto") ChapterQueryDto chapterQueryDto) {
        chapterQueryDto.setIsDesc(0);
        chapterQueryDto.setOrderType(2);
        if (StringUtils.isBlank(chapterQueryDto.getOriginName())) {
            chapterQueryDto.setOriginName(null);
        }

        return chapterService.getPage(chapterQueryDto);
    }

    @RequestMapping("load.do")
    @ResponseBody
    public Map<String, Object> load(
            @RequestParam(value = "id", required = false) Long id) {

        Map<String, Object> responseMap = Maps.newHashMap();
        responseMap.put("success", "true");
        responseMap.put("object", chapterService.get(id));
        return responseMap;
    }

    @RequestMapping("save.do")
    @ResponseBody
    public Map<String, Object> save(
            @ModelAttribute("chapter") Chapter chapter,
            @RequestParam(value = "text", required = false) String text,
            @SessionAttribute("adminUser") AdminUser adminUser) {
        Map<String, Object> responseMap = Maps.newHashMap();
        responseMap.put("success", "false");

        ResultDto result = chapterService.save(chapter, text, adminUser);

        responseMap.put("success", result.getSuccess());
        responseMap.put("reload", result.getReload());
        responseMap.put("info", result.getInfo());
        FilterResultDto r = filterWordGeneralService.filter(chapter.getOriginName(), Constants.FilterType.Highlight);
        responseMap.put("originName", r.getText());
        //卷名,用于页面回显
        responseMap.put("volume", chapter.getVolume());
        if (MapUtils.isNotEmpty(result.getCallBack()) &&
                StringUtils.isNotBlank(result.getCallBack().get("filterWords"))) {
            responseMap.put("filterWords", result.getCallBack().get("filterWords"));
        } else {
            responseMap.put("filterWords", "");
        }

        return responseMap;
    }

    @RequestMapping("multiAdd.do")
    @ResponseBody
    public Map<String, Object> multiAdd(
            HttpServletResponse response,
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "bookId", required = false) Long bookId,
            @SessionAttribute("adminUser") AdminUser adminUser) {

        response.setContentType("text/html; charset=utf-8");
        String data = "{success:false,info:'文件上传失败'}";

        if (file != null && StringUtils.isNotBlank(file.getOriginalFilename())) {
            UploadResultDto u = fileService.save(file, Constants.UploadFileType.ChapterContent);
            if (u != null && u.getSuccess()) {
                ResultDto result = chapterService.multiAdd(bookId, u.getFile(), Constants.VOLUME_SEPARATOR, Constants.CHAPTER_SEPARATOR, adminUser);
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


    @RequestMapping("multiOpt.do")
    @ResponseBody
    public Map<String, Object> multiOpt(
            @RequestParam(value = "opt", required = false) String opt,
            @RequestParam(value = "ids", required = false) String ids,
            @RequestParam(value = "bookId", required = false) Long bookId,
            @SessionAttribute("adminUser") AdminUser adminUser) {
        Map<String, Object> responseMap = Maps.newHashMap();
        responseMap.put("success", "false");

        if (StringUtils.isNotBlank(ids)) {
            List<String> data = Splitter.on(",").omitEmptyStrings().splitToList(ids);
            List<Long> list = Lists.newArrayList();
            for (String id : data) {
                list.add(Long.valueOf(id));
            }

            if (StringUtils.isNotBlank(opt)) {
                ResultDto resultDto = null;
                if (StringUtils.equals(opt, "del")) { //批量删除
                    resultDto = chapterService.delete(list, bookId, adminUser);
                } else if (StringUtils.equals(opt, "audit")) { //批量审核
                    resultDto = chapterService.audit(list, bookId, adminUser);
                } else if (StringUtils.equals(opt, "publish")) { //批量发布
                    resultDto = chapterService.online(list, bookId, adminUser);

                } else if (StringUtils.equals(opt, "offline")) { //批量下线
                    resultDto = chapterService.offline(list, bookId, adminUser);
                }
                responseMap.put("success", resultDto.getSuccess() ? "true" : "false");
                responseMap.put("info", resultDto.getInfo());
            } else {
                responseMap.put("info", "操作参数错误！！");
            }
        } else {
            responseMap.put("info", "请选择要操作的数据！");
        }

        return responseMap;
    }

    /**
     * Description 更新章节的虚拟价格
     * @author Wangpeitao
     * @param id 用户id
     * @param newPrice 章节新的虚拟价格
     * @return
     */
    @RequestMapping("updatePrice.do")
    @ResponseBody
    public Map<String, Object> updatePrice(
            @RequestParam(value = "id", required = false) Long id,
            @RequestParam(value = "newPrice", required = false) Integer newPrice,
            @SessionAttribute("adminUser") AdminUser adminUser){

        Map<String, Object> responseMap = Maps.newHashMap();
        responseMap.put("success", "false");

        chapterService.updateChapterPrice(id, newPrice, adminUser);

        responseMap.put("success", "true");
        responseMap.put("info", "虚拟价格修改成功！");

        return responseMap;
    }

    /**
     * Description 校验是否允许修改章节价格,只有在书籍收费且开始收费的章节orderId小于当前章节orderId才允许修改其价格
     * @param request
     * @param response
     * @param chapterId
     * @return
     */
    @RequestMapping("allowUpdatePrice.do")
    @ResponseBody
    public Map<String, Object> allowUpdatePrice(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestParam(value = "chapterId", required = false) Long chapterId){

        Map<String, Object> responseMap = Maps.newHashMap();
        //默认不允许修改章节价格信息
        responseMap.put("success", "false");

        if(null!=chapterId){
            responseMap.put("success", chapterService.allowUpdatePrice(chapterId));
        }

        return responseMap;
    }
}
