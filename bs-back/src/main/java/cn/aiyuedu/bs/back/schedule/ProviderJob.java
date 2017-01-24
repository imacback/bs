package cn.aiyuedu.bs.back.schedule;

import cn.aiyuedu.bs.back.service.ProviderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Description:
 *
 * @author yz.wu
 */
public class ProviderJob {

    private final Logger logger = LoggerFactory.getLogger(ProviderJob.class);

    @Autowired
    private ProviderService providerService;

    public void execute() {
        if (logger.isDebugEnabled()) {
            logger.debug("ProviderJob start");
        }
        providerService.statis();
        if (logger.isDebugEnabled()) {
            logger.debug("ProviderJob end");
        }
    }
}
