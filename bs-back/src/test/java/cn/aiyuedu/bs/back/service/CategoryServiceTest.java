package cn.aiyuedu.bs.back.service;

import cn.aiyuedu.bs.back.BaseTest;
import cn.aiyuedu.bs.common.Constants;
import cn.aiyuedu.bs.cache.service.CategoryCacheService;
import cn.aiyuedu.bs.common.model.CategoryBase;
import com.google.common.base.Joiner;
import com.google.common.primitives.Ints;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Description:
 *
 * @author yz.wu
 */
@Ignore
public class CategoryServiceTest extends BaseTest {

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private CategoryCacheService categoryCacheService;

    @Test
    public void testAnalyseBookCategory() {
        Integer tagClassifyId = 103;//原创-小说
        Integer tagSexId = 105;//男频
        List<Integer> tagContentIds = Ints.asList(109,115,165);
        List<Integer> tagSupplyIds = Ints.asList(173,196,214,242,243,258);

        List<Integer> ids = categoryService.analyseBookCategory(
                tagClassifyId,tagSexId,
                tagContentIds, tagSupplyIds);

        if (CollectionUtils.isNotEmpty(ids)) {
            System.out.println(Joiner.on(Constants.SEPARATOR).join(ids));
        } else {
            System.out.println("----null----");
        }
    }

    @Test
    public void testPublish() {
        categoryService.publish();

        List<CategoryBase> list = categoryCacheService.getAll();
        if (CollectionUtils.isNotEmpty(list)) {
            for (CategoryBase p : list) {
                System.out.println(p.getName()+","+p.getId());
                if (CollectionUtils.isNotEmpty(p.getChildren())) {
                    for (CategoryBase c : p.getChildren()) {
                        System.out.println("---- "+c.getName()+","+c.getId());
                    }
                }
            }
        }
    }
}
