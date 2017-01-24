package cn.aiyuedu.bs.back.service;

import cn.aiyuedu.bs.common.Constants.*;
import cn.aiyuedu.bs.common.dto.ResultDto;
import cn.aiyuedu.bs.common.model.ComponentDataBase;
import cn.aiyuedu.bs.common.orm.Page;
import cn.aiyuedu.bs.dao.dto.ComponentDataDto;
import cn.aiyuedu.bs.dao.dto.ComponentDataQueryDto;
import cn.aiyuedu.bs.dao.entity.*;
import cn.aiyuedu.bs.dao.mongo.repository.ComponentDataRepository;
import cn.aiyuedu.bs.dao.mongo.repository.SiteRepository;
import com.google.common.collect.Lists;
import jodd.bean.BeanCopy;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("componentDataService")
public class ComponentDataService {

    @Autowired
    private ComponentDataRepository componentDataRepository;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ContainerService containerService;
    @Autowired
    private BookService bookService;
    @Autowired
    private ComponentService componentService;
    @Autowired
    private ComponentTypeService componentTypeService;
    @Autowired
    private SiteRepository siteRepository;

    public ComponentData get(Integer id) {
        return componentDataRepository.findOne(id);
    }

    public ResultDto checkData(ComponentData componentData) {
        //组件数据中的数据类型和数据
        String data = componentData.getData();
        Integer dataType = componentData.getDataType();

        ResultDto r = new ResultDto();
        r.setSuccess(true);
        //如果数据类型为搜索时,data数据允许为空
        if (dataType != null && (StringUtils.isNotEmpty(data) ||
                (dataType == ComponentDataType.Search.getValue() && StringUtils.isEmpty(data)))) {
            if (dataType == ComponentDataType.Category.getValue()) {
                Category o = categoryService.get(Integer.valueOf(data));
                if (o == null || null==o.getIsUse() || o.getIsUse() != 1) {
                    r.setSuccess(false);
                    r.setInfo("该分类不存在或已禁用！");
                }
            } else if (dataType == ComponentDataType.Container.getValue()) {
                Container o = containerService.get(Integer.valueOf(data));
                if (o == null || null==o.getIsUse() || o.getIsUse() != 1) {
                    r.setSuccess(false);
                    r.setInfo("该页面不存或未发布！");
                }
            } else if (dataType == ComponentDataType.Book.getValue()) {
                Book o = bookService.get(Long.valueOf(data));
                if (o == null || null==o.getStatus() || o.getStatus() != BookStatus.Online.getId()) {
                    r.setSuccess(false);
                    r.setInfo("该书籍不存在或未发布！");
                }else{//存在书籍,则校验当前模块添加书籍的权限
                    //组件
                    Component component = componentService.get(componentData.getComponentId());
                    if(null!=component && null!=component.getContainerId()){
                        //页面
                        Container container = containerService.get(component.getContainerId());
                        if(null!=container && null!=container.getSiteId()){
                            //站点
                            Site site = siteRepository.findOne(container.getSiteId());
                            if(null!=site){
                                ResultDto result = bookService.check(Long.valueOf(data), BookModule.Component, site.getPlatformId());
                                if(null!=result && !result.getSuccess()){
                                    r.setSuccess(false);
                                    r.setInfo(result.getInfo());
                                }
                            }else{
                                r.setSuccess(false);
                                r.setInfo("当前组件数据所在的站点不存在！");
                            }
                        }else{
                            r.setSuccess(false);
                            r.setInfo("当前组件数据所在的页面不存在！");
                        }
                    }else{
                        r.setSuccess(false);
                        r.setInfo("当前组件数据所在的组件不存在！");
                    }
                }
            } else if (dataType == ComponentDataType.Url.getValue()) {
                //TODO url验证
            }
        } else {
            r.setSuccess(false);
            r.setInfo("输入数据异常！");
        }

        return r;
    }

    public boolean add(ComponentData componentData) {
        componentDataRepository.persist(componentData);
        return true;
    }

    public boolean update(ComponentData componentData) {
        //从DB获取更新前的信息
        ComponentData old = componentDataRepository.findOne(componentData.getId());

        //BeanCopy后,old为所要update的信息
        BeanCopy.beans(componentData, old).ignoreNulls(true).copy();

        componentDataRepository.persist(old);

        return true;
    }

    public ResultDto save(ComponentData componentData, AdminUser adminUser) {

        //组件数据所在的组件所在的页面的ID
        Integer containerId = null;

//        ResultDto result = checkData(componentData.getData(), componentData.getDataType());
        ResultDto result = checkData(componentData);

        if (result.getSuccess()) {
            if (componentData.getGroupId() == null) {
                componentData.setGroupId(0);
            }

            if (componentData.getDataType() != null &&
                    StringUtils.isNotEmpty(componentData.getData()) &&
                    StringUtils.isEmpty(componentData.getTitle())) {
                switch (componentData.getDataType()) {
                    case 1://分类
                        Category category = null;

                        //设置标题
                        if (StringUtils.isEmpty(componentData.getTitle())) {
                            category = categoryService.get(Integer.valueOf(componentData.getData()));
                            if (category != null) {
                                componentData.setTitle(category.getName());
                            }
                        }

                        //如果分类组件下的组件数据的图片为null时,则设置其图片为分类的图片
                        if(StringUtils.isBlank(componentData.getLogo())){
                            if(null==category){
                                category = categoryService.get(Integer.valueOf(componentData.getData()));
                            }
                            if(null!=category){
                                componentData.setLogo(category.getLogo());
                            }
                        }

                        break;
                    case 2://页面
                        if (StringUtils.isEmpty(componentData.getTitle())) {
                            Container container = containerService.get(Integer.valueOf(componentData.getData()));
                            if (container != null) {
                                componentData.setTitle(container.getName());
                            }
                        }
                        break;
                    case 3://书
                        Book book = bookService.get(Long.valueOf(componentData.getData()));
                        if (book != null) {
                            if (StringUtils.isEmpty(componentData.getTitle())) {
                                componentData.setTitle(book.getName());
                            }
                        }
                        break;
                }
            }

            List<ComponentDataBase> list = find(componentData.getComponentId(), componentData.getGroupId());
            boolean reorder = false;

            //组件
            Component c = componentService.get(componentData.getComponentId());

            if (componentData.getId() != null) { // update
                //更新前数据
                ComponentDataBase old = get(componentData.getId());

                if (CollectionUtils.isNotEmpty(list)) {
                    if (componentData.getOrderId() > list.size()) {
                        componentData.setOrderId(list.size());
                    }

                    //orderId发生变化
                    if (old != null && old.getOrderId() != componentData.getOrderId()) {
                        reorder = true;
                        //修改的组件数据对象在原来List中的下标
                        int index = -1;
                        if (CollectionUtils.isNotEmpty(list)) {
                            for (int i = 0, size = list.size(); i < size; i++) {
                                if (old.getId().intValue() == list.get(i).getId().intValue()) {
                                    index = i;
                                    break;
                                }
                            }
                            if (index > -1) {
                                //将修改的组件数据对象从原来List中remove
                                list.remove(index);
                            }
                        }
                    }
                } else {
                    componentData.setOrderId(1);
                }

                if (update(componentData)) {
                    result.setSuccess(true);
                    result.setInfo("更新成功！");

                    //对OrderId按顺序重新排序
                    if (reorder) {
                        ComponentDataBase cdb = null;
                        ComponentData cd = null;
                        if (CollectionUtils.isNotEmpty(list)) {
                            for (int i = 0, size = list.size(), order = 1; i < size; i++) {
                                if (order == componentData.getOrderId()) {
                                    order = order + 1;
                                }
                                cdb = list.get(i);
                                cd = new ComponentData(cdb.getId(), order++);
                                update(cd);
                            }
                        }
                    }

                    //获取组件数据所在的组件所在的页面的ID,用于更新页面状态为未发布
                    Component cn = componentService.get(componentData.getComponentId());
                    if(null!=cn){
                        containerId = cn.getContainerId();
                    }

                    //当前组件类型为"推荐书籍"
                    if(CompType.books.val()==c.getTypeId()){
                    }
                } else {
                    result.setInfo("更新失败！");
                }
            } else { // add

                if (c != null) {
                    ComponentType ct = componentTypeService.get(c.getTypeId());
                    if (ct != null) {
                        int currentCount = count(componentData.getComponentId(), null);
                        if (currentCount >= ct.getDataLimit() && ct.getDataLimit() > 0) {
                            result.setInfo("组件数据数量超过限制！");
                            return result;
                        }
                    }
                }

                if (CollectionUtils.isNotEmpty(list)) {
                    if (componentData.getOrderId() > list.size()) {
                        componentData.setOrderId(list.size() + 1);
                    } else {
                        reorder = true;
                    }
                } else {
                    componentData.setOrderId(1);
                }

                if (add(componentData)) {
                    result.setSuccess(true);
                    result.setInfo("保存成功！");

                    //对OrderId按顺序重新排序
                    if (reorder) {
                        ComponentDataBase cdb = null;
                        ComponentData cd = null;
                        for (int i = 0, size = list.size(), order = 1; i < size; i++) {
                            if (order == componentData.getOrderId()) {
                                order = order + 1;
                            }
                            cdb = list.get(i);
                            cd = new ComponentData(cdb.getId(), order++);
                            update(cd);
                        }
                    }

                    //获取组件数据所在的组件所在的页面的ID,用于更新页面状态为未发布
                    containerId = c.getContainerId();

                    //如果当前组件类型为"推荐书籍"并且组件数据未书籍,则需要将书籍信息放到"书籍推荐管理"中
                    if(CompType.books.val()==c.getTypeId() && ComponentDataType.Book.getValue()==componentData.getDataType()){
                        //新增中无法根据ID获取组件数据,但是可以根据组件ID和orderId获取
                        ComponentDataQueryDto queryDto = new ComponentDataQueryDto();
                        queryDto.setComponentId(c.getId());
                        queryDto.setOrderId(componentData.getOrderId());
                        List<ComponentData> cdList = componentDataRepository.find(queryDto);

                        if(CollectionUtils.isNotEmpty(cdList)){
                            ComponentData cd = cdList.get(0);
                            //ResultDto dto = this.saveRecommendLogByCompData(cd, adminUser);

                            //如果保存推荐书籍失败,则设置对应的提示信息
                            /*
                            if(!dto.getSuccess()){
                                result.setInfo(dto.getInfo());
                            }
                            */
                        }
                    }
                } else {
                    result.setInfo("保存失败！");
                }
            }
        }

        //新增或修改组件数据成功后得变更组件数据所在的组件所在页面的发布状态为未发布
        if(result.getSuccess()){
            containerService.changeStatus(containerId, 0);
        }

        return result;
    }

    public List<ComponentDataBase> find(Integer componentId, Integer groupId) {

        ComponentDataQueryDto queryDto = new ComponentDataQueryDto();
        queryDto.setComponentId(componentId);
        queryDto.setGroupId(groupId);
        //按orderId升序查询
        queryDto.setOrderType(2);

        List<ComponentData> cdList = componentDataRepository.find(queryDto);

        List<ComponentDataBase> cdbList = null;
        ComponentDataBase cdb = null;

        if(CollectionUtils.isNotEmpty(cdList)){
            cdbList = Lists.newArrayList();
            for(ComponentData cd : cdList){
                cdb = new ComponentDataBase();
                BeanCopy.beans(cd, cdb).ignoreNulls(false).copy();

                cdbList.add(cdb);
            }
        }

        return cdbList;
    }

    public int count(Integer componentId, Integer groupId) {

        ComponentDataQueryDto queryDto = new ComponentDataQueryDto();
        queryDto.setComponentId(componentId);
        queryDto.setGroupId(groupId);

        return new Long(componentDataRepository.count(queryDto)).intValue();
    }

    public Page<ComponentDataDto> getPage(Integer componentId, Integer groupId, Integer startIndex, Integer pageSize) {

        ComponentDataQueryDto queryDto = new ComponentDataQueryDto();
        queryDto.setComponentId(componentId);
        queryDto.setGroupId(groupId);
        queryDto.setStart(startIndex);
        queryDto.setLimit(pageSize);
        //按orderId升序排列
        queryDto.setOrderType(2);

        Page<ComponentData> page = componentDataRepository.getPage(queryDto);

        ComponentDataDto dto = null;
        String channel = null;
        Category category = null;
        Container container = null;
        Book book = null;
        List<ComponentDataDto> componentDataDtoList = Lists.newArrayList();

        //将ComponentData-->ComponentDataDto,并设置其中Title和Logo的信息
        if(null!=page && CollectionUtils.isNotEmpty(page.getResult())){
            for(ComponentData componentData : page.getResult()){
                dto = new ComponentDataDto();
                BeanCopy.beans(componentData, dto).ignoreNulls(false).copy();

                if (dto.getDataType() != null && StringUtils.isNotEmpty(dto.getData()) &&
                        (StringUtils.isEmpty(dto.getTitle()) || StringUtils.isNotEmpty(dto.getLogo()))) {
                    switch (dto.getDataType()) {
                        case 2://分类
                            if (StringUtils.isEmpty(dto.getTitle())) {
                                category = categoryService.get(Integer.valueOf(dto.getData()));
                                if (category != null) {
                                    dto.setTitle(category.getName());
                                }
                            }
                            break;
                        case 3://页面
                            if (StringUtils.isEmpty(dto.getTitle())) {
                                container = containerService.get(Integer.valueOf(dto.getData()));
                                if (container != null) {
                                    dto.setTitle(container.getName());
                                }
                            }
                            break;
                        case 4://书
                            book = bookService.get(Long.valueOf(dto.getData()));
                            if (book != null) {
                                if (StringUtils.isEmpty(dto.getTitle())) {
                                    dto.setTitle(book.getName());
                                }
                                if (StringUtils.isEmpty(dto.getLogo())) {
                                    dto.setLogo(book.getSmallPic());
                                }
                            }
                            break;
                    }
                }

                componentDataDtoList.add(dto);
            }
        }

        return new Page<>(componentDataDtoList, page.getTotalItems());
    }

    /**
     * Description 删除组件数据,删除后对其排序号重新按从1开始连续排序
     * @param ids
     * @return
     */
    public boolean delete(List<Integer> ids) {

        //获取所在组件和组的ID
        Integer componentId = null;
        Integer groupId = null;
        if(CollectionUtils.isNotEmpty(ids)){
            //获取要删除组件数据中的一个
            ComponentData cd = this.get(ids.get(0));
            if(null!=cd){
                componentId = cd.getComponentId();
                groupId = cd.getGroupId();
            }
        }

        //删除结果
        componentDataRepository.removeMulti(ids);
        boolean isDel = true;

        //如果删除成功则对组件数据从新排序并且更新所在的页面的状态为未发布
        if(isDel && null!=componentId && null!=groupId){
            //获取所在组件下的所有组件数据
            List<ComponentDataBase> list = find(componentId, groupId);
            //重新对orderId按顺序排序
            if(CollectionUtils.isNotEmpty(list)){
                ComponentDataBase cdb = null;
                ComponentData cd = null;
                for (int i = 0, size = list.size(), order = 1; i < size; i++) {
                    cdb = list.get(i);
                    cd = new ComponentData(cdb.getId(), order++);
                    update(cd);
                }
            }

            //获取组件数据所在的组件所在的页面的ID,用于更新页面状态为未发布
            Component cn = componentService.get(componentId);
            if(null!=cn){
                Integer containerId = cn.getContainerId();
                containerService.changeStatus(containerId, 0);
            }

            //如果当前组件类型为推荐书籍
            if(CompType.books.val()==cn.getTypeId()){
                for(Integer id : ids){
                    //recommendLogService.deleteByComponentInfo(1, id.toString(), null);
                }
            }
        }

        return isDel;
    }

}
