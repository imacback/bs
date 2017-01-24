package cn.aiyuedu.bs.dao.mongo.repository.custom;

import cn.aiyuedu.bs.common.orm.Page;
import cn.aiyuedu.bs.dao.dto.PayDetailQueryDto;
import cn.aiyuedu.bs.dao.entity.PayDetail;

import java.util.List;

public interface PayDetailRepositoryCustom {

    public PayDetail persist(PayDetail payDetail);

    public Page<PayDetail> getPage(PayDetailQueryDto payDetailQueryDto);

    List<PayDetail> boughtChapters(Integer uid, Long id, List<Long> chapList);

    boolean exist(Long bookId, Long chapterId, Integer uid);
}
