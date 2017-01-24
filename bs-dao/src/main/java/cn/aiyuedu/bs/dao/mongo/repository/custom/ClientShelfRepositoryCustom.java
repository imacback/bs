package cn.aiyuedu.bs.dao.mongo.repository.custom;

import cn.aiyuedu.bs.common.orm.Page;
import cn.aiyuedu.bs.dao.dto.ClientShelfQueryDto;
import cn.aiyuedu.bs.dao.entity.ClientShelf;

import java.util.List;

/**
 * Created by Thinkpad on 2015/1/4.
 */
public interface ClientShelfRepositoryCustom {

    ClientShelf persist(ClientShelf clientShelf);

    long count(ClientShelfQueryDto clientShelfQueryDto);

    List<ClientShelf> find(ClientShelfQueryDto clientShelfQueryDto);

    Page<ClientShelf> getPage(ClientShelfQueryDto clientShelfQueryDto);

    void removeMulti(List<Integer> ids);
}
