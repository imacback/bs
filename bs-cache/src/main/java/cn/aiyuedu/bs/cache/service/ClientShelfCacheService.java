package cn.aiyuedu.bs.cache.service;

 
import cn.aiyuedu.bs.cache.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.duoqu.commons.redis.client.RedisClient;
import cn.aiyuedu.bs.common.model.ClientShelfBase;

import java.util.ArrayList;
import java.util.List;

@Service("clientShelfCacheService")
public class ClientShelfCacheService {
	@Autowired
	private RedisClient redisClient;




    public List<String> getKey(ClientShelfBase base){
        List<String> list = new ArrayList<>();
        if (!base.getDitchIds().equals("0")) {
            String[] disids =base.getDitchIds().split(";");
            for (String dict : disids) {
                for (String v : base.getVersion().split(";")) {
                    String key = base.getPlatformId() + "#" + v + "#" + dict;
                    list.add(key);
                }
            }
        }else{
            for (String v : base.getVersion().split(";")) {
                 String key = base.getPlatformId() + "#" + v;
                 list.add(key);

            }
        }


        return list;
    }


	/***
	 * 写入redis
	 * @param base
	 */
	public void set(List<ClientShelfBase> base) {
        redisClient.addToListWithMsgPack(RedisKeyUtil.LIST_CLIENT_SHELF, true, base);
	}
    public void del(){
        redisClient.delete(RedisKeyUtil.LIST_CLIENT_SHELF,true);
    }

    public List<ClientShelfBase> getAll(){
        int end = redisClient.getListSize(RedisKeyUtil.LIST_CLIENT_SHELF,true)  ;
        return redisClient.getPageFromListWithMsgPack(RedisKeyUtil.LIST_CLIENT_SHELF,ClientShelfBase.class ,0,end);
    }



	/***
	 * 发送消息
	 * 
	 * @param st
	 */
	public void publish(String st) {
		redisClient.publish(st, "reloading_ClientShelfBase");
	}
}
