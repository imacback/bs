package cn.aiyuedu.bs.back.service;

import com.duoqu.commons.redis.client.RedisClient;
import cn.aiyuedu.bs.cache.service.TagCacheService;
import cn.aiyuedu.bs.common.Constants;
import cn.aiyuedu.bs.common.Constants.TagType;
import cn.aiyuedu.bs.common.dto.ChildrenDto;
import cn.aiyuedu.bs.common.dto.ResultDto;
import cn.aiyuedu.bs.common.orm.Page;
import cn.aiyuedu.bs.dao.dto.BookQueryDto;
import cn.aiyuedu.bs.dao.dto.TagDto;
import cn.aiyuedu.bs.dao.dto.TagQueryDto;
import cn.aiyuedu.bs.dao.entity.AdminUser;
import cn.aiyuedu.bs.dao.entity.Tag;
import cn.aiyuedu.bs.dao.mongo.repository.TagRepository;
import cn.aiyuedu.bs.service.TagGeneralService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import jodd.bean.BeanCopy;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;

/**
 * Description:
 *
 * @author yz.wu
 */
@Service("tagService")
public class TagService {

    @Autowired
    private TagRepository tagRepository;
    @Autowired
    private AdminUserService adminUserService;
    @Autowired
    private TagGeneralService tagGeneralService;
    @Autowired
    private TagCacheService tagCacheService;
    @Autowired
    private RedisClient redisClient;
    @Autowired
    private Properties redisConfig;
    @Autowired
    private BookService bookService;

    private Map<Integer, Tag> map;
    private Map<String, Integer> nameMap;
    private List<Tag> parentTagList;

    @PostConstruct
    @Order(1)
    public synchronized void reload() {
        map = Maps.newHashMap();
        nameMap = Maps.newHashMap();
        List<TagDto> list = getTagDtos(null, null, null, null, null, null);
        if (CollectionUtils.isNotEmpty(list)) {
            Tag t;
            for (TagDto o : list) {
                map.put(o.getId(), o);
                if (o.getTypeId() == 1 && o.getIsLeaf() == 1) {
                    t = map.get(o.getParentId());
                    if (t != null) {
                        nameMap.put(t.getName().trim() + Constants.SEPARATOR_2 + o.getName().trim(), o.getId());
                    }
                } else {
                    nameMap.put(o.getName().trim(), o.getId());
                }
            }
        }

        TagQueryDto queryDto = new TagQueryDto();
        queryDto.setScope("1");
        queryDto.setIsNotNullScope(1);
        queryDto.setIsLeaf(0);
        queryDto.setParentId(0);
        parentTagList = tagRepository.find(queryDto);
    }

    public Tag get(Integer id) {
        return map.get(id);
    }

    public Tag getByName(String name) {
        if (StringUtils.isNotBlank(name)) {
            Integer id = nameMap.get(name);
            if (id != null) {
                return map.get(id);
            }
        }

        return null;
    }

    public boolean isExist(Integer id, String name, Integer parentId) {
        TagQueryDto queryDto = new TagQueryDto();
        queryDto.setId(id);
        queryDto.setIsNEId(1);
        queryDto.setName(name);
        queryDto.setParentId(parentId);
        return tagRepository.count(queryDto) > 0;
    }

    public List<Tag> getParentTagList() {
        return parentTagList;
    }

    public boolean add(Tag tag) {
        tagRepository.persist(tag);
        return true;
    }

    public boolean update(Tag tag) {

        //从DB获取更新前的信息
        Tag old = tagRepository.findOne(tag.getId());

        //BeanCopy后,old为所要update的信息
        BeanCopy.beans(tag, old).ignoreNulls(true).copy();

        tagRepository.persist(old);

        return true;
    }

    private List<TagDto> tag2TagDto(List<Tag> tagList){
        List<TagDto> tagDtoList = Lists.newArrayList();
        if(CollectionUtils.isNotEmpty(tagList)){
            TagDto dto = null;
            Tag c = null;
            for(Tag tag : tagList){
                dto = new TagDto();
                BeanCopy.beans(tag, dto).ignoreNulls(false).copy();

                adminUserService.infoOperate(dto);

                if (dto.getParentId() != null) {
                    if (dto.getParentId() == 0) {
                        dto.setParentName("无");
                    } else {
                        c = get(dto.getParentId());
                        if (c != null) {
                            dto.setParentName(c.getName());
                        }
                    }
                }

                tagDtoList.add(dto);
            }
        }
        return tagDtoList;
    }

    public List<TagDto> getTagDtos(Integer id, Integer isUse, String name,
                                   Integer parentId, Integer isLeaf, Integer typeId) {

        TagQueryDto queryDto = new TagQueryDto();
        queryDto.setId(id);
        queryDto.setIsUse(isUse);
        queryDto.setName(name);
        queryDto.setIsLikeName(1);
        queryDto.setParentId(parentId);
        queryDto.setIsLeaf(isLeaf);
        queryDto.setTypeId(typeId);

        LinkedHashMap<String, Sort.Direction> map = Maps.newLinkedHashMap();
        map.put("isLeaf", Sort.Direction.ASC);
        map.put(Constants.MONGODB_ID_KEY, Sort.Direction.ASC);
        queryDto.setOrderMap(map);

        List<Tag> tagList = tagRepository.find(queryDto);

        return this.tag2TagDto(tagList);
    }

    public int count(Integer id, Integer isUse, String name,
                     Integer parentId, Integer isLeaf, Integer typeId) {

        TagQueryDto queryDto = new TagQueryDto();
        queryDto.setId(id);
        queryDto.setIsUse(isUse);
        queryDto.setName(name);
        queryDto.setIsLikeName(1);
        queryDto.setParentId(parentId);
        queryDto.setIsLeaf(isLeaf);
        queryDto.setTypeId(typeId);

        return new Long(tagRepository.count(queryDto)).intValue();
    }

    public Page<TagDto> getPage(Integer id, Integer isUse, String name,
                                Integer parentId, Integer isLeaf, Integer typeId,
                                Integer startIndex, Integer pageSize) {
        TagQueryDto queryDto = new TagQueryDto();
        queryDto.setId(id);
        queryDto.setIsUse(isUse);
        queryDto.setName(name);
        queryDto.setIsLikeName(1);
        queryDto.setParentId(parentId);
        queryDto.setIsLeaf(isLeaf);
        queryDto.setTypeId(typeId);
        queryDto.setStart(startIndex);
        queryDto.setLimit(pageSize);

        LinkedHashMap<String, Sort.Direction> map = Maps.newLinkedHashMap();
        map.put("isLeaf", Sort.Direction.ASC);
        map.put(Constants.MONGODB_ID_KEY, Sort.Direction.ASC);
        queryDto.setOrderMap(map);

        Page<Tag> page = tagRepository.getPage(queryDto);

        List<TagDto> tagDtoList = Lists.newArrayList();
        if(null!=page && CollectionUtils.isNotEmpty(page.getResult())){
            tagDtoList = this.tag2TagDto(page.getResult());
        }

        return new Page<>(tagDtoList, page.getTotalItems());
    }

    public List<Tag> getTagsByTypeId(Integer typeId) {

        TagQueryDto queryDto = new TagQueryDto();
        queryDto.setParentId(0);
        queryDto.setTypeId(typeId);

        List<Tag> list = tagRepository.find(queryDto);
        if (CollectionUtils.isNotEmpty(list)) {
            for (Tag t : list) {
                if (t.getIsLeaf() == 0) {
                    ChildrenDto<Tag> childrenDto = new ChildrenDto<>();
                    queryDto.setParentId(t.getId());
                    childrenDto.setItems(tagRepository.find(queryDto));
                    if (CollectionUtils.isNotEmpty(childrenDto.getItems())) {
                        childrenDto.setTotalItems(childrenDto.getItems().size());
                    } else {
                        childrenDto.setTotalItems(0);
                    }
                    t.setChildren(childrenDto);
                }
            }
        }

        return list;
    }

    public ResultDto delete(List<Integer> ids) {
        ResultDto result = new ResultDto();
        result.setSuccess(false);
        result.setInfo("删除失败！");
        if (CollectionUtils.isNotEmpty(ids)) {
            Tag t;
            int count;
            for (Integer id : ids) {
                t = get(id);
                if (t.getIsLeaf() == 0) {
                    count = count(null, null, null, id, null, null);
                    if (count > 0) {
                        result.setInfo("标签[" + id + "] 有子标签，不能执行删除操作");
                        return result;
                    }
                } else {
                    if (t.getBookCount() > 0) {
                        result.setInfo("标签[" + id + "] 已关联书籍，不能执行删除操作");
                        return result;
                    }
                }
            }
        }

        tagRepository.removeMulti(ids);

        reload();
        publish();
        result.setSuccess(true);
        result.setInfo("删除成功！");

        return result;
    }

    public ResultDto save(Tag tag, AdminUser user) {
        tag.setIsUse(1);
        ResultDto result = new ResultDto();
        result.setSuccess(false);
        result.setInfo("更新成功！");
        if (user != null) {
            tag.setEditDate(new Date());
            tag.setEditorId(user.getId());
        }

        if (tag.getId() != null) {//update
            if (!isExist(tag.getId(), tag.getName(), tag.getParentId())) {
                Tag t = get(tag.getId());
                if (t.getIsLeaf() == 0 && tag.getIsLeaf() == 1) {
                    int count = count(null, null, null, tag.getId(), null, null);
                    if (count > 0) {
                        result.setInfo("此标签有下级分类，不能转为二级分类");
                        return result;
                    }
                } else if (t.getIsLeaf() == 1 && tag.getIsLeaf() == 0) {
                    if (t.getBookCount() > 0) {
                        result.setInfo("此标签已关联书箱，不能转为一级分类");
                        return result;
                    }
                }
                if (update(tag)) {
                    result.setSuccess(true);
                    result.setInfo("更新成功！");
                } else {
                    result.setInfo("更新失败！");
                }
            } else {
                result.setInfo("数据已存在");
            }
        } else {//insert
            if (!isExist(null, tag.getName(), tag.getParentId())) {
                if (user != null) {
                    tag.setCreateDate(new Date());
                    tag.setCreatorId(user.getId());
                }
                tag.setBookCount(0);

                if (add(tag)) {
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

    public ResultDto publish() {
        ResultDto result = new ResultDto();
        result.setSuccess(false);
        result.setInfo("更新成功！");
        List<TagDto> list = getTagDtos(null, null, null, null, null, null);
        tagCacheService.set(list);
        tagGeneralService.reload();
        redisClient.publish(redisConfig.getProperty("redis.topic.back.tag"), "reload");
        redisClient.publish(redisConfig.getProperty("redis.topic.back.upgrade"), "reload");
        redisClient.publish(redisConfig.getProperty("redis.topic.wap.tag"), "reload");
        result.setSuccess(true);
        result.setInfo("发布成功！");
        return result;
    }

    public void statis() {
        List<TagDto> list = getTagDtos(null, 1, null, null, 1, null);
        if (CollectionUtils.isNotEmpty(list)) {
            BookQueryDto queryDto;
            int count = 0;
            Tag tag;
            for (TagDto t : list) {
                queryDto = new BookQueryDto();
                if (t.getTypeId() == TagType.Classify.getId()) {
                    queryDto.setTagClassifyId(t.getId());
                } else if (t.getTypeId() == TagType.Sex.getId()) {
                    queryDto.setTagSexId(t.getId());
                } else if (t.getTypeId() == TagType.Content.getId()) {
                    queryDto.setTagContentId(t.getId());
                } else if (t.getTypeId() == TagType.Supply.getId()) {
                    queryDto.setTagSupplyId(t.getId());
                }
                count = bookService.count(queryDto);
                tag = new Tag();
                tag.setId(t.getId());
                tag.setBookCount(count);
                update(tag);
            }
        }
    }
}
