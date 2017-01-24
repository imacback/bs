package cn.aiyuedu.bs.cache.service;

import java.util.ArrayList;
import java.util.List;


import java.util.Set;

import cn.aiyuedu.bs.cache.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.duoqu.commons.redis.client.RedisClient;
import cn.aiyuedu.bs.common.model.ClientTabBase;

@Service("clientTabCacheService")
public class ClientTabCacheService {
	    @Autowired
	    private RedisClient redisClient;
	    
	    
	    public void del(){
        redisClient.delete(RedisKeyUtil.HASH_TAB,true);
    }
	    
	    public void set(List < ClientTabBase> list) {
            redisClient.delete(RedisKeyUtil.HASH_TAB,true);
	    	for(int i = 0 ; i< list.size() ; i++){
	    		ClientTabBase tab = list.get(i);
	    		redisClient.addToHashWithMsgPack(RedisKeyUtil.HASH_TAB, "RS_CLIENTTAB"+i, tab);
	    	}
	        
	    }

	    public List <ClientTabBase> get( ) {
	    	List <ClientTabBase> list = new ArrayList <ClientTabBase>();
	    	Set<Object>  set = redisClient.getKeysFromHash(RedisKeyUtil.HASH_TAB,true);

	    	if(set.size()==0){
                return null;
            }

	    	for(int i = 0 ; i<set.size() ; i++){
	    		ClientTabBase basd =     redisClient.getFromHashWithMsgPack(RedisKeyUtil.HASH_TAB,  "RS_CLIENTTAB"+i, ClientTabBase.class);
	    	    list.add(basd);
	    	}
	     return list;
	    }
	    
	    
	    
	    
	    
	    
	    
	    
 
	    public void reload(String msg){
	    	 redisClient.publish(msg,"reload");
	    }
}
