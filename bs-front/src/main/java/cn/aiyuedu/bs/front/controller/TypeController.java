package cn.aiyuedu.bs.front.controller;

import com.duoqu.commons.web.spring.RequestAttribute;
import cn.aiyuedu.bs.front.service.BookService;
import cn.aiyuedu.bs.front.service.TypeService;
import cn.aiyuedu.bs.front.vo.BookVo;
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
@RequestMapping("/**/ft/type/*")
public class TypeController {
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    @Autowired
    BookService bookService;
    @Autowired
    TypeService typeService;

    @RequestMapping("show.do")
    public ModelAndView list1(HttpServletRequest request,
                              ModelMap model) {
        model.put("typeList", typeService.list1());
        return new ModelAndView("/pages/type");
    }

    @RequestMapping("list.do")
    public ModelAndView list2(HttpServletRequest request,
                              @RequestParam(value = "id", required = true) Integer typeId,
                              @RequestParam(value = "p", required = false, defaultValue = "1") Integer pageNum,
                              @RequestParam(value = "name", required = false, defaultValue = "分类") String typeName,
                               @RequestAttribute(value = "platform", required = false) Integer platform,
                              ModelMap model) {
        List<BookVo> list = bookService.listByType(typeId,platform, pageNum, 5);
        model.put("list", list);
        model.put("typeId", typeId);
        model.put("typeName", typeName);
        return new ModelAndView("/pages/type2");
    }

    @RequestMapping("listAjax.do")
    @ResponseBody
    public List<BookVo> listAjax(HttpServletRequest request,
                                 @RequestParam(value = "id", required = true) Integer type,
                                 @RequestParam(value = "p", required = false, defaultValue = "1") Integer pageNum,
                                  @RequestAttribute(value = "platform", required = false) Integer platform,
                                 ModelMap model) {
        return bookService.listByType(type,platform, pageNum, 5);
    }
}
