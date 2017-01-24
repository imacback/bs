package cn.aiyuedu.bs.cache.service;

import cn.aiyuedu.bs.common.model.ContainerBase;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Description:
 *
 * @author yz.wu
 */
//@Ignore
public class ContainerCacheServiceTest extends BaseTest {

    @Autowired
    private ContainerCacheService containerCacheService;

    @Test
    public void testGet() {
        Integer id = 23;
        ContainerBase c = containerCacheService.get(id);
        if (c != null) {
            System.out.println(c.getName());
        }
    }

    @Test
    public void getAllTest(){
        List<ContainerBase> list = containerCacheService.getAll();
        if(CollectionUtils.isNotEmpty(list)){
            for(ContainerBase b : list){
                System.out.println(b.getName());
            }
        }
    }
}
