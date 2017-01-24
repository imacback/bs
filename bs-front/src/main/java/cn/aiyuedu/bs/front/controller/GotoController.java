package cn.aiyuedu.bs.front.controller;

import cn.aiyuedu.bs.front.exception.IllegalUserException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * 广告跳转
 *
 * @author Scott
 */
@Controller
@RequestMapping("/**/ft/*")
public class GotoController {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @RequestMapping("go.do")
    public ModelAndView adv(HttpServletRequest request,
                             @RequestParam(value = "link", required = true) String link,
                             ModelMap model) throws IllegalUserException {
        //
        return new ModelAndView("redirect:" + link);
    }
}
