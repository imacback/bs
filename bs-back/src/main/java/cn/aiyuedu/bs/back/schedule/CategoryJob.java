package cn.aiyuedu.bs.back.schedule;

import cn.aiyuedu.bs.back.service.CategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Description:
 *
 * @author yz.wu
 */
public class CategoryJob {

    private final Logger logger = LoggerFactory.getLogger(CategoryJob.class);

    @Autowired
    private CategoryService categoryService;

    public void execute() {
        if (logger.isDebugEnabled()) {
            logger.debug("CategoryJob start");
        }
        categoryService.statis();
        if (logger.isDebugEnabled()) {
            logger.debug("CategoryJob end");
        }
    }

}
