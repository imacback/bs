package cn.aiyuedu.bs.back.service;

import cn.aiyuedu.bs.common.dto.ResultDto;
import cn.aiyuedu.bs.common.orm.Page;
import cn.aiyuedu.bs.common.util.IdUtil;
import cn.aiyuedu.bs.dao.dto.ContainerDto;
import cn.aiyuedu.bs.dao.dto.ProviderDto;
import cn.aiyuedu.bs.dao.dto.ProviderQueryDto;
import cn.aiyuedu.bs.dao.dto.SiteDto;
import cn.aiyuedu.bs.dao.entity.AdminUser;
import cn.aiyuedu.bs.dao.entity.Provider;
import cn.aiyuedu.bs.dao.entity.Site;
import cn.aiyuedu.bs.dao.mongo.repository.SiteRepository;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import jodd.bean.BeanCopy;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Description:
 *
 * @author yz.wu
 */
@Service("siteService")
public class SiteService {

    private List<SiteDto> list = Lists.newArrayList();
    private Map<Integer, SiteDto> map = Maps.newHashMap();

    @Autowired
    private SiteRepository siteRepository;
    @Autowired
    private ContainerService containerService;

    @PostConstruct
    public synchronized void reload() {
        Iterable<Site> sites = siteRepository.findAll();
        if (sites != null) {
            list.clear();
            map.clear();
            SiteDto d;
            ContainerDto c;
            for (Site s : sites) {
                d = new SiteDto();
                BeanCopy.beans(s, d).ignoreNulls(false).copy();
                if (d.getHomeContainerId() != null) {
                    c = containerService.get(d.getHomeContainerId());
                    if (c != null) {
                        d.setHomeContainerName(c.getName());
                    }
                }
                list.add(d);
                map.put(s.getId(), d);
            }
        }
    }

    public ResultDto save(Site site, AdminUser adminUser) {
        ResultDto result = new ResultDto();
        result.setSuccess(false);
        result.setInfo("更新成功！");
        Date now = new Date();
        site.setEditDate(now);
        site.setEditorId(adminUser.getId());

        if (site.getId() != null) {//update
            result.setInfo("更新成功！");
        } else {//insert
            site.setCreateDate(now);
            site.setCreatorId(adminUser.getId());

            result.setInfo("保存成功！");  result.setInfo("数据已存在");
        }

        siteRepository.persist(site);
        result.setSuccess(true);

        if (result.getSuccess()) {
            reload();
        }

        return result;
    }

    public SiteDto get(Integer id) {
        return map.get(id);
    }

    public List<SiteDto> getAll() {
        return list;
    }

    public Page<SiteDto> getPage(Integer startIndex, Integer pageSize) {

        Page<Site> page = siteRepository.getPage(startIndex, pageSize);

        SiteDto dto;
        ContainerDto c;
        List<SiteDto> siteDtoList = Lists.newArrayList();

        //将Provider-->ProviderDto,并设置创建人和修改人名称
        if(null!=page && CollectionUtils.isNotEmpty(page.getResult())){
            for(Site o : page.getResult()){
                dto = new SiteDto();
                BeanCopy.beans(o, dto).ignoreNulls(false).copy();

                if (dto.getHomeContainerId() != null) {
                    c = containerService.get(dto.getHomeContainerId());
                    if (c != null) {
                        dto.setHomeContainerName(c.getName());
                    }
                }

                siteDtoList.add(dto);
            }
        }

        return new Page<>(siteDtoList, page.getTotalItems());
    }

    public void initData() {
        Site s = new Site();
        s.setName("站1");
        s.setCreateDate(new Date());
        s.setEditDate(new Date());
        s.setCreatorId(1);
        s.setEditorId(1);
        s.setStatus(1);
        s.setPlatformId(1);

        siteRepository.persist(s);

        s = new Site();
        s.setName("站2");
        s.setCreateDate(new Date());
        s.setEditDate(new Date());
        s.setCreatorId(1);
        s.setEditorId(1);
        s.setStatus(1);
        s.setPlatformId(1);

        siteRepository.persist(s);
    }
}
