package cn.aiyuedu.bs.cache.service;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Description:
 *
 * @author yz.wu
 */
@Ignore
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/WEB-INF/config/spring/applicationContext.xml"})
public class BaseTest {
    protected Logger log = LoggerFactory.getLogger(BaseTest.class);
    @Test
    public void test() {
    }
}
