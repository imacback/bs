package cn.aiyuedu.bs.wap.controller;

import cn.aiyuedu.bs.common.orm.Page;
import cn.aiyuedu.bs.dao.dto.PayDetailDto;
import cn.aiyuedu.bs.dao.entity.User;
import cn.aiyuedu.bs.service.ChapterContentService;
import cn.aiyuedu.bs.wap.dto.ConsumeResultDto;
import cn.aiyuedu.bs.wap.dto.ParamDto;
import cn.aiyuedu.bs.wap.dto.PayResultDto;
import cn.aiyuedu.bs.wap.service.PayService;
import cn.aiyuedu.bs.wap.util.StringUtil;
import com.duoqu.commons.web.spring.RequestAttribute;
import com.duoqu.commons.web.spring.SessionAttribute;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Description:
 *
 * @author yz.wu
 */
@Controller
@RequestMapping("/pay/*")
public class PayController {

    public static final int PAGE_SIZE = 20;

    @Autowired
    private ChapterContentService chapterContentService;
    @Autowired
    private PayService payService;

    @RequestMapping("consume.do")
    @ResponseBody
    public ConsumeResultDto consume(
            HttpServletRequest request,
            @SessionAttribute(value = "user", required = true) User user,
            @RequestAttribute(value = "param", required = true) ParamDto param) {

        ConsumeResultDto result = new ConsumeResultDto();

        if (user == null || user.getVirtualCorn() == null || user.getVirtualCorn() == 0) {
            result.setResult("coin");
        } else {
            if (user.getId() != null) {
                PayResultDto payResultDto = payService.buyChapter(request, user, param);
                if (payResultDto.getStatus() == 1) {
                    String content = chapterContentService.getChapterContent(param.getBookId(), param.getChapterId());

                    result.setContent(StringUtil.getContent(content));

                    result.setResult("success");
                } else if (payResultDto.getStatus() == 2) {
                    result.setResult("coin");
                }
            } else {
                result.setResult("login");
            }
        }

        return result;
    }

    @RequestMapping("batchConsume.do")
    @ResponseBody
    public ConsumeResultDto batchConsume(
            HttpServletRequest request,
            @SessionAttribute(value = "user", required = true) User user,
            @RequestParam(value = "bid", required = false) Long bid,
            @RequestParam(value = "cid", required = false) Long cid) {

        ConsumeResultDto result = new ConsumeResultDto();

        if (user == null || user.getVirtualCorn() == null || user.getVirtualCorn() == 0) {
            result.setResult("coin");
        } else {
            if (user.getId() != null) {
                PayResultDto payResultDto = payService.buyChapters(request, user, bid, cid);
                if (payResultDto.getStatus() == 1) {
                    String content = chapterContentService.getChapterContent(bid, cid);

                    result.setContent(StringUtil.getContent(content));

                    result.setResult("success");
                } else if (payResultDto.getStatus() == 2) {
                    result.setResult("coin");
                }
            } else {
                result.setResult("login");
            }
        }

        return result;
    }

    @RequestMapping("list.do")
    public String list(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestAttribute(value = "param", required = true) ParamDto param,
            @SessionAttribute(value = "user", required = true) User user,
            @RequestParam(value = "pageNo", required = false, defaultValue = "1") Integer pageNo,
            ModelMap model) {
        Page<PayDetailDto> page = payService.getList(user.getId(), (pageNo-1)*PAGE_SIZE, PAGE_SIZE);
        model.put("page", page);
        model.put("param", param);
        if (user != null) {
            model.put("user", user);
        }
        return "/page/payList.html";
    }
}
