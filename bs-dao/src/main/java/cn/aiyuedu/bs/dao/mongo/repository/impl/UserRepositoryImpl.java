package cn.aiyuedu.bs.dao.mongo.repository.impl;

import cn.aiyuedu.bs.common.Constants;
import cn.aiyuedu.bs.common.orm.Page;
import cn.aiyuedu.bs.dao.dto.UserQueryDto;
import cn.aiyuedu.bs.dao.entity.User;
import cn.aiyuedu.bs.dao.mongo.SequenceDao;
import cn.aiyuedu.bs.dao.mongo.repository.custom.UserRepositoryCustom;
import cn.aiyuedu.bs.dao.util.QueryLikeUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.List;

public class UserRepositoryImpl implements UserRepositoryCustom {

	@Autowired
	private MongoOperations mongoOperations;
	@Autowired
	private SequenceDao sequenceDao;

	@Override
	public User persist(User user) {
		if (user != null) {
			if (user.getId() == null) {
				user.setId(sequenceDao.getSequence(mongoOperations
						.getCollectionName(User.class)));
				mongoOperations.insert(user);
			} else {
				mongoOperations.save(user);
			}
		}
		return user;
	}

	@Override
	public boolean exist(String userName, String email) {
		Query query = new Query();
		if (StringUtils.isNotEmpty(userName)) {
			query.addCriteria(Criteria.where("userName").is(userName));
		} else if (StringUtils.isNotBlank(email)) {
			query.addCriteria(Criteria.where("email").is(email));
		}
		return mongoOperations.exists(query, User.class);
	}

	private Query getQuery(UserQueryDto userQueryDto) {
		Query query = new Query();

		// 如果查询对象不为空才增加过滤条件,否则查询所有信息
		if (userQueryDto != null) {
			if (userQueryDto.getId() != null) {
				query.addCriteria(Criteria.where(Constants.MONGODB_ID_KEY).is(
						userQueryDto.getId()));
			}

			if (StringUtils.isNotBlank(userQueryDto.getUserName())) {
				if (StringUtils.isNotBlank(userQueryDto.getPassword())) {
					query.addCriteria(Criteria.where("userName").is(
							userQueryDto.getUserName()));
					query.addCriteria(Criteria.where("password").is(
							userQueryDto.getPassword()));
				} else {
					query.addCriteria(Criteria.where("userName").regex(
							QueryLikeUtils.normalizeKeyword(userQueryDto
									.getUserName())));
				}
			}

			if (StringUtils.isNotBlank(userQueryDto.getUid())) {
				query.addCriteria(Criteria.where("uid").is(
						userQueryDto.getUid()));
			}

			if (StringUtils.isNotBlank(userQueryDto.getMobile())) {
				query.addCriteria(Criteria.where("mobile").regex(
						QueryLikeUtils.normalizeKeyword(userQueryDto
								.getMobile())));
			}

			if (StringUtils.isNotBlank(userQueryDto.getNickname())) {
				query.addCriteria(Criteria.where("nickname").regex(
						QueryLikeUtils.normalizeKeyword(userQueryDto
								.getNickname())));
			}
			if (StringUtils.isNotBlank(userQueryDto.getQqAppId())) {
				query.addCriteria(Criteria.where("qqAppId").is(
						userQueryDto.getQqAppId()));
			}
			if (StringUtils.isNotBlank(userQueryDto.getQqOpenId())) {
				query.addCriteria(Criteria.where("qqOpenId").is(
						userQueryDto.getQqOpenId()));
			}
			
			if (StringUtils.isNotBlank(userQueryDto.getWeixinAppId())) {
				query.addCriteria(Criteria.where("weixinAppId").is(
						userQueryDto.getWeixinAppId()));
			}
			if (StringUtils.isNotBlank(userQueryDto.getWeixinOpenId())) {
				query.addCriteria(Criteria.where("weixinOpenId").is(
						userQueryDto.getWeixinOpenId()));
			}
		}

		return query;
	}

	@Override
	public long count(UserQueryDto userQueryDto) {
		return mongoOperations.count(getQuery(userQueryDto), User.class);
	}

	@Override
	public List<User> find(UserQueryDto userQueryDto) {
		Query query = getQuery(userQueryDto);

		// 排序
		query.with(new Sort(Sort.Direction.DESC, Constants.MONGODB_ID_KEY));

		return mongoOperations.find(query, User.class);
	}

	@Override
	public Page<User> getPage(UserQueryDto userQueryDto) {
		Query query = getQuery(userQueryDto);

		// 总数
		long count = 0l;
		if (null != userQueryDto && userQueryDto.getStart() != null) {
			count = mongoOperations.count(query, User.class);
			// 分页
			query.skip(userQueryDto.getStart()).limit(userQueryDto.getLimit());
		}

		// 排序
		query.with(new Sort(Sort.Direction.DESC, Constants.MONGODB_ID_KEY));

		return new Page<>(mongoOperations.find(query, User.class), count);
	}

	@Override
	public void removeMulti(List<Integer> ids) {
		mongoOperations.remove(new Query(Criteria.where("id").in(ids)),
				User.class);
	}

	@Override
	public User findOne(UserQueryDto userQueryDto) {
		return mongoOperations.findOne(getQuery(userQueryDto), User.class);
	}

	@Override
	public User update(Integer id, String uid, Integer virtualCorn,
			Integer status) {
		Query query = new Query();
		if (id != null) {
			query.addCriteria(Criteria.where("id").is(id));
		} else if (StringUtils.isNotBlank(uid)) {
			query.addCriteria(Criteria.where("uid").is(uid));
		}
		Update update = new Update();
		if (virtualCorn != null) {
			update.inc("virtualCorn", virtualCorn);
		}
		if (status != null) {
			update.set("status", status);
		}
		return mongoOperations.findAndModify(query, update,
				FindAndModifyOptions.options().returnNew(true), User.class);
	}

	@Override
	public User update(Integer id, String nickname) {
		Query query = new Query();
		if (id != null) {
			query.addCriteria(Criteria.where("id").is(id));
		}
		Update update = new Update();
		if (StringUtils.isNotBlank(nickname)) {
			update.set("nickname", nickname);
		}
		return mongoOperations.findAndModify(query, update,
				FindAndModifyOptions.options().returnNew(true), User.class);
	}
}
