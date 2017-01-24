package cn.aiyuedu.bs.wap.service;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.aiyuedu.bs.dao.dto.UserQueryDto;
import cn.aiyuedu.bs.dao.entity.User;
import cn.aiyuedu.bs.dao.mongo.repository.UserRepository;
import cn.aiyuedu.bs.wap.dto.UserResultDto;

import com.duoqu.commons.utils.DigestUtils;

/**
 * Description:
 *
 * @author yz.wu
 */
@Service("userService")
public class UserService {

	@Autowired
	private UserRepository userRepository;

	public UserResultDto register(User temp, User user, String confirmPassword) {
		UserResultDto result = new UserResultDto();
		result.setSuccess(false);
		if (user != null) {
			if (StringUtils.isBlank(user.getUserName())) {
				result.setInfo("请输入用户名");
			} else if (StringUtils.isBlank(user.getEmail())) {
				result.setInfo("请输入email");
			} else if (StringUtils.isBlank(user.getPassword())) {
				result.setInfo("请输入密码");
			} else if (StringUtils.isBlank(confirmPassword)) {
				result.setInfo("请输入确认密码");
			} else if (!StringUtils.equals(user.getPassword(), confirmPassword)) {
				result.setInfo("两次输入的密码不一致");
			} else {
				boolean exist = userRepository.exist(user.getUserName(), null);
				if (exist) {
					result.setInfo("用户名已被注册");
				} else {
					exist = userRepository.exist(null, user.getEmail());
					if (exist) {
						result.setInfo("，邮箱已被注册");
					} else {
						temp.setUserName(user.getUserName());
						temp.setNickname(user.getUserName());
						temp.setPassword(DigestUtils.sha1ToBase64UrlSafe(user
								.getPassword()));
						temp.setCreateDate(new Date());
						temp.setStatus(1);
						temp.setSex(user.getSex());
						if (temp.getVirtualCorn() == null) {
							temp.setVirtualCorn(0);
						}

						userRepository.persist(temp);

						result.setUser(temp);
						result.setSuccess(true);
					}
				}

			}
		} else {
			result.setInfo("注册异常");
		}

		return result;
	}

	public UserResultDto registerOAuth(User user) {
		UserResultDto result = new UserResultDto();
		result.setSuccess(false);
		if (user != null) {

			if (user.getCreateDate() == null)
				user.setCreateDate(new Date());
			if (user.getStatus() == null)
				user.setStatus(1);
			if (user.getSex() == null)
				user.setSex(1);
			if (user.getVirtualCorn() == null) {
				user.setVirtualCorn(0);
			}

			userRepository.persist(user);

			result.setUser(user);
			result.setSuccess(true);

		} else {
			result.setInfo("注册异常");
		}

		return result;
	}

	public UserResultDto login(String userName, String password) {
		UserResultDto result = new UserResultDto();
		result.setSuccess(false);
		if (StringUtils.isNotBlank(userName)
				&& StringUtils.isNotBlank(password)) {
			boolean exist = userRepository.exist(userName, null);
			if (exist) {
				UserQueryDto queryDto = new UserQueryDto();
				queryDto.setPassword(DigestUtils.sha1ToBase64UrlSafe(password));
				queryDto.setUserName(userName);
				User user = userRepository.findOne(queryDto);
				if (user != null) {
					result.setUser(user);
					result.setSuccess(true);
				} else {
					result.setInfo("密码不正确");
				}
			} else {
				result.setInfo("用户名不存在");
			}
		} else {
			result.setInfo("请输入用户名及密码");
		}

		return result;
	}

	public User createTempUser(User user) {
		user.setCreateDate(new Date());
		user.setStatus(1);
		user.setVirtualCorn(0);
		userRepository.persist(user);
		return userRepository.persist(user);
	}

	public User findOne(Integer id, String uid) {
		UserQueryDto queryDto = new UserQueryDto();
		queryDto.setId(id);
		queryDto.setUid(uid);
		return userRepository.findOne(queryDto);
	}

	public User findOAuthQQ(String openId, String appId) {
		UserQueryDto queryDto = new UserQueryDto();
		queryDto.setQqAppId(appId);
		queryDto.setQqOpenId(openId);
		return userRepository.findOne(queryDto);
	}
	public User findOAuthWeixin(String openId, String appId) {
		UserQueryDto queryDto = new UserQueryDto();
		queryDto.setWeixinAppId(appId);
		queryDto.setWeixinOpenId(openId);
		return userRepository.findOne(queryDto);
	}

	public User update(Integer id, String uid, Integer virtualCorn,
			Integer status) {
		return userRepository.update(id, uid, virtualCorn, status);
	}

	public User update(User user) {
		return userRepository.persist(user);
	}

	public User updateName(Integer id, String nickname) {
		return userRepository.update(id, nickname);
	}
}
