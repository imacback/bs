package cn.aiyuedu.bs.service;

import com.duoqu.commons.encrypt.AES;
import com.duoqu.commons.utils.CompressUtil;
import com.duoqu.commons.utils.EncodeUtils;
import cn.aiyuedu.bs.dao.entity.ChapterContent;
import cn.aiyuedu.bs.dao.entity.ChapterContentEncrypt;
import cn.aiyuedu.bs.dao.entity.ChapterContentFilter;
import cn.aiyuedu.bs.dao.mongo.repository.ChapterContentEncryptRepository;
import cn.aiyuedu.bs.dao.mongo.repository.ChapterContentFilterRepository;
import cn.aiyuedu.bs.dao.mongo.repository.ChapterContentRepository;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Properties;


/**
 * 章节内容服务
 */
public class ChapterContentService {

    private static final Logger log = LoggerFactory.getLogger(ChapterContentService.class);

    @Autowired
    private ChapterContentRepository chapterContentRepository;
    @Autowired
    private ChapterContentFilterRepository chapterContentFilterRepository;
    @Autowired
    private ChapterContentEncryptRepository chapterContentEncryptRepository;
    @Autowired
    private Properties keyConfig;

    /**
     * 删除章节内容
     *
     * @param bookId
     * @param chapterId
     */
    public void removeChapterContent(Long bookId, Long chapterId) {
        if (bookId != null && chapterId != null) {
            chapterContentRepository.delete(chapterId);
            chapterContentFilterRepository.delete(chapterId);
            chapterContentEncryptRepository.delete(chapterId);
        }
    }

    /**
     * 批量删除章节内容
     *
     * @param bookId
     * @param chapterIds
     */
    public void removeChapterContents(Long bookId, List<Long> chapterIds) {
        if (bookId != null && CollectionUtils.isNotEmpty(chapterIds)) {
            chapterContentRepository.delete(chapterIds);
            chapterContentFilterRepository.delete(chapterIds);
            chapterContentEncryptRepository.delete(chapterIds);
        }
    }

    /**
     * 删除一本书的所有章节内容
     *
     * @param bookId
     */
    public void removeAllChapterByBookId(Long bookId) {
        if (bookId != null) {
            chapterContentRepository.deleteByBookId(bookId);
            chapterContentFilterRepository.deleteByBookId(bookId);
            chapterContentEncryptRepository.deleteByBookId(bookId);
        }
    }

    public boolean saveChapterContentWithApi(Long bookId, Long chapterId, String content, String contentSource) {
        if (bookId != null && chapterId != null) {
            if (StringUtils.isNotEmpty(contentSource) && StringUtils.isNotEmpty(content)) {
                ChapterContent c = new ChapterContent(chapterId, bookId, contentSource);
                chapterContentRepository.save(c);

                content = EncodeUtils.htmlUnescape(content);
                ChapterContentFilter f = new ChapterContentFilter(chapterId, bookId, content);
                chapterContentFilterRepository.save(f);

                ChapterContentEncrypt e = new ChapterContentEncrypt(chapterId, bookId, encryptContent(content));
                chapterContentEncryptRepository.save(e);

                return true;
            }

        }
        return false;
    }

    public boolean saveChapterContent(Long bookId, Long chapterId, String content, String unfilteredContent) {
        if (bookId != null && chapterId != null) {
            if (StringUtils.isNotEmpty(unfilteredContent) && StringUtils.isNotEmpty(content)) {
                ChapterContent c = new ChapterContent(chapterId, bookId, unfilteredContent);
                chapterContentRepository.save(c);

                content = EncodeUtils.htmlUnescape(content);
                ChapterContentFilter f = new ChapterContentFilter(chapterId, bookId, content);
                chapterContentFilterRepository.save(f);

                ChapterContentEncrypt e = new ChapterContentEncrypt(chapterId, bookId, encryptContent(content));
                chapterContentEncryptRepository.save(e);

                return true;
            }

        }
        return false;
    }

    /**
     * 取未过滤未加密的章节内容
     * @param bookId
     * @param chapterId
     * @return
     */
    public String getChapterContent(Long bookId, Long chapterId) {
        ChapterContent o = chapterContentRepository.findOne(chapterId);
        if (o != null) {
            return o.getContent();
        }
        return null;
    }

    /**
     * 取过滤后的章节内容
     * @param bookId
     * @param chapterId
     * @return
     */
    public String getChapterContentFilter(Long bookId, Long chapterId) {
        ChapterContentFilter o = chapterContentFilterRepository.findOne(chapterId);
        if (o != null) {
            return o.getContent();
        }
        return null;
    }

    /**
     * 取加密的章节内容
     * @param bookId
     * @param chapterId
     * @return
     */
    public String getChapterContentEncrypt(Long bookId, Long chapterId) {
        ChapterContentEncrypt o = chapterContentEncryptRepository.findOne(chapterId);
        if (o != null) {
            return o.getContent();
        }
        return null;
    }

    /**
     * 取加密章节内容
     * @param bookId
     * @param chapterIds
     * @return
     */
    public Map<Long, String> getChapterContentsMap(Long bookId, List<Long> chapterIds) {
        if (bookId != null && CollectionUtils.isNotEmpty(chapterIds)) {
            Map<Long, String> contents = Maps.newHashMap();
            List<ChapterContentEncrypt> list = chapterContentEncryptRepository.find(chapterIds);
            for (ChapterContentEncrypt o : list) {
                contents.put(o.getChapterId(), o.getContent());
            }
            return contents;
        }
        return null;
    }

    /**
     * 批量取加密的章节内容
     * @param bookId
     * @param chapterIds
     * @return
     */
    public List<String> getChapterContents(Long bookId, List<Long> chapterIds) {
        if (bookId != null && CollectionUtils.isNotEmpty(chapterIds)) {
            List<String> contents = Lists.newArrayList();
            List<ChapterContentEncrypt> list = chapterContentEncryptRepository.find(chapterIds);
            for (ChapterContentEncrypt e : list) {
                contents.add(e.getContent());
            }
            return contents;
        }
        return null;
    }

    /**
     * 加密并替换中文空格
     *
     * @param content
     * @return
     */
    public String encryptContent(String content) {
        return AES.encrypt(CompressUtil.gzip(content.trim().replaceAll("　", " ").getBytes()), keyConfig.getProperty("rs.aes.key"));
    }

    /**
     * 解密
     *
     * @param encrypt
     * @return
     */
    public String  decryptContent(String encrypt){
        return new String(CompressUtil.ungzip(AES.decrypt(encrypt.getBytes(),keyConfig.getProperty("rs.aes.key").getBytes())));
    }

}
