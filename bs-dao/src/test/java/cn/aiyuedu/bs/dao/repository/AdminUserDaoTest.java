package cn.aiyuedu.bs.dao.repository;

import cn.aiyuedu.bs.dao.BaseTest;
import cn.aiyuedu.bs.dao.mongo.repository.AdminUserRepository;
import com.duoqu.commons.utils.DigestUtils;
import com.duoqu.commons.utils.RandomUtil;
import cn.aiyuedu.bs.dao.entity.AdminUser;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

/**
* Description:
*
* @author yz.wu
*/
@Ignore
public class AdminUserDaoTest extends BaseTest {

    @Autowired
    private AdminUserRepository adminUserRepository;

    @Test
    public void testAdd() {
        AdminUser u = new AdminUser();

        Date date = new Date();
        u.setCreateDate(date);
        u.setCreatorId(1);
        u.setEditDate(date);
        u.setEditorId(1);
        u.setEmail("webywyz@163.com");
        u.setIsUse(1);
        u.setName("test");
        u.setNickname("test" + RandomUtil.getRandomInt(3));
        u.setPassword(DigestUtils.sha1ToBase64UrlSafe("123"));
        u.setRoleId(2);

        adminUserRepository.persist(u);

        System.out.println(u.getId());
    }
}
