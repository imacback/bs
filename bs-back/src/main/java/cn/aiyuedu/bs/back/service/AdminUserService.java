package cn.aiyuedu.bs.back.service;

import com.duoqu.commons.utils.DigestUtils;
import com.duoqu.commons.utils.RandomUtil;
import cn.aiyuedu.bs.common.global.Operation;
import cn.aiyuedu.bs.common.orm.Page;
import cn.aiyuedu.bs.dao.dto.AdminUserDto;
import cn.aiyuedu.bs.dao.dto.AdminUserQueryDto;
import cn.aiyuedu.bs.dao.entity.AdminUser;
import cn.aiyuedu.bs.dao.entity.Role;
import cn.aiyuedu.bs.dao.mongo.repository.AdminUserRepository;
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
@Service("adminUserService")
public class AdminUserService {

    @Autowired
    private AdminUserRepository adminUserRepository;
    @Autowired
    private RoleService roleService;
    @Autowired
    private MailService mailService;

    private Map<Integer, AdminUser> adminUserMap;
    private Map<String, AdminUser> adminUserNameMap;

    @PostConstruct
    public synchronized void reload() {
        adminUserMap = Maps.newHashMap();
        adminUserNameMap = Maps.newHashMap();

        List<AdminUser> list = adminUserRepository.find(null);
        for (AdminUser u : list) {
            adminUserMap.put(u.getId(), u);
            adminUserNameMap.put(u.getName(), u);
        }
    }

    public AdminUser get(Integer id) {
        return adminUserMap.get(id);
    }

    public AdminUser getByName(String name) {
        return adminUserNameMap.get(name);
    }

    public boolean isExist(AdminUserQueryDto adminUserQueryDto){
        return adminUserRepository.count(adminUserQueryDto) > 0;
    }

    public AdminUser getByNameAndPass(String userName, String userPass) {

        AdminUserQueryDto queryDto = new AdminUserQueryDto();
        queryDto.setName(userName);
        queryDto.setPassword(DigestUtils.sha1ToBase64UrlSafe(userPass));

        List<AdminUser> list = adminUserRepository.find(queryDto);

        if (CollectionUtils.isNotEmpty(list) && list.size() == 1) {
            AdminUser user = list.get(0);
            adminUserMap.put(user.getId(), user);

            return user;
        }

        return null;
    }

    public List<AdminUser> getByNameList(String userName ) {
        AdminUserQueryDto queryDto = new AdminUserQueryDto();
        queryDto.setName(userName);
        return adminUserRepository.find(queryDto);
    }

    public boolean add(AdminUser user) {

        AdminUserQueryDto queryDto = new AdminUserQueryDto();
        queryDto.setId(user.getId());
        queryDto.setName(user.getName());
        queryDto.setEmail(user.getEmail());

        if (!isExist(queryDto)) {
            String password = RandomUtil.getRandomInt(6);
            user.setPassword(DigestUtils.sha1ToBase64UrlSafe(password));
            boolean result = mailService.resetPassword(password,
                    user.getEmail());

            if (result) {
                adminUserRepository.persist(user);
                return true;
            }
        }

        return false;
    }

    public boolean update(AdminUser user) {

        //从DB获取更新前的信息
        AdminUser old = adminUserRepository.findOne(user.getId());

        //BeanCopy后,old为所要update的信息
        BeanCopy.beans(user, old).ignoreNulls(true).copy();

        adminUserRepository.persist(old);

        return true;
    }

    public Page<AdminUserDto> getPage(AdminUserQueryDto adminUserQueryDto) {

        if(null!=adminUserQueryDto){
            //昵称和邮箱按Like进行查询
            adminUserQueryDto.setIsLikeNickname(1);
            adminUserQueryDto.setIsLikeEmail(1);
        }

        Page<AdminUser> page = adminUserRepository.getPage(adminUserQueryDto);

        AdminUserDto dto = null;
        Role r = null;
        List<AdminUserDto> adminUserDtoList = Lists.newArrayList();

        //将AdminUser-->AdminUserDto,并设置角色名称
        if(null!=page && CollectionUtils.isNotEmpty(page.getResult())){
            for(AdminUser adminUser : page.getResult()){
                dto = new AdminUserDto();
                BeanCopy.beans(adminUser, dto).ignoreNulls(false).copy();

                //角色名称
                r = roleService.getById(adminUser.getRoleId());
                if(null!=r){
                    dto.setRoleName(r.getName());
                }
                infoOperate(dto);

                adminUserDtoList.add(dto);
            }
        }

        return new Page<>(adminUserDtoList, page.getTotalItems());
    }

    public boolean delete(List<Integer> ids) {
        adminUserRepository.removeMulti(ids);
        return true;
    }

    public void infoOperate(Operation o) {
        AdminUser u = null;
        if (o.getCreatorId() != null) {
            u = get(o.getCreatorId());
            if (u != null) {
                o.setCreatorName(u.getNickname());
            }
        }
        if (o.getEditorId() != null) {
            u = get(o.getEditorId());
            if (u != null) {
                o.setEditorName(u.getNickname());
            }
        }
    }

    public void initData() {
        adminUserRepository.drop();

        AdminUser o = new AdminUser();
        o.setName("admin");
        o.setCreateDate(new Date());
        o.setCreatorId(1);
        o.setEditorId(1);
        o.setEditDate(new Date());
        o.setEmail("wuyongzhao@aiyuedu.cn");
        o.setNickname("admin");
        o.setRoleId(1);
        o.setIsUse(1);
        o.setPassword(DigestUtils.sha1ToBase64UrlSafe("aiyuedu2015"));

        adminUserRepository.persist(o);

        reload();
    }
}
