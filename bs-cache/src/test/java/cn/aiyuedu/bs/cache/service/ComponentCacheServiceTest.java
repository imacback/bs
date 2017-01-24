package cn.aiyuedu.bs.cache.service;

import cn.aiyuedu.bs.common.model.ComponentBase;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Description:
 *
 * @author yz.wu
 */
@Ignore
public class ComponentCacheServiceTest extends BaseTest {

    @Autowired
    private ComponentCacheService componentCacheService;

    @Test
    public void testGet() {
        Integer componentId = 27;
        ComponentBase c = componentCacheService.get(componentId);
        System.out.println(c.getName());
    }
}
