package cn.aiyuedu.bs.back.schedule;

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
public class BookCounterJobTest extends BaseTest {

    @Autowired
    private BookCounterJob bookCounterJob;

    @Test
    public void testExcute() {
        bookCounterJob.execute();
    }
}
