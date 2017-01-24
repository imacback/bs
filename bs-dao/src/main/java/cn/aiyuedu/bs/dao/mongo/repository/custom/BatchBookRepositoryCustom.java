package cn.aiyuedu.bs.dao.mongo.repository.custom;

import cn.aiyuedu.bs.common.orm.Page;
import cn.aiyuedu.bs.dao.dto.BatchBookQueryDto;
import cn.aiyuedu.bs.dao.entity.BatchBook;

import java.util.List;

/**
 * Created by Thinkpad on 2015/1/1.
 */
public interface BatchBookRepositoryCustom {

    BatchBook persist(BatchBook batchBook);

    long count(BatchBookQueryDto batchBookQueryDto);

    List<BatchBook> find(BatchBookQueryDto batchBookQueryDto);

    Page<BatchBook> getPage(BatchBookQueryDto batchBookQueryDto);

    void removeMulti(List<Long> ids);
}
