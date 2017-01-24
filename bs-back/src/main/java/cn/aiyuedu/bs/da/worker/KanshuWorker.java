package cn.aiyuedu.bs.da.worker;

import cn.aiyuedu.bs.da.enumeration.ConfigEnum;
import cn.aiyuedu.bs.da.model.CpBook;
import cn.aiyuedu.bs.da.model.CpChapter;
import cn.aiyuedu.bs.da.model.kanshu.*;
import cn.aiyuedu.bs.da.utils.XmlUtil;
import com.duoqu.commons.urlplus.URL;
import com.duoqu.commons.utils.HttpClient;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

/**
 * Created by tonydeng on 15/7/31.
 */
@Component("kanshuWorker")
public class KanshuWorker extends AbstractWorker {
    private List<String> conos;

    public List<Integer> getCpBookIds() {
        int error = 0;
        try {
            String s = null;
            while (s == null && error < getConos().size()) {
                URL url = getURL(
                        ConfigEnum.DA.KANSHU_HOST.getKey(),
                        ConfigEnum.KanshuPath.BOOK_LIST.getKey(),
                        getParams(null, null, error)
                );
                s = HttpClient.get(url.toString());

                if (StringUtils.isNotEmpty(s)) {
                    s = HttpClient.get(url.toString());
                    if (StringUtils.isNotEmpty(s) && s.indexOf("error") < 0) {
                        KanshuBookBases kanshuBookBases = XmlUtil.xmlToJavaByJAXB(s.getBytes(), KanshuBookBases.class);
                        if (kanshuBookBases != null && CollectionUtils.isNotEmpty(kanshuBookBases.getKanshuBookBases())) {
                            List<Integer> ids = Lists.newArrayList();
                            for (KanshuBookBase b : kanshuBookBases.getKanshuBookBases()) {
                                ids.add(b.getId());
                            }
                            return ids;
                        }
                    }
                }
                error++;
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (log.isErrorEnabled()) {
                log.error("kanshu get book list info error:'" + e.getMessage() + "'");
            }
        }
        return null;
    }

    @Override
    public CpBook getBookInfo(String cpBookId) {
        int error = 0;
        URL url = null;
        try {
            String s = null;
            while (s == null && error < getConos().size()) {
                url = getURL(
                        ConfigEnum.DA.KANSHU_HOST.getKey(),
                        ConfigEnum.KanshuPath.BOOK.getKey(),
                        getParams(cpBookId, null, error)
                );

                if (log.isDebugEnabled())
                    log.debug("kanshu get book url:'{}'", url.toString());
                s = HttpClient.get(url.toString());
                if (StringUtils.isNotEmpty(s) && s.indexOf("error") < 0) {
                    KanshuBooks kanshuBooks = XmlUtil.xmlToJavaByJAXB(s.getBytes(), KanshuBooks.class);
                    if (kanshuBooks != null) {
                        return kanshuBooks.getKanshuBook();
                    }
                } else {
                    s = null;
                }
                error++;
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (log.isErrorEnabled()) {
                log.error("kanshu get book  info error:'" + e.getMessage() + "'");
            }
        }
        return null;
    }

    @Override
    public List<CpChapter> getChapterList(String cpBookId) {
        int error = 0;
        try {
            String s = null;
            while (s == null && error < getConos().size()) {
                URL url = getURL(
                        ConfigEnum.DA.KANSHU_HOST.getKey(),
                        ConfigEnum.KanshuPath.CHAPTER_LIST.getKey(),
                        getParams(cpBookId, "0", error)
                );
                s = HttpClient.get(url.toString());

                if (StringUtils.isNotEmpty(s)) {
                    s = HttpClient.get(url.toString());
                    if (StringUtils.isNotEmpty(s) && s.indexOf("error") < 0) {
                        KanshuChapters kanshuChapters = XmlUtil.xmlToJavaByJAXB(s.getBytes(), KanshuChapters.class);
                        if (kanshuChapters != null && CollectionUtils.isNotEmpty(kanshuChapters.getChapters())) {
                            List<CpChapter> chapters = Lists.newArrayList();
                            chapters.addAll(kanshuChapters.getChapters());
                            return chapters;
                        }
                    }
                }
                error++;
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (log.isErrorEnabled()) {
                log.error("kanshu get chapter list info error:'" + e.getMessage() + "'");
            }
        }
        return null;
    }

    /**
     * 获取章节内容
     *
     * @param cpBookId
     * @param chapterId
     * @return
     */
    public String getChapterContent(String cpBookId, String chapterId) {
//        String s = null;
        int error = 0;
        URL url = null;

        try {
            String s = null;
            while (s == null && error < getConos().size()) {
                url = getURL(
                        ConfigEnum.DA.KANSHU_HOST.getKey(),
                        ConfigEnum.KanshuPath.CHAPTER_CONTENT.getKey(),
                        getParams(cpBookId, chapterId, error)
                );

                if (log.isDebugEnabled())
                    log.debug("kanshu get chapter content url:'{}'", url.toString());
                s = HttpClient.get(url.toString());
                if (StringUtils.isNotEmpty(s) && s.indexOf("error") < 0) {
                    KanshuContents kanshuContents = XmlUtil.xmlToJavaByJAXB(s.getBytes(), KanshuContents.class);
                    if (kanshuContents != null) {
                        return kanshuContents.getContent().getContent();
                    }
                }
                error++;
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (log.isErrorEnabled()) {
                log.error("kanshu get chapter list info error:'" + e.getMessage() + "'");
            }
        }
        return null;
    }

    /**
     * @param cpBookId
     * @param chapterId
     * @param errorNum
     * @return
     */
    public Map<String, String> getParams(@NotNull String cpBookId, String chapterId, int errorNum) {
        Map<String, String> params = Maps.newHashMap();
        if (StringUtils.isNotBlank(cpBookId)) {
            params.put("bookid", cpBookId);
        }
        if (errorNum < getConos().size()) {
            params.put("cono", getConos().get(errorNum));
        }
        if (StringUtils.isNotEmpty(chapterId)) {
            params.put("chapterid", chapterId);
        }
        return params;
    }

    public List<String> getConos() {
        if (CollectionUtils.isEmpty(conos)) {
            conos = Lists.newArrayList(Splitter.on(",").trimResults().split(ConfigEnum.DA.KANSHU_CONO.getKey()));
        }
        return conos;
    }
}
