package cn.aiyuedu.bs.dao.mongo.repository.custom;

import cn.aiyuedu.bs.common.orm.Page;
import cn.aiyuedu.bs.dao.entity.Tag;
import cn.aiyuedu.bs.dao.dto.TagQueryDto;

import java.util.List;

/**
 * Created by Thinkpad on 2015/1/7.
 */
public interface TagRepositoryCustom {

    Tag persist(Tag tag);

    long count(TagQueryDto tagQueryDto);

    List<Tag> find(TagQueryDto tagQueryDto);

    Page<Tag> getPage(TagQueryDto tagQueryDto);

    void removeMulti(List<Integer> ids);
}
