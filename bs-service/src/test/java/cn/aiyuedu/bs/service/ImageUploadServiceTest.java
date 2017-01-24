package cn.aiyuedu.bs.service;

import com.duoqu.commons.utils.FileUtil;
import cn.aiyuedu.bs.common.Constants;
import com.google.common.collect.Lists;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * Created by tonydeng on 14-9-17.
 */
@Ignore
public class ImageUploadServiceTest extends BaseTest {
    @Autowired
    private ImageUploadService imageUploadService;
    @Autowired
    private Properties ossConfig;

    private static final File src = new File("/dq/img/rs/de/4e/75/2060187790.jpg");
    private static final File desc = new File("/dq/tmp/2060187790.jpg");

    @Before
    public void initImage() {
        FileUtil.copyFile(src, desc);
    }

    @Test
    public void uploadComponentImageTest(){
        String url = imageUploadService.uploadComponentImage(desc);
        log.info("component image url:'"+url+"'");
    }
    @Test
    public void getImageToBase64Test(){
        List<String> urls = Lists.newArrayList(
//                "http://imgtest.duoquyuedu.com/4/113/0dc84ed1cc72425da97cf41456aff716_large.jpg",
//                "http://img.duoquyuedu.com/4/113/0dc84ed1cc72425da97cf41456aff716_large.jpg"
                "http://imgtest.duoquyuedu.com/comp/2014092060187790.jpg"
//                "http://imgtest.duoquyuedu.com/4/113/0dc84ed1cc72425da97cf41456aff716_small.jpg"
        );
        for(String imageUrl:urls){
            String bs = imageUploadService.getImageToBase64(imageUrl);
            log.info(bs);
            FileUtil.writeString2File("/tmp/base64.txt",bs,false);
        }
    }

//        @Test
    public void uploadBookCoverTest() {
        Map<String, String> map = imageUploadService.uploadBookCover(123, "88381", desc);
        for (String key : map.keySet()) {
            log.info("key:'" + key + "'  value:'" + map.get(key) + "'");
        }
    }

//    @Test
    public void uploadBookCoverByUrlTest() {
        Map<String, String> map = imageUploadService.uploadBookCover(123, "88381", "http://image.cmfu.com/books/8795/879512313.jpg");
//        assertNull(map);
//        for (String key : map.keySet()) {
//            log.info("key:'" + key + "'  value:'" + map.get(key) + "'");
//        }
    }

    //    @Test
    public void uploadAdvImageTest() {
        Map<String, String> map = imageUploadService.uploadAdvImage(desc, Constants.UploadFileType.LoadingAdv);
        for (String key : map.keySet()) {
            log.info("key:'" + key + "'  value:'" + map.get(key) + "'");
        }
        initImage();
        map = imageUploadService.uploadAdvImage(desc, Constants.UploadFileType.ShelfAdv);
        for (String key : map.keySet()) {
            log.info("key:'" + key + "'  value:'" + map.get(key) + "'");
        }
        initImage();
        map = imageUploadService.uploadAdvImage(desc, Constants.UploadFileType.TextAdv);
        for (String key : map.keySet()) {
            log.info("key:'" + key + "'  value:'" + map.get(key) + "'");
        }
    }

}
