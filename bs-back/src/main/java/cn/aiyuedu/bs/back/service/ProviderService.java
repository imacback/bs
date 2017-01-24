package cn.aiyuedu.bs.back.service;

import com.duoqu.commons.redis.client.RedisClient;
import cn.aiyuedu.bs.cache.service.ProviderCacheService;
import cn.aiyuedu.bs.common.Constants;
import cn.aiyuedu.bs.common.dto.ResultDto;
import cn.aiyuedu.bs.common.orm.Page;
import cn.aiyuedu.bs.common.util.IdUtil;
import cn.aiyuedu.bs.dao.dto.BookQueryDto;
import cn.aiyuedu.bs.dao.dto.ProviderDto;
import cn.aiyuedu.bs.dao.dto.ProviderQueryDto;
import cn.aiyuedu.bs.dao.entity.AdminUser;
import cn.aiyuedu.bs.dao.entity.Provider;
import cn.aiyuedu.bs.dao.mongo.repository.BookRepository;
import cn.aiyuedu.bs.dao.mongo.repository.ProviderRepository;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import jodd.bean.BeanCopy;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;

@Service("providerService")
public class ProviderService {

    @Autowired
    private ProviderRepository providerRepository;
    @Autowired
    private AdminUserService adminUserService;
    @Autowired
    private ProviderCacheService providerCacheService;
    @Autowired
    private RedisClient redisClient;
    @Autowired
    private Properties redisConfig;
    @Autowired
    private BookRepository bookRepository;

    private Map<Integer, Provider> providerMap;

    @PostConstruct
    public synchronized void reload() {
        providerMap = Maps.newHashMap();

        List<Provider> list = providerRepository.find(null);
        for (Provider o : list) {
            providerMap.put(o.getId(), o);
        }
    }

    public Provider get(Integer id) {
        return providerMap.get(id);
    }


    public boolean isExist(Integer id, String name) {

        ProviderQueryDto queryDto = new ProviderQueryDto();
        queryDto.setId(id);
        queryDto.setIsNEId(1);
        queryDto.setName(name);

        return providerRepository.count(queryDto) > 0;
    }

    public boolean add(Provider provider) {
        providerRepository.persist(provider);
        return true;
    }

    public boolean update(Provider provider) {

        //从DB获取更新前的信息
        Provider old = providerRepository.findOne(provider.getId());

        //BeanCopy后,old为所要update的信息
        BeanCopy.beans(provider, old).ignoreNulls(true).copy();

        providerRepository.persist(old);

        return true;
    }

    public ResultDto save(Provider provider, AdminUser adminUser) {
        ResultDto result = new ResultDto();
        result.setSuccess(false);
        result.setInfo("更新成功！");
        Date now = new Date();
        provider.setEditDate(now);
        provider.setEditorId(adminUser.getId());

        if (provider.getId() != null) {//update
            if (!isExist(provider.getId(), provider.getName())) {
                if (update(provider)) {
                    result.setSuccess(true);
                    result.setInfo("更新成功！");
                } else {
                    result.setInfo("更新失败！");
                }
            } else {
                result.setInfo("数据已存在");
            }
        } else {//insert
            provider.setCreateDate(now);
            provider.setCreatorId(adminUser.getId());
            provider.setSecretKey(IdUtil.uuid());
            provider.setBatchCount(0);
            provider.setBookCount(0);
            provider.setOnlineCount(0);

            if (!isExist(provider.getId(), provider.getName())) {
                if (add(provider)) {
                    result.setSuccess(true);
                    result.setInfo("保存成功！");
                } else {
                    result.setInfo("保存失败！");
                }
            } else {
                result.setInfo("数据已存在");
            }
        }

        if (result.getSuccess()) {
            reload();
        }

        return result;
    }

    public Page<ProviderDto> getPage(Integer id, String name, Integer startIndex, Integer pageSize) {

        ProviderQueryDto queryDto = new ProviderQueryDto();
        queryDto.setId(id);
        queryDto.setName(name);
        queryDto.setIsLikeName(1);
        queryDto.setStart(startIndex);
        queryDto.setLimit(pageSize);
        queryDto.setIsDesc(1);

        Page<Provider> page = providerRepository.getPage(queryDto);

        ProviderDto dto = null;
        List<ProviderDto> providerDtoList = Lists.newArrayList();

        //将Provider-->ProviderDto,并设置创建人和修改人名称
        if(null!=page && CollectionUtils.isNotEmpty(page.getResult())){
            for(Provider provider : page.getResult()){
                dto = new ProviderDto();
                BeanCopy.beans(provider, dto).ignoreNulls(false).copy();

                //设置创建者和修改者的名称
                adminUserService.infoOperate(dto);

                providerDtoList.add(dto);
            }
        }

        return new Page<>(providerDtoList, page.getTotalItems());
    }

    public boolean delete(List<Integer> ids) {
        providerRepository.removeMulti(ids);
        return true;
    }

    public ResultDto publish() {
        ResultDto resultDto = new ResultDto();
        List<Provider> list = providerRepository.find(null);
        providerCacheService.set(list);
        redisClient.publish(redisConfig.getProperty("redis.topic.back.provider"), "reload");

        resultDto.setInfo("发布成功");
        resultDto.setSuccess(true);
        return resultDto;
    }

    public void statis() {
        List<Provider> list = providerRepository.find(null);
        if (CollectionUtils.isNotEmpty(list)) {
            BookQueryDto bookQueryDto = null;
            Provider provider = null;
            for (Provider p : list) {
                if (p != null) {
                    provider = new Provider();
                    provider.setId(p.getId());
                    bookQueryDto = new BookQueryDto();
                    bookQueryDto.setProviderId(p.getId());
                    provider.setBookCount((int)bookRepository.count(bookQueryDto));

                    bookQueryDto.setStatus(Constants.BookStatus.Online.getId());
                    provider.setOnlineCount((int)bookRepository.count(bookQueryDto));

                    //provider.setBatchCount(batchService.count(p.getId()));

                    update(provider);
                }
            }
        }
    }
}
