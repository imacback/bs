package cn.aiyuedu.bs.dao.mongo.repository;

import cn.aiyuedu.bs.dao.entity.Recharge;
import cn.aiyuedu.bs.dao.mongo.repository.custom.RechargeRepositoryCustom;
import org.springframework.data.repository.CrudRepository;


public interface RechargeRepository extends CrudRepository<Recharge, Integer>, RechargeRepositoryCustom {
}
