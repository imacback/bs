package cn.aiyuedu.bs.service;


import com.duoqu.commons.encrypt.AES;
import com.duoqu.commons.utils.CompressUtil;
import com.duoqu.commons.utils.FileUtil;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Created by tonydeng on 14-8-23.
 */
@Ignore
public class ChapterContentServiceTest extends BaseTest {
    @Autowired
    private ChapterContentService chapterContentService;
    @Autowired
    private Properties keyConfig;
    //private static final String content = new String(FileUtil.readFile(System.getProperty("user.dir") + "/src/test/resources/data/content.txt"));

    //    @Test
    public void contentBlankRepTest() {
        Long bookId = 1254l;
        Long chapterId = 158641l;
        String content = chapterContentService.getChapterContent(bookId, chapterId);

        String temp = content.trim().replaceAll("　", " ");
        log.info(temp);

        chapterContentService.saveChapterContent(bookId, chapterId, temp, chapterContentService.getChapterContent(bookId, chapterId));


    }

//    @Test
    public void encryptContentTest() {
        String key = keyConfig.getProperty("rs.aes.key");
        log.info("key:'{}', size:{}",key,key.getBytes().length);
        byte[] paramArrayOfByte = key.getBytes();
        int i = paramArrayOfByte.length / 4;
        log.info("i:'{}'",i);
        log.info("((i != 4) && (i != 6) && (i != 8)):'{}'", ((i != 4) && (i != 6) && (i != 8)));
        log.info("(i * 4 != paramArrayOfByte.length):'{}'",(i * 4 != paramArrayOfByte.length));
        if (((i != 4) && (i != 6) && (i != 8)) || (i * 4 != paramArrayOfByte.length)) {
            throw new IllegalArgumentException("Key length not 128/192/256 bits.");
        }

        Long bookId = 1254l;
        Long chapterId = 158641l;
        String content = chapterContentService.getChapterContent(bookId, chapterId);

        String temp = chapterContentService.encryptContent(content);
        log.info(temp);

        log.info(AES.decrypt(temp,key));
    }

//    @Test
    public void encryptAndDecrptyTest(){
        String key = keyConfig.getProperty("rs.aes.key");
        Long bookId = 2740l;
        Long chapterId = 893302l;
        String content = chapterContentService.getChapterContentEncrypt(bookId, chapterId);
//
//        String temp = chapterContentService.encryptContent(content);
//        log.info(temp);

        //byte[] gzip = CompressUtil.ungzip(AES.decrypt(content.getBytes(),key.getBytes()));
        //log.info("content:'{}'",new String(gzip));
    }

//    @Test
    public void decryptContentByFileTest(){
        String key ="88f23dd228cd44201b99068f43356f37";
        List<String> files = Lists.newArrayList(
                "/Users/tonydeng/Downloads/1.dqt",
                "/Users/tonydeng/Downloads/2.dqt",
                "/Users/tonydeng/Downloads/3.txt"
        );
        for(String f:files){
            byte[] enctyptByte = FileUtil.getByteForFile(new File(f));

            //byte[] gzip = CompressUtil.ungzip(AES.decrypt(enctyptByte,key.getBytes()));
            //log.info("file:'{}' \n content:'{}'",f,new String(gzip));
        }
    }

    @Test
    public void decryptContentByStringTest(){
        String enctyptStr = "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";

        log.info(new String(CompressUtil.ungzip(AES.decrypt(enctyptStr, keyConfig.getProperty("rs.aes.key")).getBytes())));

        String content = chapterContentService.getChapterContent(1226l,156790l);

        String enctyptContent = chapterContentService.encryptContent(content);
        log.info(enctyptContent);

        log.info("enctyptStr equest enctyptContent:'{}'", StringUtils.equals(enctyptStr,enctyptContent));
//
//        String key = keyConfig.getProperty("rs.aes.key");
//        byte[] gzip = CompressUtil.ungzip(AES.decrypt(enctyptContent.getBytes(),key.getBytes()));

        log.info("dectyptContent:'{}'",chapterContentService.decryptContent(enctyptContent));
    }


    //    @Test
    public void removeChapterContentsTest() {
        List<Long> chapterIds = Lists.newArrayList(
                new Long(1), new Long(2), new Long(3)
        );
        chapterContentService.removeChapterContents(1l, chapterIds);
    }

    //    @Test
    public void removeAllChapterByBookIdTest() {
        chapterContentService.removeAllChapterByBookId(1l);
    }


    //    @Test
    public void removeChapterContentTest() {
//        chapterContentService.removeChapterContent(null,400l);
    }

    //            @Test
    public void saveContentTest() {
        for (int i = 1; i < 15; i++) {
            //chapterContentService.saveChapterContent(new Long(1), new Long(i), content, content);
        }
    }

    //    @Test
    public void getContentTest() {
        Long bookId = 1250l;
        Long chapterId = 158412l;
        System.out.println(chapterContentService.getChapterContent(bookId, chapterId));

    }

    //    @Test
    public void getChapterContentsMapTest() {
        Map<Long, String> contents = chapterContentService.getChapterContentsMap(4l, Lists.newArrayList(401l, 402l));
        for (Long key : contents.keySet()) {
            log.info("chapterId:'{}'  content:'{}'", key, contents.get(key));
        }
    }

    //    @Test
    public void getChapterContentsTest() {
//        List<String> contents = chapterContentService.getChapterContents(new Long(105), Lists.newArrayList(new Long(4)));
//        for (String content : contents) {
//            log.info(content);
//        }
    }

    //        @Test
    public void getOriginContentTest() {
//        Long bookId = 139l;
//        Long chapterId = 6826l;
//        String origin = chapterContentService.getChapterContent(bookId, chapterId, true);
//        log.info("origin:'" + origin + "'");
//        String content = chapterContentService.getChapterContent(bookId, chapterId);
//        log.info("content:'" + content + "'");
    }

    //    @Test
    public void gzipTest() {
//        log.info("content string length:'" + content.length() + "'");
//        byte[] ba = content.getBytes();
//        log.info("content byte array length:'" + ba.length + "'");
//        byte[] gzip = CompressUtil.gzip(ba);
//        log.info("gzip byte array length:'" + gzip.length + "'");
//        log.info("gzip string length:'" + new String(gzip).length() + "'");
    }
}
