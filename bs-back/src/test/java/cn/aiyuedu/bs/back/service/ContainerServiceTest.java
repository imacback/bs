package cn.aiyuedu.bs.back.service;

import cn.aiyuedu.bs.back.BaseTest;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Description:
 *
 * @author yz.wu
 */
@Ignore
public class ContainerServiceTest extends BaseTest {

    @Autowired
    private ContainerService containerService;

    @Test
    public void testPublish() {
        Integer id = 23;
        containerService.publish(23);
    }
}
