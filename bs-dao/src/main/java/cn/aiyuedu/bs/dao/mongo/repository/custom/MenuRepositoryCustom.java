package cn.aiyuedu.bs.dao.mongo.repository.custom;

import cn.aiyuedu.bs.dao.entity.Menu;
import cn.aiyuedu.bs.dao.dto.MenuQueryDto;

import java.util.List;

/**
 * Created by Thinkpad on 2014/12/29.
 */
public interface MenuRepositoryCustom {

    void drop();

    Menu persist(Menu menu);

    List<Menu> find(MenuQueryDto menuQueryDto);

    void removeMulti(List<Integer> ids);
}
