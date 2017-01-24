package cn.aiyuedu.bs.cache.service;

import cn.aiyuedu.bs.common.model.CategoryBase;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by webwyz on 14/10/22.
 */
@Ignore
public class CategoryCacheServiceTest extends BaseTest {

    @Autowired
    private CategoryCacheService categoryCacheService;

    @Test
    public void test() {
        List<CategoryBase> list = categoryCacheService.getAll();
        if (CollectionUtils.isNotEmpty(list)) {
            for (CategoryBase c : list) {
                System.out.println(c.getName()+", orderId:"+c.getOrderId());
                if (CollectionUtils.isNotEmpty(c.getChildren())) {
                    for (CategoryBase cc : c.getChildren()) {
                        System.out.println("-----"+cc.getName()+", orderId:"+cc.getOrderId());
                    }
                }
            }
        }
    }
}
