package cn.aiyuedu.bs.cache.service;

import cn.aiyuedu.bs.cache.util.RedisKeyUtil;
import com.duoqu.commons.redis.client.RedisClient;
import cn.aiyuedu.bs.common.Constants;
import cn.aiyuedu.bs.common.model.ClientEntryBase;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by Wangpeitao on 2014/9/25.
 */
@Service("clientEntryCacheService")
public class ClientEntryCacheService {
    @Autowired
    private RedisClient redisClient;

    /**
     * Description ClientEntryBase中的版本信息为单个版本,而不是多个版本的版本串;这里的ClientEntryBase是从redis取出来的
     * @param clientEntryBase
     * @return
     */
    public String getKey(ClientEntryBase clientEntryBase) {
        return getKey(clientEntryBase.getPlatformId(), clientEntryBase.getVersions());
    }

    /**
     *
     * @param platformId 平台ID
     * @param version 版本,这里的版本是单个版本,而不是多个版本的版本串
     * @return
     */
    public String getKey(Integer platformId, String version) {
        StringBuilder sb = new StringBuilder();
        sb.append(platformId).append(Constants.SEPARATOR_2).append(version);
        return sb.toString();
    }

    /**
     * Description 将ClientEntryBase(其中可能包含多个版本信息)转成包含单版本信息的ClientEntryBase后放到Redis中
     * @param clientEntryBase 启动入口配置对象,其中的版本信息可能是一个多个版本的版本串,也有可能只有一个版本信息
     */
    public void publish(ClientEntryBase clientEntryBase) {
        Map<String, ClientEntryBase> map = this.multiVerToSingVerMap(clientEntryBase);
        if (MapUtils.isNotEmpty(map)) {
            redisClient.addToHashWithMsgPack(RedisKeyUtil.HASH_CLIENT_ENTRY, map);
        }
    }

    /**
     * Description 从Redis删除对应的入口配置信息
     * @param clientEntryBase
     */
    public void remove(ClientEntryBase clientEntryBase) {
        Map<String, ClientEntryBase> map = this.multiVerToSingVerMap(clientEntryBase);

        if(MapUtils.isNotEmpty(map)){
            //第二个参数,true表示二进制;false表示字符串
            redisClient.removeFromHash(RedisKeyUtil.HASH_CLIENT_ENTRY, true, Lists.newArrayList(map.keySet()));
        }
    }

    /**
     * Description 从Redis中删除所有入口配置信息
     */
    public void removeAll(){
        redisClient.delete(RedisKeyUtil.HASH_CLIENT_ENTRY, true);
    }

    /**
     * Description 根据平台ID和版本信息从Redis里获取一个入口配置信息
     * @param platformId 平台ID
     * @param version 版本,这里的版本是单版本信息,而不是多个版本的版本串
     * @return
     */
    public ClientEntryBase get(Integer platformId, String version) {
        return redisClient.getFromHashWithMsgPack(RedisKeyUtil.HASH_CLIENT_ENTRY,
                getKey(platformId, version), ClientEntryBase.class);
    }

    /**
     * Description 从Redis获取所有的启动入口配置信息,其中的版本是单个版本号,而不是多个版本的版本串
     * @return
     */
    public List<ClientEntryBase> getAll() {
        return redisClient.getAllFromHashWithMsgPack(RedisKeyUtil.HASH_CLIENT_ENTRY, ClientEntryBase.class);
    }

    /**
     * Description 将多个版本的版本串ClientEntryBase转成只包含单版本信息的List<ClientEntryBase>
     * @param clientEntryBase 启动入口配置对象,其中的版本信息是一个多个版本的版本串
     * @return
     */
    public List<ClientEntryBase> multiVerToSingVerList(ClientEntryBase clientEntryBase){

        List<ClientEntryBase> list = null;

        if(null==clientEntryBase || StringUtils.isBlank(clientEntryBase.getVersions())){
            return list;
        }

        list = Lists.newArrayList();

        //使用 ";" 切分字符串并去除空串与空格
        Iterable<String> iterable = Splitter.on(";").omitEmptyStrings().trimResults().split(clientEntryBase.getVersions());
        ClientEntryBase tempBase = null;
        for(String v : iterable){
            tempBase = new ClientEntryBase();
            tempBase.setId(clientEntryBase.getId());
            tempBase.setEntryType(clientEntryBase.getEntryType());
            tempBase.setPlatformId(clientEntryBase.getPlatformId());
            tempBase.setVersions(v);

            list.add(tempBase);
        }

        return list;
    }

    /**
     * Description 将多个版本的版本串ClientEntryBase转成只包含单版本信息的Map<String, ClientEntryBase>
     * @param clientEntryBase 启动入口配置对象,其中的版本信息是一个多个版本的版本串
     * @return
     */
    public Map<String, ClientEntryBase> multiVerToSingVerMap(ClientEntryBase clientEntryBase){

        Map<String, ClientEntryBase> map = null;

        if(null==clientEntryBase || StringUtils.isBlank(clientEntryBase.getVersions())){
            return map;
        }

        map = Maps.newHashMap();

        //使用 ";" 切分字符串并去除空串与空格
        Iterable<String> iterable = Splitter.on(";").omitEmptyStrings().trimResults().split(clientEntryBase.getVersions());
        ClientEntryBase tempBase = null;
        for(String v : iterable){
            tempBase = new ClientEntryBase();
            tempBase.setId(clientEntryBase.getId());
            tempBase.setEntryType(clientEntryBase.getEntryType());
            tempBase.setPlatformId(clientEntryBase.getPlatformId());
            tempBase.setVersions(v);

            map.put(this.getKey(clientEntryBase.getPlatformId(), v), tempBase);
        }

        return map;
    }
}
