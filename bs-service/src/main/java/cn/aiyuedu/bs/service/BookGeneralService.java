package cn.aiyuedu.bs.service;

import com.duoqu.commons.redis.client.RedisClient;
import com.duoqu.commons.utils.EncodeUtils;
import cn.aiyuedu.bs.cache.service.BookCacheService;
import cn.aiyuedu.bs.cache.util.RedisKeyUtil;
import cn.aiyuedu.bs.common.Constants;
import cn.aiyuedu.bs.common.Constants.BookStatus;
import cn.aiyuedu.bs.common.Constants.ChapterStatus;
import cn.aiyuedu.bs.common.dto.ResultDto;
import cn.aiyuedu.bs.common.model.BookBase;
import cn.aiyuedu.bs.common.model.ChapterBase;
import cn.aiyuedu.bs.common.model.TagBase;
import cn.aiyuedu.bs.common.orm.Page;
import cn.aiyuedu.bs.dao.dto.BookQueryDto;
import cn.aiyuedu.bs.dao.dto.ChapterQueryDto;
import cn.aiyuedu.bs.dao.dto.FilterResultDto;
import cn.aiyuedu.bs.dao.entity.Book;
import cn.aiyuedu.bs.dao.entity.Chapter;
import cn.aiyuedu.bs.dao.mongo.repository.BookRepository;
import cn.aiyuedu.bs.dao.mongo.repository.ChapterRepository;
import cn.aiyuedu.bs.dao.util.BeanCopierUtils;
import cn.aiyuedu.bs.index.BookIndexDao;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Description:
 *
 * @author yz.wu
 */
public class BookGeneralService {

    private final Logger logger = LoggerFactory.getLogger(BookGeneralService.class);

    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private BookIndexDao bookIndexDao;
    @Autowired
    private BookCacheService bookCacheService;
    @Autowired
    private Properties bsBgConfig;
    @Autowired
    private TagGeneralService tagGeneralService;
    @Autowired
    private FilterWordGeneralService filterWordGeneralService;
    @Autowired
    private ChapterGeneralService chapterGeneralService;
    @Autowired
    private ChapterRepository chapterRepository;
    @Autowired
    private RedisClient redisClient;
    @Autowired
    private MongoOperations mongoOperations;

    /**
     * api调用保存书籍
     * 新增书籍：1.状态为入库，保存到mongo；2.章节数、发布章节数、总字数为0，最新章节id为0
     * 已存在相同cp下的cp书籍id，不允许更新
     *
     * @param book
     * @return
     */
    public boolean saveWithApi(Book book) {
        ResultDto result = new ResultDto();
        Book b = get(book.getCpBookId(), book.getProviderId());
        if (b != null) {
            book.setId(b.getId());
            book.setChapters(b.getChapters());
            book.setFeeChapter(b.getFeeChapter());
            result.setSuccess(false);
            result.setInfo("same book exists with providerId and cpBookId");
        } else {
            Date date = new Date();
            book.setCreateDate(date);
            book.setEditDate(date);
            book.setStatus(BookStatus.Saved.getId());
            if (StringUtils.isNotBlank(book.getMemo())) {
                book.setMemo(EncodeUtils.htmlUnescape(book.getMemo()));
            }
            if (book.getChapters() == null) {
                book.setChapters(0);
            }
            book.setPublishChapters(0);//发布章节数
            book.setUpdateChapterId(0l);//最新章节id
            book.setWords(0);//总字数

            book.setFeePlatformIds(Lists.newArrayList(1,2));//默认收费平台：android, 360

            if (StringUtils.isEmpty(book.getSmallPic())) {//默认图片
                book.setSmallPic(bsBgConfig.getProperty("noimg.small"));
                book.setLargePic(bsBgConfig.getProperty("noimg.large"));
            }
            if (!isExist(book.getId(), book.getName(), book.getAuthor(), BookStatus.Online.getId())) {//当线上有相同书名和作者名的书，不允许新增
                bookRepository.persist(book);
                result.setSuccess(true);
            } else {
                result.setSuccess(false);
                result.setInfo("same book exists with bookName、author and online status");
            }
        }

        if (logger.isDebugEnabled()) {
            StringBuilder sb = new StringBuilder();
            sb.append("book saveWithApi")
                    .append(", providerId:").append(book.getProviderId())
                    .append(", batchId:").append(book.getBatchId())
                    .append(", cpBookId:").append(book.getCpBookId())
                    .append(", name:[").append(book.getName()).append("]")
                    .append(", success:").append(result.getSuccess());
            if (!result.getSuccess()) {
                sb.append(", reason:").append(result.getInfo());
            }
            logger.debug(sb.toString());
        }

        return result.getSuccess();
    }

    /**
     * 更新书籍章节数
     *
     * @param bookId
     * @param chapters
     * @return
     */
    public void updateChaptersAmount(Long bookId, Integer chapters) {
        bookRepository.updateChapters(bookId, chapters);
    }

    /**
     * 取书籍对象
     *
     * @param bookId
     * @return
     */
    public BookBase get(Long bookId, Integer operatePlatformId) {
        BookBase bookBase = bookCacheService.get(bookId);
        if (bookBase != null && operatePlatformId != null &&
                CollectionUtils.isNotEmpty(bookBase.getOperatePlatformIds()) &&
                bookBase.getOperatePlatformIds().contains(operatePlatformId)) {
            return bookBase;
        }

        return null;
    }

    public BookBase get(Long bookId) {
        return bookCacheService.get(bookId);
    }

    public String getBookBodyFromCache(Long bookId) {
        BookBase bookBase = get(bookId);
        if (bookBase != null) {
            return bookBase.toString();
        } else {
            return "not in redis";
        }
    }

    /**
     * 统计书籍数量
     *
     * @param bookQueryDto
     * @return
     */
    public int count(BookQueryDto bookQueryDto) {
        return (int) bookRepository.count(bookQueryDto);
    }

    /**
     * 根据分类分页取书籍列表
     * 按书籍pv倒序
     *
     * @param categoryId
     * @param pageNum
     * @param pageSize
     * @return
     */
    public Page<BookBase> getBooksByCategory(Integer categoryId, Integer operatePlatformId, Integer pageNum, Integer pageSize) {
        Integer start = (pageNum - 1) * pageSize;
        return copyPage(bookRepository.getPageByCategory(categoryId, operatePlatformId, start, pageSize));
    }

    private Page<BookBase> copyPage(Page<Book> page) {
        if (page != null && CollectionUtils.isNotEmpty(page.getResult())) {
            List<BookBase> list = Lists.newArrayList();
            BookBase b;
            for (Book o : page.getResult()) {
                b = new BookBase();
                BeanCopierUtils.bookBaseCopy(o, b);
                list.add(b);
            }

            return new Page<>(list, page.getTotalItems());
        }

        return new Page<>(new ArrayList<BookBase>(), 0);
    }

    /**
     * 根据标签分页取书籍列表
     *
     * @param tagContentId
     * @param pageNum
     * @param pageSize
     * @return
     */
    public Page<BookBase> getBooksByTag(Integer tagContentId, Integer operatePlatformId, Integer pageNum, Integer pageSize) {
        Integer start = (pageNum - 1) * pageSize;
        return copyPage(bookRepository.getPageByTagContentId(null, tagContentId, operatePlatformId, start, pageSize));
    }

    /**
     * 取书籍内容类型标签
     *
     * @param bookId
     * @return
     */
    public List<TagBase> getBookContentTags(Long bookId) {
        BookBase bookBase = bookCacheService.get(bookId);
        if (bookBase != null) {
            List<Integer> tagContentIds = bookBase.getTagContentIds();
            if (CollectionUtils.isNotEmpty(tagContentIds)) {
                List<TagBase> list = Lists.newArrayList();
                TagBase t;
                for (Integer id : tagContentIds) {
                    t = tagGeneralService.get(id);
                    if (t != null) {
                        list.add(t);
                    }
                }
                if (CollectionUtils.isNotEmpty(list)) {
                    return list;
                }
            }
        }
        return null;
    }

    public List<TagBase> getBookContentTags(BookBase bookBase) {
        if (bookBase != null) {
            List<Integer> tagContentIds = bookBase.getTagContentIds();
            if (CollectionUtils.isNotEmpty(tagContentIds)) {
                List<TagBase> list = Lists.newArrayList();
                TagBase t;
                for (Integer id : tagContentIds) {
                    t = tagGeneralService.get(id);
                    if (t != null) {
                        list.add(t);
                    }
                }
                if (CollectionUtils.isNotEmpty(list)) {
                    return list;
                }
            }
        }
        return null;
    }

    /**
     * 排行榜
     *
     * @param rankingId
     * @param pageNum
     * @param pageSize
     * @return
     */
    public Page<BookBase> getRankingList(Integer rankingId, Integer operatePlatformId, Integer pageNum, Integer pageSize) {
        Integer start = (pageNum - 1) * pageSize;
        return copyPage(bookRepository.getRankingPage(rankingId, operatePlatformId, start, pageSize));
    }

    /**
     * 个人中心推荐书籍列表
     *
     * @return
     */
    public List<BookBase> getUserCenterRecommend() {
        return null;
    }

    /**
     * 取与书籍相同的故事类型的书籍列表
     * 如列表中没有数据，则取pv倒序排序列表书籍
     *
     * @param bookId
     * @return
     */
    public List<BookBase> getRecommendBooks(Long bookId, Integer operatePlatformId) {
        return getRecommendBooks(bookId, operatePlatformId, 4);
    }

    /**
     * 取与书籍相同的故事类型的书籍列表
     * 如列表中没有数据，则取pv倒序排序列表书籍
     *
     * @param bookId 被推荐的书籍id
     * @param count  返回推荐书籍数量
     * @return
     */
    public List<BookBase> getRecommendBooks(Long bookId, Integer operatePlatformId, int count) {
        BookBase bookBase = get(bookId);
        List<Long> excludeIds = Lists.newArrayList();
        if (bookBase != null) {
            BookQueryDto queryDto = new BookQueryDto();
            queryDto.setTagStoryId(bookBase.getTagStoryId());
            excludeIds.add(bookId);
            queryDto.setStart(0);
            queryDto.setLimit(count);
            queryDto.setIsDesc(1);
            queryDto.setOrderType(2);
            queryDto.setExcludeIds(excludeIds);
            queryDto.setOperatePlatformId(operatePlatformId);
            queryDto.setCheckLevel(Constants.BookModule.Recommend.getBookLevel());
            queryDto.setStatus(BookStatus.Online.getId());
            queryDto.setPublishChapters(0);
            List<Book> books = bookRepository.find(queryDto);
            queryDto.setTagStoryId(null);
            if (CollectionUtils.isEmpty(books)) {
                books = bookRepository.find(queryDto);
            } else if (books.size() < count) {
                for (Book b : books) {
                    excludeIds.add(b.getId());
                }
                queryDto.setExcludeIds(excludeIds);
                queryDto.setLimit(count - books.size());
                books.addAll(bookRepository.find(queryDto));
            }

            if (CollectionUtils.isNotEmpty(books)) {
                List<BookBase> list = Lists.newArrayList();
                BookBase b;
                for (Book o : books) {
                    b = new BookBase();
                    BeanCopierUtils.bookBaseCopy(o, b);
                    list.add(b);
                }

                return list;
            }
        }
        return null;
    }

    private boolean isExist(Long id, String name, String author, Integer status) {
        return bookRepository.exist(id, name, author, status);
    }

    /**
     * 根据providerId 取书籍对象
     *
     * @param cpBookId
     * @param providerId
     * @return
     */
    public Book get(String cpBookId, Integer providerId) {
        return bookRepository.findOne(cpBookId, providerId);
    }

    /**
     * Description 根据书籍ID集合,获取书籍集合
     *
     * @param ids
     * @return List<Book>
     */
    public List<BookBase> getBookFromDbByIds(List<Long> ids) {
        //非空处理
        if (null == ids || ids.size() <= 0) {
            return null;
        }

        List<BookBase> list = new ArrayList<BookBase>();
        for (Long l : ids) {
            list.add(this.get(l));
        }
        return list;
    }

    public void addPageView(Long bookId) {
        bookCacheService.addPageView(bookId);
    }

    public void restoreBook(Long bookId) {
        Book b = bookRepository.findOne(bookId);
        if (b != null) {
            boolean isPass = true;
            if (StringUtils.isBlank(b.getOriginMemo()) && StringUtils.isNotBlank(b.getMemo())) {
                isPass = false;
                b.setOriginMemo(b.getMemo());
            }
            FilterResultDto r = filterWordGeneralService.filter(b.getOriginMemo(), Constants.FilterType.Replace);
            if (!r.getIsPassed()) {
                isPass = false;
                b.setOriginMemo(r.getOriginText());
                b.setMemo(r.getText());
            }
            if (!isPass) {
                bookRepository.updateMemo(bookId, b.getOriginMemo(), EncodeUtils.htmlUnescape(b.getMemo()));
            }
        }

        chapterGeneralService.restoreChapters(b);
    }

    public void restoreBook(BookStatus bookStatus) {
        BookQueryDto queryDto = new BookQueryDto();
        if (bookStatus != null) {
            queryDto.setStatus(bookStatus.getId());
        }

        int count = (int) bookRepository.count(queryDto);

        int start = 0;
        int pageSize = 10;
        Page<Book> page;
        queryDto.setLimit(pageSize);
        int order = 0;
        while (start < count) {
            queryDto.setStart(start);
            page = bookRepository.getPage(queryDto);
            if (page != null && CollectionUtils.isNotEmpty(page.getResult())) {
                for (Book b : page.getResult()) {
                    if (b != null) {
                        if (logger.isDebugEnabled()) {
                            logger.debug("共" + count + "本书，当前第" + (++order) + "本，bookId:" + b.getId() + ", " + b.getName());
                        }
                        boolean isPass = true;
                        if (StringUtils.isBlank(b.getOriginMemo()) && StringUtils.isNotBlank(b.getMemo())) {
                            isPass = false;
                            b.setOriginMemo(b.getMemo());
                        }
                        FilterResultDto r = filterWordGeneralService.filter(b.getOriginMemo(), Constants.FilterType.Replace);
                        if (!r.getIsPassed()) {
                            isPass = false;
                            b.setOriginMemo(r.getOriginText());
                            b.setMemo(r.getText());
                        }
                        if (!isPass) {
                            b.setMemo(EncodeUtils.htmlUnescape(b.getMemo()));
                            bookRepository.updateMemo(b.getId(), b.getOriginMemo(), b.getMemo());
                            bookCacheService.set(b);
                            bookIndexDao.saveBook(b);
                        }

                        chapterGeneralService.restoreChapters(b);
                    }
                }
            }
            start = start + pageSize;
        }
    }

    public void restoreBook(Long... bookIds) {
        Book b;
        int count = bookIds.length;
        int order = 0;
        List<Book> list = Lists.newArrayList();
        for (Long bookId : bookIds) {
            b = bookRepository.findOne(bookId);
            if (b != null) {
                if (logger.isDebugEnabled()) {
                    logger.debug("共" + count + "本书，当前第" + (++order) + "本，bookId:" + b.getId() + ", " + b.getName());
                }
                boolean isPass = true;
                if (StringUtils.isBlank(b.getOriginMemo()) && StringUtils.isNotBlank(b.getMemo())) {
                    isPass = false;
                    b.setOriginMemo(b.getMemo());
                }
                FilterResultDto r = filterWordGeneralService.filter(b.getOriginMemo(), Constants.FilterType.Replace);
                if (!r.getIsPassed()) {
                    isPass = false;
                    b.setOriginMemo(r.getOriginText());
                    b.setMemo(r.getText());
                }
                if (!isPass) {
                    b.setMemo(EncodeUtils.htmlUnescape(b.getMemo()));
                    bookRepository.updateMemo(b.getId(), b.getOriginMemo(), b.getMemo());
                    bookCacheService.set(b);
                    bookIndexDao.saveBook(b);
                }

                chapterGeneralService.restoreChapters(b);
            }
        }
    }

    public void reloadBookChapterList() {
        BookQueryDto bookQueryDto = new BookQueryDto();
        bookQueryDto.setStatus(BookStatus.Online.getId());
        bookQueryDto.setPublishChapters(0);
        List<Book> books = bookRepository.find(bookQueryDto);
        if (CollectionUtils.isNotEmpty(books)) {
            if (logger.isDebugEnabled()) {
                logger.debug("reloadBookChapterList, books size:" + books.size());
            }
            List<Chapter> chapters;
            ChapterQueryDto chapterQueryDto = new ChapterQueryDto();
            chapterQueryDto.setIsDesc(0);
            chapterQueryDto.setOrderType(2);
            chapterQueryDto.setStatus(ChapterStatus.Online.getId());
            int i = 0;
            Map<String, String> orderIdMap = Maps.newHashMap();
            Map<Double, String> idsMap = Maps.newHashMap();

            for (Book b : books) {
                if (logger.isDebugEnabled()) {
                    logger.debug((++i) + ", bookId:" + b.getId());
                }
                chapterQueryDto.setBookId(b.getId());
                chapters = chapterRepository.find(chapterQueryDto);
                if (CollectionUtils.isNotEmpty(chapters)) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("------ bookId:" + b.getId() + ", chapters size:" + chapters.size());
                    }

                    orderIdMap.clear();
                    idsMap.clear();
                    for (ChapterBase c : chapters) {
                        orderIdMap.put(c.getOrderId().toString(), c.getId().toString());
                        idsMap.put(c.getOrderId().doubleValue(), c.getId().toString());
                    }

                    redisClient.delete(RedisKeyUtil.getBookChapterIdsByOrderIdHashKey(b.getId()), false);
                    redisClient.delete(RedisKeyUtil.getBookChapterIdsZSetKey(b.getId()), false);
                    if (logger.isDebugEnabled()) {
                        logger.debug("------ bookId:" + b.getId() + ", chapters removed");
                    }

                    redisClient.addToSortedSet(RedisKeyUtil.getBookChapterIdsZSetKey(b.getId()), idsMap);
                    redisClient.addToHash(RedisKeyUtil.getBookChapterIdsByOrderIdHashKey(b.getId()), orderIdMap);
                    if (logger.isDebugEnabled()) {
                        logger.debug("------ bookId:" + b.getId() + ", chapters inserted");
                    }
                }
            }
        }

    }

    public void batchOnline(List<Long> bookIds,List<Integer> feePlatforms,List<Integer> operatePlatforms, boolean isFee) {
        if (CollectionUtils.isNotEmpty(bookIds)) {
            Book book;
            for (Long bookId : bookIds) {
                book = bookRepository.findOne(bookId);
                if (book != null) {
                    onlineBook(book, feePlatforms, operatePlatforms, isFee);
                }
            }
        }
    }

    public int countByPic(Integer providerId, String picStartWithPattern) {
        BookQueryDto queryDto = new BookQueryDto();
        if (providerId != null) {
            queryDto.setProviderId(providerId);
        }
        if (StringUtils.isNotBlank(picStartWithPattern)) {
            queryDto.setSmallPic(picStartWithPattern);
        }

        return (int) bookRepository.count(queryDto);
    }

    public List<Book> getBooksByPic(Integer providerId, String picStartWithPattern, Integer start, Integer limit) {
        BookQueryDto queryDto = new BookQueryDto();
        if (providerId != null) {
            queryDto.setProviderId(providerId);
        }
        if (StringUtils.isNotBlank(picStartWithPattern)) {
            queryDto.setSmallPic(picStartWithPattern);
        }
        if (start != null && limit != null) {
            queryDto.setStart(start);
            queryDto.setLimit(limit);
        }

        return bookRepository.find(queryDto);
    }

    /**
     * 更新书籍书封，如果书籍为上线状态，同步更新上线
     *
     * @param bookId
     * @param smallPic
     * @param largePic
     */
    public void updatePic(Long bookId, String smallPic, String largePic) {
        Book book = bookRepository.updateCover(bookId, smallPic, largePic);
        if (book.getStatus() == BookStatus.Online.getId()) {
            bookCacheService.set(book);
            bookIndexDao.saveBook(book);
        }
    }

    /**
     * 批量设置书籍的收费信息
     * @param bookIds 批量书籍id
     * @param isFee 是否收费 true:收费，false:免费
     */
    public void setBooksWithFee(List<Long> bookIds, boolean isFee, List<Integer> feePlatformIds) {
        BookQueryDto queryDto = new BookQueryDto();
        queryDto.setIds(bookIds);
        List<Book> list = bookRepository.find(queryDto);
        int order = 1;
        if (CollectionUtils.isNotEmpty(list)) {
            int size = list.size();
            for (Book b : list) {
                if (logger.isDebugEnabled()) {
                    logger.debug("共" + size + "本书，当前第" + (++order) + "本，bookId:" + b.getId() + ", " + b.getName());
                }

                b.setOperatePlatformIds(Lists.newArrayList(1, 2, 3));//1:android, 2:360, 3:wap
                b.setFeePlatformIds(feePlatformIds);//1:android, 2:360
                b.setIsWholeFee(0);
                b.setWholePrice(null);

                if (isFee) {
                    Integer isSerial = b.getIsSerial();
                    if (b.getChapters() > 0) {
                        Integer chapters = b.getChapters();
                        Integer feeChapter;
                        if (isSerial != null && isSerial == 1 && chapters <= 100) {
                            feeChapter = null;
                            b.setIsFee(0);
                        } else {
                            b.setIsFee(1);
                            if (chapters <= 100) {
                                feeChapter = 11;
                            } else if (chapters <= 500) {
                                feeChapter = 21;
                            } else if (chapters <= 1000) {
                                feeChapter = 31;
                            } else {
                                feeChapter = 41;
                            }
                        }
                        b.setFeeChapter(feeChapter);
                    }
                } else {
                    b.setIsFee(0);
                    b.setFeeChapter(null);
                }

                if (b.getStatus() == BookStatus.Online.getId()) {

                    redisClient.addToHashWithMsgPack(RedisKeyUtil.HASH_BOOK, b.getId().toString(), b);
                    if (CollectionUtils.isNotEmpty(b.getCategoryIds()) &&
                            CollectionUtils.isNotEmpty(b.getOperatePlatformIds())) {
                        for (Integer id : b.getCategoryIds()) {
                            for (Integer operatePlatformId : b.getOperatePlatformIds()) {
                                redisClient.addToSortedSet(RedisKeyUtil.getBookCategoryZSetKey(id, operatePlatformId),
                                        b.getId().toString(),
                                        1 / Double.parseDouble(b.getCreateDate().getTime() + b.getId() + ""));
                            }
                        }
                    }
                }

                bookRepository.persist(b);

                chapterGeneralService.resetChapterFee(b.getId(), b.getFeeChapter());
            }
        }
    }

    public void onlineBook(Book b, List<Integer> feePlatformIds, List<Integer> operatePlatformIds, boolean isFee) {
        if (logger.isDebugEnabled()) {
            logger.debug("bookId:" + b.getId() + ", " + b.getName());
        }

        b.setOperatePlatformIds(operatePlatformIds);//1:android, 2:360, 3:wap
        b.setFeePlatformIds(feePlatformIds);//1:android, 2:360
        b.setIsWholeFee(0);
        b.setWholePrice(null);
        b.setDayPublishChapters(2);
        b.setCheckLevel(30);
        b.setStatus(BookStatus.Online.getId());

        if (isFee) {
            Integer isSerial = b.getIsSerial();
            if (b.getChapters() > 0) {
                Integer chapters = b.getChapters();
                Integer feeChapter;
                if (isSerial != null && isSerial == 1 && chapters <= 100) {
                    feeChapter = null;
                    b.setIsFee(0);
                } else {
                    b.setIsFee(1);
                    if (chapters <= 100) {
                        feeChapter = 11;
                    } else if (chapters <= 500) {
                        feeChapter = 21;
                    } else if (chapters <= 1000) {
                        feeChapter = 31;
                    } else {
                        feeChapter = 41;
                    }
                }
                b.setFeeChapter(feeChapter);
            }
        } else {
            b.setIsFee(0);
            b.setFeeChapter(null);
        }

        chapterGeneralService.batchOnline(b);
    }

    /**
     * 取书籍列表
     *
     * @param bookQueryDto
     * @return
     */
    public List<Book> find(BookQueryDto bookQueryDto) {
        return bookRepository.find(bookQueryDto);
    }

    /**
     * 删除书籍下所有章节
     *
     * @param bookIds
     * @return
     */
    public boolean deleteBookAllChapters(Long... bookIds) {
        if (bookIds != null && bookIds.length > 0) {
            if (logger.isDebugEnabled()) {
                logger.debug("delete book all chapters, book count : " + bookIds.length + ", start ###");
            }
            int count = 0;
            Book b;
            for (Long id : bookIds) {
                if (logger.isDebugEnabled()) {
                    logger.debug((++count) + ", book id : " + id);
                }
                b = bookRepository.findOne(id);
                if (b != null &&
                        (b.getStatus() == BookStatus.Offline.getId() || b.getStatus() == BookStatus.Saved.getId())
                        ) {
                    b.setPublishChapters(0);
                    b.setChapters(0);
                    b.setUpdateChapter("");
                    b.setUpdateChapterId(null);

                    bookRepository.persist(b);

                    bookCacheService.delete(id);

                    chapterGeneralService.deleteChaptersByBookId(id);
                }
            }

            if (logger.isDebugEnabled()) {
                logger.debug("delete book all chapters, end ###");
            }

            return true;
        }

        return false;
    }

    public boolean updateFeePlatformIds() {
        Update update = new Update().set("feePlatformIds", Lists.newArrayList(1, 2));
        mongoOperations.updateMulti(Query.query(Criteria.where(Constants.MONGODB_ID_KEY).gt(0)),
                update,
                Book.class);

        BookQueryDto queryDto = new BookQueryDto();
        queryDto.setStatus(BookStatus.Online.getId());

        int count = (int) bookRepository.count(queryDto);

        int start = 0;
        int pageSize = 100;
        Page<Book> page;
        queryDto.setLimit(pageSize);
        int order = 0;
        while (start < count) {
            queryDto.setStart(start);
            page = bookRepository.getPage(queryDto);
            if (page != null && CollectionUtils.isNotEmpty(page.getResult())) {
                for (Book b : page.getResult()) {
                    if (b != null) {
                        if (logger.isDebugEnabled()) {
                            logger.debug("共" + count + "本书，当前第" + (++order) + "本，bookId:" + b.getId() + ", " + b.getName());
                        }
                        bookCacheService.setBook(b);
                    }
                }
            }
            start = start + pageSize;
        }

        return false;
    }

    public boolean processBookData() {
        BookQueryDto queryDto = new BookQueryDto();
        queryDto.setStatus(BookStatus.Online.getId());
        queryDto.setIsFee(1);
        queryDto.setChapters(1);

        int count = (int) bookRepository.count(queryDto);

        int start = 0;
        int pageSize = 100;
        Page<Book> page;
        queryDto.setLimit(pageSize);
        int order = 0;
        while (start < count) {
            queryDto.setStart(start);
            page = bookRepository.getPage(queryDto);
            if (page != null && CollectionUtils.isNotEmpty(page.getResult())) {
                for (Book b : page.getResult()) {
                    if (b != null) {
                        if (logger.isDebugEnabled()) {
                            logger.debug("共" + count + "本书，当前第" + (++order) + "本，bookId:" + b.getId() + ", " + b.getName());
                        }
                        chapterGeneralService.resetChapterFee(b.getId(), b.getFeeChapter());
                    }
                }
            }
            start = start + pageSize;
        }

        return true;
    }

    /**
     * 书籍总章节数及发布章节数清洗
     * @return
     */
    public boolean restoreChaptersWithAllBooks() {
        BookQueryDto queryDto = new BookQueryDto();
        List<Book> list = bookRepository.find(queryDto);
        int order = 1;
        if (CollectionUtils.isNotEmpty(list)) {
            int size = list.size();

            ChapterQueryDto chapterQueryDto = new ChapterQueryDto();

            for (Book b : list) {
                if (logger.isDebugEnabled()) {
                    logger.debug("共" + size + "本书，当前第" + (++order) + "本，bookId:" + b.getId() + ", " + b.getName());
                }

                chapterQueryDto.setStatus(null);
                chapterQueryDto.setBookId(b.getId());
                int chapters = (int) chapterRepository.count(chapterQueryDto);
                chapterQueryDto.setStatus(ChapterStatus.Online.getId());
                int publishChapters = (int) chapterRepository.count(chapterQueryDto);

                if (logger.isDebugEnabled()) {
                    logger.debug("bookId:" + b.getId() + ", chapters:" + b.getChapters() + "|" + chapters + ", publishChapters:" + b.getPublishChapters() + "|" + publishChapters);
                }

                if (b.getChapters() == null ||
                        chapters != b.getChapters() ||
                        b.getPublishChapters() == null ||
                        publishChapters != b.getPublishChapters()) {
                    b.setChapters(chapters);
                    b.setPublishChapters(publishChapters);
                    bookRepository.updateChapters(b.getId(), chapters, publishChapters);

                    if (logger.isDebugEnabled()) {
                        logger.debug("bookId:" + b.getId() + ", chapters:" + chapters + ", publishChapters:" + publishChapters);
                    }

                    if (b.getStatus() != null && b.getStatus() == BookStatus.Online.getId()) {
                        redisClient.addToHashWithMsgPack(RedisKeyUtil.HASH_BOOK, b.getId().toString(), b);
                    }
                }

            }

            return true;
        }

        return false;
    }
}
