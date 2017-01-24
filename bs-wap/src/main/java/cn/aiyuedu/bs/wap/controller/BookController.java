package cn.aiyuedu.bs.wap.controller;

import cn.aiyuedu.bs.common.model.BookBase;
import cn.aiyuedu.bs.dao.entity.User;
import cn.aiyuedu.bs.service.BookGeneralService;
import cn.aiyuedu.bs.wap.dto.ParamDto;
import cn.aiyuedu.bs.wap.service.BookService;
import cn.aiyuedu.bs.wap.service.BookshelfService;
import cn.aiyuedu.bs.wap.util.CookieUtil;
import com.duoqu.commons.web.spring.RequestAttribute;
import com.duoqu.commons.web.spring.SessionAttribute;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Description:
 *
 * @author yz.wu
 */
@Controller
@RequestMapping("/book/*")
public class BookController {

    @Autowired
    private BookService bookService;
    @Autowired
    private BookGeneralService bookGeneralService;
    @Autowired
    private BookshelfService bookshelfService;

    @RequestMapping("show.do")
    public String detail(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestAttribute(value = "param", required = true) ParamDto param,
            @SessionAttribute(value = "user", required = true) User user,
            @RequestParam(value = "bid", required = true) Long bookId,
            ModelMap model) {

        //从parm中取值
        Integer isLight = param.getIsLight();
        String fontSize = param.getFontSize();
        String style = param.getStyle();
        String spacing = param.getSpacing();
        if (style.equals("night—box")) {
            isLight = 1;
            style = "";
        }
        if (isLight == 1) {
            style = "";
        }

        model.put("isLight", isLight);
        model.put("font", fontSize);
        model.put("style1", CookieUtil.getCookinByName(request, "style1"));
        model.put("style", style);
        model.put("spacing", spacing);

        model.put("orderId", bookshelfService.getBookHistory(bookId, param));

        model.put("user", user);

        BookBase book = bookService.get(bookId);
        List<BookBase> recommendBooks = bookService.getListByRecommend(bookId);
        model.put("recommendBooks", recommendBooks);
        model.put("book", bookService.getBookDto(book));
        model.put("tagList", bookGeneralService.getBookContentTags(book));

        return "/page/book.html";
    }

    @RequestMapping(value = "setup.do")
    public String setup(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestAttribute(value = "param", required = true) ParamDto param,
            ModelMap model) {

        //将operation存入param,operation (操作标识符) 2）阅读页设置 #设置
        param.setOperation(2);
        String style = CookieUtil.getCookinByName(request, "style1");
        if (param.getIsLight() == 1 ){
            style="";
        }
        Integer isLight = param.getIsLight();
        model.put("isLight", isLight);
        //取cookie中的样式相关信息
        model.put("color", style);
        model.put("font", CookieUtil.getCookinByName(request, "fontSize1"));
        model.put("spacing", CookieUtil.getCookinByName(request, "spacing1"));
        model.put("url", "/chapter/read.do?bid=" + param.getBookId() + "&orderId=" + param.getOrderId() + "&isNext=0&maxChaper=" + param.getMaxChapter() + "&isLight=0");
        return "/page/setup.html";
    }
}
