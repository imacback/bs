package cn.aiyuedu.bs.dao.mongo.repository;

import cn.aiyuedu.bs.dao.entity.ComponentDataGroup;
import cn.aiyuedu.bs.dao.entity.Sequence;
import cn.aiyuedu.bs.dao.BaseTest;
import com.google.common.collect.Lists;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

/**
 * Created by Thinkpad on 2015/1/6.
 */
@Ignore
public class ComponentDataGroupRepositoryTest extends BaseTest {

    private final Logger logger = LoggerFactory.getLogger(ComponentDataGroupRepositoryTest.class);

    @Autowired
    private MongoOperations mongoOperations;
    @Autowired
    private MongoTemplate mongoTemplate;

//    @Autowired
//    private ComponentDataGroupDao componentDataGroupDao;
    @Autowired
    private ComponentDataGroupRepository componentDataGroupRepository;

    @Test
    public void componentDataGroupDataMigration(){
        //从MySql中获取所有信息
//        List<ComponentDataGroup> list = componentDataGroupDao.findAll();
        List<ComponentDataGroup> list = Lists.newArrayList();

        //如果MySql中存在数据
        if(CollectionUtils.isNotEmpty(list)){

            System.out.println("======>size="+list.size());

            //判断原来Mongodb中是否有信息,有则先删除
            List<ComponentDataGroup> mgList = componentDataGroupRepository.find(null);
            if(CollectionUtils.isNotEmpty(mgList)){
                List<Integer> ids = Lists.newArrayList();
                for(ComponentDataGroup m : mgList){
                    ids.add(m.getId());
                }
                componentDataGroupRepository.removeMulti(ids);
            }

            //用于保存批量保存后的sequence的值
            Integer sequenceNum = 0;
            //用于保存批量数据中没有ID的数据,这些数据插入时使用sequence的自动增长后的值作为ID
            List<ComponentDataGroup> tempList = Lists.newArrayList();

            //向mongodb中批量插入mysql中的数据，并且记录这些数据中最大值的ID,将其设置为sequence的当前值
            for(ComponentDataGroup ct : list){
                if(null!=ct.getId()){
                    componentDataGroupRepository.persist(ct);
                    sequenceNum = (ct.getId()>sequenceNum)?ct.getId():sequenceNum;
                    System.out.println("save success===>>>"+ct.getId());
                }else{
                    tempList.add(ct);
                }
            }

            //mongodb中对应集合的名称
            String collectionName = mongoOperations.getCollectionName(ComponentDataGroup.class);
            //获取当前集合对应的sequence对象,如果存在则设为当前最大的ID值,如果不存在则创建一个新的sequence并设置其值为当前最大的ID值
            List<Sequence> sequenceList = mongoTemplate.find(new Query(Criteria.where("id").is(collectionName)), Sequence.class);
            Sequence s = null;
            if(CollectionUtils.isNotEmpty(sequenceList)){
                s = sequenceList.get(0);
                s.setSequence(sequenceNum);
                mongoTemplate.save(s);
            }else{
                s = new Sequence();
                s.setId(collectionName);
                s.setSequence(sequenceNum);
                mongoTemplate.save(s);
            }

            //将之前ID为null的记录保存到mongodb中,其ID使用sequence自动增长
            if(CollectionUtils.isNotEmpty(tempList)){
                if(logger.isInfoEnabled()){
                    logger.info("The existence of a primary key ID for empty data, auto generation id is :");
                }

                for(ComponentDataGroup temp : tempList){
                    componentDataGroupRepository.persist(temp);

                    if(logger.isInfoEnabled()){
                        logger.info("Id is -----> ["+temp.getId()+"]");
                    }
                }
            }
        }
    }
}
