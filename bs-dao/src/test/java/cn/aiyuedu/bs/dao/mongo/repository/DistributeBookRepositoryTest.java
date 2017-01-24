package cn.aiyuedu.bs.dao.mongo.repository;

import cn.aiyuedu.bs.common.orm.Page;
import cn.aiyuedu.bs.dao.BaseTest;
import cn.aiyuedu.bs.dao.entity.DistributeBook;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Thinkpad on 2014/11/19.
 */
@Ignore
public class DistributeBookRepositoryTest extends BaseTest {
    @Autowired
    DistributeBookRepository distributeBookRepository;

   @Test
    public void getBookid(){

   }

    @Test
    public void insterAndUpdate(){
        DistributeBook boo = new DistributeBook();
        boo.setBookid(1002L);
        boo.setDisId(2);
       // boo.setCreateDate(new Date());
        boo.setEditDate(new Date());
       // boo.setCreatorId(1);
        boo.setEditorId(2);
        boo.setId(1);
        boo =   distributeBookRepository.persist(boo);
        System.out.println(" id = " + boo.getId());
        System.out.println(" setEditDate = " + boo.getEditDate());
        System.out.println(" setDistributeId = " + boo.getDisId());
        System.out.println(" setEditorId = " + boo.getEditorId());



    }

    @Test
    public void getPage(){

        Map<String, Object> map = new HashMap<>();
        map.put("bookId" ,1002L);
        map.put("start",0);
        map.put("limit",20);
        Page page = distributeBookRepository.getPage(null);
        List<DistributeBook> list = page.getResult();
        for(DistributeBook boo :list){
            System.out.println(" id = " + boo.getId());
            System.out.println(" setEditDate = " + boo.getEditDate());
            System.out.println(" setDistributeId = " + boo.getDisId());
            System.out.println(" setEditorId = " + boo.getEditorId());
        }

    }


}
