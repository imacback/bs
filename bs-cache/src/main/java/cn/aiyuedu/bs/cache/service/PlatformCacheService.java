package cn.aiyuedu.bs.cache.service;

import cn.aiyuedu.bs.cache.util.RedisKeyUtil;
import com.duoqu.commons.redis.client.RedisClient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * Created by Thinkpad on 2014/9/26.
 */


@Service("platformCacheService")
public class PlatformCacheService {
    @Autowired
    private RedisClient redisClient;
     //

    public  void set(List<String> list  ){
        for (int i = 0; i <list.size(); i++) {
            String[] infos = list.get(i).split("#");
            redisClient.addToHashWithMsgPack(RedisKeyUtil.HASH_RS_PLATFORM,infos[0]  , infos[1]);

        }
    }


    public String getByid(String id){

        if(id.equals("0")){
            return "全部平台";
        }
         if(redisClient.getFromHashWithMsgPack(RedisKeyUtil.HASH_RS_PLATFORM, id, String.class)!=null){
             return redisClient.getFromHashWithMsgPack(RedisKeyUtil.HASH_RS_PLATFORM,id ,String.class);
         }
        return "";
    }


//    public void setList(List<Map<Object ,Object>> list){
//
//    }
}
