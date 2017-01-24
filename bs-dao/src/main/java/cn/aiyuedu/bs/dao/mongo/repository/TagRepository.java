package cn.aiyuedu.bs.dao.mongo.repository;

import cn.aiyuedu.bs.dao.mongo.repository.custom.TagRepositoryCustom;
import cn.aiyuedu.bs.dao.entity.Tag;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by Thinkpad on 2015/1/7.
 */
public interface TagRepository extends CrudRepository<Tag, Integer>, TagRepositoryCustom {
}
