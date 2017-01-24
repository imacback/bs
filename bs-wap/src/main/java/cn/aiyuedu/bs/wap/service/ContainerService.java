package cn.aiyuedu.bs.wap.service;

import cn.aiyuedu.bs.cache.service.ContainerCacheService;
import cn.aiyuedu.bs.common.Constants;
import cn.aiyuedu.bs.common.model.ComponentBase;
import cn.aiyuedu.bs.common.model.ComponentDataBase;
import cn.aiyuedu.bs.common.model.ContainerBase;
import com.google.common.collect.Lists;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Description:
 *
 * @author yz.wu
 */
@Service("containerService")
public class ContainerService {

    @Autowired
    private ContainerCacheService containerCacheService;
    @Autowired
    private BookService bookService;

    public ContainerBase getContainer(Integer id) {
        ContainerBase container = containerCacheService.get(id);
        if (container != null && CollectionUtils.isNotEmpty(container.getComponents())) {
            Integer typeId;
            List<ComponentDataBase> componentDataBases;
            List<String> bookIds;
            List<ComponentBase> componentBases = Lists.newArrayList();
            for (ComponentBase component : container.getComponents()) {
                typeId = component.getTypeId();
                if (typeId == 4 || typeId == 5 || typeId == 6) {
                    componentDataBases = component.getData();
                    if (CollectionUtils.isNotEmpty(componentDataBases)) {
                        bookIds = Lists.newArrayList();
                        for (ComponentDataBase componentDataBase : componentDataBases) {
                            if (componentDataBase.getDataType() == Constants.ComponentDataType.Book.getValue()) {
                                bookIds.add(componentDataBase.getData());
                            }
                        }
                        if (CollectionUtils.isNotEmpty(bookIds)) {
                            component.setBooks(bookService.getBooks(bookIds));
                        }
                    }
                }

                componentBases.add(component);
            }

            container.setComponents(componentBases);
        }

        return container;
    }
}
