package cn.aiyuedu.bs.dao.mongo.repository.custom;

import cn.aiyuedu.bs.common.orm.Page;
import cn.aiyuedu.bs.dao.dto.DistributeBookQueryDto;
import cn.aiyuedu.bs.dao.entity.DistributeBook;

import java.util.List;

/**
 * Created by Thinkpad on 2014/11/18.
 */
public interface DistributeBookRepositoryCustom {

    boolean exist(Long bookId, Integer disId);

    int count(Integer disId);

    DistributeBook persist(DistributeBook distributeBook);

    List<DistributeBook> persist(List<DistributeBook> list);

    List<DistributeBook> queryBookList(Integer disId);

    List<DistributeBook> queryBookList(DistributeBookQueryDto distributeBookQueryDto);

    void deleteAll();

    Page<DistributeBook> getPage(DistributeBookQueryDto dto);


    Long getBookCountBydisId(Integer id);

    void update(DistributeBook distribute);
}
