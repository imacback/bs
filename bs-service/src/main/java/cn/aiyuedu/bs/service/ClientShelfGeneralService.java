package cn.aiyuedu.bs.service;

import cn.aiyuedu.bs.cache.service.ClientShelfCacheService;
import cn.aiyuedu.bs.common.Constants;
import cn.aiyuedu.bs.common.model.ClientShelfBase;
import cn.aiyuedu.bs.common.model.ShelfBookBase;
import cn.aiyuedu.bs.common.util.StringUtil;
import com.google.common.collect.Maps;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

public class ClientShelfGeneralService {
    private final Logger logger = LoggerFactory.getLogger(ClientShelfGeneralService.class);
    private Map<String, ShelfBookBase> ClientShelfMap;


    @Autowired
    ClientShelfCacheService clientShelfCacheService;

//    @PostConstruct
    public synchronized void reload() {
        if (ClientShelfMap == null) {
            ClientShelfMap = Maps.newHashMap();
        } else {
            ClientShelfMap.clear();
        }
        if (logger.isInfoEnabled()) {
            logger.info("方法调用 === ClientShelfGeneralService.reload（）");
        }
        List<ClientShelfBase> list = clientShelfCacheService.getAll();
        if (CollectionUtils.isNotEmpty(list)) {
            for (ClientShelfBase base : list) {
                ShelfBookBase shelfBook = new ShelfBookBase();
                List<Long> listBookid = StringUtil.split2Long(base.getBookIds(), Constants.GROUP_SEPARATOR);
                shelfBook.setChapters(base.getChapters());
                shelfBook.setList(listBookid);
                List<String> keyList = clientShelfCacheService.getKey(base);
                for (String key : keyList) {
                    ClientShelfMap.put(key, shelfBook);
                }

            }

        }
        if (logger.isInfoEnabled()) {
            logger.info("方法调用 === ClientShelfGeneralService.reload（）   === " + ClientShelfMap);
        }
    }

    /**
     * @param dit   ：渠道
     * @param plat： 平台编号
     * @param ver   ： 版本
     * @return List<BookBase> 对象   BookBase 中
     */
    public ShelfBookBase get(String dit, String plat, String ver) {
        if (logger.isInfoEnabled()) {
            logger.info("方法调用 === ClientShelfGeneralService.get（） ClientShelfMap = " + ClientShelfMap);
        }
        String key = plat + "#" + ver;
        ShelfBookBase book = ClientShelfMap.get(key + "#" + dit);
        if (book == null) {
            book = ClientShelfMap.get(key);
        }
        return book;
    }

}
