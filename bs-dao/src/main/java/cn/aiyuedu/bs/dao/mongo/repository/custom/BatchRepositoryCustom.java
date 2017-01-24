package cn.aiyuedu.bs.dao.mongo.repository.custom;

import cn.aiyuedu.bs.common.orm.Page;
import cn.aiyuedu.bs.dao.dto.BatchQueryDto;
import cn.aiyuedu.bs.dao.entity.Batch;

import java.util.List;

/**
 * Created by Thinkpad on 2014/12/29.
 */
public interface BatchRepositoryCustom {

    Batch persist(Batch batch);

    long count(BatchQueryDto batchQueryDto);

    List<Batch> find(BatchQueryDto batchQueryDto);

    Page<Batch> getPage(BatchQueryDto batchQueryDto);

    void removeMulti(List<Integer> ids);
}
