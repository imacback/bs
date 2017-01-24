package cn.aiyuedu.bs.service;

import cn.aiyuedu.bs.cache.service.PlatformCacheService;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by Thinkpad on 2014/9/26.
 */
@Ignore
public class PlatformCeneralServiceTest  extends BaseTest {
    @Autowired
    PlatformCacheService platformCacheService;

    @Autowired
    private PlatformCeneralService platformCeneralService;
    @Test
    public void get(){
//
//        Map<String, Object> parameters = Maps.newHashMap();
//        parameters.put("startIndex", 0);
//        parameters.put("pageSize", 100);
//        parameters.put("isUse", 1);
//        List<PlatformDto> list = platformDao.getPlatformDtos(parameters);
//        List<String> list1 = new ArrayList<>();
//
//        for(PlatformDto o  : list){
//            String b = o.getId() +"#"+o.getName();
//            System.out.println("d ================  " + b);
//            list1.add(b);
//        }
//        platformCacheService.set(list1);


        String d  = platformCeneralService.get("");
        System.out.println("d ================  " + d);

    }

}
