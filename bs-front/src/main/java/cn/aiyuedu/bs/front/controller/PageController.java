package cn.aiyuedu.bs.front.controller;

import cn.aiyuedu.bs.front.service.PageService;
import com.alibaba.fastjson.JSON;
import cn.aiyuedu.bs.common.model.ContainerBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * Description:
 *
 * @author Scott
 */
@Controller
@RequestMapping("/**/ft/page/*")
public class PageController {
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    @Autowired
    PageService pageService;

    @RequestMapping("show.do")
    public ModelAndView show(HttpServletRequest request,
                             @RequestParam(value = "id", required = true) String pageId,
                             ModelMap model) {
        model.put("page", pageService.getPage(pageId));
        model.put("pageId", pageId);
        return new ModelAndView("/pages/page");
    }

    @RequestMapping("preview.do")
    public ModelAndView preview(HttpServletRequest request,
                                @RequestBody ContainerBase ctn,
                                ModelMap model) {
        log.info("preview: " + JSON.toJSONString(ctn));
        model.put("page", pageService.getPage(ctn));
        model.put("pageId", ctn.getId());
        return new ModelAndView("/pages/page");
    }

    @RequestMapping("error.do")
//    @ExceptionHandler(Throwable.class)
    public ModelAndView ex(HttpServletRequest request,
                           @RequestParam(value = "code", required = false) String code,
                           Throwable ex) {
        // Object o = request.getAttribute("javax.servlet.error.exception");
        String refer = request.getHeader("referer");
        log.error("ExceptionHandler Throwable code=" + code + ",refer=" + refer,
                request.getAttribute("javax.servlet.error.exception"));
//        log.error("ExceptionHandler Throwable", ex);
        return new ModelAndView("/pages/error");
    }

}
