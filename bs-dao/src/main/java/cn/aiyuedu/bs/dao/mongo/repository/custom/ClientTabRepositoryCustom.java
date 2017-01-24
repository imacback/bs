package cn.aiyuedu.bs.dao.mongo.repository.custom;

import cn.aiyuedu.bs.common.orm.Page;
import cn.aiyuedu.bs.dao.dto.ClientTabQueryDto;
import cn.aiyuedu.bs.dao.entity.ClientTab;

import java.util.List;

/**
 * Created by Thinkpad on 2015/1/5.
 */
public interface ClientTabRepositoryCustom {

    ClientTab persist(ClientTab clientTab);

    List<ClientTab> find(ClientTabQueryDto clientTabQueryDto);

    Page<ClientTab> getPage(ClientTabQueryDto clientTabQueryDto);

    void removeMulti(List<Integer> ids);
}
