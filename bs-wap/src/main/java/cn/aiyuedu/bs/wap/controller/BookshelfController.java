package cn.aiyuedu.bs.wap.controller;

import cn.aiyuedu.bs.wap.dto.ParamDto;
import cn.aiyuedu.bs.wap.service.BookshelfService;
import com.duoqu.commons.web.spring.RequestAttribute;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Description:
 *
 * @author yz.wu
 */
@Controller
@RequestMapping("/bookshelf/*")
public class BookshelfController {

    @Autowired
    private BookshelfService bookshelfService;

    @RequestMapping("show.do")
    public String show(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestAttribute(value = "param", required = true) ParamDto param,
            ModelMap model) {

        if (param != null && CollectionUtils.isNotEmpty(param.getBookshelfDtos())) {
            model.put("bookshelfDtos", bookshelfService.getBooks(param));
        }

        return "/page/bookshelf.html";
    }
}
