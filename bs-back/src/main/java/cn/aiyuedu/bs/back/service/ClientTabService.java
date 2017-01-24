package cn.aiyuedu.bs.back.service;

import com.duoqu.commons.redis.client.RedisClient;
import cn.aiyuedu.bs.cache.service.ClientTabCacheService;
import cn.aiyuedu.bs.common.model.ClientTabBase;
import cn.aiyuedu.bs.common.orm.Page;

import cn.aiyuedu.bs.dao.dto.ClientTabQueryDto;
import cn.aiyuedu.bs.dao.entity.ClientTab;
import cn.aiyuedu.bs.dao.mongo.repository.ClientTabRepository;
import com.google.common.collect.Lists;

import jodd.bean.BeanCopy;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service("clientTabService")
public class ClientTabService {

    @Autowired
    private ClientTabRepository clientTabRepository;

	@Autowired
	private  ClientTabCacheService tabCacheService;
    @Autowired
    private RedisClient redisClient;
    @Autowired
    private Properties redisConfig;

    public Page<ClientTab> getPage(Integer startIndex, Integer pageSize){

        ClientTabQueryDto queryDto = new ClientTabQueryDto();
        queryDto.setStart(startIndex);
        queryDto.setLimit(pageSize);

        Page<ClientTab> page = clientTabRepository.getPage(queryDto);

        List<ClientTab> list = Lists.newArrayList();
        //对orderId进行排序,因为orderId为String,没在查询数据库时进行order by排序
        if(null!=page && CollectionUtils.isNotEmpty(page.getResult())){
            Collections.sort(page.getResult(), new Comparator<ClientTab>(){
                @Override
                public int compare(ClientTab o1, ClientTab o2){
                    return Integer.valueOf(o1.getOrderId()) - Integer.valueOf(o2.getOrderId());
                }
            });
            list = page.getResult();
        }

        return new Page<>(list, page.getTotalItems());
    }

    public int updateOrderid(int id , int orderid,int userid,String url,String name){

        List<ClientTab> list = clientTabRepository.find(null) ;
        //对结果集按orderId进行排序
        if(CollectionUtils.isNotEmpty(list)){
            Collections.sort(list, new Comparator<ClientTab>(){
                @Override
                public int compare(ClientTab o1, ClientTab o2){
                    return Integer.valueOf(o1.getOrderId()) - Integer.valueOf(o2.getOrderId());
                }
            });
        }

    	LinkedList link = new LinkedList();
    	ClientTab ta_temp = new ClientTab();
        //****将调整顺序的对象在队列中剔除******/
        for(  ClientTab ta :list) {
            ta.setEditorId(userid);
            ta.setEditDate(new Date());
            if(ta.getId()==id){
                ta_temp = ta;
            }else{
                link.addLast(ta);
            }
        }
        ta_temp.setUrl(url);
        ta_temp.setName(name);
        //**根据调整的顺序 ，是否为队列之首 队列之尾 队列中间三种情况处理*****/
    	if(orderid<=1){
            link.addFirst(ta_temp);
    	}else if(orderid>list.size()){
            link.addLast(ta_temp);
    	}else{
            link.add(orderid-1 ,ta_temp);
        }

    	for(int k = 0 ; k< link.size() ;k++){
    		ClientTab ta =(ClientTab) link.get(k);
    		ta.setOrderId(""+(k+1));
            clientTabRepository.persist(ta);
    	}

    	return 1 ;
    }

    public int release(){
        //获取数据库中所有信息
        List<ClientTab> clientTabList = clientTabRepository.find(null);
        //结果
        List<ClientTabBase> list = null;
        if(CollectionUtils.isNotEmpty(clientTabList)){
            list = Lists.newArrayList();
            ClientTabBase base = null;
            for(ClientTab ct : clientTabList){
                base = new ClientTabBase();
                BeanCopy.beans(ct, base).ignoreNulls(true).copy();
                list.add(base);
            }
        }

        tabCacheService.set(list);

        redisClient.publish(redisConfig.getProperty("redis.topic.back.clientTab"), "reload");
	    return 1 ;
    }
    
    
}
