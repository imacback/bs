package cn.aiyuedu.bs.dao.mongo.repository.custom;

import cn.aiyuedu.bs.common.orm.Page;
import cn.aiyuedu.bs.dao.entity.Category;
import cn.aiyuedu.bs.dao.dto.CategoryQueryDto;

import java.util.List;

/**
 * Description:
 *
 * @author yz.wu
 */
public interface CategoryRepositoryCustom {

    Category persist(Category category);

    List<Category> persist(List<Category> categories);

    boolean exist(Integer id, String name);

    List<Category> find(CategoryQueryDto categoryQueryDto);

    Page<Category> getPage(CategoryQueryDto categoryQueryDto);

    void updateOrderId(Integer id, Integer orderId);

    void updateBookCount(Integer id, Integer bookCount);

    void removeMulti(List<Integer> ids);
}
