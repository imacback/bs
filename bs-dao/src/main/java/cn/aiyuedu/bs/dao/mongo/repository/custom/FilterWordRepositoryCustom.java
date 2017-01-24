package cn.aiyuedu.bs.dao.mongo.repository.custom;

import cn.aiyuedu.bs.common.orm.Page;
import cn.aiyuedu.bs.dao.dto.FilterWordQueryDto;
import cn.aiyuedu.bs.dao.entity.FilterWord;

import java.util.List;

/**
 * Description:
 *
 * @author yz.wu
 */
public interface FilterWordRepositoryCustom {

    FilterWord persist(FilterWord category);

    boolean exist(Integer id, String name);

    FilterWord findOne(String word);

    List<FilterWord> find(FilterWordQueryDto filterWordQueryDto);

    Page<FilterWord> getPage(FilterWordQueryDto filterWordQueryDto);

    void updateStatus(List<Integer> ids, Integer status, Integer adminUserId);

    void updateLength(Integer id, Integer length);

    void delete(List<Integer> ids);
}
