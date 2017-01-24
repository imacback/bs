package cn.aiyuedu.bs.service;

import com.duoqu.commons.redis.client.RedisClient;
import cn.aiyuedu.bs.cache.service.CategoryCacheService;
import cn.aiyuedu.bs.cache.util.RedisKeyUtil;
import cn.aiyuedu.bs.dao.dto.CategoryQueryDto;
import cn.aiyuedu.bs.dao.entity.Category;
import cn.aiyuedu.bs.dao.mongo.repository.CategoryRepository;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Description:
 *
 * @author yz.wu
 */
@Ignore
public class CategoryGeneralServiceTest extends BaseTest {

    @Autowired
    private CategoryGeneralService categoryGeneralService;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private CategoryCacheService categoryCacheService;
    @Autowired
    private RedisClient redisClient;

    @Test
    public void testSet() {
        List<Category> list = categoryRepository.find(new CategoryQueryDto());
        redisClient.delete(RedisKeyUtil.LIST_CATEGORY, true);
        redisClient.addToListWithMsgPack(RedisKeyUtil.LIST_CATEGORY, false, list);
    }



}
