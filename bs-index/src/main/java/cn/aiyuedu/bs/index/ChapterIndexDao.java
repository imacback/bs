package cn.aiyuedu.bs.index;

import cn.aiyuedu.bs.common.dto.ChapterIndexDto;
import cn.aiyuedu.bs.index.base.IndexDao;

import java.util.*;

/**
 * 章节索引接口
 */
public class ChapterIndexDao {
    IndexDao chapSolrImpl;

    public IndexDao getChapSolrImpl() {
        return chapSolrImpl;
    }

    public void setChapSolrImpl(IndexDao chapSolrImpl) {
        this.chapSolrImpl = chapSolrImpl;
    }

    public void saveChapter(ChapterIndexDto chap) {
        chapSolrImpl.addIndex(Collections.singletonList(buildDoc(chap)));
    }

    public void saveChapter(List<ChapterIndexDto> list) {
        List<Map<String, Object>> mapList = new ArrayList<>();
        for (ChapterIndexDto chap : list) {
            mapList.add(buildDoc(chap));
        }
        chapSolrImpl.addIndex(mapList);
    }

    private Map<String, Object> buildDoc(ChapterIndexDto chap) {
        Map<String, Object> doc = new HashMap<>();
        doc.put(CHAP_KEY_ID, chap.getChapterBase().getId());
        doc.put(CHAP_KEY_BOOK_ID, chap.getChapterBase().getBookId());
        doc.put(CHAP_KEY_CHAP_NAME, chap.getChapterBase().getName());
        doc.put(CHAP_KEY_CHAP_CTN, chap.getText());
        doc.put(CHAP_KEY_EDATE, new Date());
        return doc;
    }
    public void delByBookId(Long bookId) {
        String query = CHAP_KEY_BOOK_ID+":"+bookId;
        chapSolrImpl.delete(query);
    }
    public void delete(Long chapId) {
        String query = CHAP_KEY_ID+":"+chapId;
        chapSolrImpl.delete(query);
    }
    public void multiDelete(List<Long> ids) {
        for (Long id:ids){
            delete(id);
        }
    }
    public static final String CHAP_KEY_ID = "id";
    public static final String CHAP_KEY_BOOK_ID = "bookId";
    public static final String CHAP_KEY_CHAP_NAME = "chapName";
    public static final String CHAP_KEY_CHAP_CTN = "content";
    public static final String CHAP_KEY_EDATE = "editeDate";

}
