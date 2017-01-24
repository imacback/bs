package cn.aiyuedu.bs.wap.service;

import cn.aiyuedu.bs.common.Constants;
import cn.aiyuedu.bs.common.model.BookBase;
import cn.aiyuedu.bs.common.model.ChapterBase;
import cn.aiyuedu.bs.dao.entity.Chapter;
import cn.aiyuedu.bs.service.BookGeneralService;
import cn.aiyuedu.bs.wap.dto.BookshelfDto;
import cn.aiyuedu.bs.wap.dto.ParamDto;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Description:
 *
 * @author yz.wu
 */
@Service("bookshelfService")
public class BookshelfService {

    @Autowired
    private BookGeneralService bookGeneralService;
    @Autowired
    private CookieService cookieService;

    public List<BookshelfDto> getBooks(ParamDto paramDto) {
        List<BookshelfDto> result = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(paramDto.getBookshelfDtos())) {
            BookBase b;
            BookshelfDto s;
            for (int size = paramDto.getBookshelfDtos().size(), i = size - 1; i >= 0; i--) {
                s = paramDto.getBookshelfDtos().get(i);
                b = bookGeneralService.get(s.getBookId());
                if (b != null && b.getStatus() == Constants.BookStatus.Online.getId()) {
                    s.setBookName(b.getName());
                    s.setLargePic(b.getLargePic());

                    result.add(s);
                }
            }
        }

        return result;
    }

    public void addBookshelf(BookBase bookBase, ChapterBase chapter, ParamDto paramDto, HttpServletResponse response) {
        BookshelfDto s = new BookshelfDto();
        s.setBookId(bookBase.getId());
        s.setChapterId(chapter.getId());
        s.setOrderId(chapter.getOrderId());

        List<BookshelfDto> list = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(paramDto.getBookshelfDtos())) {
            for (BookshelfDto b : paramDto.getBookshelfDtos()) {
                if (b.getBookId().intValue() != bookBase.getId().intValue()) {
                    list.add(b);
                }
            }
            if (list.size() == 10) {
                list.remove(0);
            }
        }

        list.add(s);

        cookieService.setBookShelf(response, JSON.toJSONString(list));
    }

    public int getBookHistory(Long bookId, ParamDto param) {
        if (bookId != null && param != null && CollectionUtils.isNotEmpty(param.getBookshelfDtos())) {
            for (BookshelfDto b : param.getBookshelfDtos()) {
                if (StringUtils.equals(b.getBookId().toString(), bookId.toString())) {
                    return b.getOrderId();
                }
            }
        }

        return 1;
    }
}
