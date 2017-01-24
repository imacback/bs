package cn.aiyuedu.bs.front.controller;

import cn.aiyuedu.bs.front.vo.BookVo;
import com.duoqu.commons.web.spring.RequestAttribute;
import cn.aiyuedu.bs.cache.service.BookCacheService;
import cn.aiyuedu.bs.front.service.BookService;
import cn.aiyuedu.bs.service.BookGeneralService;
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
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Description:
 *
 * @author Scott
 */
@Controller
@RequestMapping("/**/ft/book/*")
public class BookController {
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    @Autowired
    BookService bookService;
    @Autowired
    BookGeneralService bookGeneralService;
    @Autowired
    BookCacheService bookCacheService;

    @RequestMapping("detail.do")
    public ModelAndView detail(HttpServletRequest request,
                               HttpServletResponse response,
                               @RequestParam(value = "id", required = true) Long bookId,
                               @RequestParam(value = "showTab", required = false, defaultValue = "1") Integer showTab,
                               @RequestAttribute(value = "platform", required = false) Integer platform,
                               ModelMap model) {
        BookVo book = bookService.get(bookId, platform);
        List<BookVo> rcmdList = bookService.rcmdList(bookId, platform);
        model.put("rcmdList", rcmdList);
        model.put("book", book);
        model.put("tagList", bookGeneralService.getBookContentTags(book.getId()));

        model.put("showTab", showTab);
        try {
            bookCacheService.addPageView(bookId);
        } catch (Throwable e) {
        }
        return new ModelAndView("/pages/book");
    }

    @RequestMapping("read.do")
    @ResponseBody
    public Object read(HttpServletRequest request,
                       @RequestParam(value = "id", required = false) Integer bookId,
                       ModelMap model) {
        return null;
    }
}
