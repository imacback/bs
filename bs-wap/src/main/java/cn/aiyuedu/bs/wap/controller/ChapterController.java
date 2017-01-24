package cn.aiyuedu.bs.wap.controller;

import cn.aiyuedu.bs.common.Constants;
import cn.aiyuedu.bs.common.dto.ChapterBaseDto;
import cn.aiyuedu.bs.common.model.BookBase;
import cn.aiyuedu.bs.common.model.ChapterBase;
import cn.aiyuedu.bs.dao.entity.Chapter;
import cn.aiyuedu.bs.dao.entity.User;
import cn.aiyuedu.bs.service.ChapterContentService;
import cn.aiyuedu.bs.service.ChapterGeneralService;
import cn.aiyuedu.bs.wap.dto.ChapterPageDto;
import cn.aiyuedu.bs.wap.dto.ConsumeResultDto;
import cn.aiyuedu.bs.wap.dto.ParamDto;
import cn.aiyuedu.bs.wap.dto.PayResultDto;
import cn.aiyuedu.bs.wap.service.*;
import cn.aiyuedu.bs.wap.util.CookieUtil;
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
@RequestMapping("/chapter/*")
public class ChapterController {

    @Autowired
    private ChapterService chapterService;
    @Autowired
    private ChapterContentService chapterContentService;
    @Autowired
    private ChapterGeneralService chapterGeneralService;
    @Autowired
    private BookService bookService;
    @Autowired
    private CookieService cookieService;
    @Autowired
    private PayService payService;
    @Autowired
    private BookshelfService bookshelfService;

    /**
     * @param
     * @return
     */
    @RequestMapping(value = "/catalog.do")
    public String chapterList(ModelMap model, HttpServletRequest request,
                              @SessionAttribute(value = "user", required = true) User user,
                              @RequestAttribute(value = "param", required = true) ParamDto param,
                              @RequestParam(value = "isDesc", required = false, defaultValue = "false") boolean isDesc) {

        //将operation存入param,operation (操作标识符) 4）目录列表 #单本书的目录列表页
        param.setOperation(4);

        BookBase bookBase = bookService.get(param.getBookId());
        model.put("pvBook", bookService.getListByRecommend(param.getBookId(), 3));//pv
        if (bookBase == null || bookBase.getStatus() != Constants.BookStatus.Online.getId()) {
            return "error-go";
        }
        ChapterPageDto<ChapterBase> page = new ChapterPageDto();
        page.setTotal(bookBase.getPublishChapters());
        page.setSize(param.getPageSize());
        if (param.getStart() + param.getIsNext() <= 0) {
            page.setStart(0);
        } else {
            page.setStart(param.getStart() + param.getIsNext());
        }
        page.setIsDesc(isDesc);
        page.setBookId(param.getBookId());
        //将值放入model中
        model.put("book", bookBase);
        model.put("maxChaper", param.getMaxChapter());
        model.put("orderId", param.getOrderId());
        model.put("mark", param.getMark());
        model.put("isLight", param.getIsLight());
        model.put("font", param.getFontSize());
        model.put("style", param.getStyle());
        model.put("spacing", param.getSpacing());
        if (param.getMark() == 1) {
            page.setStart(0);
        }

        model.put("user", user);

        page = chapterService.getChapters(page);
        if (page != null) {
            model.put("page", page);
            model.put("buyMap", chapterService.getBuyMap(page, user.getId()));
            return "/page/catalog.html";
        }
        return "error-go.html";
    }

    /**
     * 书籍章节信息
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/read.do")
    public String chapter(HttpServletRequest request, ModelMap model,
                          HttpServletResponse response,
                          @SessionAttribute(value = "user", required = true) User user,
                          @RequestAttribute(value = "param", required = true) ParamDto param,
                          @RequestParam(value = "isNext", defaultValue = "0") Integer isNext,
                          @RequestParam(value = "token", defaultValue = "") String token,
                          @RequestParam(value = "novel_id", defaultValue = "") String novelId) {

        long bookId = param.getBookId();
        long chaperId = param.getBookId();
        //获取书籍信息
        BookBase bookBase = bookService.get(bookId);
        model.put("pvBook", bookService.getListByRecommend(bookId));//pv
        //下架的书籍提示，书籍以下架
        if (bookBase == null || bookBase.getStatus() != Constants.BookStatus.Online.getId()) {
            return "error-go";
        }
        int le = param.getOrderId() + param.getIsNext();
        if (le <= 0) {
            le = 0;
        }
        //获取chapter信息
        ChapterBaseDto dto = chapterService.getChaptersOne(bookId, le, le);
        ChapterBase chapter = dto.getChapterBase();

        boolean isBuy = false;
        boolean isFee = false;
        if (bookBase.getIsFee() == 1 && chapter.getIsFee() == 1) {
            isFee = true;
            if (user != null) {
                if (StringUtils.isNotBlank(user.getUserName())) {
                    isBuy = payService.exist(bookId, chapter.getId(), user.getId());
                }
            }
        }

        boolean isEnough = true;//isEnough:false 充值
        boolean isRegister = true;//是滞需要注册
        if (!isBuy) {
            if (isFee) {
                int words = chapter.getWords();
                int corn = RechargeService.getChapterFee(words);
                chapter.setPrice(corn);
                if (user == null || user.getVirtualCorn() == null ||
                        user.getVirtualCorn() < corn) {
                    isEnough = false;
                } else {
                    if (user == null || StringUtils.isBlank(user.getUserName())) {
                        isRegister = false;
                    }
                }
            }
        }

        model.put("isEnough", isEnough);
        model.put("isBuy", isBuy);
        model.put("isRegister", isRegister);
        model.put("isFee", isFee);

        if (chapter.getIsFee() == 0 || (isEnough && isRegister)) {
            bookshelfService.addBookshelf(bookBase, chapter, param, response);
        }

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

        //将isLight，fontSize，style，spacing值存入cookie
        if (StringUtils.isNotEmpty(style)) {
            cookieService.setStyle(response, style);
        }
        cookieService.setIsLight(response, isLight);
        cookieService.setFontSize(response, fontSize);
        cookieService.setSpacing(response, spacing);

        //将值放入model
        model.put("book", bookBase);
        model.put("chapter", dto.getChapterBase());

        model.put("maxChaper", bookBase.getPublishChapters());
        //将书籍阅读信息存入cookie
        CookieUtil.setCookie(request, response, bookId, le, "BK_");
        //将operation存入param,operation (操作标识符)1）阅读章节 #阅读页
        param.setOperation(1);

        model.put("isLight", isLight);
        model.put("font", fontSize);
        model.put("style1", CookieUtil.getCookinByName(request, "style1"));
        model.put("style", style);
        model.put("spacing", spacing);
        //记录百度阅读记录
        String cookieToken = CookieUtil.getCookinByName(request, "token");
        String cookieNovelId = CookieUtil.getCookinByName(request, "novelId");
        if (StringUtils.isNotEmpty(token) && StringUtils.isNotEmpty(novelId)) {
            if (token == cookieToken && novelId.toString() == cookieNovelId) {
                token = cookieToken;
                novelId = cookieNovelId;
            }
            CookieUtil.setCookie(request, response, "token", token);
            CookieUtil.setCookie(request, response, "novelId", novelId.toString());
        } else {
            if (cookieToken != "" && cookieNovelId != "") {
                token = cookieToken;
                novelId = cookieNovelId;
            }
        }
        if (token != "" && novelId != "") {
            String query = request.getQueryString();
            if (!query.contains("token")) {
                query = request.getQueryString() + "&token=" + token + "&novel_id=" + novelId;//如果url中没有token，添加token
            }
            if (StringUtils.isNotEmpty(query)) {
                query = "?" + query;
            } else {
                query = "";
            }
            String url = request.getRequestURL().toString() + query;
            model.put("uri", url.replaceAll("&", "%26"));
            model.put("token", token);
            model.put("novelId", novelId);
            model.put("chapterName", dto.getChapterBase().getName());
        }

        model.put("user", user);
        model.put("book", bookBase);
        model.put("chapter", chapter);

        return "/page/read.html";
    }

    @RequestMapping("fetch.do")
    @ResponseBody
    public ConsumeResultDto fetch(
            HttpServletRequest request,
            @SessionAttribute(value = "user", required = true) User user,
            @RequestAttribute(value = "param", required = true) ParamDto param) {

        long bookId = param.getBookId();
        long chapterId = param.getChapterId();
        //获取书籍信息
        BookBase bookBase = bookService.get(bookId);
        Chapter chapter = chapterGeneralService.get(chapterId);

        boolean isBuy = false;
        boolean isFee = false;
        if (bookBase.getIsFee() == 1 && chapter.getIsFee() == 1) {
            isFee = true;
            if (user.getId() != null) {
                isBuy = payService.exist(bookId, chapter.getId(), user.getId());
            }
        }

        boolean isRegister = false;//是滞需要注册
        if (user != null && StringUtils.isNotBlank(user.getUserName())) {
            isRegister = true;
        }

        if (isFee) {
            int words = chapter.getWords();
            int corn = RechargeService.getChapterFee(words);
            chapter.setPrice(corn);
        }

        ConsumeResultDto result = new ConsumeResultDto();

        if ((isRegister && isBuy) || !isFee) {
            String content = chapterContentService.getChapterContent(bookId, chapterId);
            //result.setContent(content);
            result.setContent(StringUtil.getContent(content));
            result.setResult("success");
        } else if (!isBuy) {
            result.setResult("consume");
        } else if (!isRegister) {
            result.setResult("login");
        }

        return result;
    }
}
