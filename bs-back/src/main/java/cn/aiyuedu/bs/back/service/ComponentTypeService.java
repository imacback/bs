package cn.aiyuedu.bs.back.service;

import cn.aiyuedu.bs.common.orm.Page;
import cn.aiyuedu.bs.dao.dto.ComponentTypeQueryDto;
import cn.aiyuedu.bs.dao.entity.AdminUser;
import cn.aiyuedu.bs.dao.entity.ComponentType;
import cn.aiyuedu.bs.dao.mongo.repository.ComponentTypeRepository;
import cn.aiyuedu.bs.common.dto.ResultDto;
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

@Service("componentTypeService")
public class ComponentTypeService {

    @Autowired
    private ComponentTypeRepository componentTypeRepository;

    private Map<Integer, ComponentType> componentTypeMap;

    @PostConstruct
    public synchronized void reload() {
        componentTypeMap = Maps.newHashMap();

        ComponentTypeQueryDto queryDto = new ComponentTypeQueryDto();
        queryDto.setIsDesc(1);
        List<ComponentType> list = componentTypeRepository.find(queryDto);

        for (ComponentType row : list) {
            componentTypeMap.put(row.getId(), row);
        }
    }

    public ComponentType get(Integer id) {
        return componentTypeMap.get(id);
    }


    public boolean isExist(Integer id, String name){
        ComponentTypeQueryDto queryDto = new ComponentTypeQueryDto();
        queryDto.setId(id);
        queryDto.setIsNEId(1);
        queryDto.setName(name);
        return componentTypeRepository.count(queryDto) > 0;
    }


    public boolean add(ComponentType componentType) {

        componentTypeRepository.persist(componentType);
        return true;
    }

    public boolean update(ComponentType componentType) {

        //从DB获取更新前的信息
        ComponentType old = componentTypeRepository.findOne(componentType.getId());

        //BeanCopy后,old为所要update的信息
        BeanCopy.beans(componentType, old).ignoreNulls(true).copy();

        componentTypeRepository.persist(old);

        return true;
    }

    public ResultDto save(ComponentType componentType, AdminUser user) {
        ResultDto result = new ResultDto();
        result.setSuccess(false);
        result.setInfo("更新成功！");
        componentType.setEditDate(new Date());
        componentType.setEditorId(user.getId());

        if (componentType.getId() != null) {//update
            if (update(componentType)) {
                result.setSuccess(true);
                result.setInfo("更新成功！");
            } else {
                result.setInfo("更新失败！");
            }
        } else {//insert
            componentType.setCreateDate(new Date());
            componentType.setCreatorId(user.getId());

            if (add(componentType)) {
                result.setSuccess(true);
                result.setInfo("保存成功！");
            } else {
                result.setInfo("保存失败！");
            }
        }

        if (result.getSuccess()) {
            reload();
        }

        return result;
    }

    public Page<ComponentType> getPage(String name, Integer startIndex, Integer pageSize) {

        ComponentTypeQueryDto queryDto = new ComponentTypeQueryDto();
        queryDto.setName(name);
        queryDto.setIsLikeName(1);
        queryDto.setStart(startIndex);
        queryDto.setLimit(pageSize);
        queryDto.setIsDesc(1);

        Page<ComponentType> page = componentTypeRepository.getPage(queryDto);

        List<ComponentType> componentTypeList = Lists.newArrayList();
        if(null!=page && CollectionUtils.isNotEmpty(page.getResult())){
            componentTypeList = page.getResult();
        }

        return new Page<>(componentTypeList, page.getTotalItems());
    }

    public boolean delete(List<Integer> ids) {
        componentTypeRepository.removeMulti(ids);
        return true;
    }
}
