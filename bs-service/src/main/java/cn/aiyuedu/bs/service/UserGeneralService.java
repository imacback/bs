package cn.aiyuedu.bs.service;

import cn.aiyuedu.bs.common.Constants;
import cn.aiyuedu.bs.dao.dto.UserQueryDto;
import cn.aiyuedu.bs.dao.entity.User;
import cn.aiyuedu.bs.dao.mongo.repository.UserRepository;
import com.duoqu.commons.encrypt.AES;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;

import java.util.List;


/**
 * 用户注册及登陆借口
 */
public class UserGeneralService {

    public static final int USVC_SUCCESS = 1;
    public static final int USVC_LOGIN_FAILED = 2;
    public static final int USVC_NO_USER = 3;
    public static final int USVC_REG_FAILED = 4;
    public static final int USVC_DUPLICATE_KEY = 5;
    public static final int USVC_UPDATE_FAILED = 6;

    private final Logger log = LoggerFactory.getLogger(this.getClass());
    //    @Autowired
//    UserCacheService userCacheService;
    @Autowired
    UserRepository userRepository;

    /**
     * 修改密码
     *
     * @param id
     * @param password
     * @param npassword
     * @param npassword2
     */
    public boolean modifyPwd(Integer id, String password, String npassword, String npassword2) {
        User user = userRepository.findOne(id);
        if (user == null) {
            return false;
        }
        String pwdEnc = AES.encrypt(npassword, Constants.PWD_SEED);
        user.setPassword(pwdEnc);
        userRepository.save(user);
        return true;
    }

    public Result update(User user) {
        Result out = new Result();
        try {
            User r = userRepository.save(user);
            if (r == null) {
                out.setStatus(USVC_UPDATE_FAILED);
            } else {
                out.setStatus(USVC_SUCCESS);
//                userCacheService.set(user);
            }
            return out;
        } catch (DuplicateKeyException e) {
            out.setStatus(USVC_DUPLICATE_KEY);
        } catch (Exception e) {
            out.setStatus(USVC_UPDATE_FAILED);
        } finally {
            return out;
        }
    }

    public User get(Integer uid) {
//        User user = userCacheService.get(uid);
//        if (user == null) {
        User user = userRepository.findOne(uid);
//            userCacheService.set(user);
//        }
        return user;
    }

    /*
    public Result registerOrLogin(String clientId, String userName, String pwd) {
        Result out = new Result();
        out.setStatus(USVC_SUCCESS);
        User user;
        if (StringUtils.hasText(userName) && StringUtils.hasText(pwd)) {//有用户名+密码——登陆
            user = userRepository.loginFind(null, userName, pwd);
            if (user == null) out.setStatus(USVC_LOGIN_FAILED);
        } else if (id != null && StringUtils.hasText(pwd)) {//有UID+密码——登陆
            user = userRepository.loginFind(id, null, pwd);
            if (user == null) out.setStatus(USVC_LOGIN_FAILED);
        } else {//无用户名
            LastLogin lstl = lastLoginRepository.find(cli.getCid());
            if (lstl != null) {//有绑定关系——登陆
                user = userRepository.findOne(lstl.getUid());
                if (user == null) out.setStatus(USVC_NO_USER);
            } else {//无绑定关系——注册  //1.1.00.12 ，平台，客户端，渠道
                ClientInfo.ClientParam param = cli.clientParam();
                user = userRepository.register(cli.getCid(), cli.getChannel(), Integer.valueOf(param.getPlatform()),
                        cli.getImei(), cli.getImsi(), param.getVersion());
                if (user == null) out.setStatus(USVC_REG_FAILED);
                user.setNewUser(true);
            }
        }
        if (user != null) {
            log.info("last login: cid=" + cli.getCid() + ", id=" + user.getId());
            lastLoginRepository.insertOrUpdate(new LastLogin(cli.getCid(), user.getId()));
            String pwdDecr =AES.decrypt(user.getPassword(), Constants.PWD_SEED);
            user.setPassword(pwdDecr);
            out.setUser(user);
        }
        return out;
    }
    */

    public void updateVirtualCorn(Integer id, Integer virtualCorn) {
        userRepository.update(id, null, virtualCorn, null);
    }

    public void updateStatus(Integer id, Integer status) {
        userRepository.update(id, null, null, status);
    }

    public class Result {
        private User user;
        private Integer status = -1;

        public User getUser() {
            return user;
        }

        public void setUser(User user) {
            this.user = user;
        }

        public Integer getStatus() {
            return status;
        }

        public void setStatus(Integer status) {
            this.status = status;
        }
    }

}
