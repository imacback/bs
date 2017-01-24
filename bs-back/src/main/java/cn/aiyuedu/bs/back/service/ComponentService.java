package cn.aiyuedu.bs.back.service;

import cn.aiyuedu.bs.dao.entity.*;
import cn.aiyuedu.bs.cache.service.ComponentCacheService;
import cn.aiyuedu.bs.common.Constants;
import cn.aiyuedu.bs.common.dto.ResultDto;
import cn.aiyuedu.bs.common.model.ComponentBase;
import cn.aiyuedu.bs.common.model.ComponentDataBase;
import cn.aiyuedu.bs.common.model.ComponentDataGroupBase;
import cn.aiyuedu.bs.common.orm.Page;
import cn.aiyuedu.bs.dao.dto.ComponentDto;
import cn.aiyuedu.bs.dao.dto.ComponentQueryDto;
import cn.aiyuedu.bs.dao.mongo.repository.ComponentRepository;
import com.google.common.collect.Lists;
import jodd.bean.BeanCopy;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service("componentService")
public class ComponentService {

    @Autowired
    private ComponentRepository componentRepository;
    @Autowired
    private ComponentTypeService componentTypeService;
    @Autowired
    private ComponentDataService componentDataService;
    @Autowired
    private ComponentDataGroupService componentDataGroupService;
    @Autowired
    private ContainerService containerService;
    @Autowired
    private AdminUserService adminUserService;
    @Autowired
    private ComponentCacheService componentCacheService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private BookService bookService;

    public Component get(Integer id) {
        return componentRepository.findOne(id);
    }

    public boolean isExist(Integer id, String name) {

        ComponentQueryDto queryDto = new ComponentQueryDto();
        queryDto.setId(id);
        queryDto.setIsNEId(1);
        queryDto.setName(name);

        return componentRepository.count(queryDto) > 0;
    }

    public boolean add(Component component) {
        componentRepository.persist(component);
        return true;
    }

    public boolean update(Component component) {

        //从DB获取更新前的信息
        Component old = componentRepository.findOne(component.getId());

        //BeanCopy后,old为所要update的信息
        BeanCopy.beans(component, old).ignoreNulls(true).copy();

        componentRepository.persist(old);

        return true;
    }

    public ResultDto save(Component component, AdminUser user) {
        ResultDto result = new ResultDto();
        result.setSuccess(false);
        result.setInfo("更新成功！");
        Date date = new Date();
        component.setEditDate(date);
        component.setEditorId(user.getId());

        //所在页面ID
        Integer containerId = null;

        //同一页面下的所有组件
        List<Component> list = null;

        //是否需要重新排序
        boolean reorder = false;

        if (component.getId() != null) {//update
            //修改前
            Component old = get(component.getId());

            if(null!=old){
                containerId = old.getContainerId();
                if(null!=containerId){//如果不做非空校验,则会取出系统中所有的组件
                    //因为修改时,页面上将所属的页面变成disable,所以要根据old取所属的页面
                    list = find(null, null, containerId);
                }
            }

            if (CollectionUtils.isNotEmpty(list)) {
                if (null==component.getOrderId() || component.getOrderId() > list.size()) {
                    component.setOrderId(list.size());
                }

                //orderId发生变化
                if (old != null && old.getOrderId() != component.getOrderId()) {
                    reorder = true;
                    int index = -1;
                    if (CollectionUtils.isNotEmpty(list)) {
                        for (int i = 0, size = list.size(); i < size; i++) {
                            if (old.getId().intValue() == list.get(i).getId().intValue()) {
                                index = i;
                                break;
                            }
                        }
                        if (index > -1) {
                            list.remove(index);
                        }
                    }
                }
            } else {
                component.setOrderId(1);
            }

            if (update(component)) {
                result.setSuccess(true);
                result.setInfo("更新成功！");

                ///对OrderId按顺序重新排序
                if (reorder) {
                    Component q,c;
                    if (CollectionUtils.isNotEmpty(list)) {
                        for (int i = 0, size = list.size(), order = 1; i < size; i++) {
                            if (order == component.getOrderId()) {
                                order = order + 1;
                            }
                            q = list.get(i);
                            c = new Component(q.getId(), order++);
                            update(c);
                        }
                    }
                }
            } else {
                result.setInfo("更新失败！");
            }
        } else {//insert
            component.setCreateDate(date);
            component.setCreatorId(user.getId());

            list = find(null, null, component.getContainerId());

            if (CollectionUtils.isNotEmpty(list)) {
                if (null==component.getOrderId() || component.getOrderId() > list.size()) {
                    component.setOrderId(list.size() + 1);
                } else {
                    reorder = true;
                }
            } else {
                component.setOrderId(1);
            }

            if (add(component)) {
                result.setSuccess(true);
                result.setInfo("保存成功！");

                //对OrderId按顺序重新排序
                if (reorder) {
                    Component q, c;
                    for (int i = 0, size = list.size(), order = 1; i < size; i++) {
                        if (order == component.getOrderId()) {
                            order = order + 1;
                        }
                        q = list.get(i);
                        c = new Component(q.getId(), order++);
                        update(c);
                    }
                }

                //获取所在页面ID,用于更新页面状态为未发布
                containerId = component.getContainerId();
            } else {
                result.setInfo("保存失败！");
            }
        }

        //新增或修改组件成功后得变更组件所在页面的发布状态为未发布
        if(result.getSuccess()){
            containerService.changeStatus(containerId, 0);
        }

        return result;
    }

    public List<Component> find(String name, Integer isUse, Integer containerId) {

        ComponentQueryDto queryDto = new ComponentQueryDto();
        queryDto.setName(name);
        queryDto.setIsLikeName(1);
        queryDto.setIsUse(isUse);
        queryDto.setContainerId(containerId);
        //按orderId字段升序查询
        queryDto.setOrderType(2);

        return componentRepository.find(queryDto);
    }

    public Page<ComponentDto> getPage(String name, Integer isUse, Integer containerId, Integer startIndex, Integer pageSize) {

        ComponentQueryDto queryDto = new ComponentQueryDto();
        queryDto.setName(name);
        queryDto.setIsLikeName(1);
        queryDto.setIsUse(isUse);
        queryDto.setContainerId(containerId);
        queryDto.setStart(startIndex);
        queryDto.setLimit(pageSize);
        //按orderId字段升序查询
        queryDto.setOrderType(2);

        Page<Component> page = componentRepository.getPage(queryDto);

        ComponentDto dto = null;
        ComponentType ct = null;
        Container c = null;
        List<ComponentDto> componentDtoList = Lists.newArrayList();

        //将Component-->ComponentDto,并设置组件类型名称、页面名称
        if(null!=page && CollectionUtils.isNotEmpty(page.getResult())){
            for(Component component : page.getResult()){
                dto = new ComponentDto();
                BeanCopy.beans(component, dto).ignoreNulls(false).copy();

                //设置创建者和修改者的名称
                adminUserService.infoOperate(dto);

                //组件类型
                if (dto.getTypeId() != null) {
                    ct = componentTypeService.get(dto.getTypeId());
                    if (ct != null) {
                        dto.setTypeName(ct.getName());
                        dto.setDataGroup(ct.getDataGroup());
                        dto.setDataLimit(ct.getDataLimit());
                    }
                }

                //页面
                if (dto.getContainerId() != null) {
                    c = containerService.get(dto.getContainerId());
                    if (c != null) {
                        dto.setContainerName(c.getName());
                    }
                }

                componentDtoList.add(dto);
            }
        }

        return new Page<>(componentDtoList, page.getTotalItems());
    }

    public boolean delete(List<Integer> ids) {

        componentRepository.removeMulti(ids);

        return true;
    }

    public ResultDto publishCheck(Integer componentId, AdminUser adminUser) {
        ResultDto r = new ResultDto();
        r.setSuccess(false);
        r.setInfo("发布失败！");
        Component c = get(componentId);

        ComponentDataBase entry = new ComponentDataBase();
        entry.setTitle(c.getEntryTitle());
        entry.setDataType(c.getEntryDataType());
        entry.setData(c.getEntryData());
        c.setEntry(entry);

        if (c != null && c.getIsUse() == 1) {
            ComponentType ct = componentTypeService.get(c.getTypeId());
            if (ct != null) {
                int dataGroup = ct.getDataGroup();
                int dataLimit = ct.getDataLimit();

                List<ComponentDataBase> data = null;
                if (dataGroup == 1) {
                    data = componentDataService.find(componentId, null);
                    if (CollectionUtils.isEmpty(data)) {
                        r.setInfo("组件下没有数据！");
                    } else if (data.size() > dataLimit && dataLimit > 0) {
                        r.setInfo("组件下数据超过数量限制！");
                    } else {
                        r.setSuccess(true);
                        c.setData(data);
                    }
                } else {
                    List<ComponentDataGroupBase> groups = componentDataGroupService.find(componentId);
                    if (CollectionUtils.isNotEmpty(groups)) {
                        for (ComponentDataGroupBase g : groups) {
                            data = componentDataService.find(componentId, g.getId());
                            if (CollectionUtils.isEmpty(data)) {
                                r.setSuccess(false);
                                r.setInfo("组件分组[" + g.getTitle() + "]下没有数据！");
                                break;
                            } else if (data.size() > dataLimit && dataLimit > 0) {
                                r.setSuccess(false);
                                r.setInfo("组件分组[" + g.getTitle() + "]下数据超过数量限制！");
                                break;
                            } else {
                                r.setSuccess(true);
                                g.setList(checkData(data));
                            }
                        }
                        c.setGroups(groups);
                    } else {
                        r.setInfo("组件下没有分组数据！");
                    }
                }
            }

            if (r.getSuccess()) {
                c.setEditDate(new Date());
                if (adminUser != null) {
                    c.setEditorId(adminUser.getId());
                }
                update(c);
                componentCacheService.publish(c);

                r.setInfo("发布成功！");
            }


        } else {
            r.setInfo("组件不存在或不可用！");
        }

        return r;
    }

    public List<ComponentDataBase> checkData(List<ComponentDataBase> list) {
        Category category = null;
        Container container = null;
        Book book = null;
        for (ComponentDataBase c : list) {
            if (StringUtils.isEmpty(c.getTitle()) && StringUtils.isNotEmpty(c.getData())) {
                if (c.getDataType() == Constants.ComponentDataType.Category.getValue()) {
                    if (StringUtils.isNumeric(c.getData())) {
                        category = categoryService.get(Integer.valueOf(c.getData()));
                        if (category != null) {
                            c.setTitle(category.getName());
                        }
                    }
                } else if (c.getDataType() == Constants.ComponentDataType.Container.getValue()) {
                    if (StringUtils.isNumeric(c.getData())) {
                        container = containerService.get(Integer.valueOf(c.getData()));
                        if (container != null) {
                            c.setTitle(container.getName());
                        }
                    }
                } else if (c.getDataType() == Constants.ComponentDataType.Book.getValue()) {
                    if (StringUtils.isNumeric(c.getData())) {
                        book = bookService.get(Long.valueOf(c.getData()));
                        if (book != null) {
                            c.setTitle(book.getName());
                            if (StringUtils.isEmpty(c.getLogo())) {
                                c.setLogo(book.getLargePic());
                            }
                        }
                    }
                }
            }
        }

        return list;
    }

    public ComponentBase getPreviewComponent(Integer componentId) {
        Component c = get(componentId);

        if (c != null) {
            ComponentDataBase entry = new ComponentDataBase();
            entry.setTitle(c.getEntryTitle());
            entry.setDataType(c.getEntryDataType());
            entry.setData(c.getEntryData());
            c.setEntry(entry);

            ComponentType ct = componentTypeService.get(c.getTypeId());
            if (ct != null) {
                int dataGroup = ct.getDataGroup();

                List<ComponentDataBase> data = null;
                if (dataGroup == 1) {
                    data = componentDataService.find(componentId, null);
                    if (CollectionUtils.isNotEmpty(data)) {
                        c.setData(checkData(data));
                    }
                } else {
                    List<ComponentDataGroupBase> groups = componentDataGroupService.find(componentId);
                    if (CollectionUtils.isNotEmpty(groups)) {
                        for (ComponentDataGroupBase g : groups) {
                            data = componentDataService.find(componentId, g.getId());
                            if (CollectionUtils.isNotEmpty(data)) {
                                g.setList(checkData(data));
                            }
                        }
                        c.setGroups(groups);
                    }
                }
            }
        }

        return c;
    }
}
