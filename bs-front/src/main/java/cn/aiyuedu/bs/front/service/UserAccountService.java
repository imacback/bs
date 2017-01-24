package cn.aiyuedu.bs.front.service;


import cn.aiyuedu.bs.common.Constants;
import cn.aiyuedu.bs.common.model.BookBase;
import cn.aiyuedu.bs.front.vo.PayTotalVo;
import cn.aiyuedu.bs.front.vo.RechargeVo;
import cn.aiyuedu.bs.service.BookGeneralService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Description:
 *
 * @author Scott
 */
@Service
public class UserAccountService {
    @Autowired
    RechargeRepository rechargeRepository;
    @Autowired
    PayTotalRepository payTotalRepository;
    @Autowired
    BookGeneralService bookGeneralService;
    @Autowired
    private Properties frontConfig;

    public List<RechargeBase> listRecharge(Integer uid, int pageNum, int pageSize) {
        int skip = (pageNum - 1) * pageSize;
        int limit = pageSize;
        return rechargeRepository.find(uid, null, Constants.RechStatus.serverDone.val(), skip, limit);
    }

    public List<RechargeVo> listRechargeVo(Integer uid, int pageNum, int pageSize) {
        String productName = frontConfig.getProperty("pay.productName");
        int skip = (pageNum - 1) * pageSize;
        int limit = pageSize;
        List<RechargeVo> out = new ArrayList<>();
        List<RechargeBase> list = rechargeRepository.find(uid, null, Constants.RechStatus.serverDone.val(), skip, limit);
        for (RechargeBase r : list) {
            RechargeVo vo = new RechargeVo(productName);
            BeanUtils.copyProperties(r,vo);
            out.add(vo);
        }
        return out;
    }

    public List<PayTotalVo> listPay(Integer uid, Integer payType, int pageNum, int pageSize) {

        String productName = frontConfig.getProperty("pay.productName");
        List<PayTotalVo> out = new ArrayList<>();
        int skip = (pageNum - 1) * pageSize;
        int limit = pageSize;
        List<PayTotal> list = payTotalRepository.find(uid, payType, skip, limit);
        for (PayTotal pt : list) {
            PayTotalVo vo = new PayTotalVo(productName);
            BookBase book = bookGeneralService.get(pt.getBookId());
            vo.setBookName(book.getName()); //书名
            vo.setCreateTime(pt.getCreateTime());//时间
            vo.setType(pt.getType());  //类型
            vo.setCost(pt.getCost());//金额
            vo.setEditTime(pt.getEditTime());
            out.add(vo);
        }
        return out;
    }
}
