package cn.aiyuedu.bs.dao.mongo.repository;

import cn.aiyuedu.bs.common.orm.Page;
import cn.aiyuedu.bs.dao.dto.FilterWordQueryDto;
import cn.aiyuedu.bs.dao.entity.FilterWord;
import cn.aiyuedu.bs.dao.BaseTest;
import org.apache.commons.collections4.CollectionUtils;
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
public class FilterWordRepositoryTest extends BaseTest {

    @Autowired
    private FilterWordRepository filterWordRepository;

    @Test
    public void testGetPage() {
        FilterWordQueryDto queryDto = new FilterWordQueryDto();
        queryDto.setStart(0);
        queryDto.setLimit(20);
        Page<FilterWord> page = filterWordRepository.getPage(queryDto);
        System.out.println(page.getTotalItems());
        if (page != null && CollectionUtils.isNotEmpty(page.getResult())) {
            for (FilterWord f : page.getResult()) {
                System.out.println(f.getWord());
            }
        }
    }

    @Test
    public void testUpdateLength() {
        FilterWordQueryDto queryDto = new FilterWordQueryDto();
        List<FilterWord> list = filterWordRepository.find(queryDto);
        if (CollectionUtils.isNotEmpty(list)) {
            for (FilterWord f : list) {
                int length = f.getWord().length();
                filterWordRepository.updateLength(f.getId(), length);
            }
        }
    }
}
