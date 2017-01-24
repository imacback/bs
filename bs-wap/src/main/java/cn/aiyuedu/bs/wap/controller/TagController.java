package cn.aiyuedu.bs.wap.controller;

import cn.aiyuedu.bs.common.model.BookBase;
import cn.aiyuedu.bs.dao.entity.User;
import cn.aiyuedu.bs.wap.service.BookService;
import com.duoqu.commons.web.spring.SessionAttribute;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * Description:
 *
 * @author yz.wu
 */
@Controller
@RequestMapping("/tag/*")
public class TagController {

    @Autowired
    private BookService bookService;

    @RequestMapping("books.do")
    public ModelAndView getBooks(
            @RequestParam(value = "id", required = true) Integer tag,
            @RequestParam(value = "p", required = false, defaultValue = "1") Integer pageNum,
            @RequestParam(value = "name", required = false, defaultValue = "标签") String tagName,
            @SessionAttribute(value = "user", required = true) User user,
            ModelMap model) {

        List<BookBase> books = bookService.getListByTag(tag, pageNum, 5);
        model.put("books", books);
        model.put("tagId", tag);
        model.put("tagName",tagName);
        model.put("user",user);
        return new ModelAndView("/page/tagBooks.html");
    }

    @RequestMapping("booksAjax.do")
    @ResponseBody
    public List<BookBase> getBooksByAjax(
            @RequestParam(value = "id", required = true) Integer tag,
            @RequestParam(value = "p", required = false, defaultValue = "1") Integer pageNum) {

        return bookService.getListByTag(tag, pageNum, 5);
    }
}
