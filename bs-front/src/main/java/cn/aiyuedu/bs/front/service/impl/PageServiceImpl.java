package cn.aiyuedu.bs.front.service.impl;

import cn.aiyuedu.bs.common.model.*;
import cn.aiyuedu.bs.cache.service.BookCacheService;
import cn.aiyuedu.bs.front.service.PageService;
import cn.aiyuedu.bs.front.vo.component.BookChannelVo;
import cn.aiyuedu.bs.front.vo.component.ComponentBaseVo;
import cn.aiyuedu.bs.front.vo.component.NavVo;
import cn.aiyuedu.bs.front.service.BookService;
import cn.aiyuedu.bs.front.vo.BookVo;
import cn.aiyuedu.bs.front.vo.component.BookBigVo;
import cn.aiyuedu.bs.front.vo.component.data.DataBaseVo;
import cn.aiyuedu.bs.front.vo.page.PageBaseVo;
import cn.aiyuedu.bs.service.ContainerGeneralService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import static cn.aiyuedu.bs.common.Constants.CompType;


/**
 * Description:
 *
 * @author Scott
 */
@Service("pageService")
public class PageServiceImpl extends PageService {
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    //    @Autowired
//    private ContainerCacheService containerCacheService;
    @Autowired
    private BookCacheService bookCacheService;
    @Autowired
    private BookService bookService;
    @Autowired
    private ContainerGeneralService containerGeneralService;

    @Override
    public PageBaseVo getPage(String pageId) {
        ContainerBase ctn = containerGeneralService.get(Integer.valueOf(pageId));
        return getPage(ctn);
    }

    public PageBaseVo getPage(ContainerBase ctn) {
        log.info("build page: [" + ctn.getId() + "]=" + (ctn.getComponents() != null ? ctn.getComponents().size() : 0));
        PageBaseVo page = new PageBaseVo();
        List<ComponentBaseVo> compList = new ArrayList<>();
        int i = 0;
        for (ComponentBase c : ctn.getComponents()) {
            log.info("build page: [" + c.getId() + "]=" + c.getName());
            ComponentBaseVo cv = makeCompVo(c);
            if (cv == null)
                continue;
            cv.setIndex(i++);
            compList.add(cv);
        }
        page.setCompList(compList);
        page.setName(ctn.getName());
        page.setId(ctn.getId());
//        System.out.println(JSON.toJSONString(ctn));
//        log.info("build page: [" + ctn.getId() + "]=" + (ctn.getComponents() != null ? ctn.getComponents().size() : 0));
        return page;
    }


    /**
     * 根据组件类型，创建VO对象
     */
    private ComponentBaseVo makeCompVo(ComponentBase c) {
        try {
            if (CompType.adGroup.val().equals(c.getTypeId())) {
                return makeCommonComp(c, "../models/ad_group.html");
            } else if (CompType.navIcon.val().equals(c.getTypeId())) {
                return makeNav(c, "../models/nav_icon.html");
            } else if (CompType.navText.val().equals(c.getTypeId())) {
                return makeNav(c, "../models/nav_txt.html");
            } else if (CompType.books.val().equals(c.getTypeId())) {
                return makeBooks(c);
            } else if (CompType.channelSmall.val().equals(c.getTypeId())) {
                return makeBookChannel(c, "../models/channel_small.html");
            } else if (CompType.channelBig.val().equals(c.getTypeId())) {
                return makeBookChannel(c, "../models/channel_big.html");
            } else if (CompType.adBanner.val().equals(c.getTypeId())) {
                return makeCommonComp(c, "../models/ad_banner.html");
            } else if (CompType.adDouble.val().equals(c.getTypeId())) {
                return makeCommonComp(c, "../models/ad_double.html");
            } else if (CompType.types.val().equals(c.getTypeId())) {
                return makeCommonComp(c, "../models/types.html");
            } else if (CompType.search.val().equals(c.getTypeId())) {
                return makeCommonComp(c, "../models/search.html");
            } else if (CompType.links.val().equals(c.getTypeId())) {
                return makeCommonComp(c, "../models/links.html");
            } else if (CompType.rank.val().equals(c.getTypeId())) {
                return makeCommonComp(c, "../models/rank.html");
            }
        } catch (Throwable e) {
            log.error("MakeCompVoException：" + c.getId() + "_" + c.getTypeId() + "_" + c.getName(), e);
        }
        return null;
    }


    private NavVo makeNav(ComponentBase c, String templateName) throws UnsupportedEncodingException {
        NavVo vo = new NavVo();
        vo.setTemplate(templateName);
        vo.setName(c.getTitle());
        vo.setStyleColor(c.getFontColor());
        vo.setId(c.getId());
        for (ComponentDataGroupBase gg : c.getGroups()) {
            ComponentBaseVo groupVo = new ComponentBaseVo();
            for (ComponentDataBase d : gg.getList()) {
                groupVo.getList().add(
                        new DataBaseVo(d.getDataType(), d.getId() + "", d.getLogo(), d.getTitle(), d.getData(), d.getMemo())
                );
            }
            vo.getGroup().add(groupVo);
        }
        return vo;
    }

    private ComponentBaseVo makeCommonComp(ComponentBase c, String templateName) throws UnsupportedEncodingException {
        ComponentBaseVo vo = new ComponentBaseVo();
        vo.setTemplate(templateName);
        vo.setName(c.getTitle());
        vo.setLogo(c.getIcon());
        vo.setStyleColor(c.getFontColor());
        vo.setId(c.getId());
        ComponentDataBase e = c.getEntry();
        if (e != null) {
            DataBaseVo entryVo = new DataBaseVo(e.getDataType(), e.getId() + "", e.getLogo(), e.getTitle(), e.getData(), e.getMemo());
            vo.setEntryName(entryVo.getName());
            vo.setUrl(entryVo.getUrl());
        }
        for (ComponentDataBase d : c.getData()) {
            vo.getList().add(new DataBaseVo(d.getDataType(), d.getData(), d.getLogo(), d.getTitle(), d.getData(), d.getMemo()));
        }
        return vo;
    }

    private ComponentBaseVo makeBooks(ComponentBase c) {
        BookBigVo bookBig = new BookBigVo();
        bookBig.setTemplate("../models/books.html");
        bookBig.setName(c.getTitle());
        bookBig.setStyleColor(c.getFontColor());
        bookBig.setLogo(c.getIcon());
        bookBig.setId(c.getId());
        for (ComponentDataBase d : c.getData()) {
            BookBase book = bookCacheService.get(Long.valueOf(d.getData()));
            BookVo vo = bookService.makeBookVo(book);
            if (StringUtils.hasText(d.getLogo())) {
                vo.setSmallPic(d.getLogo());
                vo.setLargePic(d.getLogo());
            }
            if (StringUtils.hasText(d.getTitle())) {
                vo.setName(d.getTitle());
            }
            bookBig.getBookList().add(vo);
        }
        return bookBig;
    }

    private BookChannelVo makeBookChannel(ComponentBase c, String tName) throws UnsupportedEncodingException {
        BookChannelVo channelSmallVo = new BookChannelVo();
        channelSmallVo.setTemplate(tName);
        channelSmallVo.setName(c.getTitle());
        channelSmallVo.setStyleColor(c.getFontColor());
        channelSmallVo.setLogo(c.getIcon());
        ComponentDataBase e = c.getEntry();
        DataBaseVo entryVo = new DataBaseVo(e.getDataType(), e.getId() + "", e.getLogo(), e.getTitle(), e.getData(), e.getMemo());
        channelSmallVo.setEntryName(entryVo.getName());
        channelSmallVo.setUrl(entryVo.getUrl());
        channelSmallVo.setId(c.getId());
        for (ComponentDataBase d : c.getData()) {
            BookBase book = bookCacheService.get(Long.valueOf(d.getData()));
            BookVo vo = bookService.makeBookVo(book);
            if (StringUtils.hasText(d.getLogo())) {
                vo.setSmallPic(d.getLogo());
                vo.setLargePic(d.getLogo());
            }
            if (StringUtils.hasText(d.getTitle())) {
                vo.setName(d.getTitle());
            }
            channelSmallVo.getBookList().add(vo);
        }
        return channelSmallVo;
    }

}
