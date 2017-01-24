package cn.aiyuedu.bs.wap.service;

import cn.aiyuedu.bs.common.Constants;
import cn.aiyuedu.bs.common.model.BookBase;
import cn.aiyuedu.bs.common.model.ChapterBase;
import cn.aiyuedu.bs.common.orm.Page;
import cn.aiyuedu.bs.dao.dto.PayDetailDto;
import cn.aiyuedu.bs.dao.dto.PayDetailQueryDto;
import cn.aiyuedu.bs.dao.dto.RechargeQueryDto;
import cn.aiyuedu.bs.dao.entity.Chapter;
import cn.aiyuedu.bs.dao.entity.PayDetail;
import cn.aiyuedu.bs.dao.entity.Recharge;
import cn.aiyuedu.bs.dao.entity.User;
import cn.aiyuedu.bs.dao.mongo.repository.PayDetailRepository;
import cn.aiyuedu.bs.service.BookGeneralService;
import cn.aiyuedu.bs.service.ChapterGeneralService;
import cn.aiyuedu.bs.wap.dto.ParamDto;
import cn.aiyuedu.bs.wap.dto.PayResultDto;
import com.google.common.collect.Lists;
import jodd.bean.BeanCopy;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * Description:
 *
 * @author yz.wu
 */
@Service("payService")
public class PayService {

    @Autowired
    private PayDetailRepository payDetailRepository;
    @Autowired
    private ChapterGeneralService chapterGeneralService;
    @Autowired
    private UserService userService;
    @Autowired
    private BookGeneralService bookGeneralService;

    public boolean exist(Long bookId, Long chapterId, Integer userId) {
        return payDetailRepository.exist(bookId, chapterId, userId);
    }

    public PayResultDto buyChapter(HttpServletRequest request, User user, ParamDto param) {
        boolean exist = exist(param.getBookId(), param.getChapterId(), user.getId());
        if (exist) {
            return new PayResultDto(user, null, PayResultDto.BUY_RESULT_DUPLICATE);
        }
        //余额不足判断
        Chapter chapter = chapterGeneralService.get(param.getChapterId());
        user = userService.findOne(user.getId(), null);
        int price = RechargeService.getChapterFee(chapter.getFilteredWords());
        if (user.getVirtualCorn() < price) {
            return new PayResultDto(user, price, PayResultDto.BUY_RESULT_NO_MONEY);
        }
        //添加购买记录 书籍，章节，用户 购买类型
        PayDetail payDetail = new PayDetail();
        payDetail.setCost(chapter.getPrice());////???????该价格为标记价格 非成交价格 是否为Bug？？？？ZhengQian 20150908
        payDetail.setUid(user.getId());
        payDetail.setType(Constants.PayType.chapter.val());
        payDetail.setBookId(param.getBookId());
        payDetail.setChapterId(param.getChapterId());
        payDetail.setChannelId(param.getDistributeId());
        payDetail.setCreateTime(new Date());

        payDetailRepository.persist(payDetail);
        user = userService.update(user.getId(), user.getUid(), 0-price, null);
        request.getSession().setAttribute("user", user);

        return new PayResultDto(user, chapter.getPrice(), PayResultDto.BUY_RESULT_SUCCESS);

    }

    public PayResultDto buyChapters(HttpServletRequest request, User user, Long bookId, Long chapterId) {
        boolean exist = exist(bookId, chapterId, user.getId());
        if (exist) {
            return new PayResultDto(user, null, PayResultDto.BUY_RESULT_DUPLICATE);
        }
        //余额不足判断
        Chapter chapter = chapterGeneralService.get(chapterId);
        user = userService.findOne(user.getId(), null);
        int price = RechargeService.getChapterFee(chapter.getFilteredWords());
        if (user.getVirtualCorn() < price) {
            return new PayResultDto(user, price, PayResultDto.BUY_RESULT_NO_MONEY);
        }
        //添加购买记录 书籍，章节，用户 购买类型
        PayDetail payDetail = new PayDetail();
        payDetail.setCost(price);
        payDetail.setUid(user.getId());
        payDetail.setType(Constants.PayType.chapter.val());
        payDetail.setBookId(bookId);
        payDetail.setChapterId(chapterId);
        payDetail.setCreateTime(new Date());

        payDetailRepository.persist(payDetail);
        user = userService.update(user.getId(), user.getUid(), 0-price, null);

        PayResultDto payResultDto = new PayResultDto(user, price, PayResultDto.BUY_RESULT_SUCCESS);

        int startOrderId = chapter.getOrderId();
        int maxOrderId = chapterGeneralService.getMaxChapterOrderId(bookId);
        if (startOrderId + 1 < maxOrderId) {
            List<ChapterBase> chapters = chapterGeneralService.getChaptersByOrderId(bookId, startOrderId+1, maxOrderId, false);
            if (CollectionUtils.isNotEmpty(chapters)) {
                for (ChapterBase c : chapters) {
                    exist = exist(bookId, c.getId(), user.getId());
                    if (exist) {
                        continue;
                    }

                    price = RechargeService.getChapterFee(c.getWords());
                    if (user.getVirtualCorn() < price) {
                        break;
                    }

                    payDetail = new PayDetail();
                    payDetail.setCost(price);
                    payDetail.setUid(user.getId());
                    payDetail.setType(Constants.PayType.chapter.val());
                    payDetail.setBookId(bookId);
                    payDetail.setChapterId(c.getId());
                    payDetail.setCreateTime(new Date());

                    payDetailRepository.persist(payDetail);
                    user = userService.update(user.getId(), user.getUid(), 0 - price, null);
                }
            }
        }

        request.getSession().setAttribute("user", user);

        return payResultDto;
    }

    public Page<PayDetailDto> getList(Integer userId, Integer start, Integer limit) {
        PayDetailQueryDto queryDto = new PayDetailQueryDto();
        queryDto.setUserId(userId);
        queryDto.setLimit(limit);
        queryDto.setStart(start);

        Page<PayDetail> page = payDetailRepository.getPage(queryDto);
        Page<PayDetailDto> p = new Page<>();

        p.setTotalItems(page.getTotalItems());
        p.setPageSize(limit);

        PayDetailDto dto;
        BookBase b;
        ChapterBase c;
        for (PayDetail o : page.getResult()) {
            dto = new PayDetailDto();
            BeanCopy.beans(o, dto).copy();

            b = bookGeneralService.get(o.getBookId());
            if (b != null) {
                dto.setBookName(b.getName());
            }
            c = chapterGeneralService.get(o.getChapterId());
            if (c != null) {
                dto.setChapterName(c.getName());
                dto.setCost(RechargeService.getChapterFee(c.getWords()));
                dto.setOrderId(c.getOrderId());
            }

            if (CollectionUtils.isEmpty(p.getResult())) {
                p.setResult(Lists.newArrayList());
            }
            p.getResult().add(dto);
        }

        return p;
    }
}
