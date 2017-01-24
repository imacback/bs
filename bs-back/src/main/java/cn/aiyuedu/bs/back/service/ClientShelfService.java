package cn.aiyuedu.bs.back.service;

import com.duoqu.commons.redis.client.RedisClient;
import cn.aiyuedu.bs.cache.service.ClientShelfCacheService;
import cn.aiyuedu.bs.common.Constants;
import cn.aiyuedu.bs.common.dto.ResultDto;
import cn.aiyuedu.bs.common.model.ClientShelfBase;
import cn.aiyuedu.bs.common.orm.Page;

import cn.aiyuedu.bs.common.util.StringUtil;
import cn.aiyuedu.bs.dao.dto.ClientShelfDto;
import cn.aiyuedu.bs.dao.dto.ClientShelfQueryDto;
import cn.aiyuedu.bs.dao.entity.ClientShelf;

import cn.aiyuedu.bs.dao.entity.Platform;
import cn.aiyuedu.bs.dao.mongo.repository.ClientShelfRepository;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import jodd.bean.BeanCopy;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


import java.util.*;

@Service("clientShelfService")
public class ClientShelfService {
    private final Logger logger = LoggerFactory.getLogger(ClientShelfService.class);

    @Autowired
    private ClientShelfRepository clientShelfRepository;
    @Autowired
    private RedisClient redisClient;
    @Autowired
    private Properties redisConfig;
    @Autowired
    private ClientShelfCacheService clientShelfCacheService;
    @Autowired
    private  BookService bookService;
    @Autowired
    private  PlatformService platformService;


    public Page<ClientShelfDto> getPage(ClientShelfQueryDto clientShelfQueryDto){

        //版本、渠道、书籍按Like进行搜索
        clientShelfQueryDto.setIsLikeVersion(1);
        clientShelfQueryDto.setIsLikeDitchId(1);
        clientShelfQueryDto.setIsLikeBookId(1);

        Page<ClientShelf> page = clientShelfRepository.getPage(clientShelfQueryDto);

        ClientShelfDto dto = null;
        Platform p = null;
        List<ClientShelfDto> clientShelfDtoList = Lists.newArrayList();

        //将ClientShelf-->ClientShelfDto,并设置平台名称
        if(null!=page && CollectionUtils.isNotEmpty(page.getResult())){
            for(ClientShelf clientShelf : page.getResult()){
                dto = new ClientShelfDto();
                BeanCopy.beans(clientShelf, dto).ignoreNulls(false).copy();

                //平台名称
                if(dto.getPlatformId()==0){
                    dto.setPlatformName("全部");
                }else{
                    p = platformService.get(dto.getPlatformId());
                    if(p!=null){
                        dto.setPlatformName(p.getName());
                    }
                }

                clientShelfDtoList.add(dto);
            }
        }

        return new Page<>(clientShelfDtoList, page.getTotalItems());
    }
    
    /**
     * 上线下线
     * @param id 修改编号
     * @param status 状态  
     * @param userid 修改用户编号
     * @return
     */
     public boolean upateStatus(int id , int status ,int userid){

         ClientShelf clientShelf = clientShelfRepository.findOne(id);
         if(null!=clientShelf){
             clientShelf.setStatus(status);
             clientShelf.setEditorId(userid);
             clientShelf.setEditDate(new Date());
             clientShelfRepository.persist(clientShelf);
             this.release();
             return true;
         }else{
             return false;
         }
     }
    
     /**
      * 上线下线
      * @return
      */
      public ResultDto upate(ClientShelf clientShelf){
          List<Long> list = StringUtil.split2Long(clientShelf.getBookIds(),";");
          ResultDto b;
          for (Long id : list){
              b = bookService.check(id, Constants.BookModule.Shelf,clientShelf.getPlatformId());
              if (!b.getSuccess()){
                  return  b;
              }
          }

          b = new ResultDto();
          b.setSuccess(false);
          b.setInfo("更新失败！");

          if(null!=clientShelf.getId()){
              //从DB获取更新前的信息
              ClientShelf old = clientShelfRepository.findOne(clientShelf.getId());

              //BeanCopy后,old为所要update的信息
              BeanCopy.beans(clientShelf, old).ignoreNulls(true).copy();

              clientShelfRepository.persist(old);

              release();

              b.setSuccess(true);
              b.setInfo("保存成功！");
          }
          return b ;
      }
     
      
      /**
       * 保存信息
       * @return
       */
       public ResultDto insert(ClientShelf s){
           List<Long> list = StringUtil.split2Long(s.getBookIds(),";");
           ResultDto b;
           for (Long id : list){
                 b = bookService.check(id, Constants.BookModule.Shelf,s.getPlatformId());
               if (!b.getSuccess()){
                   return  b;
               }
           }

           s.setId(null);
           clientShelfRepository.persist(s);

           if (s.getStatus() !=0) {
               release();
           }
           b = new ResultDto();
           b.setSuccess(true);
           b.setInfo("保存成功！");
           return b ;
       }
      
    public boolean delete(String idStr){
        String[] s =idStr.split(",");
        List<Integer> ids = Lists.newArrayList();
    	for (int i = 0; i < s.length; i++) {
            ids.add(new Integer(s[i]));
		}

        clientShelfRepository.removeMulti(ids);
    	
    	return true;
    }

	/***
	 * 发布
	 * @return
	 */
    public int release(){
        if(logger.isInfoEnabled()){
            logger.info("ClientShelfService release");
        }

        //结果
        List<ClientShelfBase> list = null;

        //查询对象
        ClientShelfQueryDto queryDto = new ClientShelfQueryDto();
        queryDto.setStatus(1);
        LinkedHashMap<String, Sort.Direction> map = Maps.newLinkedHashMap();
        map.put("editDate", Sort.Direction.DESC);
        map.put(Constants.MONGODB_ID_KEY, Sort.Direction.DESC);
        queryDto.setOrderMap(map);

        List<ClientShelf> clientShelfList = clientShelfRepository.find(queryDto);

        if(CollectionUtils.isNotEmpty(clientShelfList)){
            list = Lists.newArrayList();
            ClientShelfBase base = null;
            for(ClientShelf cs : clientShelfList){
                base = new ClientShelfBase();
                BeanCopy.beans(cs, base).ignoreNulls(true).copy();
                list.add(base);
            }
        }

        if(list!=null) {
            clientShelfCacheService.del();
            clientShelfCacheService.set(list);
            redisClient.publish(redisConfig.getProperty("redis.topic.back.clientShelf"), "reload");
        }
        if(logger.isInfoEnabled()){
            logger.info("ClientShelfService release   " + list);
        }
       return 1;
    }
    
}
