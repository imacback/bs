package cn.aiyuedu.bs.back.component;

import cn.aiyuedu.bs.da.enumeration.ConfigEnum;
import cn.aiyuedu.bs.da.service.KanshuService;
import com.google.common.collect.Lists;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Description:
 *
 * @author yz.wu
 */
@Component("bookComponent")
public class BookComponent {

    @Autowired
    private KanshuService kanshuService;

    @Async
    public void snatchKanshu() {
        List<Integer> ids = kanshuService.getCpBookIds();
        if (CollectionUtils.isNotEmpty(ids)) {
            List<String> bookIds = Lists.newArrayList();
            for (Integer id : ids) {
                bookIds.add(id.toString());
            }
            kanshuService.update(ConfigEnum.Provider.Kanshu, bookIds);
        }
    }
}
