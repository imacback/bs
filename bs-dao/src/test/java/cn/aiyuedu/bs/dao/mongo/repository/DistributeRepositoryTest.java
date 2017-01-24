package cn.aiyuedu.bs.dao.mongo.repository;

import cn.aiyuedu.bs.common.orm.Page;
import cn.aiyuedu.bs.dao.BaseTest;
import cn.aiyuedu.bs.dao.entity.Distribute;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

/**
 * Created by Thinkpad on 2014/11/18.
 */
@Ignore
public class DistributeRepositoryTest extends BaseTest {

    @Autowired
    DistributeRepository distributeRepository;


    @Test
    public  void delete(){
  //String s =   distributeRepository.getDistrbuteName(4);
  //  System.out.println("s============ " + s);
    }
//

   // @Test
    public void inset(){
        Distribute distribute = new Distribute();
        distribute.setCreateDate( new Date());
        distribute.setEditDate(new Date());
        distribute.setCreatorId(1);
        distribute.setEditorId(1);
        distribute.setName("谷歌");
        distribute.setId(4);
        distribute.setStatus(0);
        distribute.setKey("1234");
        distributeRepository.persist(distribute);


    }
//
    @Test
    public void getpage(){
        List<Integer> list = new ArrayList<>();
        list.add(1);
        Map<String , Object> map = new HashMap<>();
        map.put("name" ,null);
        map.put("username",null);
        map.put("start" ,0);
        map.put("limit",20);
      Page<Distribute> pat =  distributeRepository.getPage(null);
        if(pat.getResult() !=null) {
            for(Distribute d :pat.getResult()){
                System.out.println("nam -=" + d.getName());
                System.out.println("id =  " + d.getId());
                System.out.println("userid = " + d.getCreatorId());
                System.out.println("ste = " + d.getStatus());
            }
        }
        }

    @Test
    public void upaget(){
        Distribute distribute = new Distribute();
       // distribute.setCreateDate( new Date());
        distribute.setEditDate(new Date());
       // distribute.setCreatorId(1);
        distribute.setEditorId(1);
        distribute.setName("谷歌22");
        distribute.setId(4);
        distribute.setStatus(1);
        distribute.setKey("1234");
        distributeRepository.update(distribute);
    }
//
//
//    }

   // @Test
    public void find(){


    }

}
