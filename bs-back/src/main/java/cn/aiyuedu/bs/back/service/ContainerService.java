package cn.aiyuedu.bs.back.service;

import cn.aiyuedu.bs.cache.service.ComponentCacheService;
import cn.aiyuedu.bs.cache.service.ContainerCacheService;
import cn.aiyuedu.bs.common.dto.ResultDto;
import cn.aiyuedu.bs.common.model.ComponentBase;
import cn.aiyuedu.bs.common.model.ComponentDataBase;
import cn.aiyuedu.bs.common.model.ComponentDataGroupBase;
import cn.aiyuedu.bs.common.orm.Page;
import cn.aiyuedu.bs.dao.dto.ContainerDto;
import cn.aiyuedu.bs.dao.dto.ContainerQueryDto;
import cn.aiyuedu.bs.dao.entity.AdminUser;
import cn.aiyuedu.bs.dao.entity.Component;
import cn.aiyuedu.bs.dao.entity.ComponentType;
import cn.aiyuedu.bs.dao.entity.Container;
import cn.aiyuedu.bs.dao.mongo.repository.ContainerRepository;
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

@Service("containerService")
public class ContainerService {

    @Autowired
    private ContainerRepository containerRepository;
    @Autowired
    private ComponentService componentService;
    @Autowired
    private ComponentTypeService componentTypeService;
    @Autowired
    private ComponentDataService componentDataService;
    @Autowired
    private ComponentDataGroupService componentDataGroupService;
    @Autowired
    private ComponentCacheService componentCacheService;
    @Autowired
    private ContainerCacheService containerCacheService;
    @Autowired
    private AdminUserService adminUserService;

    private Map<Integer, ContainerDto> containerMap;

    @PostConstruct
    public synchronized void reload() {
        containerMap = Maps.newHashMap();

        List<Container> list = containerRepository.find(null);
        if(CollectionUtils.isNotEmpty(list)){
            ContainerDto dto = null;
            for(Container c : list){
                dto = new ContainerDto();
                BeanCopy.beans(c, dto).ignoreNulls(false).copy();
                containerMap.put(dto.getId(), dto);
            }
        }
    }

    public ContainerDto get(Integer id) {
        return containerMap.get(id);
    }

    public boolean isExist(Integer id, String name) {

        ContainerQueryDto queryDto = new ContainerQueryDto();
        queryDto.setId(id);
        queryDto.setIsNEId(1);
        queryDto.setName(name);
        return containerRepository.count(queryDto) > 0;
    }

    public boolean add(Container container) {
        containerRepository.persist(container);
        return true;
    }

    public boolean update(Container container) {
        //从DB获取更新前的信息
        Container old = containerRepository.findOne(container.getId());

        //BeanCopy后,old为所要update的信息
        BeanCopy.beans(container, old).ignoreNulls(true).copy();

        containerRepository.persist(old);

        return true;
    }

    public ResultDto save(Container container, AdminUser user) {
        ResultDto result = new ResultDto();
        result.setSuccess(false);
        result.setInfo("更新成功！");
        container.setEditDate(new Date());
        container.setEditorId(user.getId());

        if (container.getId() != null) {//update
            if (!isExist(container.getId(), container.getName())) {
                if (update(container)) {
                    result.setSuccess(true);
                    result.setInfo("更新成功！");
                } else {
                    result.setInfo("更新失败！");
                }
            } else {
                result.setInfo("数据已存在");
            }
        } else {//insert
            container.setCreateDate(new Date());
            container.setCreatorId(user.getId());

            if (!isExist(container.getId(), container.getName())) {
                if (add(container)) {
                    result.setSuccess(true);
                    result.setInfo("保存成功！");
                } else {
                    result.setInfo("保存失败！");
                }
            } else {
                result.setInfo("数据已存在");
            }
        }

        if (result.getSuccess()) {
            reload();
        }

        return result;
    }

    public Page<ContainerDto> getPage(String name, Integer isUse, Integer startIndex, Integer pageSize) {

        ContainerQueryDto queryDto = new ContainerQueryDto();
        //name按like进行查询
        queryDto.setName(name);
        queryDto.setIsLikeName(1);
        queryDto.setIsUse(isUse);
        queryDto.setStart(startIndex);
        queryDto.setLimit(pageSize);
        //结果按id降序
        queryDto.setIsDesc(1);

        Page<Container> page = containerRepository.getPage(queryDto);

        ContainerDto dto = null;
        List<ContainerDto> containerDtoList = Lists.newArrayList();

        //将Container-->ContainerDto,并设置创建人和修改人名称
        if(null!=page && CollectionUtils.isNotEmpty(page.getResult())){
            for(Container container : page.getResult()){
                dto = new ContainerDto();
                BeanCopy.beans(container, dto).ignoreNulls(false).copy();

                //设置创建者和修改者的名称
                adminUserService.infoOperate(dto);

                containerDtoList.add(dto);
            }
        }

        return new Page<>(containerDtoList, page.getTotalItems());
    }

    public boolean delete(List<Integer> ids) {
        containerRepository.removeMulti(ids);
        return true;
    }

    public ResultDto publish(Integer containerId) {
        ResultDto r = new ResultDto();
        r.setSuccess(false);
        r.setInfo("数据发布失败！");

        if (containerId != null) {
            ContainerDto container = get(containerId);
            if (container != null) {
                List<Component> list = componentService.find(null, 1, containerId);
                if (CollectionUtils.isEmpty(list)) {
                    r.setSuccess(false);
                    r.setInfo("该页面下没有任何组件数据，请核实！");

                    return r;
                } else {
                    List<ComponentBase> componentBases = Lists.newArrayList();
                    ComponentDataBase entry = null;
                    ComponentType ct = null;
                    List<ComponentDataGroupBase> groups = null;
                    for (Component c : list) {

                        entry = new ComponentDataBase();
                        entry.setTitle(c.getEntryTitle());
                        entry.setDataType(c.getEntryDataType());
                        entry.setData(c.getEntryData());
                        c.setEntry(entry);

                        ct = componentTypeService.get(c.getTypeId());
                        if (ct != null) {
                            int dataGroup = ct.getDataGroup();
                            int dataLimit = ct.getDataLimit();

                            List<ComponentDataBase> data = null;
                            if (dataGroup == 1) {
                                data = componentDataService.find(c.getId(), null);
                                if (CollectionUtils.isEmpty(data)) {
                                    r.setSuccess(false);
                                    r.setInfo("组件["+c.getName()+"]下没有数据！");
                                    return r;
                                } else if (data.size() > dataLimit && dataLimit > 0) {
                                    r.setSuccess(false);
                                    r.setInfo("组件["+c.getName()+"]下数据超过数量限制！");
                                    return r;
                                } else {
                                    r.setSuccess(true);
                                    c.setData(data);
                                }
                            } else {
                                groups = componentDataGroupService.find(c.getId());
                                if (CollectionUtils.isNotEmpty(groups)) {
                                    for (ComponentDataGroupBase g : groups) {
                                        data = componentDataService.find(c.getId(), g.getId());
                                        if (CollectionUtils.isEmpty(data)) {
                                            r.setSuccess(false);
                                            r.setInfo("组件["+c.getName()+"]分组[" + g.getTitle() + "]下没有数据！");
                                            return r;
                                        } else if (data.size() > dataLimit && dataLimit > 0) {
                                            r.setSuccess(false);
                                            r.setInfo("组件" + c.getName() + "分组[" + g.getTitle() + "]下数据超过数量限制！");
                                            return r;
                                        } else {
                                            g.setList(data);
                                        }
                                    }
                                    c.setGroups(groups);
                                } else {
                                    r.setSuccess(false);
                                    r.setInfo("组件下没有分组数据！");
                                    return r;
                                }
                            }
                        }

                        componentBases.add(c);
                    }

                    container.setComponents(componentBases);
                    containerCacheService.publish(container);

                    container.setStatus(1);
                    update(container);

                    r.setSuccess(true);
                    r.setInfo("发布成功！");
                }
            }
        }

        return r;
    }

    public Container getPreviewContainer(Integer containerId) {
        if (containerId != null) {
            Container container = get(containerId);
            if (container != null) {
                //页面下的组件
                List<Component> list = componentService.find(null, 1, containerId);

                if (CollectionUtils.isNotEmpty(list)) {
                    List<ComponentBase> components = Lists.newArrayList();
                    ComponentBase component = null;
                    for (Component c : list) {
                        component = componentService.getPreviewComponent(c.getId());
                        components.add(component);
                    }

                    if (CollectionUtils.isNotEmpty(components)) {
                        container.setComponents(components);
                    }
                }
            }

            return container;
        }

        return null;
    }

    public boolean changeStatus(Integer containerId, Integer status) {
        Container c = new Container();
        c.setId(containerId);
        c.setStatus(status);
        return update(c);
    }
}
