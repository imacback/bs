package cn.aiyuedu.bs.dao.mongo.repository.custom;

import cn.aiyuedu.bs.common.orm.Page;
import cn.aiyuedu.bs.dao.dto.ProviderQueryDto;
import cn.aiyuedu.bs.dao.entity.Provider;

import java.util.List;

/**
 * Created by Thinkpad on 2015/1/6.
 */
public interface ProviderRepositoryCustom {

    Provider persist(Provider provider);

    long count(ProviderQueryDto providerQueryDto);

    List<Provider> find(ProviderQueryDto providerQueryDto);

    Page<Provider> getPage(ProviderQueryDto providerQueryDto);

    void removeMulti(List<Integer> ids);
}
