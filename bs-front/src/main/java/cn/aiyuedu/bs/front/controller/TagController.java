package cn.aiyuedu.bs.front.controller;

import com.duoqu.commons.web.spring.RequestAttribute;
import cn.aiyuedu.bs.front.service.BookService;
import cn.aiyuedu.bs.front.service.TypeService;
import cn.aiyuedu.bs.front.vo.BookVo;
import cn.aiyuedu.bs.service.TagGeneralService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Description:
 *
 * @author Scott
 */
@Controller
@RequestMapping("/**/ft/tag/*")
public class TagController {
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    @Autowired
    BookService bookService;
    @Autowired
    TypeService typeService;
    @Autowired
    TagGeneralService tagGeneralService;
    //////////////////////////////////////////////////////////////////////
    @RequestMapping("list.do")
    public ModelAndView tagList2(HttpServletRequest request,
                                 @RequestParam(value = "id", required = true) Integer tag,
                                 @RequestParam(value = "p", required = false, defaultValue = "1") Integer pageNum,
                                 @RequestParam(value = "name", required = false, defaultValue = "标签") String tagName,
                                  @RequestAttribute(value = "platform", required = false) Integer platform,
                                 ModelMap model) {
        List<BookVo> list = bookService.listByTag(tag,platform, pageNum, 5);
        model.put("list", list);
        model.put("tagId", tag);
        model.put("tagName",tagName);
        return new ModelAndView("/pages/tag");
    }

    @RequestMapping("listAjax.do")
    @ResponseBody
    public List<BookVo> tagAjax(HttpServletRequest request,
                                 @RequestParam(value = "id", required = true) Integer tag,
                                 @RequestParam(value = "p", required = false, defaultValue = "1") Integer pageNum,
                                  @RequestAttribute(value = "platform", required = false) Integer platform,
                                 ModelMap model) {
        return bookService.listByTag(tag,platform, pageNum, 5);
    }
}
