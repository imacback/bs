package cn.aiyuedu.bs.front.service.impl;

import cn.aiyuedu.bs.common.model.BookBase;
import cn.aiyuedu.bs.common.orm.Page;
import cn.aiyuedu.bs.front.service.RankService;
import cn.aiyuedu.bs.front.service.BookService;
import cn.aiyuedu.bs.front.vo.RankVo;
import cn.aiyuedu.bs.service.BookGeneralService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Description:
 *
 * @author Scott
 */
@Service("rankService")
public class RankServiceImpl extends RankService {
    @Autowired
    RankingGeneralService rankingGeneralService;
    @Autowired
    BookGeneralService bookGeneralService;
    @Autowired
    BookService bookService;

    @Override
    public List<RankVo> list(Integer platform) {
        List<RankVo> out = new ArrayList<>();
        List<? extends RankingBase> list = rankingGeneralService.getRankingBases();
        for (RankingBase rankingBase : list) {
            Page<BookBase> page = bookGeneralService.getRankingList(rankingBase.getId(),platform, 1, 6);
            if (CollectionUtils.isEmpty(page.getResult())) {
                continue;
            }
            RankVo vo = new RankVo();
            for (BookBase bookBase : page.getResult()) {
                vo.getList().add(bookService.makeBookVo(bookBase));
            }

            vo.setName(rankingBase.getName());
            vo.setId(rankingBase.getId() + "");
            out.add(vo);
        }
        return out;
    }

}
