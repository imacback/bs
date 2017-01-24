package cn.aiyuedu.bs.back.schedule;

import cn.aiyuedu.bs.back.service.TagService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Description:
 *
 * @author yz.wu
 */
public class TagJob {

    private final Logger logger = LoggerFactory.getLogger(TagJob.class);

    @Autowired
    private TagService tagService;

    public void execute() {
        if (logger.isDebugEnabled()) {
            logger.debug("TagJob start");
        }
        tagService.statis();
        if (logger.isDebugEnabled()) {
            logger.debug("TagJob end");
        }
    }
}
