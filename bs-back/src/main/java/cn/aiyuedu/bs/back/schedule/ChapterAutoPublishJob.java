package cn.aiyuedu.bs.back.schedule;

import cn.aiyuedu.bs.back.service.BookService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Description:
 *
 * @author yz.wu
 */
public class ChapterAutoPublishJob {

    private final Logger logger = LoggerFactory.getLogger(ChapterAutoPublishJob.class);

    @Autowired
    private BookService bookService;

    public void execute() {
        if (logger.isDebugEnabled()) {
            logger.debug("ChapterAutoPublishJob start");
        }
        bookService.autoPublish();
        if (logger.isDebugEnabled()) {
            logger.debug("ChapterAutoPublishJob end");
        }
    }
}
