package cn.aiyuedu.bs.service;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


/**
 * Base Test class
 */
@Ignore
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/WEB-INF/config/spring/applicationContext.xml"})
public class BaseTest {
    protected static final Logger log = LoggerFactory.getLogger(BaseTest.class);

    @Test
    public void test(){
        log.info("test....................");
    }
}
