package cn.aiyuedu.bs.back.service;

import cn.aiyuedu.bs.back.dto.CategoryTagDto;
import cn.aiyuedu.bs.cache.service.CategoryCacheService;
import cn.aiyuedu.bs.common.dto.ResultDto;
import cn.aiyuedu.bs.common.model.CategoryBase;
import cn.aiyuedu.bs.common.orm.Page;
import cn.aiyuedu.bs.dao.dto.BookQueryDto;
import cn.aiyuedu.bs.dao.dto.CategoryDto;
import cn.aiyuedu.bs.dao.dto.CategoryQueryDto;
import cn.aiyuedu.bs.dao.entity.AdminUser;
import cn.aiyuedu.bs.dao.entity.Category;
import cn.aiyuedu.bs.dao.entity.Tag;
import cn.aiyuedu.bs.dao.mongo.repository.CategoryRepository;
import cn.aiyuedu.bs.dao.util.BeanCopierUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.common.collect.Sets.SetView;
import jodd.bean.BeanCopy;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;

/**
 * Description:
 *
 * @author yz.wu
 */
@Service("categoryService")
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private CategoryCacheService categoryCacheService;
    @Autowired
    private AdminUserService adminUserService;
    @Autowired
    private TagService tagService;
    @Autowired
    private BookService bookService;

    private Map<Integer, Category> map;
    private Map<String, List<CategoryTagDto>> categoryTagMap;

    @PostConstruct
    public synchronized void reload() {
        map = Maps.newHashMap();
        CategoryQueryDto queryDto = new CategoryQueryDto();
        List<Category> list = find(queryDto);

        categoryTagMap = Maps.newHashMap();
        List<CategoryTagDto> categoryTagDtos = null;
        CategoryTagDto categoryTagDto = null;
        String key = null;

        for (Category o : list) {
            if (o != null && o.getIsLeaf() != null && o.getIsLeaf() == 1) {
                map.put(o.getId(), o);
                key = getTagKey(o.getTagClassifyId(), o.getTagSexId());
                categoryTagDtos = categoryTagMap.get(key);
                if (CollectionUtils.isEmpty(categoryTagDtos)) {
                    categoryTagDtos = Lists.newArrayList();
                }
                categoryTagDtos.add(createCategoryTagDto(o));
                categoryTagMap.put(key, categoryTagDtos);
            }
        }
    }

    public CategoryTagDto createCategoryTagDto(Category category) {
        CategoryTagDto c = new CategoryTagDto();
        c.setTagMap(parseTags(category.getTagContentIds()));
        c.getTagMap().putAll(parseTags(category.getTagSupplyIds()));
        c.setCategoryId(category.getId());
        return c;
    }

    public Map<Integer, Set<Integer>> parseTags(List<Integer> ids) {
        Map<Integer, Set<Integer>> map = Maps.newHashMap();
        Set<Integer> set = null;
        if (CollectionUtils.isNotEmpty(ids)) {
            Tag t = null;
            Tag pt = null;
            for (Integer id : ids) {
                t = tagService.get(id);
                if (t != null) {
                    pt = tagService.get(t.getParentId());
                    if (pt != null &&
                            StringUtils.isNotBlank(pt.getScope()) &&
                            pt.getScope().startsWith("0")) {
                        continue;
                    }

                    set = map.get(t.getParentId());
                    if (CollectionUtils.isEmpty(set)) {
                        set = Sets.newHashSet();
                    }
                    set.add(t.getId());
                    map.put(t.getParentId(), set);
                }
            }
        }
        return map;
    }

    public String getTagKey(Integer tagClassifyId, Integer tagSexId) {
        return tagClassifyId + ":" + tagSexId;
    }

    public Category get(Integer id) {
        return categoryRepository.findOne(id);
    }

    public boolean isExist(Integer id, String name) {
        return categoryRepository.exist(id, name);
    }

    public void save(Category category) {
        categoryRepository.persist(category);
    }

    public List<Category> find(CategoryQueryDto categoryQueryDto) {
        //按分类的orderId升序查找
        categoryQueryDto.setIsDesc(0);
        categoryQueryDto.setOrderType(2);
        return categoryRepository.find(categoryQueryDto);
    }

    public Page<CategoryDto> getPage(CategoryQueryDto categoryQueryDto) {
        Page<Category> page = categoryRepository.getPage(categoryQueryDto);
        List<CategoryDto> result = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(page.getResult())) {
            CategoryDto cd = null;

            AdminUser u = null;
            Category cp = null;
            Tag t = null;
            Tag p = null;
            StringBuilder sb = new StringBuilder();

            for (Category c : page.getResult()) {
                cd = new CategoryDto();
                BeanCopierUtils.categoryCopy(c, cd);

                if (categoryQueryDto.getStart() != null) {
                    if (cd.getParentId() != null) {
                        cp = get(cd.getParentId());
                        if (cp != null) {
                            cd.setParentName(cp.getName());
                        }
                    }
                    adminUserService.infoOperate(cd);

                    t = tagService.get(cd.getTagClassifyId());
                    if (t != null) {
                        p = tagService.get(t.getParentId());
                        if (p != null) {
                            cd.setTagClassifyName(p.getName() + "-" + t.getName());
                        }
                    }

                    t = tagService.get(cd.getTagSexId());
                    if (t != null) {
                        cd.setTagSexName(t.getName());
                    }

                    sb.setLength(0);
                    if (CollectionUtils.isNotEmpty(cd.getTagContentIds())) {
                        for (int i = 0, size = cd.getTagContentIds().size(); i < size; i++) {
                            t = tagService.get(cd.getTagContentIds().get(i));
                            if (t != null) {
                                if (i > 0) {
                                    sb.append(",");
                                }
                                sb.append(t.getName());
                            }
                            cd.setTagContentNames(sb.toString());
                        }
                    }

                    sb.setLength(0);
                    if (CollectionUtils.isNotEmpty(cd.getTagSupplyIds())) {
                        for (int i = 0, size = cd.getTagSupplyIds().size(); i < size; i++) {
                            t = tagService.get(cd.getTagSupplyIds().get(i));
                            if (t != null) {
                                if (i > 0) {
                                    sb.append(",");
                                }
                                sb.append(t.getName());
                            }
                            cd.setTagSupplyNames(sb.toString());
                        }
                    }
                }

                result.add(cd);
            }
        }

        return new Page<>(result, page.getTotalItems());

    }

    public ResultDto delete(List<Integer> ids) {
        ResultDto result = new ResultDto();
        result.setSuccess(false);
        result.setInfo("删除失败！");
        if (CollectionUtils.isNotEmpty(ids)) {
            Category c;
            for (Integer id : ids) {
                c = get(id);
                if (c != null && c.getBookCount() > 0) {
                    result.setInfo("分类["+id+"] 已关联书籍，不能执行删除操作");
                    return result;
                }
            }
        }

        categoryRepository.removeMulti(ids);

        CategoryQueryDto queryDto = new CategoryQueryDto();
        queryDto.setIsLeaf(1);
        queryDto.setIsDesc(0);
        queryDto.setOrderType(2);
        List<Category> list = categoryRepository.find(queryDto);
        for (int i=0,size=list.size();i<size;i++) {
            categoryRepository.updateOrderId(list.get(i).getId(), i+1);
        }

        result.setSuccess(true);
        result.setInfo("删除成功！");

        return result;
    }

    public ResultDto save(Category category, AdminUser adminUser) {
        category.setIsUse(1);
        ResultDto result = new ResultDto();
        result.setSuccess(false);
        result.setInfo("更新失败！");
        Date now = new Date();
        category.setEditDate(now);
        category.setEditorId(adminUser.getId());

        //用于标识是否需要重新对orderId进行排序
        boolean reorder = false;
        //目前(数据库)已有的分类集合
        List<Category> list = null;

        if (category.getId() != null) {//update
            Category old = get(category.getId());
            int oldOrderId = old.getOrderId();
            BeanCopy.beans(category, old).ignoreNulls(true).copy();
            category = old;
            if (!isExist(category.getId(), category.getName())) {
                if (category.getOrderId() != null && category.getOrderId() > 0) {

                    //获取当前数据库已有的分类
                    CategoryQueryDto queryDto = new CategoryQueryDto();
                    queryDto.setIsLeaf(1);
                    list = find(queryDto);

                    if (category.getOrderId() > list.size()) {
                        category.setOrderId(list.size());
                    }

                    if (oldOrderId != category.getOrderId()) {
                        reorder = true;
                        int index = -1;
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

                save(category);
                result.setSuccess(true);
                result.setInfo("更新成功！");

                if (reorder && CollectionUtils.isNotEmpty(list)) {
                    Category c;
                    for (int i = 0, size = list.size(), order = 1; i < size; i++) {
                        if (order == category.getOrderId()) {
                            order = order + 1;
                        }
                        c = list.get(i);
                        categoryRepository.updateOrderId(c.getId(), order++);
                    }
                }
            } else {
                result.setInfo("数据已存在");
            }
        } else {//insert
            if (!isExist(null, category.getName())) {
                if (category.getIsLeaf() == null) {
                    category.setIsLeaf(1);
                }

                if(category.getOrderId() == null) {
                    category.setOrderId(1);
                }

                //获取当前数据库已有的分类
                CategoryQueryDto queryDto = new CategoryQueryDto();
                queryDto.setIsLeaf(1);
                list = find(queryDto);
                //根据当前已有分类
                if(CollectionUtils.isNotEmpty(list)){
                    if(category.getOrderId() > list.size()) {
                        category.setOrderId(list.size() + 1);
                    }else{
                        reorder = true;
                    }
                }else{
                    category.setOrderId(1);
                }

                category.setBookCount(0);
                category.setCreateDate(now);
                category.setCreatorId(adminUser.getId());

                save(category);

                result.setSuccess(true);
                result.setInfo("保存成功！");

                //对OrderId按顺序重新排序
                if (reorder){
                    Category c;
                    for(int i = 0, size = list.size(), order = 1; i < size; i++) {
                        if(order == category.getOrderId()) {
                            order = order + 1;
                        }
                        c = list.get(i);
                        categoryRepository.updateOrderId(c.getId(), order++);
                    }
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

    public ResultDto publish() {
        ResultDto r = new ResultDto();
        r.setSuccess(false);
        r.setInfo("发布失败");

        CategoryQueryDto queryDto = new CategoryQueryDto();
        queryDto.setParentId(0);
        queryDto.setIsLeaf(0);
        List<Category> parents = find(queryDto);
        List<Category> list;
        List<CategoryBase> children;
        CategoryBase c;
        if (CollectionUtils.isNotEmpty(parents)) {
            for (Category o : parents) {
                queryDto = new CategoryQueryDto();
                queryDto.setParentId(o.getId());
                queryDto.setIsLeaf(1);
                list = find(queryDto);

                if (CollectionUtils.isNotEmpty(list)) {
                    children = Lists.newArrayList();
                    for (Category category : list) {
                        c = new CategoryBase();
                        BeanCopierUtils.categoryBaseCopy(category, c);
                        children.add(c);
                    }

                    o.setChildren(children);
                }
            }

            categoryCacheService.publish(parents);
            r.setSuccess(true);
            r.setInfo("发布成功");
        }

        return r;
    }

    public List<Integer> analyseBookCategory(Integer tagClassifyId, Integer tagSexId,
                                             List<Integer> tagContentIds, List<Integer> tagSupplyIds) {
        if (tagClassifyId != null && tagSexId != null && CollectionUtils.isNotEmpty(tagContentIds)) {
            String key = getTagKey(tagClassifyId, tagSexId);
            List<CategoryTagDto> list = categoryTagMap.get(key);

            List<Integer> result = Lists.newArrayList();
            if (CollectionUtils.isNotEmpty(list)) {
                for (CategoryTagDto c : list) {
                    if (contains(c.getTagMap(), tagContentIds) && contains(c.getTagMap(), tagSupplyIds)) {
                        result.add(c.getCategoryId());
                    }
                }
            }
            if (CollectionUtils.isNotEmpty(result)) {
                return result;
            }
        }
        return null;
    }

    public boolean contains(Map<Integer, Set<Integer>> map, List<Integer> ids) {
        Map<Integer, Set<Integer>> m = parseTags(ids);
        SetView sv = null;
        for (Map.Entry<Integer, Set<Integer>> entry : m.entrySet()) {
            sv = Sets.intersection(map.get(entry.getKey()), entry.getValue());
            if (CollectionUtils.isEmpty(sv)) {
                return false;
            }
        }

        return true;
    }

    public void initData() {
        Category c = null;

        c = new Category();
        c.setName("男生原创小说");
        c.setBookCount(0);
        c.setCreateDate(new Date());
        c.setCreatorId(1);
        c.setEditDate(new Date());
        c.setEditorId(1);
        c.setIsLeaf(0);
        c.setIsUse(1);
        c.setParentId(0);
        c.setOrderId(1);
        categoryRepository.persist(c);

        c = new Category();
        c.setName("女生原创小说");
        c.setBookCount(0);
        c.setCreateDate(new Date());
        c.setCreatorId(1);
        c.setEditDate(new Date());
        c.setEditorId(1);
        c.setIsLeaf(0);
        c.setIsUse(1);
        c.setParentId(0);
        c.setOrderId(2);
        categoryRepository.persist(c);

        c = new Category();
        c.setName("热销出版图书");
        c.setBookCount(0);
        c.setCreateDate(new Date());
        c.setCreatorId(1);
        c.setEditDate(new Date());
        c.setEditorId(1);
        c.setIsLeaf(0);
        c.setIsUse(1);
        c.setParentId(0);
        c.setOrderId(3);
        categoryRepository.persist(c);
    }

    public void statis() {
        CategoryQueryDto queryDto = new CategoryQueryDto();
        queryDto.setIsLeaf(1);
        queryDto.setIsUse(1);
        List<Category> list = find(queryDto);
        if (CollectionUtils.isNotEmpty(list)) {
            BookQueryDto bookQueryDto = new BookQueryDto();

            int count = 0;
            for (Category c : list) {
                bookQueryDto.setCategoryId(c.getId());
                count = bookService.count(bookQueryDto);
                if (count > 0 && c.getBookCount() != count) {
                    categoryRepository.updateBookCount(c.getId(), count);
                }
            }
        }
    }

}
