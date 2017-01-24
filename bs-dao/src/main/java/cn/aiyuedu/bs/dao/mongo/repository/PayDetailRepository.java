package cn.aiyuedu.bs.dao.mongo.repository;

import cn.aiyuedu.bs.dao.entity.PayDetail;
import cn.aiyuedu.bs.dao.mongo.repository.custom.PayDetailRepositoryCustom;
import org.springframework.data.repository.CrudRepository;


public interface PayDetailRepository extends CrudRepository<PayDetail, Integer>, PayDetailRepositoryCustom {
}
