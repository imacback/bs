package cn.aiyuedu.bs.back.service;

import cn.aiyuedu.bs.dao.entity.*;
import cn.aiyuedu.bs.service.FilterWordGeneralService;
import cn.aiyuedu.bs.service.SolrGeneralService;
import com.duoqu.commons.utils.EncodeUtils;
import cn.aiyuedu.bs.back.dto.BookTagDto;
import cn.aiyuedu.bs.cache.service.BookCacheService;
import cn.aiyuedu.bs.common.Constants;
import cn.aiyuedu.bs.common.Constants.BookCheckLevel;
import cn.aiyuedu.bs.common.Constants.BookModule;
import cn.aiyuedu.bs.common.Constants.BookStatus;
import cn.aiyuedu.bs.common.Constants.ChapterStatus;
import cn.aiyuedu.bs.common.dto.ResultDto;
import cn.aiyuedu.bs.common.orm.Page;
import cn.aiyuedu.bs.common.util.StringUtil;
import cn.aiyuedu.bs.dao.dto.BookDto;
import cn.aiyuedu.bs.dao.dto.BookQueryDto;
import cn.aiyuedu.bs.dao.dto.ChapterQueryDto;
import cn.aiyuedu.bs.dao.dto.FilterResultDto;
import cn.aiyuedu.bs.dao.mongo.repository.BookRepository;
import cn.aiyuedu.bs.dao.mongo.repository.ChapterRepository;
import cn.aiyuedu.bs.dao.util.BeanCopierUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import jodd.bean.BeanCopy;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;

@Service("bookService")
public class BookService {

    private final Logger logger = LoggerFactory.getLogger(BookService.class);

    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private BookCacheService bookCacheService;
    @Autowired
    private ChapterService chapterService;
    @Autowired
    private ChapterRepository chapterRepository;
    @Autowired
    private SolrGeneralService solrGeneralService;
    @Autowired
    private ProviderService providerService;
    @Autowired
    private TagService tagService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private Properties bsBgConfig;
    @Autowired
    private MailService mailService;
    @Autowired
    private FilterWordGeneralService filterWordGeneralService;

    /**
     * 校验书籍权限
     *
     * @param id
     * @param bookModule
     * @param platformId
     * @return
     */
    public ResultDto check(Long id, BookModule bookModule, Integer platformId) {
        ResultDto result = new ResultDto();
        result.setSuccess(false);
        Book book = get(id);
        if (book != null) {
            if (book.getStatus() == BookStatus.Online.getId()) {
                if (book.getPublishChapters() > 0) {
                    if (book.getCheckLevel() == null) {
                        result.setInfo("书籍[" + id + "]没有设置权限，不能使用");
                    } else if (book.getCheckLevel() < bookModule.getBookLevel()) {
                        result.setInfo("书籍[" + id + "]不能加到此模块中");
                    } else if (CollectionUtils.isNotEmpty(book.getOperatePlatformIds()) && platformId != null) {
                        boolean contain = false;
                        for (Integer pid : book.getOperatePlatformIds()) {
                            if (pid == platformId) {
                                contain = true;
                                break;
                            }
                        }
                        if (contain) {
                            result.setSuccess(true);
                            result.setInfo("通过");
                        } else {
                            result.setInfo("书籍[" + id + "]不能运营在平台[" + platformId + "]");
                        }
                    } else {
                        result.setInfo("书籍或模块中缺少平台信息");
                    }
                } else {
                    result.setInfo("书籍[" + id + "]还没有发布任何章节");
                }
            } else {
                result.setInfo("书籍[" + id + "]还没有发布上线");
            }
        } else {
            result.setInfo("书籍[" + id + "]不存在");
        }

        return result;
    }

    public Book get(Long id) {
        return bookRepository.findOne(id);
    }

    public BookDto getBookDto(Long id) {
        return getBookDto(get(id));
    }

    public BookDto getBookDto(String cpBookId, Integer providerId) {
        return getBookDto(bookRepository.findOne(cpBookId, providerId));
    }

    private BookDto getBookDto(Book book) {
        BookDto o = null;
        if (book != null) {
            o = new BookDto();
            BeanCopierUtils.bookCopy(book, o);
        }

        if (o != null) {
            if (o.getProviderId() != null) {
                Provider p = providerService.get(o.getProviderId());
                if (p != null) {
                    o.setProviderName(p.getName());
                }
            }

            Tag t;
            if (o.getTagClassifyId() != null) {
                t = tagService.get(o.getTagClassifyId());
                if (t != null) {
                    if (t.getParentId() != null) {
                        Tag tp = tagService.get(t.getParentId());
                        if (tp != null) {
                            o.setTagClassifyName(tp.getName() + "-" + t.getName());
                        }
                    } else {
                        o.setTagClassifyName(t.getName());
                    }
                }
            }

            if (o.getTagSexId() != null) {
                t = tagService.get(o.getTagSexId());
                if (t != null) {
                    o.setTagSexName(t.getName());
                }
            }

            if (o.getTagStoryId() != null) {
                t = tagService.get(o.getTagStoryId());
                if (t != null) {
                    o.setTagStoryName(t.getName());
                }
            }

            StringBuilder sb = new StringBuilder();
            if (CollectionUtils.isNotEmpty(o.getTagContentIds())) {
                int j = 0;
                for (Integer tagId : o.getTagContentIds()) {
                    t = tagService.get(tagId);
                    if (t != null) {
                        if (j++ > 0) {
                            sb.append(",");
                        }
                        sb.append(t.getName());
                    }
                }
                if (sb.length() > 0) {
                    o.setTagContentNames(sb.toString());
                    sb.setLength(0);
                }
            }

            if (CollectionUtils.isNotEmpty(o.getTagSupplyIds())) {
                int j = 0;
                for (Integer tagId : o.getTagSupplyIds()) {
                    t = tagService.get(tagId);

                    if (t != null) {
                        if (j++ > 0) {
                            sb.append(",");
                        }
                        sb.append(t.getName());
                    }
                }
                if (sb.length() > 0) {
                    o.setTagSupplyNames(sb.toString());
                    sb.setLength(0);
                }
            }

            if (o.getBatchId() != null) {
                /*
                Batch b = batchService.get(o.getBatchId());
                if (b != null) {
                    o.setAuthorizeEndDate(DateUtils.getDateString(b.getAuthorizeEndDate(), "yyyy-MM-dd"));
                    o.setAuthorizeStartDate(DateUtils.getDateString(b.getAuthorizeStartDate(), "yyyy-MM-dd"));
                }
                */
            }

            if (StringUtils.isBlank(o.getOriginMemo())) {
                o.setOriginMemo(o.getMemo());
            }
            if (StringUtils.isNotBlank(o.getOriginMemo())) {
                FilterResultDto r = filterWordGeneralService.filter(o.getOriginMemo(), Constants.FilterType.Highlight);
                o.setOriginMemo(r.getText());
            }

            if (StringUtils.isBlank(o.getSmallPic())) {
                o.setSmallPic(bsBgConfig.getProperty("noimg.small"));
                o.setLargePic(bsBgConfig.getProperty("noimg.large"));
            }
        }

        return o;
    }

    public boolean isExist(Long id, String name, String author, Integer status) {
        return bookRepository.exist(id, name, author, status);
    }

    public int count(BookQueryDto bookQueryDto) {
        return (int) bookRepository.count(bookQueryDto);
    }

    public ResultDto save(Book book, AdminUser adminUser) {
        ResultDto result = new ResultDto();
        result.setSuccess(false);
        result.setInfo("保存失败！");

        if (isExist(book.getId(), book.getName(), book.getAuthor(), null)) {
            result.setInfo("数据已存在");
        } else {
            Date now = new Date();
            Integer userId = adminUser.getId();
            book.setEditDate(now);
            book.setEditorId(userId);

            if (StringUtils.isNotBlank(book.getOriginMemo())) {
                FilterResultDto r = filterWordGeneralService.filter(book.getOriginMemo(), Constants.FilterType.Replace);
                book.setOriginMemo(r.getOriginText());
                book.setMemo(EncodeUtils.htmlUnescape(r.getText()));
            }

            List<Integer> categoryIds = categoryService.analyseBookCategory(
                    book.getTagClassifyId(), book.getTagSexId(),
                    book.getTagContentIds(), book.getTagSupplyIds());
            if (CollectionUtils.isNotEmpty(categoryIds)) {
                book.setCategoryIds(categoryIds);
            }

            if (book.getId() == null) { //后台手动添加书籍
                book.setCreateDate(now);
                book.setCreatorId(userId);
                if (book.getIsFee() == 0) {
                    book.setIsWholeFee(0);
                }
                book.setChapters(0);
                book.setPublishChapters(0);
                book.setUpdateChapterId(0l);
                if (StringUtils.isEmpty(book.getSmallPic())) {
                    book.setSmallPic(bsBgConfig.getProperty("noimg.small"));
                    book.setLargePic(bsBgConfig.getProperty("noimg.large"));
                }

                if (book.getStatus() == BookStatus.Online.getId()) {
                    book.setOnlineDate(now);
                }
                bookRepository.persist(book);//保存到mongo
                if (book.getStatus() == BookStatus.Online.getId()) {
                    online(book, true);
                } else if (book.getStatus() == BookStatus.Offline.getId()) {
                    offline(book, false);
                }

                result.setSuccess(true);
                result.setInfo("保存成功！");
            } else { //后台更新书籍
                Book b = get(book.getId());
                boolean allowStore2Solr = solrGeneralService.allowStoreBook(book, b);
                if (b.getIsFee() != book.getIsFee() || b.getIsWholeFee() != b.getIsWholeFee() || b.getFeeChapter() != book.getFeeChapter()) {
                    chapterService.updateChaptersFee(book.getId(), book.getIsFee(), book.getFeeChapter(), adminUser);
                }

                Integer oldStatus = b.getStatus();
                Integer status = book.getStatus();
                BeanCopy.beans(book, b).ignoreNulls(true).copy();
                b.setFeeChapter(book.getFeeChapter());

                if (status == BookStatus.Online.getId()) { //上线书籍操作
                    if (oldStatus != BookStatus.Online.getId()) {
                        b.setOnlineDate(now);
                    }
                    b.setPublishChapters(chapterService.count(b.getId(), ChapterStatus.Online));
                    if (b.getPublishChapters() > 0 && b.getUpdateChapterDate() == null) {
                        Chapter chapter = chapterRepository.findOne(b.getId(), ChapterStatus.Online.getId(), null, 1);
                        if (chapter != null && chapter.getPublishDate() != null) {
                            b.setUpdateChapterDate(chapter.getPublishDate());
                            b.setUpdateChapter(chapter.getName());
                            b.setUpdateChapterId(chapter.getId());
                        }
                    }
                    online(b, allowStore2Solr);
                } else if (oldStatus == BookStatus.Online.getId() &&
                        status == BookStatus.Offline.getId()) {//下线书籍操作
                    offline(b, allowStore2Solr);
                }
                bookRepository.persist(b);//保存到mongo
                result.setSuccess(true);
                result.setInfo("更新成功！");
            }
        }

        return result;
    }

    /**
     * 书籍上线
     *
     * @param book
     */
    public void online(Book book, boolean allowStore2Solr) {
        if (book.getStatus() == BookStatus.Online.getId()) {
            bookCacheService.set(book);

            if (allowStore2Solr) {
                solrGeneralService.saveBook(book);
            }
        } else {
            if (logger.isErrorEnabled()) {
                logger.error("book save error for ths status is not online!");
            }
        }
    }

    /**
     * 书籍上线
     *
     * @param ids
     * @param adminUser
     */
    public void online(List<Long> ids, AdminUser adminUser) {
        BookQueryDto queryDto = new BookQueryDto();
        queryDto.setIds(ids);
        List<Book> books = bookRepository.find(queryDto);
        List<Long> newOnlineIds = Lists.newArrayList();
        List<Long> oldIds = Lists.newArrayList();
        for (Book book : books) {
            if (book.getStatus() != BookStatus.Online.getId()) {
                newOnlineIds.add(book.getId());
            } else {
                oldIds.add(book.getId());
            }
        }
        if (CollectionUtils.isNotEmpty(newOnlineIds)) {
            bookRepository.updateStatus(newOnlineIds, BookStatus.Online.getId(), adminUser.getId(), true);
        }
        if (CollectionUtils.isNotEmpty(oldIds)) {
            bookRepository.updateStatus(oldIds, BookStatus.Online.getId(), adminUser.getId(), false);
        }
        if (CollectionUtils.isNotEmpty(books)) {
            int publishChapters;
            for (Book book : books) {
                publishChapters = chapterService.count(book.getId(), ChapterStatus.Online);
                bookRepository.updatePublishChapters(book.getId(), publishChapters);
                book.setPublishChapters(publishChapters);
                book.setStatus(BookStatus.Online.getId());
                online(book, false);
            }
        }
    }

    /**
     * 书籍下线
     *
     * @param book
     */
    private void offline(Book book, boolean allowStore2Solr) {
        bookCacheService.set(book);
        if (allowStore2Solr) {
            solrGeneralService.deleteBook(book.getId());
            //chapterService.offline(book.getId());
        }
    }

    /**
     * 书籍下线
     *
     * @param ids
     * @param adminUser
     */
    public void offline(List<Long> ids, AdminUser adminUser) {
        bookRepository.updateStatus(ids, BookStatus.Offline.getId(), adminUser.getId(), false);
        solrGeneralService.multiDeleteBook(ids);
        Book book;
        for (Long id : ids) {
            book = get(id);
            bookCacheService.set(book);
            //chapterService.deleteByBook(id);
        }
    }

    /**
     * 书籍删除
     *
     * @param ids
     */
    public void delete(List<Long> ids, AdminUser adminUser) {
        offline(ids, adminUser);
        bookRepository.removeMulti(ids);
    }

    public Page<BookDto> getPage(BookQueryDto bookQueryDto) {
        Page<Book> page = bookRepository.getPage(bookQueryDto);
        List<BookDto> result = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(page.getResult())) {
            Provider p;
            Tag t;
            Tag tp;
            BookDto o;
            for (Book b : page.getResult()) {
                o = new BookDto();
                BeanCopierUtils.bookCopy(b, o);
                if (bookQueryDto.getStart() != null) {
                    if (o.getProviderId() != null) {
                        p = providerService.get(o.getProviderId());
                        if (p != null) {
                            o.setProviderName(p.getName());
                        }
                    }

                    if (o.getTagClassifyId() != null) {
                        t = tagService.get(o.getTagClassifyId());
                        if (t != null) {
                            if (t.getParentId() != null) {
                                tp = tagService.get(t.getParentId());
                                if (tp != null) {
                                    o.setTagClassifyName(tp.getName() + "-" + t.getName());
                                }
                            } else {
                                o.setTagClassifyName(t.getName());
                            }
                        }
                    }

                    if (o.getTagSexId() != null) {
                        t = tagService.get(o.getTagSexId());
                        if (t != null) {
                            o.setTagSexName(t.getName());
                        }
                    }

                    if (o.getTagStoryId() != null) {
                        t = tagService.get(o.getTagStoryId());
                        if (t != null) {
                            o.setTagStoryName(t.getName());
                        }
                    }
                }
                result.add(o);
            }
        }

        return new Page<>(result, page.getTotalItems());
    }

    /**
     * 自动发布章节
     */
    public void autoPublish() {
        BookQueryDto queryDto = new BookQueryDto();
        queryDto.setStatus(BookStatus.Online.getId());
        queryDto.setDayPublishChapters(0);
        int count = (int) bookRepository.count(queryDto);
        if (count > 0) {
            List<Book> books;
            List<Chapter> chapters;
            List<Long> chapterIds;

            ChapterQueryDto chapterQueryDto = new ChapterQueryDto();
            chapterQueryDto.setStatus(ChapterStatus.Audited.getId());
            chapterQueryDto.setIsDesc(0);
            chapterQueryDto.setOrderType(2);
            chapterQueryDto.setStart(0);

            int pageSize = 100;
            int page = (count - 1) / pageSize + 1;
            for (int i = 0; i < page; i++) {
                queryDto.setStart(i * pageSize);
                queryDto.setLimit(pageSize);
                books = bookRepository.find(queryDto);
                if (CollectionUtils.isNotEmpty(books)) {
                    for (Book book : books) {
                        if (book != null && book.getChapters() > book.getPublishChapters()) {
                            chapterQueryDto.setBookId(book.getId());
                            chapterQueryDto.setLimit(book.getDayPublishChapters());

                            chapters = chapterRepository.find(chapterQueryDto);
                            if (CollectionUtils.isNotEmpty(chapters)) {
                                chapterIds = Lists.newArrayList();
                                for (Chapter c : chapters) {
                                    if (logger.isDebugEnabled()) {
                                        logger.debug("autoPublish chapters, bookId:"+c.getBookId()+", chapterId:"+c.getId());
                                    }
                                    chapterIds.add(c.getId());
                                }
                                if (CollectionUtils.isNotEmpty(chapterIds)) {
                                    chapterService.online(chapterIds, book.getId(), null);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public ResultDto multiAddTag(File file, Integer providerId, AdminUser adminUser) {
        ResultDto result = new ResultDto();
        result.setSuccess(false);
        result.setInfo("添加失败！");

        if (file.exists()) {
            List<Map<Integer, String>> list = Lists.newArrayList();
            Map<Integer, String> map;
            try {
                Workbook wb = null;
                String fileName = file.getName().toLowerCase();
                if (fileName.indexOf(".xlsx") > -1) {
                    wb = new XSSFWorkbook(new FileInputStream(file));
                } else if (fileName.indexOf(".xls") > -1) {
                    wb = new HSSFWorkbook(new FileInputStream(file));
                }

                Sheet sheet = wb.getSheetAt(0);
                int rows = sheet.getPhysicalNumberOfRows();
                Row row;
                Cell cell;
                String cellValue;
                int cellType;
                for (int r = 2; r < rows; r++) {
                    row = sheet.getRow(r);
                    if (row == null) continue;
                    map = Maps.newHashMap();
                    for (int c = 0; c < 14; c++) {
                        cell = row.getCell(c);
                        if (cell != null) {
                            cellValue = null;
                            cellType = cell.getCellType();
                            if (cellType == Cell.CELL_TYPE_NUMERIC) {
                                cellValue = (int) cell.getNumericCellValue() + "";
                            } else if (cellType == Cell.CELL_TYPE_STRING) {
                                cellValue = cell.getStringCellValue();
                            }
                            if (StringUtils.isNotBlank(cellValue)) {
                                map.put(c, cellValue);
                            }
                        }
                    }
                    list.add(map);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                file.delete();
            }

            if (CollectionUtils.isNotEmpty(list)) {
                BookTagDto bookTagDto = process(list, providerId);
                if (bookTagDto.getSuccess()) {
                    for (BookQueryDto q : bookTagDto.getBookQueryDtos()) {
                        bookRepository.updateTags(q);
                    }
                }
                result.setSuccess(true);
                result.setInfo(bookTagDto.getInfo());
            }
        }

        return result;
    }

    public BookTagDto process(List<Map<Integer, String>> list, Integer providerId) {
        BookTagDto result = new BookTagDto();
        result.setSuccess(false);
        if (CollectionUtils.isNotEmpty(list)) {
            List<BookQueryDto> bookQueryDtos = Lists.newArrayList();
            BookQueryDto queryDto;
            Book book;
            Tag tag;
            int row = 1;
            String cpBookId;
            String a, b;
            StringBuilder sb = new StringBuilder();
            boolean success;
            int count = 0;
            for (Map<Integer, String> map : list) {
                success = true;
                queryDto = new BookQueryDto();
                if (map.get(-1) != null) {//bookId
                    queryDto.setId(Long.valueOf(map.get(-1).toString()));
                    cpBookId = map.get(-1).toString();
                } else {
                    cpBookId = map.get(0);
                    if (StringUtils.isNotBlank(cpBookId)) {
                        book = bookRepository.findOne(cpBookId, providerId);
                        if (book == null) {
                            success = false;
                            sb.append("书籍" + cpBookId + "不存在<br/>");
                        } else {
                            queryDto.setId(book.getId());
                        }
                    } else {
                        success = false;
                        sb.append("第" + row + "行，原站作品ID为空\n");
                    }
                }

                //tagClassifyId
                a = map.get(3);
                b = map.get(4);
                if (StringUtils.isNotBlank(a) && StringUtils.isNotBlank(b)) {
                    tag = tagService.getByName(a + Constants.SEPARATOR_2 + b);
                    if (tag == null) {
                        success = false;
                        sb.append("书籍" + cpBookId + "类别属性[" + a + "]不存在<br/>");
                    } else {
                        queryDto.setTagClassifyId(tag.getId());
                    }
                } else {
                    success = false;
                    sb.append("第" + row + "行，类别属性为空<br/>");
                }

                //tagSexId
                a = map.get(5);
                if (StringUtils.isNotBlank(a)) {
                    tag = tagService.getByName(a);
                    if (tag == null) {
                        success = false;
                        sb.append("书籍" + cpBookId + "性别属性[" + a + "]不存在<br/>");
                    } else {
                        queryDto.setTagSexId(tag.getId());
                    }
                } else {
                    success = false;
                    sb.append("第" + row + "行，类别属性为空<br/>");
                }

                //tagContentIds
                a = map.get(6);
                if (StringUtils.isNotBlank(a)) {
                    tag = tagService.getByName(a);
                    if (tag == null) {
                        success = false;
                        sb.append("书籍" + cpBookId + "内容时空[" + a + "]不存在<br/>");
                    } else {
                        List<Integer> tagContentIds = Lists.newArrayList();
                        tagContentIds.add(tag.getId());
                        queryDto.setTagContentIds(tagContentIds);
                    }
                } else {
                    success = false;
                    sb.append("书籍" + cpBookId + "内容时空为空<br/>");
                }
                a = map.get(7);
                if (StringUtils.isNotBlank(a)) {
                    tag = tagService.getByName(a);
                    if (tag == null) {
                        success = false;
                        sb.append("书籍" + cpBookId + "内容类型[" + a + "]不存在<br/>");
                    } else {
                        List<Integer> tagContentIds = queryDto.getTagContentIds();
                        tagContentIds.add(tag.getId());
                        queryDto.setTagStoryId(tag.getId());
                    }
                } else {
                    success = false;
                    sb.append("书籍" + cpBookId + "内容类型为空<br/>");
                }
                a = map.get(8);
                if (StringUtils.isNotBlank(a)) {
                    tag = tagService.getByName(map.get(8));
                    if (tag == null) {
                        success = false;
                        sb.append("书籍" + cpBookId + "内容风格[" + a + "]不存在<br/>");
                    } else {
                        List<Integer> tagContentIds = queryDto.getTagContentIds();
                        tagContentIds.add(tag.getId());
                    }
                } else {
                    success = false;
                    sb.append("书籍" + cpBookId + "内容风格为空<br/>");
                }

                //tagSupplyIds
                Iterable<String> names;
                a = map.get(9);
                if (StringUtils.isNotBlank(a)) {
                    names = StringUtil.split(a, Constants.GROUP_SEPARATOR);
                    for (String name : names) {
                        tag = tagService.getByName(name);
                        if (tag == null) {
                            success = false;
                            sb.append("书籍" + cpBookId + "内容情节补充[" + name + "]不存在<br/>");
                        } else {
                            List<Integer> tagSupplyIds = queryDto.getTagSupplyIds();
                            if (CollectionUtils.isEmpty(tagSupplyIds)) {
                                tagSupplyIds = Lists.newArrayList();
                            }
                            tagSupplyIds.add(tag.getId());
                            queryDto.setTagSupplyIds(tagSupplyIds);
                        }
                    }
                } else {
                    success = false;
                    sb.append("书籍" + cpBookId + "内容情节补充为空<br/>");
                }
                a = map.get(10);
                if (StringUtils.isNotBlank(a)) {
                    names = StringUtil.split(a, Constants.GROUP_SEPARATOR);
                    for (String name : names) {
                        tag = tagService.getByName(name);
                        if (tag == null) {
                            success = false;
                            sb.append("书籍" + cpBookId + "角色特征[" + name + "]不存在<br/>");
                        } else {
                            List<Integer> tagSupplyIds = queryDto.getTagSupplyIds();
                            if (CollectionUtils.isEmpty(tagSupplyIds)) {
                                tagSupplyIds = Lists.newArrayList();
                            }
                            tagSupplyIds.add(tag.getId());
                            queryDto.setTagSupplyIds(tagSupplyIds);
                        }
                    }
                } else {
                    success = false;
                    sb.append("书籍" + cpBookId + "角色特征为空<br/>");
                }
                a = map.get(11);
                if (StringUtils.isNotBlank(a)) {
                    names = StringUtil.split(a, Constants.GROUP_SEPARATOR);
                    for (String name : names) {
                        tag = tagService.getByName(name);
                        if (tag == null) {
                            success = false;
                            sb.append("书籍" + cpBookId + "角色身份[" + name + "]不存在<br/>");
                        } else {
                            List<Integer> tagSupplyIds = queryDto.getTagSupplyIds();
                            if (CollectionUtils.isEmpty(tagSupplyIds)) {
                                tagSupplyIds = Lists.newArrayList();
                            }
                            tagSupplyIds.add(tag.getId());
                            queryDto.setTagSupplyIds(tagSupplyIds);
                        }
                    }
                } else {
                    success = false;
                    sb.append("书籍" + cpBookId + "角色身份为空<br/>");
                }
                a = map.get(12);
                if (StringUtils.isNotBlank(a)) {
                    names = StringUtil.split(a, Constants.GROUP_SEPARATOR);
                    for (String name : names) {
                        tag = tagService.getByName(name);
                        if (tag == null) {
                            success = false;
                            sb.append("书籍" + cpBookId + "角色关系[" + name + "]不存在<br/>");
                        } else {
                            List<Integer> tagSupplyIds = queryDto.getTagSupplyIds();
                            if (CollectionUtils.isEmpty(tagSupplyIds)) {
                                tagSupplyIds = Lists.newArrayList();
                            }
                            tagSupplyIds.add(tag.getId());
                            queryDto.setTagSupplyIds(tagSupplyIds);
                        }
                    }
                } else {
                    success = false;
                    sb.append("书籍" + cpBookId + "角色关系为空<br/>");
                }
                a = map.get(13);
                if (StringUtils.isNotBlank(a)) {
                    names = StringUtil.split(a, Constants.GROUP_SEPARATOR);
                    for (String name : names) {
                        tag = tagService.getByName(name);
                        if (tag != null) {
                            List<Integer> tagSupplyIds = queryDto.getTagSupplyIds();
                            if (CollectionUtils.isEmpty(tagSupplyIds)) {
                                tagSupplyIds = Lists.newArrayList();
                            }
                            tagSupplyIds.add(tag.getId());
                            queryDto.setTagSupplyIds(tagSupplyIds);
                        }
                    }
                }

                List<Integer> categoryIds = categoryService.analyseBookCategory(
                        queryDto.getTagClassifyId(), queryDto.getTagSexId(),
                        queryDto.getTagContentIds(), queryDto.getTagSupplyIds());
                queryDto.setCategoryIds(categoryIds);

                //默认书籍等级为正常30
                queryDto.setCheckLevel(BookCheckLevel.Normal.getValue());

                if (success) {
                    count++;
                    bookQueryDtos.add(queryDto);
                }
            }

            sb.append("<br/>共处理数据" + list.size() + "条，成功入库" + count + "条");

            if (list.size() != count) {
                mailService.sendHtmlMail(bsBgConfig.getProperty("mail.from"), "wuyongzhao@duoquyuedu.com", "multiAddTag message", sb.toString());
                result.setInfo("共处理数据" + list.size() + "条，成功入库" + count + "条<br/>异常日志已发送到管理员邮箱");
            } else {
                result.setInfo("标签保存成功");
            }

            sb.setLength(0);
            result.setSuccess(true);
            result.setBookQueryDtos(bookQueryDtos);
        }

        return result;
    }
}
