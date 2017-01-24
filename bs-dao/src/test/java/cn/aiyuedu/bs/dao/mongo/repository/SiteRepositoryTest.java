package cn.aiyuedu.bs.dao.mongo.repository;

import cn.aiyuedu.bs.dao.BaseTest;
import cn.aiyuedu.bs.dao.entity.Site;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

/**
 * Description:
 *
 * @author yz.wu
 */
@Ignore
public class SiteRepositoryTest extends BaseTest {

    @Autowired
    private SiteRepository siteRepository;

    @Test
    public void testSave() {
        Site s = new Site();
        s.setId(1);
        s.setName("默认");
        s.setCreateDate(new Date());
        s.setEditDate(new Date());
        s.setCreatorId(1);
        s.setEditorId(1);
        s.setStatus(1);
        s.setPlatformId(1);

        siteRepository.persist(s);
    }
}
