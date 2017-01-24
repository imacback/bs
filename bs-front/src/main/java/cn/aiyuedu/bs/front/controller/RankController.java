package cn.aiyuedu.bs.front.controller;

import cn.aiyuedu.bs.front.service.RankService;
import cn.aiyuedu.bs.front.vo.BookVo;
import cn.aiyuedu.bs.front.vo.RankVo;
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
@RequestMapping("/**/ft/rank/*")
public class RankController {
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    @Autowired
    RankService rankService;
    @Autowired
    BookService bookService;

    @RequestMapping("show.do")
    public ModelAndView list1(HttpServletRequest request,
                               @RequestAttribute(value = "platform", required = false) Integer platform,
                              ModelMap model) {
        List<RankVo> list = rankService.list(platform);
        model.put("list", list);
        return new ModelAndView("/pages/rank");
    }

    @RequestMapping("list.do")
    public ModelAndView list2(HttpServletRequest request,
                              @RequestParam(value = "id", required = true) String rankId,
                              @RequestParam(value = "p", required = false, defaultValue = "1") Integer pageNum,
                              @RequestParam(value = "name", required = false, defaultValue = "排行") String rankName,
                               @RequestAttribute(value = "platform", required = false) Integer platform,
                              ModelMap model) {
        List<BookVo> list = bookService.listByRank(rankId,platform,pageNum,5);
        model.put("list", list);
        model.put("rankName", rankName);
        model.put("rankId", rankId);
        return new ModelAndView("/pages/rank2");
    }

    @RequestMapping("listAjax.do")
    @ResponseBody
    public List<BookVo> listAjax(HttpServletRequest request,
                                 @RequestParam(value = "id", required = true) String rankId,
                                 @RequestParam(value = "p", required = false, defaultValue = "1") Integer pageNum,
                                  @RequestAttribute(value = "platform", required = false) Integer platform,
                                 ModelMap model) {
        return bookService.listByRank(rankId,platform,pageNum,5);
    }

}
