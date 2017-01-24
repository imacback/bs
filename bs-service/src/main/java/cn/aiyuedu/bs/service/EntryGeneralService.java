package cn.aiyuedu.bs.service;

import cn.aiyuedu.bs.cache.service.ClientEntryCacheService;
import cn.aiyuedu.bs.common.Constants;
import cn.aiyuedu.bs.common.model.ClientEntryBase;
import com.google.common.collect.Maps;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import javax.annotation.PostConstruct;

import java.util.List;
import java.util.Map;

/**
 * Description 入口配置相关的操作的Service
 * Created by Wangpeitao on 2014/9/22.
 */
public class EntryGeneralService {

    private final Logger logger = LoggerFactory.getLogger(EntryGeneralService.class);

    private Map<String, ClientEntryBase> map;

    @Autowired
    private ClientEntryCacheService clientEntryCacheService;

    /**
     * Description 从redis里重新加载启动入口配置信息到内存Map中
     */
    @PostConstruct
    public synchronized void reload() {
//        map.clear();
        map = Maps.newHashMap();
        //从redis里获取启动入口配置信息
        List<ClientEntryBase> list = clientEntryCacheService.getAll();
        if (CollectionUtils.isNotEmpty(list)) {
            for (ClientEntryBase u : list) {
                map.put(clientEntryCacheService.getKey(u), u);
            }
        }
    }

    /**
     * @param platformId
     * @param version    版本号,这里是单个版本,而不是多个版本的版本串
     * @return
     */
    public ClientEntryBase get(Integer platformId, String version) {
        String key = clientEntryCacheService.getKey(platformId, version);
        return map.get(key);
    }

    /**
     * Description 启动页后跳转,根据平台ID和版本信息,返回入口
     *
     * @param platformId 平台ID
     * @param version    版本,这里是单个版本,而不是多个版本的版本串
     * @return 入口, 1书架, 2书城
     */
    public int startPageForward(Integer platformId, String version) {
        //默认调到书城
        int result = Constants.EntryType.Bookstore.getId();
        ClientEntryBase base = this.get(platformId, version);
        if (null != base) {
            result = base.getEntryType().intValue();

            if (logger.isInfoEnabled()) {
                logger.info("Find the ClientEntry, result is [" + result + "].");
            }
        } else {
            //获取不到对应的入口,返回默认入口"书城"
            if (logger.isInfoEnabled()) {
                logger.info("It isn't ClientEntry, return default value of [bookstore].");
            }
        }

        if (logger.isDebugEnabled()) {
            logger.debug("Parameter:{platformId=" + platformId + ",version:" + version + "};the clientEntry result is [" + result + "].");
        }

        return result;
    }
}
