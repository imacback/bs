package cn.aiyuedu.bs.service;

import cn.aiyuedu.bs.cache.service.ContainerCacheService;
import cn.aiyuedu.bs.common.model.ContainerBase;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Description:
 *
 * @author yz.wu
 */
public class ContainerGeneralService {

    @Autowired
    private ContainerCacheService containerCacheService;

    public ContainerBase get(Integer containerId) {
        return containerCacheService.get(containerId);
    }
}
