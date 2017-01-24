package cn.aiyuedu.bs.wap.controller;

import cn.aiyuedu.bs.common.model.BookBase;
import cn.aiyuedu.bs.wap.service.BookService;
import cn.aiyuedu.bs.wap.service.CategoryService;
import com.duoqu.commons.web.spring.RequestAttribute;
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
 * @author yz.wu
 */
@Controller
@RequestMapping("/category/*")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private BookService bookService;

    @RequestMapping("list.do")
    public String getList(
            HttpServletRequest request,
            ModelMap model) {
        model.put("categoryList", categoryService.getList());

        return "/page/category.html";
    }

    @RequestMapping("books.do")
    public String getBooks(
            HttpServletRequest request,
            @RequestParam(value = "id", required = true) Integer id,
            @RequestParam(value = "p", required = false, defaultValue = "1") Integer pageNum,
            @RequestParam(value = "name", required = false, defaultValue = "分类") String categoryName,
            @RequestAttribute(value = "platform", required = false) Integer platform,
            ModelMap model) {
        List<BookBase> list = bookService.getListByCategory(id, pageNum, 5);
        model.put("list", list);
        model.put("categoryId", id);
        model.put("categoryName", categoryName);

        return "/page/categoryBooks.html";
    }

    @RequestMapping("booksAjax.do")
    @ResponseBody
    public List<BookBase> getBooksAjax(
            HttpServletRequest request,
            @RequestParam(value = "id", required = true) Integer id,
            @RequestParam(value = "p", required = false, defaultValue = "1") Integer pageNum,
            @RequestAttribute(value = "platform", required = false) Integer platform) {
        return bookService.getListByCategory(id, pageNum, 5);
    }
}
