package cn.aiyuedu.bs.front.service.impl;

import cn.aiyuedu.bs.common.model.CategoryBase;
import cn.aiyuedu.bs.front.vo.TypeVo;
import cn.aiyuedu.bs.service.CategoryGeneralService;
import cn.aiyuedu.bs.front.service.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Description:
 *
 * @author Scott
 */
@Service("typeService")
public class TypeServiceImpl extends TypeService {
    @Autowired
    private CategoryGeneralService categoryGeneralService;

    public List<TypeVo> list1() {
        List<CategoryBase> list = categoryGeneralService.getCategoryBases();
        List<TypeVo> out = new ArrayList<>();
        for (CategoryBase t1 : list) {
            if (CollectionUtils.isEmpty(t1.getChildren())) {
                continue;
            }
            TypeVo vo1 = new TypeVo();
            vo1.setName(t1.getName());
            vo1.setId(t1.getId());
            vo1.setOrderId(t1.getOrderId());

            for (CategoryBase t2 : t1.getChildren()) {
                TypeVo vo2 = new TypeVo();
                vo2.setName(t2.getName());
                vo2.setId(t2.getId());
                vo2.setOrderId(t2.getOrderId());
                vo2.setRecommend(t2.getRecommend());
                vo2.setLogo(t2.getLogo());
                vo1.getList().add(vo2);
            }
            out.add(vo1);
        }
        Collections.sort(out);
        return out;
    }
}
