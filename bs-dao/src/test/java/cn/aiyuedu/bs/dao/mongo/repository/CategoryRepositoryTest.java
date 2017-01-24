package cn.aiyuedu.bs.dao.mongo.repository;

import cn.aiyuedu.bs.dao.entity.Category;
import cn.aiyuedu.bs.dao.BaseTest;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

/**
 * Description:
 *
 * @author yz.wu
 */
@Ignore
public class CategoryRepositoryTest extends BaseTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    public void testSave() {
        Category c = null;

        c = new Category();
        c.setName("男生原创小说");
        c.setBookCount(0);
        c.setCreateDate(new Date());
        c.setCreatorId(1);
        c.setEditDate(new Date());
        c.setEditorId(1);
        c.setIsLeaf(0);
        c.setIsUse(1);
        c.setParentId(0);
        c.setOrderId(1);
        categoryRepository.persist(c);

        c = new Category();
        c.setName("女生原创小说");
        c.setBookCount(0);
        c.setCreateDate(new Date());
        c.setCreatorId(1);
        c.setEditDate(new Date());
        c.setEditorId(1);
        c.setIsLeaf(0);
        c.setIsUse(1);
        c.setParentId(0);
        c.setOrderId(2);
        categoryRepository.persist(c);

        c = new Category();
        c.setName("热销出版图书");
        c.setBookCount(0);
        c.setCreateDate(new Date());
        c.setCreatorId(1);
        c.setEditDate(new Date());
        c.setEditorId(1);
        c.setIsLeaf(0);
        c.setIsUse(1);
        c.setParentId(0);
        c.setOrderId(3);
        categoryRepository.persist(c);
    }
}
