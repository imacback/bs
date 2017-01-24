package cn.aiyuedu.bs.service;

import cn.aiyuedu.bs.cache.service.BookCacheService;
import cn.aiyuedu.bs.dao.entity.DistributeBook;
import cn.aiyuedu.bs.dao.mongo.repository.DistributeBookRepository;
import com.google.common.collect.Lists;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by Thinkpad on 2014/11/24.
 */
public class DistributeBookGeneralService {

    private final Logger logger = LoggerFactory.getLogger(DistributeBookGeneralService.class);

    @Autowired
    private DistributeBookRepository distributeBookRepository;
    @Autowired
    private BookCacheService bookCacheService;

    public boolean exist(Integer distributeId, Long bookId) {
        return distributeBookRepository.exist(bookId, distributeId);
    }

    public int count(Integer disId) {
        return distributeBookRepository.count(disId);
    }

    public List<Long> getBookIdList(Integer disId) {
        List<DistributeBook> distributeBooks = distributeBookRepository.queryBookList(disId);
        if (CollectionUtils.isNotEmpty(distributeBooks)) {
            int size = distributeBooks.size();
            List<Long> list = Lists.newArrayListWithCapacity(distributeBooks.size());
            DistributeBook b;
            for (int i=0; i<size; i++) {
                b = distributeBooks.get(i);
                if (b == null || b.getBookId() == null) continue;
                list.add(distributeBooks.get(i).getBookId());
            }

            if (CollectionUtils.isNotEmpty(list))
                return list;
        }

        return null;
    }

}
