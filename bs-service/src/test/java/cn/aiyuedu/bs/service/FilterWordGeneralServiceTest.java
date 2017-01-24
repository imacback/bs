package cn.aiyuedu.bs.service;

import cn.aiyuedu.bs.common.Constants;
import cn.aiyuedu.bs.dao.dto.FilterResultDto;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Description:
 *
 * @author yz.wu
 */
@Ignore
public class FilterWordGeneralServiceTest extends BaseTest {

    @Autowired
    private FilterWordGeneralService filterWordGeneralService;

    @Test
    public void test() {
        String text = "勾引，内衣，欲望....";

        FilterResultDto r = filterWordGeneralService.filter(text, Constants.FilterType.Replace);

        System.out.println(r.getOriginText());
        System.out.println(r.getText());
    }
}
