package cn.aiyuedu.bs.back.service;

import cn.aiyuedu.bs.back.dto.DistributeTagDto;
import cn.aiyuedu.bs.common.orm.Page;
import cn.aiyuedu.bs.dao.dto.DistributeDto;
import cn.aiyuedu.bs.dao.entity.AdminUser;
import cn.aiyuedu.bs.dao.entity.Distribute;
import cn.aiyuedu.bs.dao.mongo.repository.DistributeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by Thinkpad on 2014/11/18.
 */

@Service("distributeService")
public class DistributeService {

    private final Logger logger = LoggerFactory.getLogger(DistributeService.class);
    @Autowired
    DistributeRepository distributeRepository;
    @Autowired
    AdminUserService adminUserService;


    @Autowired
    DistributeBookService distributeBookService;


    public Page<DistributeTagDto> getPage(String disName, Integer status, Integer start, Integer limit,Integer isCategory) {
        DistributeDto distributeDto = new DistributeDto();
        distributeDto.setDisName(disName);
        if (disName.equals("")) {
            distributeDto.setDisName(null);
        }
        distributeDto.setLimit(limit);
        distributeDto.setStart(start);
        distributeDto.setStatus(status);
        distributeDto.setIsCategory(isCategory);
        Page<Distribute> page = distributeRepository.getPage(distributeDto);
        Page<DistributeTagDto> pageDto = new Page<DistributeTagDto>();
        List<DistributeTagDto> listDto = new ArrayList<>();
        List<Distribute> list = page.getResult();
        if (list != null) {
            for (Distribute d : list) {
                DistributeTagDto dto = new DistributeTagDto();
                AdminUser us = adminUserService.get(d.getCreatorId());
                if(null!=us){
                    dto.setUserName(us.getName());
                }
                dto.setId(d.getId());
                dto.setName(d.getName());
                dto.setStatus(d.getStatus());
                dto.setKey(d.getKey());
                dto.setCreateDate(d.getCreateDate());
                dto.setCreatorId(d.getCreatorId());
                dto.setIsCategory(d.getIsCategory());
                dto.setCount(distributeBookService.getBookCountBydisid(d.getId()));
                listDto.add(dto);
            }
        }
        pageDto.setResult(listDto);
        pageDto.setTotalItems(page.getTotalItems());
        return pageDto;
    }

    public Map<String, Object> save(Distribute distribute, AdminUser adminUser) {
        Map<String, Object> responseMap = new HashMap<>();
        distribute.setEditorId(adminUser.getId());
        distribute.setEditDate(new Date());
        if (distribute.getId() == null) {
            distribute.setCreateDate(new Date());
            distribute.setCreatorId(adminUser.getId());
            distributeRepository.persist(distribute);
        } else {
            distributeRepository.update(distribute);
        }
        responseMap.put("success", "true");
        responseMap.put("info", "保存成功！");
        return responseMap;
    }


    public List<Distribute> findAll(Integer status,Integer isCategory) {
        return distributeRepository.findAll(status,isCategory);
    }

    public void deleteDistribute(Integer id) {
        distributeRepository.delete(id);
    }

}
