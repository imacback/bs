package cn.aiyuedu.bs.back.controller;

import cn.aiyuedu.bs.back.service.BookService;
import cn.aiyuedu.bs.back.dto.UploadResultDto;
import com.duoqu.commons.ip.constant.ImageEnum;
import com.duoqu.commons.web.spring.SessionAttribute;
import cn.aiyuedu.bs.back.service.FileService;
import cn.aiyuedu.bs.common.Constants.UploadFileType;
import cn.aiyuedu.bs.common.dto.ResultDto;
import cn.aiyuedu.bs.common.orm.Page;
import cn.aiyuedu.bs.common.util.StringUtil;
import cn.aiyuedu.bs.dao.dto.BookDto;
import cn.aiyuedu.bs.dao.dto.BookQueryDto;
import cn.aiyuedu.bs.dao.entity.AdminUser;
import cn.aiyuedu.bs.dao.entity.Book;
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
import java.util.List;
import java.util.Map;

/**
 */
@Controller
@RequestMapping("/book/*")
public class BookController {

    @Autowired
    private BookService bookService;
    @Autowired
    private FileService fileService;

    @RequestMapping("list.do")
    @ResponseBody
    public Page<BookDto> list(
            @ModelAttribute("bookQueryDto") BookQueryDto bookQueryDto) {
        if (StringUtils.isBlank(bookQueryDto.getName())) {
            bookQueryDto.setName(null);
        }
        if (StringUtils.isBlank(bookQueryDto.getAuthor())) {
            bookQueryDto.setAuthor(null);
        }
        if (bookQueryDto.getTagStoryId() != null && bookQueryDto.getTagStoryId() == 0) {
            bookQueryDto.setTagStoryId(null);
        }
        if (bookQueryDto.getProviderId() != null && bookQueryDto.getProviderId() == 0) {
            bookQueryDto.setProviderId(null);
        }
        return bookService.getPage(bookQueryDto);
    }

    @RequestMapping("load.do")
    @ResponseBody
    public Map<String, Object> load(
            @RequestParam(value = "id", required = false) Long id) {
        Map<String, Object> responseMap = Maps.newHashMap();
        responseMap.put("success", "true");
        responseMap.put("object", bookService.getBookDto(id));
        return responseMap;
    }

    @RequestMapping("save.do")
    @ResponseBody
    public Map<String, Object> save(
            HttpServletResponse response,
            @ModelAttribute("Book") Book book,
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "operatePlatformIds", required = false) String operatePlatformIds,
            @RequestParam(value = "feePlatformIds", required = false) String feePlatformIds,
            @RequestParam(value = "contentIds", required = false) String contentIds,
            @RequestParam(value = "supplyIds", required = false) String supplyIds,
            @SessionAttribute("adminUser") AdminUser adminUser) {
        response.setContentType("text/html; charset=utf-8");
        String data;

        if (file != null && StringUtils.isNotBlank(file.getOriginalFilename())) {
            UploadResultDto u = fileService.save(file, UploadFileType.BookCover,
                    book.getProviderId(), book.getCpBookId());
            if (u != null && u.getSuccess()) {
                Map<String, String> result = u.getUrlMap();
                if (result != null && StringUtils.isNotBlank(result.get(ImageEnum.Flag.SMALL.getValue()))) {
                    book.setSmallPic(result.get(ImageEnum.Flag.SMALL.getValue()));
                    book.setLargePic(result.get(ImageEnum.Flag.LARGE.getValue()));
                }
            }
        }

        if (StringUtils.isNotBlank(operatePlatformIds)) {
            book.setOperatePlatformIds(StringUtil.split2Int(operatePlatformIds));
        }
        if (StringUtils.isNotBlank(feePlatformIds)) {
            book.setFeePlatformIds(StringUtil.split2Int(feePlatformIds));
        }
        if (StringUtils.isNotBlank(contentIds)) {
            book.setTagContentIds(StringUtil.split2Int(contentIds));
        }
        if (StringUtils.isNotBlank(supplyIds)) {
            book.setTagSupplyIds(StringUtil.split2Int(supplyIds));
        }
        ResultDto result = bookService.save(book, adminUser);
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

    @RequestMapping("multiOpt.do")
    @ResponseBody
    public Map<String, Object> multiOpt(
            @RequestParam(value = "opt", required = false) String opt,
            @RequestParam(value = "ids", required = false) String ids,
            @SessionAttribute("adminUser") AdminUser adminUser) {
        Map<String, Object> responseMap = Maps.newHashMap();
        responseMap.put("success", "false");

        if (StringUtils.isNotBlank(ids)) {
            List<Long> list = StringUtil.split2Long(ids);

            if (StringUtils.isNotBlank(opt)) {
                if (StringUtils.equals(opt, "del")) {               //批量删除
                    bookService.delete(list, adminUser);
                    responseMap.put("success", "true");
                    responseMap.put("info", "成功删除数据！");
                } else if (StringUtils.equals(opt, "offline")) {    //批量下线
                    bookService.offline(list, adminUser);
                    responseMap.put("success", "true");
                    responseMap.put("info", "成功下线数据！");
                } else if (StringUtils.equals(opt, "online")) {     //批量上线
                    bookService.online(list, adminUser);
                    responseMap.put("success", "true");
                    responseMap.put("info", "成功发布数据！");
                }
            } else {
                responseMap.put("info", "操作参数错误！！");
            }
        } else {
            responseMap.put("info", "请选择要执行操作的数据！");
        }

        return responseMap;
    }

    @RequestMapping("multiAddTag.do")
    @ResponseBody
    public Map<String, Object> multiAddTag(
            HttpServletResponse response,
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "providerId", required = false) Integer providerId,
            @SessionAttribute("adminUser") AdminUser adminUser) {

        response.setContentType("text/html; charset=utf-8");
        String data = "{success:false,info:'文件上传失败'}";

        if (file != null && StringUtils.isNotBlank(file.getOriginalFilename())) {
            UploadResultDto u = fileService.save(file, UploadFileType.Excel);
            if (u != null && u.getSuccess()) {
                ResultDto result = bookService.multiAddTag(u.getFile(), providerId, adminUser);
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
}
