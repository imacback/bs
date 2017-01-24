package cn.aiyuedu.bs.cache.service;

import cn.aiyuedu.bs.common.model.ClientShelfBase;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Ignore
public class ClientShelfCacheServiceTest extends BaseTest {
    @Autowired
    ClientShelfCacheService clientShelfCacheService;

    @Test
    public void get() {
        List<ClientShelfBase> list = clientShelfCacheService.getAll();
        if (CollectionUtils.isNotEmpty(list)) {
            for (ClientShelfBase c : list) {
                System.out.println("platform:"+c.getPlatformId()+", version:"+c.getVersion()+", ditchIds:"+c.getDitchIds()+", books:"+c.getBookIds());
            }
        }
    }


}
