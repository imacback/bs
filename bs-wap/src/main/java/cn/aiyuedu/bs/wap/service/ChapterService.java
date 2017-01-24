package cn.aiyuedu.bs.wap.service;

import cn.aiyuedu.bs.common.dto.ChapterBaseDto;
import cn.aiyuedu.bs.common.model.ChapterBase;
import cn.aiyuedu.bs.service.BookGeneralService;
import cn.aiyuedu.bs.service.ChapterContentService;
import cn.aiyuedu.bs.service.ChapterGeneralService;
import cn.aiyuedu.bs.wap.dto.ChapterPageDto;
import cn.aiyuedu.bs.wap.util.StringUtil;
import com.google.common.collect.Maps;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Description:
 *
 * @author yz.wu
 */
@Service("chapterService")
public class ChapterService {

    Logger logger = LoggerFactory.getLogger(ChapterService.class);

    @Autowired
    ChapterContentService chapterContentService;
    @Autowired
    ChapterGeneralService chapterGeneralService;
    @Autowired
    BookGeneralService bookGeneralService;
    @Autowired
    PayService payService;

    /**
     * Description 获取指定书籍的章节信息(无内容)
     *
     * @return 返回不包含内容的章节信息
     * @author Wangpeitao
     */
    public ChapterPageDto<ChapterBase> getChapters(ChapterPageDto page) {
        Integer start = page.getStart();
        if (!page.getIsDesc()) {
            switch (start) {
                case 0:
                    start = 1;
                    break;
                case 1:
                    start = page.getSize() + 1;
                    break;
                default:
                    start = page.getStart() * page.getSize() + 1;
                    break;
            }
        } else {
            switch (start) {
                case 0:
                    start = page.getTotal();
                    break;
                case 1:
                    start = page.getTotal() - (1 * page.getSize());
                    break;
                default:
                    start = page.getTotal() - (page.getStart() * page.getSize());
                    break;
            }
        }
        List<ChapterBase> chapterBaseList = chapterGeneralService.getChapters(page.getBookId(), start, page.getSize(), page.getIsDesc());
        if (page.getIsDesc()) {
            page.setMaxChapter(start + page.getSize() - 1);
            page.setMinChapter(start);
        } else {
            page.setMinChapter(start + page.getSize() - 1);
            page.setMaxChapter(start);
        }
        page.setResult(chapterBaseList);
        if (page.getTotal() > 0) {
            page.setPages((page.getTotal() - 1) / page.getSize() + 1);
        }
        return page;
    }

    public Map<String, String> getBuyMap(ChapterPageDto<ChapterBase> page, Integer userId) {
        Map<String, String> map = Maps.newHashMap();
        if (page != null && CollectionUtils.isNotEmpty(page.getResult())) {
            for (ChapterBase c : page.getResult()) {
                if (c.getIsFee() == 1) {
                    boolean isBuy = payService.exist(c.getBookId(), c.getId(), userId);
                    map.put("c_" + c.getId(), isBuy? "1": "0");
                }
            }
        }

        return map;
    }

    public ChapterBaseDto getChaptersOne(Long bookId, Integer orderIds, Integer end) {
        List<ChapterBaseDto> list = chapterGeneralService.getChaptersWithContentByOrderId(bookId, orderIds, end, false, false);
        if (CollectionUtils.isNotEmpty(list)) {
            ChapterBaseDto dot = list.get(0);
            dot.setText(StringUtil.replaceAll(dot.getText()));
            return dot;
        }
        return null;
    }
}
