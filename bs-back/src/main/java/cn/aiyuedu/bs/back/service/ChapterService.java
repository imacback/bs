package cn.aiyuedu.bs.back.service;

import cn.aiyuedu.bs.back.dto.ChapterUpdateDto;
import com.duoqu.commons.utils.EncodeUtils;
import cn.aiyuedu.bs.cache.service.BookCacheService;
import cn.aiyuedu.bs.cache.service.ChapterCacheService;
import cn.aiyuedu.bs.common.Constants;
import cn.aiyuedu.bs.common.Constants.BookStatus;
import cn.aiyuedu.bs.common.Constants.ChapterStatus;
import cn.aiyuedu.bs.common.Constants.FilterType;
import cn.aiyuedu.bs.common.Constants.UploadFileType;
import cn.aiyuedu.bs.common.dto.ResultDto;
import cn.aiyuedu.bs.common.orm.Page;
import cn.aiyuedu.bs.common.util.FileUtil;
import cn.aiyuedu.bs.common.util.StringUtil;
import cn.aiyuedu.bs.dao.dto.ChapterDto;
import cn.aiyuedu.bs.dao.dto.ChapterQueryDto;
import cn.aiyuedu.bs.dao.dto.FilterResultDto;
import cn.aiyuedu.bs.dao.entity.AdminUser;
import cn.aiyuedu.bs.dao.entity.Book;
import cn.aiyuedu.bs.dao.entity.Chapter;
import cn.aiyuedu.bs.dao.mongo.repository.BookRepository;
import cn.aiyuedu.bs.dao.mongo.repository.ChapterRepository;
import cn.aiyuedu.bs.dao.util.BeanCopierUtils;
import cn.aiyuedu.bs.service.ChapterContentService;
import cn.aiyuedu.bs.service.ChapterGeneralService;
import cn.aiyuedu.bs.service.FilterWordGeneralService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.io.Files;
import jodd.bean.BeanCopy;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.Charsets;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Service("chapterService")
public class ChapterService {

    private final Logger logger = LoggerFactory.getLogger(ChapterService.class);

    @Autowired
    private ChapterRepository chapterRepository;
    @Autowired
    private ChapterCacheService chapterCacheService;
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private BookService bookService;
    @Autowired
    private AdminUserService adminUserService;
    @Autowired
    private Properties bsBgConfig;
    @Autowired
    private FilterWordService filterWordService;
    @Autowired
    private FilterWordGeneralService filterWordGeneralService;
    @Autowired
    private ChapterContentService chapterContentService;
    @Autowired
    private ChapterGeneralService chapterGeneralService;
    @Autowired
    private BookCacheService bookCacheService;

    private boolean batchOperationStatus = false;

    public ChapterDto get(Long id) {
        Chapter chapter = chapterRepository.findOne(id);
        ChapterDto c = new ChapterDto();
        BeanCopierUtils.chapterCopy(chapter, c);

        if (c != null && c.getWords() > 0) {
            FilterResultDto r;
            String text = chapterContentService.getChapterContent(c.getBookId(), c.getId());
            if (StringUtils.isNotEmpty(text)) {
                r = filterWordGeneralService.filter(text, FilterType.Highlight);
                if (r != null) {
                    if (r.getReplaced()) {
                        c.setText(r.getText());
                    } else {
                        c.setText(text);
                    }

                    c.setFilterWords(filterWordService.getFilterWordInfo(r));
                }
            }

            if (StringUtils.isBlank(c.getOriginName())) {
                c.setOriginName(c.getName());
            }
            r = filterWordGeneralService.filter(c.getOriginName(), FilterType.Highlight);
            c.setOriginName(r.getText());
        }

        return c;
    }

    public boolean contain(Long id, String name) {
        return chapterRepository.exist(id, name);
    }

    public int count(Long bookId, Integer status) {
        ChapterQueryDto queryDto = new ChapterQueryDto();
        queryDto.setBookId(bookId);
        queryDto.setStatus(status);

        return (int) chapterRepository.count(queryDto);
    }

    /**
     * 取非选状态下最大序号
     * @param bookId
     * @param status
     * @return
     */
    public int getMaxOrderIdNotEqualStatus(Long bookId, Integer status) {
        Chapter chapter = chapterRepository.findOne(bookId, null, status, 1);
        if (chapter != null) {
            return chapter.getOrderId();
        }

        return 0;
    }

    /**
     * 取所属状态下最大序号
     * @param bookId
     * @param status
     * @return
     */
    public int getMaxOrderId(Long bookId, Integer status) {
        Chapter chapter = chapterRepository.findOne(bookId, status, null, 1);
        if (chapter != null) {
            return chapter.getOrderId();
        }

        return 0;
    }

    /**
     * 取所属状态下最小序号
     * @param bookId
     * @param status
     * @return
     */
    public int getMinOrderId(Long bookId, Integer status) {
        Chapter chapter = chapterRepository.findOne(bookId, status, null, 0);
        if (chapter != null) {
            return chapter.getOrderId();
        }

        return 0;
    }

    /**
     * 取书籍总章节数
     * @param bookId
     * @return
     */
    public int getBookChapters(Long bookId) {
        ChapterQueryDto queryDto = new ChapterQueryDto();
        queryDto.setBookId(bookId);
        queryDto.setStatuses(Lists.newArrayList(
                ChapterStatus.Online.getId(),
                ChapterStatus.Audited.getId(),
                ChapterStatus.Saved.getId()));

        return (int) chapterRepository.count(queryDto);
    }

    /**
     * Description 章节的新增或修改
     * @param chapter
     * @param text
     * @param adminUser
     * @return
     */
    public ResultDto save(Chapter chapter, String text, AdminUser adminUser) {
        ResultDto result = new ResultDto();
        result.setSuccess(false);
        result.setReload(true);
        result.setInfo("更新成功！");
        chapter.setEditDate(new Date());
        if (adminUser != null) {
            chapter.setEditorId(adminUser.getId());
        }

        ChapterStatus chapterStatus;
        ChapterUpdateDto cuo = null;
        Chapter o;

        //过滤章节名
        FilterResultDto r = filterWordGeneralService.filter(StringUtil.removeLastBrTag(chapter.getOriginName()), Constants.FilterType.Replace);
        chapter.setOriginName(r.getOriginText());
        chapter.setName(r.getText());

        //去掉多余字符
        text = StringUtil.removeExtraChar(text);

        //过滤章节内容,并设置章节过滤前后的内容长度和所过滤的敏感词
        r = chapterGeneralService.processFilterWord(chapter, text);

        if (chapter.getId() != null) {

            o = get(chapter.getId());

            if (o != null) {
                Integer maxOrderId = getMaxOrderId(o.getBookId(), o.getStatus());
                Integer minOrderId = getMinOrderId(o.getBookId(), o.getStatus());
                if (o.getStatus() != ChapterStatus.Offline.getId()) {
                    checkOrderId(chapter, o, maxOrderId, minOrderId);//修正章节顺序
                }

                cuo = new ChapterUpdateDto(chapter, o, maxOrderId, minOrderId);//更新判断对象

                BeanCopy.beans(chapter, o).ignoreNulls(true).copy();
                chapter = o;
            }
        }

        //获取章节对应的书籍的是否收费isFee、收费起始章节feeChapter
        Book book = bookRepository.findOne(chapter.getBookId());
        //设置章节价格信息(只有所在的书籍收费且在书籍的收费开始章节在当前章节之前才设置章节价格信息)
        if (null != book && null != book.getIsFee() && book.getIsFee() == 1 &&
                (null == book.getFeeChapter() || (null != book.getFeeChapter() && chapter.getOrderId() >= book.getFeeChapter()))) {

            chapter.setIsFee(1);

            //价格信息,使用过滤后的内容长度进行计算
            if(null!=chapter.getFilteredWords() && chapter.getFilteredWords() > 0){
                chapter.setPrice(chapter.getFilteredWords() / 1000 * Constants.THOUSAND_WORDS_PRICE);
            }
        } else {
            chapter.setIsFee(0);
            chapter.setPrice(0);
        }

        if (chapter.getId() != null) {//update
            chapterStatus = ChapterStatus.getById(chapter.getStatus());
            chapterRepository.save(chapter);

            result.setSuccess(true);
            result.setInfo("更新成功！");

            if (chapterStatus == ChapterStatus.Online) { //章节上线，发布到redis；章节下线，
                online(chapter);
            }

            //对所在书籍下的章节进行重新排序
            if (cuo != null && cuo.reorder()) {
                reorderChapters(chapter.getBookId(), cuo);
            }
        } else {//insert
            chapter.setCreateDate(new Date());
            if (adminUser != null) {
                chapter.setCreatorId(adminUser.getId());
            }

            if (!contain(chapter.getId(), chapter.getName())) {
                if (chapter.getOrderId() == null || chapter.getOrderId() < 1) {
                    Integer orderId = chapterGeneralService.getMaxChapterOrderId(chapter.getBookId()) + 1;
                    chapter.setOrderId(orderId);
                }

                chapter.setStatus(ChapterStatus.Saved.getId());

                chapterRepository.persist(chapter);
                bookRepository.addChapters(chapter.getBookId(), 1);

                ChapterQueryDto chapterQueryDto = new ChapterQueryDto();
                chapterQueryDto.setBookId(chapter.getBookId());
                int count = (int) chapterRepository.count(chapterQueryDto);
                bookRepository.updateChapters(chapter.getBookId(), count);
                result.setSuccess(true);
                result.setInfo("保存成功！");
            } else {
                result.setInfo("数据已存在");
            }
        }

        if (result.getSuccess()) {
            //保存章节内容
            if (StringUtils.isNotEmpty(text) && r != null) {
                //设置过滤词,用户页面的回显
                if (StringUtils.isNotBlank(chapter.getFilterWords())) {
                    Map<String, String> callBack = Maps.newHashMap();
                    callBack.put("filterWords", filterWordService.getFilterWordInfo(chapter.getFilterWords()));
                    result.setCallBack(callBack);
                }
                //保存章节内容,包括原内容、过滤后内容、加密后内容
                chapterContentService.saveChapterContent(chapter.getBookId(), chapter.getId(), EncodeUtils.htmlUnescape(r.getText()), r.getOriginText());
            }
        }

        return result;
    }



    /**
     * 审核章节
     *
     * @param ids
     * @param adminUser
     * @return
     */
    public ResultDto audit(List<Long> ids, Long bookId, AdminUser adminUser) {
        ResultDto result = new ResultDto();
        result.setSuccess(false);
        result.setInfo("章节审核失败");

        if (CollectionUtils.isNotEmpty(ids)) {
            if (bookId != null) {
                Book book = bookRepository.findOne(bookId);
                if (book == null) {
                    result.setInfo("章节所属书籍不存在");
                } else if (book.getStatus() != BookStatus.Online.getId()) {
                    result.setInfo("章节所属书籍为非发布状态");
                } else {
                    chapterRepository.updateStatus(ids, ChapterStatus.Audited.getId(), adminUser.getId());
                    result.setSuccess(true);
                    result.setInfo("章节审核成功");
                }
            }
        } else {
            result.setInfo("请选择要审核的章节");
        }

        return result;
    }

    private void online(Chapter chapter) {
        if (chapter.getStatus() == ChapterStatus.Online.getId()) {
            Book book = bookRepository.findOne(chapter.getBookId());
            if (book != null) {
                if (book.getUpdateChapterId() == chapter.getId()) {
                    bookRepository.updatePublishChapterDate(book.getId());
                    book.setUpdateChapterDate(new Date());
                    bookService.online(book, false);
                }

                chapterCacheService.set(chapter);
            }
        }
    }

    public void online(List<Chapter> chapters, Long bookId) {
        if (CollectionUtils.isNotEmpty(chapters)) {
            List<Chapter> list = Lists.newArrayList();
            for (Chapter chapter : chapters) {
                if (chapter.getStatus() == ChapterStatus.Online.getId()) {
                    list.add(chapter);
                }
            }

            if (CollectionUtils.isNotEmpty(list)) {
                chapterCacheService.set(bookId, list);
            }
        }
    }

    public ResultDto online(List<Long> ids, Long bookId, AdminUser adminUser) {
        ResultDto result = new ResultDto();
        result.setInfo("发布失败");
        result.setSuccess(false);

        Integer adminUserId = null;
        if (adminUser != null) {
            adminUserId = adminUser.getId();
        }

        Book b = bookRepository.findOne(bookId);
        if (b == null || b.getStatus() != BookStatus.Online.getId()) {
            result.setInfo("[" + b.getName() + "]所属书籍不存在或已下线");
            return result;
        }

        List<Integer> orderIds = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(ids)) {
            ChapterQueryDto queryDto = new ChapterQueryDto();
            queryDto.setIds(ids);
            queryDto.setIsDesc(0);
            queryDto.setOrderType(2);

            List<Chapter> chapters = chapterRepository.find(queryDto);
            if (CollectionUtils.isNotEmpty(chapters)) {
                Integer currentOrderId = getMaxOrderId(bookId, BookStatus.Online.getId());
                boolean reorder = false;
                int newCount = 0;
                for (Chapter c : chapters) {
                    if (c.getOrderId() > currentOrderId + 1) {
                        result.setInfo("禁止发布序号不连续的章节");
                        return result;
                    }

                    orderIds.add(c.getOrderId());

                    if (chapterRepository.exist(bookId, c.getId(), c.getOrderId())) {
                        reorder = true;
                    }

                    currentOrderId = c.getOrderId();
                    newCount++;
                }

                chapterRepository.updateStatus(ids, ChapterStatus.Online.getId(), adminUserId);

                if (reorder) {
                    ChapterUpdateDto cud = new ChapterUpdateDto();
                    cud.addOrderIds(orderIds);
                    cud.addChapterIds(ids);
                    reorderChapters(bookId, cud);
                } else {
                    Date now = new Date();
                    int words = 0;
                    if (b.getWords() != null) {
                        words = b.getWords();
                    }
                    if (reorder) words = 0;
                    Chapter c = null;
                    for (int i = 0, size = chapters.size(); i < size; i++) {
                        c = chapters.get(i);
                        if (c != null) {
                            words = words + c.getFilteredWords();
                            c.setSumWords(words);
                            chapterRepository.updateSumWord(c.getId(), words);
                        }
                    }

                    if (newCount > 0) {
                        b.setPublishChapters(count(b.getId(), ChapterStatus.Online.getId()));
                        b.setUpdateChapter(c.getName());
                        b.setUpdateChapterDate(now);
                        b.setUpdateChapterId(c.getId());
                    }
                    b.setWords(words);
                    bookRepository.persist(b);
                    bookService.online(b, false);

                    chapterGeneralService.save(b.getId(), chapters);
                }

                result.setSuccess(true);
                result.setInfo("发布成功");

            }
        }

        return result;
    }

    private void offline(Long bookId) {
        chapterCacheService.remove(bookId);
    }

    public ResultDto offline(List<Long> ids, Long bookId, AdminUser adminUser) {
        ResultDto result = new ResultDto();
        result.setInfo("操作失败");
        result.setSuccess(false);

        ChapterQueryDto queryDto = new ChapterQueryDto();
        queryDto.setIsDesc(0);
        queryDto.setOrderType(2);
        queryDto.setIds(ids);
        queryDto.setBookId(bookId);
        List<Chapter> chapters = chapterRepository.find(queryDto);

        Book book = bookRepository.findOne(bookId);

        if (CollectionUtils.isNotEmpty(chapters)) {
            for (Chapter c : chapters) {
                chapterCacheService.remove(c);
            }
        }

        chapterRepository.updateStatus(ids, ChapterStatus.Offline.getId(), adminUser.getId());

        reorderChapters(bookId, null);//重排序号并计算发布章节数

        result.setInfo("操作成功");
        result.setSuccess(true);

        return result;
    }

    public void deleteByBook(Long bookId) {
        offline(bookId);
        chapterRepository.removeMultiByBookId(bookId);
        chapterContentService.removeAllChapterByBookId(bookId);
    }

    public ResultDto delete(List<Long> ids, Long bookId, AdminUser adminUser) {
        ResultDto result = new ResultDto();
        result.setSuccess(false);
        result.setInfo("删除失败");
        if (CollectionUtils.isNotEmpty(ids)) {
            ChapterQueryDto queryDto = new ChapterQueryDto();
            queryDto.setIds(ids);
            List<Chapter> list = chapterRepository.find(queryDto);
            List<Long> onlineIds = Lists.newArrayList();
            List<Long> otherIds = Lists.newArrayList();
            if (CollectionUtils.isNotEmpty(list)) {
                for (Chapter c : list) {
                    if (c.getStatus() == ChapterStatus.Online.getId()) {
                        chapterCacheService.remove(c);
                        onlineIds.add(c.getId());
                    } else {
                        otherIds.add(c.getId());
                    }
                }
            }

            chapterRepository.delete(ids);
            chapterContentService.removeChapterContents(bookId, ids);

            reorderChapters(bookId, null);

            result.setSuccess(true);
            result.setInfo("删除成功");
        }

        return result;
    }

    /**
     * 对章节orderId重新排序,并重新设置章节的总偏移量sumWords,同时更新书籍相关信息
     * @param bookId
     * @param chapterUpdateDto
     */
    private void reorderChapters(Long bookId, ChapterUpdateDto chapterUpdateDto) {

        //按orderId升序查询bookId下的章节
        ChapterQueryDto queryDto = new ChapterQueryDto();
        queryDto.setBookId(bookId);
        queryDto.setIsDesc(0);
        queryDto.setOrderType(2);
        queryDto.setStatuses(Lists.newArrayList(
                ChapterStatus.Online.getId(),
                ChapterStatus.Audited.getId(),
                ChapterStatus.Saved.getId()));

        List<Chapter> chapters = chapterRepository.find(queryDto);

        //bookId书籍下所有章节ID的集合
        List<Long> chapterIds = Lists.newArrayList();
        //已上线的章节的集合
        List<Chapter> onlineChapters = Lists.newArrayList();

        Book book = bookRepository.findOne(bookId);

        if (CollectionUtils.isNotEmpty(chapters)) {
            //重置章节的orderId和章节的总偏移量sumWords
            int orderId = 1;
            int size = chapters.size();
            List<Chapter> list = Lists.newArrayListWithCapacity(size);
            Chapter c;
            Chapter next;
            for (int i=0; i<size; i++) {
                c = chapters.get(i);
                next = i<size-1? chapters.get(i+1): null;
                if (c != null) {
                    if (chapterUpdateDto != null) {
                        if (chapterUpdateDto.getChapterIds().contains(c.getId()) ||
                                (c.getStatus() == ChapterStatus.Audited.getId() &&
                                        next != null &&
                                        next.getStatus() == ChapterStatus.Online.getId())) { //
                            list.add(c);
                            continue;
                        } else if (chapterUpdateDto.getOrderIds().contains(orderId)) {
                            orderId = filterOrderId(chapterUpdateDto.getOrderIds(), orderId + 1);
                        }
                    } else if (c.getStatus() == ChapterStatus.Audited.getId() &&
                            next != null &&
                            next.getStatus() == ChapterStatus.Online.getId()) {
                        list.add(c);
                        continue;
                    }

                    c.setOrderId(orderId);

                    list.add(c);
                    orderId++;
                }
            }

            int words = 0;
            Chapter lastUpdateChapter = null;
            for (int i=0; i<size; i++) {
                c = list.get(i);
                chapterIds.add(c.getId());
                if (c.getStatus() == ChapterStatus.Online.getId()) {
                    words = words + c.getWords();
                    c.setSumWords(words);
                    onlineChapters.add(c);
                    lastUpdateChapter = c;
                    chapterRepository.updateOrderId(c.getId(), c.getOrderId(), c.getSumWords());
                } else {
                    chapterRepository.updateOrderId(c.getId(), c.getOrderId());
                }
            }

            if (CollectionUtils.isNotEmpty(onlineChapters)) {
                online(onlineChapters, bookId);
                book.setPublishChapters(onlineChapters.size());
            } else {
                book.setPublishChapters(0);
            }

            if (words > 0) {
                book.setWords(words);//书籍下章节的总字数

                if (lastUpdateChapter != null) {
                    if (lastUpdateChapter.getPublishDate() != null) {
                        book.setUpdateChapterDate(lastUpdateChapter.getPublishDate());
                    } else {
                        book.setUpdateChapterDate(new Date());
                    }
                    book.setUpdateChapter(lastUpdateChapter.getName());
                    book.setUpdateChapterId(lastUpdateChapter.getId());
                }
            }
        } else {
            book.setPublishChapters(0);
            book.setUpdateChapter("");
            book.setUpdateChapterId(0l);
        }

        bookRepository.save(book);
        //如果书籍为上线状态,则得更新Redis中的书籍信息
        if (book.getStatus() == BookStatus.Online.getId()) {
            bookService.online(book, false);
        }
    }

    private Integer filterOrderId(Set<Integer> orderIds, Integer orderId) {
        if (orderIds.contains(orderId)) {
            return filterOrderId(orderIds, orderId + 1);
        } else {
            return orderId;
        }
    }

    /**
     * 后台手动批量添加章节
     *
     * @param bookId
     * @param file
     * @param volumeSeparator
     * @param chapterSeparator
     * @param adminUser
     * @return
     */
    public ResultDto multiAdd(Long bookId, File file, String volumeSeparator, String chapterSeparator, AdminUser adminUser) {
        ResultDto result = new ResultDto();
        result.setSuccess(false);
        if (getBatchOperationStatus()) {
            result.setInfo("批量操作正在进行中，请稍后再操作！");
            return result;
        }
        if (file != null && file.isFile() && bookId != null && StringUtils.isNotEmpty(chapterSeparator)) {
            Book book = bookService.get(bookId);
            if (book != null) {
                Integer feeChapter = book.getFeeChapter();
                ChapterQueryDto queryDto = new ChapterQueryDto();
                queryDto.setBookId(bookId);
                int count = (int) chapterRepository.count(queryDto);
                setBatchOperationStatus(true);
                String encoding = FileUtil.getFileEncode(file);
                if (StringUtils.isNotEmpty(encoding)) {
                    try {
                        List<String> readLines = Files.readLines(file, Charsets.toCharset(encoding));
                        if (CollectionUtils.isNotEmpty(readLines)) {
                            String volume = null;
                            Chapter c = null;
                            String text;
                            StringBuilder sb = new StringBuilder();
                            String path = bsBgConfig.getProperty("path.upload.tmp")
                                    + File.separator + UploadFileType.ChapterContent.getName() + File.separator + bookId;
                            File to = new File(path);
                            if (!to.exists()) {
                                to.mkdirs();
                            }
                            boolean hasChapterName = false;
                            FilterResultDto r;
                            for (String line : readLines) {
                                if (StringUtils.isBlank(line)) continue;
                                if ((int) line.charAt(0) == 65279) line = line.substring(1);

                                if (line.startsWith(chapterSeparator)) {                        //章节名
                                    hasChapterName = true;
                                    if (sb.length() > 0 && c != null) {
                                        text = sb.toString();
                                        save(c, text, adminUser);
                                        sb.setLength(0);
                                    }

                                    c = new Chapter();
                                    c.setBookId(bookId);
                                    c.setOrderId(++count);
                                    c.setStatus(ChapterStatus.Saved.getId());
                                    if (feeChapter != null && feeChapter <= count) {
                                        c.setIsFee(1);
                                    } else {
                                        c.setIsFee(0);
                                    }

                                    r = filterWordGeneralService.filter(line.replaceFirst(chapterSeparator, ""), Constants.FilterType.Replace);
                                    c.setOriginName(r.getOriginText());
                                    c.setName(r.getText());
                                    if (StringUtils.isNoneEmpty(volume)) {
                                        c.setVolume(volume);
                                    }
                                } else if (StringUtils.isNotEmpty(volumeSeparator) &&           //卷名
                                        line.startsWith(volumeSeparator)) {
                                    volume = line.replaceFirst(volumeSeparator, "");
                                } else {                                                        //正文
                                    if (!hasChapterName) {
                                        result.setInfo("内容中没有章节名或没有标示章节分隔‘§§§‘！");
                                        setBatchOperationStatus(false);
                                        return result;
                                    }
                                    sb.append(line).append("\n");
                                }
                            }
                            if (sb.length() > 0 && c != null) {
                                text = sb.toString();
                                save(c, text, adminUser);

                                sb.setLength(0);
                            }

                            result.setSuccess(true);
                            result.setInfo("保存成功！");
                        } else {
                            result.setInfo("文件没有内容！");
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        file.delete();
                    }
                } else {
                    result.setInfo("文件编码错误，请使用UTF-8编码重新上传！");
                }
            }

            setBatchOperationStatus(false);
        }

        return result;
    }

    /**
     * Description 更新某书籍下章节的价格
     * @param bookId 书籍ID
     * @param isFee 书籍是否收费
     * @param feeChapter 开始收费的章节orderId
     * @param adminUser
     */
    public void updateChaptersFee(Long bookId, Integer isFee, Integer feeChapter, AdminUser adminUser) {
        //按orderId升序查询bookId下的章节
        ChapterQueryDto queryDto = new ChapterQueryDto();
        queryDto.setBookId(bookId);
        queryDto.setIsDesc(0);
        queryDto.setOrderType(2);
        List<Chapter> list = chapterRepository.find(queryDto);

        List<Chapter> onlineChapters = Lists.newArrayList();
        Integer isChapterFee;
        Integer price;
        if (CollectionUtils.isNotEmpty(list)) {
            for (Chapter c : list) {
                isChapterFee = 0;
                price = 0;
                if (null!=isFee && isFee == 1 && feeChapter != null && c.getOrderId() >= feeChapter) {
                    isChapterFee = 1;
                    price = (c.getFilteredWords() / 1000) * Constants.THOUSAND_WORDS_PRICE;
                }
                if (c.getStatus() == ChapterStatus.Online.getId()) {
                    c.setIsFee(isChapterFee);
                    c.setPrice(price);
                    onlineChapters.add(c);
                }
                chapterRepository.updatePrice(c.getId(), isChapterFee, price, adminUser.getId());
            }

            if (CollectionUtils.isNotEmpty(onlineChapters)) {
                chapterCacheService.set(bookId, onlineChapters);
            }
        }
    }

    /**
     * Description 根据章节字数计算章节价格
     * @param chapterId
     * @param isFee
     * @param adminUser
     */
    public void updateChapterFee(Long chapterId, Integer isFee, AdminUser adminUser) {
        Integer price = 0;
        if (isFee == 1) {
            Chapter c = chapterRepository.findOne(chapterId);
            price = c.getFilteredWords() / 1000 * Constants.THOUSAND_WORDS_PRICE;
        }
        chapterRepository.updatePrice(chapterId, isFee, price, adminUser.getId());
    }

    /**
     * Description 在页面直接编辑章节价格
     * @param chapterId
     * @param price
     * @param adminUser
     */
    public void updateChapterPrice(Long chapterId, Integer price, AdminUser adminUser){
        Integer isFee = 1;
        chapterRepository.updatePrice(chapterId, isFee, price, adminUser.getId());
    }

    public boolean getBatchOperationStatus() {
        return batchOperationStatus;
    }

    public void setBatchOperationStatus(boolean batchOperationStatus) {
        this.batchOperationStatus = batchOperationStatus;
    }

    public Page<ChapterDto> getPage(ChapterQueryDto chapterQueryDto) {
        Page<Chapter> page = chapterRepository.getPage(chapterQueryDto);
        List<ChapterDto> result = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(page.getResult())) {
            ChapterDto cd;
            Book b;
            FilterResultDto r;
            for (Chapter c : page.getResult()) {
                cd = new ChapterDto();
                BeanCopierUtils.chapterCopy(c, cd);
                if (chapterQueryDto.getStart() != null) {
                    if (cd.getBookId() != null) {
                        b = bookService.get(cd.getBookId());
                        if (b != null) {
                            cd.setBookName(b.getName());
                        }
                    }
                    adminUserService.infoOperate(cd);
                    if (StringUtils.isNotEmpty(cd.getFilterWords())) {
                        cd.setFilterWords(filterWordService.getFilterWordInfo(cd.getFilterWords()));
                    }

                    if (StringUtils.isBlank(c.getOriginName())) {
                        c.setOriginName(c.getName());
                    }
                    if (StringUtils.isNotBlank(c.getOriginName())) {
                        r = filterWordGeneralService.filter(c.getOriginName(), FilterType.Highlight);
                        cd.setOriginName(r.getText());
                    }
                }

                result.add(cd);
            }
        }

        return new Page<>(result, page.getTotalItems());
    }

    public int count(Long bookId, ChapterStatus chapterStatus) {
        ChapterQueryDto chapterQueryDto = new ChapterQueryDto();
        chapterQueryDto.setBookId(bookId);
        chapterQueryDto.setStatus(chapterStatus.getId());
        return (int) chapterRepository.count(chapterQueryDto);
    }

    /**
     * Description 校验是否允许修改章节价格,只有在书籍收费且开始收费的章节orderId小于当前章节orderId才允许修改其价格
     * @param chapterId
     * @return
     */
    public boolean allowUpdatePrice(Long chapterId){
        boolean result = false;
        Chapter chapter = chapterRepository.findOne(chapterId);
        if(null!=chapter){
            Book book = bookRepository.findOne(chapter.getBookId());
            if(null!=book && null!=book.getIsFee() && book.getIsFee()==1
                    && null!=book.getFeeChapter() && null!=chapter.getOrderId()
                    && chapter.getOrderId()>=book.getFeeChapter()){
                result = true;
            }
        }
        return result;
    }

    /**
     * 更新章节偏移量
     * @param bookId
     * @param startOrderId
     * @return
     */
    public boolean changeChaptersSumWords(Long bookId, Integer startOrderId, boolean updateBookWords) {
        Book b = bookRepository.findOne(bookId);
        if (b != null && b.getStatus() == BookStatus.Online.getId()) {
            ChapterQueryDto queryDto = new ChapterQueryDto();
            queryDto.setBookId(bookId);
            queryDto.setStartOrderId(startOrderId > 1? startOrderId - 1: 1);
            queryDto.setOrderType(2);
            queryDto.setIsDesc(0);
            queryDto.setStatus(ChapterStatus.Online.getId());
            List<Chapter> chapters = chapterRepository.find(queryDto);
            if (CollectionUtils.isNotEmpty(chapters)) {
                int size = chapters.size();
                int words = 0;
                Chapter c;
                for (int i=0; i<size; i++) {
                    c = chapters.get(i);
                    if (i == 0) {
                        if (startOrderId > 1) {
                            words = c.getSumWords() != null && c.getSumWords() > 0? c.getSumWords(): c.getFilteredWords();
                        } else {
                            words = c.getFilteredWords();
                            c.setSumWords(words);
                            chapterRepository.updateSumWord(c.getId(), c.getSumWords());
                        }
                    } else {
                        words = words + c.getFilteredWords();
                        c.setSumWords(words);

                        chapterRepository.updateSumWord(c.getId(), c.getSumWords());
                    }
                }

                if (startOrderId > 1) {
                    chapters = chapters.subList(1, size - 1);
                }
                chapterCacheService.setBookChapters(bookId, chapters);

                if (updateBookWords) {
                    b.setWords(words);
                    bookRepository.updateWords(bookId, words);
                    bookCacheService.setBook(b);
                }
            }
        }

        return false;
    }

    public void checkOrderId(Chapter _new, Chapter _old, Integer maxOrderId, Integer minOrderId) {
        if (_new.getOrderId() > maxOrderId && _new.getOrderId() != _old.getOrderId()) {
            _new.setOrderId(maxOrderId);
        }

        if (_new.getOrderId() < minOrderId && _new.getOrderId() != _old.getOrderId()) {
            _new.setOrderId(minOrderId);
        }
    }
}
