package cn.aiyuedu.bs.back.service;

import cn.aiyuedu.bs.common.orm.Page;
import cn.aiyuedu.bs.dao.dto.PlatformDto;
import cn.aiyuedu.bs.dao.dto.PlatformQueryDto;
import cn.aiyuedu.bs.dao.entity.Platform;
import cn.aiyuedu.bs.dao.mongo.repository.PlatformRepository;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import jodd.bean.BeanCopy;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

import java.util.List;
import java.util.Map;

@Service("platformService")
public class PlatformService {

    @Autowired
    private PlatformRepository platformRepository;

    private Map<Integer, Platform> platformMap;
    
    @PostConstruct
    public synchronized void reload() {
        platformMap = Maps.newHashMap();

        PlatformQueryDto queryDto = new PlatformQueryDto();
        queryDto.setIsUse(1);

        List<Platform> list = platformRepository.find(queryDto);
        if(CollectionUtils.isNotEmpty(list)){
            for (Platform o : list) {
                platformMap.put(o.getId(), o);
            }
        }
    }

    public Platform get(Integer id) {
        return platformMap.get(id);
    }

    public List<Platform> getPlatforms(Integer isUse) {

        PlatformQueryDto queryDto = new PlatformQueryDto();
        queryDto.setIsUse(isUse);
        queryDto.setIsDesc(1);

        return platformRepository.find(queryDto);
    }
    
    public Page<PlatformDto> getPage(Integer id, Integer isUse, Integer startIndex, Integer pageSize) {

        PlatformQueryDto queryDto = new PlatformQueryDto();
        queryDto.setId(id);
        queryDto.setIsUse(isUse);
        queryDto.setStart(startIndex);
        queryDto.setLimit(pageSize);

        Page<Platform> page = platformRepository.getPage(queryDto);

        PlatformDto dto = null;
        List<PlatformDto> platformDtoList = Lists.newArrayList();

        //将Platform-->PlatformDto
        if(null!=page && CollectionUtils.isNotEmpty(page.getResult())){
            for(Platform platform : page.getResult()){
                dto = new PlatformDto();
                BeanCopy.beans(platform, dto).ignoreNulls(false).copy();

                platformDtoList.add(dto);
            }
        }

        return new Page<>(platformDtoList, page.getTotalItems());
	}

//    public int release(){
//        Map<String, Object> parameters = Maps.newHashMap();
//        parameters.put("startIndex",0);
//        parameters.put("pageSize", 100);
//        parameters.put("isUse", 1);
//        List<PlatformDto> list = platformDao.getPlatformDtos(parameters);
//        List<String> list1 = new ArrayList<>();
//
//        for(PlatformDto o  : list){
//            String b = o.getId() +"#"+o.getName();
//            list1.add(b);
//        }
//        platformCacheService.set(list1);
//        return 1;
//    }


}
