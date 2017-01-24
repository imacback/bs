package cn.aiyuedu.bs.service;

import cn.aiyuedu.bs.common.model.BookBase;
import com.duoqu.commons.utils.EncodeUtils;
import cn.aiyuedu.bs.cache.service.BookCacheService;
import cn.aiyuedu.bs.cache.service.ChapterCacheService;
import cn.aiyuedu.bs.common.Constants;
import cn.aiyuedu.bs.common.Constants.ChapterStatus;
import cn.aiyuedu.bs.common.dto.ChapterBaseDto;
import cn.aiyuedu.bs.common.dto.ChapterIndexDto;
import cn.aiyuedu.bs.common.dto.ResultDto;
import cn.aiyuedu.bs.common.model.ChapterBase;
import cn.aiyuedu.bs.common.util.CharUtil;
import cn.aiyuedu.bs.common.util.StringUtil;
import cn.aiyuedu.bs.dao.dto.ChapterQueryDto;
import cn.aiyuedu.bs.dao.dto.FilterResultDto;
import cn.aiyuedu.bs.dao.entity.Book;
import cn.aiyuedu.bs.dao.entity.Chapter;
import cn.aiyuedu.bs.dao.mongo.repository.BookRepository;
import cn.aiyuedu.bs.dao.mongo.repository.ChapterRepository;
import com.google.common.collect.Lists;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Description:
 *
 * @author yz.wu
 */
public class ChapterGeneralService {

    private static final Logger log = LoggerFactory.getLogger(ChapterContentService.class);

    @Autowired
    private ChapterRepository chapterRepository;
    @Autowired
    private ChapterContentService chapterContentService;
    @Autowired
    private ChapterCacheService chapterCacheService;
    @Autowired
    private FilterWordGeneralService filterWordGeneralService;
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private SolrGeneralService solrGeneralService;
    @Autowired
    private BookCacheService bookCacheService;

    public boolean exist(Long bookId, String cpChapterId) {
        return chapterRepository.exist(bookId, cpChapterId);
    }

    public boolean contain(Long chapterId, String name) {
        return chapterRepository.contain(chapterId, name);
    }

    public Chapter get(Long id) {
        return chapterRepository.findOne(id);
    }

    public String getChapterBodyFromCache(Long bookId, Long chapterId) {
        ChapterBase chapterBase = chapterCacheService.get(bookId, chapterId);
        if (chapterBase != null) {
            return chapterBase.toString();
        } else {
            return "not in redis";
        }
    }

    public Chapter get(Long bookId, String cpChapterId) {
        return chapterRepository.findOne(bookId, cpChapterId);
    }

    public Chapter getMaxOrderChapter(Long bookId) {
        return chapterRepository.findOne(bookId, null, ChapterStatus.Offline.getId(), 1);
    }

    public Integer getMaxChapterOrderId(Long bookId) {
        BookBase bookBase = bookRepository.findOne(bookId);
        if (bookBase != null) {
            return bookBase.getChapters();
        }

        return 1;
    }

    /**
     * API调用，保存章节及章节内容
     *
     * @param chapter
     * @param text
     * @return
     */
    public ResultDto saveWithApi(Chapter chapter, String text) {
        ResultDto result = new ResultDto();
        result.setSuccess(false);
        result.setInfo("保存失败！");
        if (chapter.getBookId() != null && chapter.getCpChapterId() != null) {
            if (exist(chapter.getBookId(), chapter.getCpChapterId())) {
                result.setInfo("章节已存在！");
            } else {
                text = StringUtil.removeExtraChar(text);
                FilterResultDto r = processFilterWord(chapter, text);
                Integer orderId = getMaxChapterOrderId(chapter.getBookId()) + 1;
                chapter.setOrderId(orderId);
                chapter.setStatus(ChapterStatus.Saved.getId());
                chapter.setCreateDate(new Date());

                chapterRepository.persist(chapter);
                bookRepository.addChapters(chapter.getBookId(), 1);
                result.setSuccess(true);
                result.setInfo("保存成功！");

                if (result.getSuccess()) {
                    //保存章节内容
                    if (StringUtils.isNotEmpty(text) && r != null) {
                        chapterContentService.saveChapterContentWithApi(
                                chapter.getBookId(), chapter.getId(),
                                r.getText(), r.getOriginText());
                    }
                }
            }
        }

        return result;
    }

    public ResultDto saveWithApi(Chapter chapter, String text, Integer isFee, Integer feeChapter) {
        ResultDto result = new ResultDto();
        result.setSuccess(false);
        result.setInfo("保存失败！");
        if (chapter.getBookId() != null && chapter.getCpChapterId() != null) {
            if (exist(chapter.getBookId(), chapter.getCpChapterId())) {
                result.setInfo("章节已存在！");
            } else {
                text = StringUtil.removeExtraChar(text);
                FilterResultDto r = processFilterWord(chapter, text);
                Integer orderId = getMaxChapterOrderId(chapter.getBookId()) + 1;
                chapter.setOrderId(orderId);
                chapter.setStatus(ChapterStatus.Saved.getId());
                chapter.setCreateDate(new Date());

                //设置章节价格信息(只有所在的书籍收费且在书籍的收费开始章节在当前章节之前才设置章节价格信息)
                if (isFee != null && isFee == 1 && feeChapter != null && orderId >= feeChapter) {
                    chapter.setIsFee(1);

                    //价格信息,使用过滤后的内容长度进行计算
                    if(chapter.getFilteredWords() != null && chapter.getFilteredWords() > 0){
                        chapter.setPrice(chapter.getFilteredWords() / 1000 * Constants.THOUSAND_WORDS_PRICE);
                    }
                } else {
                    chapter.setIsFee(0);
                    chapter.setPrice(0);
                }

                chapterRepository.persist(chapter);
                bookRepository.addChapters(chapter.getBookId(), 1);
                result.setSuccess(true);
                result.setInfo("保存成功！");

                if (result.getSuccess()) {
                    //保存章节内容
                    if (StringUtils.isNotEmpty(text) && r != null) {
                        chapterContentService.saveChapterContentWithApi(
                                chapter.getBookId(), chapter.getId(),
                                r.getText(), r.getOriginText());
                    }
                }
            }
        }

        return result;
    }

    /**
     * Description 章节内容敏感词过滤
     * @param chapter
     * @param text
     * @return
     */
    public FilterResultDto processFilterWord(Chapter chapter, String text) {
        if (StringUtils.isNotEmpty(text)) {
            //过滤章节内容
            FilterResultDto r = filterWordGeneralService.filter(text, Constants.FilterType.Replace);
            //章节内容过滤前的内容长度
            chapter.setWords(CharUtil.countChinese(text));
            //章节内容过滤后的内容长度
            chapter.setFilteredWords(r.getChineseLength());
            //所过滤的敏感词
            chapter.setFilterWords(filterWordGeneralService.getFilterWordInfo(r, false));
            return r;
        } else {
            chapter.setStatus(ChapterStatus.Error.getId());
            chapter.setFilterWords("");
            chapter.setWords(0);
            chapter.setFilteredWords(0);
        }

        return null;
    }

    /**
     * 批量保存章节
     *
     * @param bookId
     * @param list
     */
    public void save(Long bookId, List<? extends ChapterBase> list) {
        chapterCacheService.set(bookId, list);
    }

    /**
     * 通过下标批量取章节
     *
     * @param bookId
     * @param start
     * @param end
     * @return
     */
    public List<ChapterBase> getChapters(Long bookId, long start, long end) {
        return chapterCacheService.getChapters(bookId, start, end);
    }

    /**
     * 通过下标批量取章节，包含章节正文内容
     *
     * @param bookId
     * @param start
     * @param end
     * @return
     */
    public List<ChapterBaseDto> getChaptersWithContent(Long bookId, long start, long end) {
        List<ChapterBase> list = chapterCacheService.getChapters(bookId, start, end);
        if (CollectionUtils.isNotEmpty(list)) {
            return getChaptersWithContent(list);
        }

        return null;
    }

    /**
     * 通过排序批量取章节
     *
     * @param bookId
     * @param start
     * @param end
     * @param isDesc false正序，true倒序
     * @return
     */
    public List<ChapterBase> getChaptersByOrderId(Long bookId, double start, double end, boolean isDesc) {
        return chapterCacheService.getChapters(bookId, start, end, isDesc);
    }

    /**
     * 通过排序批量取章节，包含章节正文内容
     *
     * @param bookId
     * @param start
     * @param end
     * @param isDesc false正序，true倒序
     * @return
     */
    public List<ChapterBaseDto> getChaptersWithContentByOrderId(Long bookId, double start, double end, boolean isDesc) {
        Set<String> ids = chapterCacheService.getChapterIds(bookId, start, end, isDesc);
        List<ChapterBase> list = chapterCacheService.getChapters(ids, bookId);
        if (CollectionUtils.isNotEmpty(list)) {
            return getChaptersWithContent(list);
        }

        return null;
    }

    /**
     * 通过排序批量取章节，包含章节正文内容
     *
     * @param bookId
     * @param start
     * @param end
     * @param isDesc false正序，true倒序
     * @param isEncrypt 内容是否加密，false不加密，true加密
     * @return
     */
    public List<ChapterBaseDto> getChaptersWithContentByOrderId(Long bookId, double start, double end, boolean isDesc, boolean isEncrypt) {
        Set<String> ids = chapterCacheService.getChapterIds(bookId, start, end, isDesc);
        List<ChapterBase> list = chapterCacheService.getChapters(ids, bookId);
        if (CollectionUtils.isNotEmpty(list)) {
            return getChaptersWithContent(list, isEncrypt);
        }

        return null;
    }

    /**
     * 通过指定序号批量取章节，包含章节正文内容
     *
     * @param bookId
     * @param orderIds
     * @return
     */
    public List<ChapterBaseDto> getChaptersWithOrderIds(Long bookId, List<Integer> orderIds) {
        List<ChapterBase> list = chapterCacheService.getChapters(bookId, orderIds);
        if (CollectionUtils.isNotEmpty(list)) {
            return getChaptersWithContent(list);
        }

        return null;
    }


    /**
     * 通过指定序号批量取章节，包含章节正文内容
     *
     * @param bookId
     * @param orderIds
     * @return
     */
    public List<ChapterBaseDto> getChaptersWithOrderIdList(Long bookId, List<Integer> orderIds) {
        List<ChapterBase> list = chapterCacheService.getChapters(bookId, orderIds);
        if (CollectionUtils.isNotEmpty(list)) {
            List<ChapterBaseDto> result = Lists.newArrayList();
            ChapterBaseDto d = null;
            for (ChapterBase c : list) {
                d = new ChapterBaseDto();
                d.setChapterBase(c);
                d.setText(chapterContentService.getChapterContent(c.getBookId(), c.getId()));
                result.add(d);
            }
            return result;
        }

        return null;
    }

    private List<ChapterBaseDto> getChaptersWithContent(List<ChapterBase> chapterBases) {
        if (CollectionUtils.isNotEmpty(chapterBases)) {
            List<ChapterBaseDto> result = Lists.newArrayList();
            ChapterBaseDto d;
            for (ChapterBase c : chapterBases) {
                d = new ChapterBaseDto();
                d.setChapterBase(c);
                d.setText(chapterContentService.getChapterContentEncrypt(c.getBookId(), c.getId()));
                result.add(d);
            }

            return result;
        }

        return null;
    }

    private List<ChapterBaseDto> getChaptersWithContent(List<ChapterBase> chapterBases, boolean isEncrypt) {
        if (CollectionUtils.isNotEmpty(chapterBases)) {
            List<ChapterBaseDto> result = Lists.newArrayList();
            ChapterBaseDto d;
            for (ChapterBase c : chapterBases) {
                d = new ChapterBaseDto();
                d.setChapterBase(c);
                if (isEncrypt) {
                    d.setText(chapterContentService.getChapterContentEncrypt(c.getBookId(), c.getId()));
                } else {
                    d.setText(chapterContentService.getChapterContentFilter(c.getBookId(), c.getId()));
                }
                result.add(d);
            }

            return result;
        }

        return null;
    }

    public List<ChapterBaseDto> getChaptersWithContentByCharIndex(Long bookId, Integer charIndex, Integer previous, Integer next) {
        Chapter chapter = chapterRepository.findOne(bookId, charIndex);
        if (chapter != null) {
            Integer orderId = chapter.getOrderId();
            return getChaptersWithContentByOrderId(bookId, orderId - previous, orderId + next, false);
        }

        return null;
    }

    /**
     * Description 获取指定书籍的章节信息(无内容)
     *
     * @param bookId  书籍ID
     * @param orderId 当前的章节编号,结果集合里包含当前章节
     * @param size    要获取的大小
     * @param isDesc  获取方向, false表示正序，true表示降序
     * @return 返回不包含内容的章节信息
     * @author Wangpeitao
     */
    public List<ChapterBase> getChapters(Long bookId, Integer orderId, int size, boolean isDesc) {
        //结果集合
        List<ChapterBase> list = Lists.newArrayList();

        //非空判断
        if (null != bookId && null != orderId && size > 0) {
            //书籍的最大章节数
            Integer maxChapterNum = this.getMaxChapterOrderId(bookId);

            //该书籍存在章节
            if (null != maxChapterNum && maxChapterNum > 0) {

                //要获取章节的区间
                Integer start = null;
                Integer end = null;

                //向后取
                if (!isDesc) {
                    start = orderId;
                    end = orderId + size - 1;
                } else {//向前取
                    start = orderId - size + 1;
                    end = orderId;
                }
                //取的章节在当前章节数内
                if (!(start > maxChapterNum || end < 1)) {
                    //开始区间不能小于1
                    if (start < 1) {
                        start = 1;
                    }
                    //结束区间不能超过最大章节数
                    if (end > maxChapterNum) {
                        end = maxChapterNum;
                    }

                    //getChapters的边界是[start,end],同时会处理end的超出边界,orderId是从1开始
                    list = this.getChaptersByOrderId(bookId, new Double(start), new Double(end), isDesc);
                }
            }
        }
        return list;
    }

    @Async
    public void restoreChapters(Book book) {
        if (log.isDebugEnabled()) {
            log.debug("book:"+ book.getName()+", start restore chapters");
        }
        Integer pageSize = 100;
        ChapterQueryDto queryDto = new ChapterQueryDto();
        queryDto.setBookId(book.getId());
        queryDto.setIsDesc(0);
        queryDto.setOrderType(2);
        queryDto.setLimit(pageSize);
        int count = (int) chapterRepository.count(queryDto);
        List<ChapterIndexDto> chapterIndexDtos = Lists.newArrayList();

        Integer start = 0;
        if (count > 0) {
            List<Chapter> list;
            String text;
            Integer words = 0;
            FilterResultDto r;
            ChapterIndexDto cio;
            int textFilterCount = 0;

            while(start < count) {
                if (log.isDebugEnabled()) {
                    log.debug("restore book:"+book.getId()+ ", chapters totalCount:"+count+", size:"+pageSize+", current:"+start);
                }
                queryDto.setStart(start);
                list = chapterRepository.find(queryDto);
                if (CollectionUtils.isNotEmpty(list)) {
                    boolean isNamePass = true;
                    boolean isExtraPass = true;
                    for (Chapter c : list) {
                        //章节名称过滤处理
                        if (StringUtils.isBlank(c.getOriginName())) {
                            isNamePass = false;
                            c.setOriginName(c.getName());
                        }
                        r = filterWordGeneralService.filter(c.getOriginName(), Constants.FilterType.Replace);
                        if (!r.getIsPassed()) {
                            isNamePass = false;
                            c.setOriginName(r.getOriginText());
                            c.setName(r.getText());
                        }
                        if (!isNamePass) {
                            chapterRepository.updateName(c.getId(), c.getOriginName(), EncodeUtils.htmlUnescape(c.getName()));
                        }

                        //章节内容过滤处理
                        text = chapterContentService.getChapterContent(c.getBookId(), c.getId());

                        //多余重复字符处理
                        if (StringUtil.hasExtraChar(text)) {
                            isExtraPass = false;
                            text = StringUtil.removeExtraChar(text);
                        }

                        r = processFilterWord(c, text);

                        if (!isNamePass || !isExtraPass || !r.getIsPassed()) {
                            if (c.getStatus() == ChapterStatus.Online.getId()) {
                                chapterCacheService.set(c);
                                cio = new ChapterIndexDto();
                                cio.setChapterBase(c);
                                cio.setText(r.getText());
                                chapterIndexDtos.add(cio);
                            }
                        }

                        chapterRepository.updateFilterWords(c.getId(), c.getFilterWords(), c.getFilteredWords());

                        if (!isExtraPass || !r.getIsPassed()) {
                            textFilterCount ++;
                            chapterContentService.saveChapterContent(c.getBookId(), c.getId(), r.getText(), r.getOriginText());
                        }

                        if (c.getStatus() == ChapterStatus.Online.getId()) {
                            words = words + c.getFilteredWords();
                        }
                    }
                }

                start = start + pageSize;
            }
            if (textFilterCount > 0) {//当有替换行为时，更新书籍总字数
                bookRepository.updateWords(book.getId(), words);
                solrGeneralService.saveBook(book);
            }
        }
        if (log.isDebugEnabled()) {
            log.debug("book:"+ book.getName()+", end restore chapters");
        }
    }

    public void batchOnline(Book book) {
        if (book != null) {
            Integer feeChapter = book.getFeeChapter();
            ChapterQueryDto queryDto = new ChapterQueryDto();
            queryDto.setBookId(book.getId());
            queryDto.setIsDesc(0);
            queryDto.setOrderType(2);

            FilterResultDto r;
            Date now = new Date();
            int count = (int) chapterRepository.count(queryDto);
            Integer pageSize = 100;
            queryDto.setLimit(pageSize);
            Integer start = 0;
            Integer isFee, price;
            int words = 0;

            if (count > 0) {
                List<Chapter> list;
                Chapter chapter = null;
                while (start < count) {
                    if (log.isDebugEnabled()) {
                        log.debug("restore chapters: chapters totalCount:" + count + ", size:" + pageSize + ", current:" + start);
                    }

                    queryDto.setStart(start);
                    list = chapterRepository.find(queryDto);

                    if (CollectionUtils.isNotEmpty(list)) {
                        for (Chapter c : list) {
                            if (StringUtils.isBlank(c.getOriginName())) {
                                c.setOriginName(c.getName());
                            }
                            r = filterWordGeneralService.filter(c.getOriginName(), Constants.FilterType.Replace);

                            c.setOriginName(r.getOriginText());
                            c.setName(r.getText());
                            c.setPublishDate(now);
                            c.setStatus(ChapterStatus.Online.getId());
                            words = words + c.getFilteredWords();
                            c.setSumWords(words);


                            if (c.getOrderId() != null) {
                                if (feeChapter == null || c.getOrderId() < feeChapter || c.getFilteredWords() == null) {
                                    isFee = 0;
                                    price = 0;
                                    c.setIsFee(0);
                                    c.setPrice(0);
                                } else {
                                    isFee = 1;
                                    price = (c.getFilteredWords() / 1000) * Constants.THOUSAND_WORDS_PRICE;
                                }

                                c.setIsFee(isFee);
                                c.setPrice(price);
                            }

                            chapterRepository.persist(c);
                            chapter = c;//最后发布章节
                        }
                    }

                    start = start + pageSize;
                }

                if (chapter != null) {
                    book.setUpdateChapterDate(now);
                    book.setUpdateChapter(chapter.getName());
                    book.setUpdateChapterId(chapter.getId());
                    book.setPublishChapters(count);
                }

                book.setWords(words);

                bookRepository.persist(book);
                bookCacheService.set(book);
                solrGeneralService.saveBook(book);

            }
        }
    }

    /**
     * 章节价格清洗
     * @param bookId
     */
    public void restoreChapters2Cache(Long bookId) {
        ChapterQueryDto queryDto = new ChapterQueryDto();
        queryDto.setIsDesc(0);
        queryDto.setBookId(bookId);
        queryDto.setStatus(ChapterStatus.Online.getId());
        queryDto.setOrderType(2);

        int count = (int) chapterRepository.count(queryDto);
        Integer pageSize = 100;
        queryDto.setLimit(pageSize);
        Integer start = 0;
        if (count > 0) {
            List<Chapter> list;
            while(start < count) {
                if (log.isDebugEnabled()) {
                    log.debug("restore chapters: chapters totalCount:"+count+", size:"+pageSize+", current:"+start);
                }
                queryDto.setStart(start);
                list = chapterRepository.find(queryDto);
                if (CollectionUtils.isNotEmpty(list)) {
                    for (Chapter c : list) {
                        c.setIsFee(0);
                        c.setPrice(0);

                        chapterRepository.updatePrice(c.getId(), 0, 0, 1);
                    }
                    chapterCacheService.setBookChapters(bookId, list);
                }

                start = start + pageSize;
            }
        }

    }

    public void resetChapterFee(Long bookId, Integer feeChapter) {
        ChapterQueryDto queryDto = new ChapterQueryDto();
        queryDto.setIsDesc(0);
        queryDto.setBookId(bookId);
        queryDto.setOrderType(2);

        int count = (int) chapterRepository.count(queryDto);
        Integer pageSize = 100;
        queryDto.setLimit(pageSize);
        Integer start = 0;
        Integer isFee, price, oldIsFee, oldPrice;
        if (count > 0) {
            List<Chapter> list;
            while(start < count) {
                if (log.isDebugEnabled()) {
                    log.debug("restore chapters: chapters totalCount:"+count+", size:"+pageSize+", current:"+start);
                }

                queryDto.setStart(start);
                list = chapterRepository.find(queryDto);

                if (CollectionUtils.isNotEmpty(list)) {
                    for (Chapter c : list) {
                        oldIsFee = c.getIsFee();
                        oldPrice = c.getPrice();
                        if (c.getOrderId() != null) {
                            if (feeChapter == null || c.getOrderId() < feeChapter || c.getFilteredWords() == null) {
                                isFee = 0;
                                price = 0;
                                c.setIsFee(0);
                                c.setPrice(0);
                            } else {
                                isFee = 1;
                                price = (c.getFilteredWords() / 1000) * Constants.THOUSAND_WORDS_PRICE;
                            }

                            c.setIsFee(isFee);
                            c.setPrice(price);

                            if (oldIsFee != isFee || oldPrice != price) {
                                chapterRepository.updatePrice(c.getId(), isFee, price, 1);
                                if (c.getStatus() == ChapterStatus.Online.getId()) {
                                    chapterCacheService.setBookChapter(c);
                                }
                            }
                        }
                    }
                }

                start = start + pageSize;
            }
        }
    }

    public void deleteChaptersByBookId(Long bookId) {
        chapterCacheService.remove(bookId);
        chapterRepository.removeMultiByBookId(bookId);
        chapterContentService.removeAllChapterByBookId(bookId);
    }
}
