package cn.aiyuedu.bs.back.service;

import cn.aiyuedu.bs.common.dto.ResultDto;
import cn.aiyuedu.bs.common.model.ComponentDataGroupBase;
import cn.aiyuedu.bs.common.orm.Page;
import cn.aiyuedu.bs.dao.dto.ComponentDataGroupDto;
import cn.aiyuedu.bs.dao.dto.ComponentDataGroupQueryDto;
import cn.aiyuedu.bs.dao.entity.AdminUser;
import cn.aiyuedu.bs.dao.entity.Component;
import cn.aiyuedu.bs.dao.entity.ComponentDataGroup;
import cn.aiyuedu.bs.dao.mongo.repository.ComponentDataGroupRepository;
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

@Service("componentDataGroupService")
public class ComponentDataGroupService {

    @Autowired
    private ComponentDataGroupRepository componentDataGroupRepository;
    @Autowired
    private AdminUserService adminUserService;
    @Autowired
    private ComponentService componentService;
    @Autowired
    private ContainerService containerService;

    private Map<Integer, ComponentDataGroupBase> componentDataGroupBaseMap;

    @PostConstruct
    public synchronized void reload() {
        componentDataGroupBaseMap = Maps.newHashMap();

        List<ComponentDataGroup> cdgList = componentDataGroupRepository.find(null);
        if(CollectionUtils.isNotEmpty(cdgList)){
            ComponentDataGroupBase base = null;
            for(ComponentDataGroup cdg : cdgList){
                base = new ComponentDataGroupBase();
                BeanCopy.beans(cdg, base).ignoreNulls(false).copy();
                componentDataGroupBaseMap.put(base.getId(), base);
            }
        }
    }

    public ComponentDataGroupBase get(Integer id) {
        return componentDataGroupBaseMap.get(id);
    }

    public boolean add(ComponentDataGroup componentDataGroup) {

        componentDataGroupRepository.persist(componentDataGroup);
        return true;
    }

    public boolean update(ComponentDataGroup componentDataGroup) {

        //从DB获取更新前的信息
        ComponentDataGroup old = componentDataGroupRepository.findOne(componentDataGroup.getId());

        //BeanCopy后,old为所要update的信息
        BeanCopy.beans(componentDataGroup, old).ignoreNulls(true).copy();

        componentDataGroupRepository.persist(old);

        return true;
    }

    public ResultDto save(ComponentDataGroup componentDataGroup, AdminUser user) {

        //所在的页面的ID
        Integer containerId = null;

        ResultDto result = new ResultDto();
        result.setSuccess(false);
        result.setInfo("更新成功！");
        componentDataGroup.setEditDate(new Date());
        componentDataGroup.setEditorId(user.getId());

        //对应组件下的所有分组
        List<ComponentDataGroupBase> list = this.find(componentDataGroup.getComponentId());
        boolean reorder = false;

        if (componentDataGroup.getId() != null) {//update
            //更新前数据
            ComponentDataGroup old = componentDataGroupRepository.findOne(componentDataGroup.getId());

            if (CollectionUtils.isNotEmpty(list)) {
                if (componentDataGroup.getOrderId() > list.size()) {
                    componentDataGroup.setOrderId(list.size());
                }

                //orderId发生变化
                if (old != null && old.getOrderId() != componentDataGroup.getOrderId()) {
                    reorder = true;
                    //修改的组件组数据对象在原来List中的下标
                    int index = -1;
                    if (CollectionUtils.isNotEmpty(list)) {
                        for (int i = 0, size = list.size(); i < size; i++) {
                            if (old.getId().intValue() == list.get(i).getId().intValue()) {
                                index = i;
                                break;
                            }
                        }
                        if (index > -1) {
                            //将修改的组件组数据对象从原来List中remove
                            list.remove(index);
                        }
                    }
                }
            }else{
                componentDataGroup.setOrderId(1);
            }

            if (update(componentDataGroup)) {
                result.setSuccess(true);
                result.setInfo("更新成功！");

                //对OrderId按顺序重新排序
                if (reorder) {
                    ComponentDataGroupBase cdgb = null;
                    ComponentDataGroup cdg = null;
                    if (CollectionUtils.isNotEmpty(list)) {
                        for (int i = 0, size = list.size(), order = 1; i < size; i++) {
                            if (order == componentDataGroup.getOrderId()) {
                                order = order + 1;
                            }
                            cdgb = list.get(i);
                            cdg = new ComponentDataGroup(cdgb.getId(), order++);
                            update(cdg);
                        }
                    }
                }

                //获取所在页面ID,用于更新页面状态为未发布
                Component cn = componentService.get(componentDataGroup.getComponentId());
                containerId = cn.getContainerId();

            } else {
                result.setInfo("更新失败！");
            }
        } else {//insert
            componentDataGroup.setCreateDate(new Date());
            componentDataGroup.setCreatorId(user.getId());

            if (CollectionUtils.isNotEmpty(list)) {
                if (componentDataGroup.getOrderId() > list.size()) {
                    componentDataGroup.setOrderId(list.size() + 1);
                } else {
                    reorder = true;
                }
            } else {
                componentDataGroup.setOrderId(1);
            }


            if (add(componentDataGroup)) {
                result.setSuccess(true);
                result.setInfo("保存成功！");

                //对OrderId按顺序重新排序
                if (reorder) {
                    ComponentDataGroupBase cdgb = null;
                    ComponentDataGroup cdg = null;
                    for (int i = 0, size = list.size(), order = 1; i < size; i++) {
                        if (order == componentDataGroup.getOrderId()) {
                            order = order + 1;
                        }
                        cdgb = list.get(i);
                        cdg = new ComponentDataGroup(cdgb.getId(), order++);
                        update(cdg);
                    }
                }

                //获取所在页面ID,用于更新页面状态为未发布
                Component cn = componentService.get(componentDataGroup.getComponentId());
                containerId = cn.getContainerId();
            } else {
                result.setInfo("保存失败！");
            }
        }

        if (result.getSuccess()){
            reload();
            //新增或修改成功后得变更所在页面的发布状态为未发布
            containerService.changeStatus(containerId, 0);
        }

        return result;
    }

    public List<ComponentDataGroupBase> find(Integer componentId) {

        ComponentDataGroupQueryDto queryDto = new ComponentDataGroupQueryDto();
        queryDto.setComponentId(componentId);
        queryDto.setOrderType(2);

        List<ComponentDataGroup> cdgList = componentDataGroupRepository.find(queryDto);

        List<ComponentDataGroupBase> cdgbList = null;
        ComponentDataGroupBase cdgb = null;

        if(CollectionUtils.isNotEmpty(cdgList)){
            cdgbList = Lists.newArrayList();
            for(ComponentDataGroup cdg : cdgList){
                cdgb = new ComponentDataGroupBase();
                BeanCopy.beans(cdg, cdgb).ignoreNulls(false).copy();

                cdgbList.add(cdgb);
            }
        }

        return cdgbList;
    }

    public Page<ComponentDataGroupDto> getPage(Integer componentId, Integer startIndex, Integer pageSize) {

        ComponentDataGroupQueryDto queryDto = new ComponentDataGroupQueryDto();
        queryDto.setComponentId(componentId);
        queryDto.setStart(startIndex);
        queryDto.setLimit(pageSize);
        queryDto.setOrderType(2);

        Page<ComponentDataGroup> page = componentDataGroupRepository.getPage(queryDto);

        ComponentDataGroupDto dto = null;
        List<ComponentDataGroupDto> componentDataGroupDtoList = Lists.newArrayList();

        //将ComponentDataGroup-->ComponentDataGroupDto,并设置创建者和修改者的信息
        if(null!=page && CollectionUtils.isNotEmpty(page.getResult())){
            for(ComponentDataGroup componentDataGroup : page.getResult()){
                dto = new ComponentDataGroupDto();
                BeanCopy.beans(componentDataGroup, dto).ignoreNulls(false).copy();

                adminUserService.infoOperate(dto);

                componentDataGroupDtoList.add(dto);
            }
        }

        return new Page<>(componentDataGroupDtoList, page.getTotalItems());
    }

    public boolean delete(List<Integer> ids) {

        //所在组件的ID
        Integer componentId = null;
        if(CollectionUtils.isNotEmpty(ids)){
            //获取要删除组件组中的一个
            ComponentDataGroup cdg = componentDataGroupRepository.findOne(ids.get(0));
            if(null!=cdg){
                componentId = cdg.getComponentId();
            }
        }

        //删除结果
        componentDataGroupRepository.removeMulti(ids);
        boolean isDel = true;


        //删除成功,则需变更所在页面的发布状态为未发布
        if(isDel && null!=componentId){
            //对应组件下的所有分组
            List<ComponentDataGroupBase> list = this.find(componentId);
            //重新对orderId按顺序排序
            if(CollectionUtils.isNotEmpty(list)){
                ComponentDataGroupBase cdgb = null;
                ComponentDataGroup cdg = null;
                for (int i = 0, size = list.size(), order = 1; i < size; i++) {
                    cdgb = list.get(i);
                    cdg = new ComponentDataGroup(cdgb.getId(), order++);
                    update(cdg);
                }
            }

            //获取组件数据所在的组件所在的页面的ID,用于更新页面状态为未发布
            Component cn = componentService.get(componentId);
            if(null!=cn){
                containerService.changeStatus(cn.getContainerId(), 0);
            }

        }

        return isDel;
    }
}
