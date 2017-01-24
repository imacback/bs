package cn.aiyuedu.bs.back.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * Description:
 *
 * @author yz.wu
 */
@Controller
@RequestMapping("/home/*")
public class HomeController {

    @RequestMapping("index.do")
    public ModelAndView index() {
        return new ModelAndView("/home/index");
    }

    @RequestMapping("main.do")
    public ModelAndView main() {
        return new ModelAndView("/home/main");
    }

    @RequestMapping("menu.do")
    public ModelAndView menu() {
        return new ModelAndView("/home/menu");
    }

    @RequestMapping("welcome.do")
    public ModelAndView welcome() {
        return new ModelAndView("/home/welcome");
    }

    @RequestMapping("top.do")
    public ModelAndView top() {
        return new ModelAndView("/home/top");
    }

    @RequestMapping("left.do")
    public ModelAndView left() {
        return new ModelAndView("/home/left");
    }
}
