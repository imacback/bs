package cn.aiyuedu.bs.wap.service;

import cn.aiyuedu.bs.common.model.CategoryBase;
import cn.aiyuedu.bs.service.BookGeneralService;
import cn.aiyuedu.bs.service.CategoryGeneralService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Description:
 *
 * @author yz.wu
 */
@Service("categoryService")
public class CategoryService {

    @Autowired
    private CategoryGeneralService categoryGeneralService;
    @Autowired
    private BookGeneralService bookGeneralService;

    public List<CategoryBase> getList() {
        return categoryGeneralService.getCategoryBases();
    }
}
