package cn.aiyuedu.bs.front.controller;

import cn.aiyuedu.bs.front.vo.BookVo;
import com.duoqu.commons.web.spring.RequestAttribute;
import cn.aiyuedu.bs.front.service.BookService;
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
@RequestMapping("/**/ft/search/*")
public class SearchController {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    BookService bookService;


    @RequestMapping("list.do")
    public ModelAndView search(HttpServletRequest request,
                               @RequestParam(value = "kw", required = false) String kw,
                               @RequestParam(value = "p", required = false, defaultValue = "1") Integer p,
                                @RequestAttribute(value = "platform", required = false) Integer platform,
                               ModelMap model) {

        List<BookVo> list =  bookService.sortList(kw,platform, p, 5, null);
                model.put("list", list);
        model.put("kw", kw);
        return new ModelAndView("/pages/search");
    }



    @RequestMapping("listAjax.do")
    @ResponseBody
    public List<BookVo> listAjax(HttpServletRequest request,
                                        @RequestParam(value = "kw", required = false) String kw,
                                        @RequestParam(value = "p", required = false, defaultValue = "1") Integer p,
                                         @RequestAttribute(value = "platform", required = false) Integer platform,
                                        ModelMap model) {
        List<BookVo> list =  bookService.sortList(kw, platform, p, 5, null);
        return list;
    }
}
