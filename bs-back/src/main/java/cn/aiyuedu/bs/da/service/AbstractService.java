package cn.aiyuedu.bs.da.service;

import cn.aiyuedu.bs.common.dto.ResultDto;
import cn.aiyuedu.bs.da.enumeration.ConfigEnum;
import cn.aiyuedu.bs.da.model.CpBook;
import cn.aiyuedu.bs.da.model.CpChapter;
import cn.aiyuedu.bs.dao.dto.BatchQueryDto;
import cn.aiyuedu.bs.dao.entity.*;
import cn.aiyuedu.bs.dao.mongo.repository.BatchRepository;
import cn.aiyuedu.bs.dao.mongo.repository.ProviderRepository;
import cn.aiyuedu.bs.service.BookGeneralService;
import cn.aiyuedu.bs.service.ChapterGeneralService;
import cn.aiyuedu.bs.service.ImageUploadService;
import com.duoqu.commons.ip.constant.ImageEnum;
import com.google.common.collect.Lists;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

/**
 * Created by tonydeng on 14-10-17.
 */
public abstract class AbstractService {
    protected static final Logger trace = LoggerFactory.getLogger("trace");
    protected Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    protected ChapterGeneralService chapterGeneralService;

    @Autowired
    protected BookGeneralService bookGeneralService;

    @Autowired
    protected ImageUploadService imageUploadService;

    @Autowired
    private ProviderRepository providerRepository;

    @Autowired
    private BatchRepository batchRepository;

    public abstract List<Integer> getCpBookIds();

    public abstract CpBook getCpBookInfo(String cpBookId);

    protected abstract Book transformBook(BatchBook batchBook, CpBook cpBook);

    protected abstract String getCpChapterContent(String cpBookId, CpChapter cpChapter);

    public abstract List<CpChapter> getChapterList(String cpBookId);

    public String whoami() {
        return this.getClass().getSimpleName();
    }

    /**
     * 抓取、切图并设置书籍的封面图
     * @param book
     * @param batchBook
     * @param cpBook
     */
    public void uploadImage(Book book,BatchBook batchBook,CpBook cpBook){
        if (StringUtils.isNotEmpty(cpBook.getCpCover())) {
            Map<String, String> covers = imageUploadService.uploadBookCover(
                    batchBook.getProviderId(),
                    batchBook.getCpBookId(),
                    cpBook.getCpCover()
            );
            //设置书封图片
            if (covers != null) {
                book.setSmallPic(covers.get(ImageEnum.Flag.SMALL.getValue()));
                book.setLargePic(covers.get(ImageEnum.Flag.LARGE.getValue()));
            }
        }
    }


    public void init(ConfigEnum.Provider provider, String initCpBookId) {

        List<BatchBook> batchBooks = getBooks(provider, initCpBookId);

        if (CollectionUtils.isNotEmpty(batchBooks)) {

            for (BatchBook batchBook : batchBooks) {
                CpBook cpBook = getCpBookInfo(batchBook.getCpBookId());

                if (cpBook != null) {
                    if (trace.isInfoEnabled())
                        trace.info("抓取书籍ID:'{}' 书名《{}》 作者:'{}'",
                                batchBook.getCpBookId(), cpBook.getCpBookName(), cpBook.getCpAuthor());

                    Book book = transformBook(batchBook, cpBook);
                    boolean status = bookGeneralService.saveWithApi(book);
                    if (book.getId() != null) {
                        //抓取章节计算器
                        int updateChaptersNumber = 0;

                        if (trace.isInfoEnabled())
                            trace.info("书籍保存成功，多趣书籍ID:'" + book.getId() + "'");
                        //获取书籍分页数据
                        List<CpChapter> cpChapters = getChapterList(batchBook.getCpBookId());
                        //如果章节列表信息不为空
                        if (CollectionUtils.isNotEmpty(cpChapters)) {
                            for (CpChapter cpChapter : cpChapters) {
                                //保存章节
                                boolean saveChapterStatus = saveChapter(batchBook, book, cpChapter, updateChaptersNumber);
                                if(saveChapterStatus)
                                    updateChaptersNumber++;
                            }
                        }
                        //更新抓取章节数
                        updateChaptersCount(book.getId(), updateChaptersNumber);
                    }
                }
            }
        }

    }


    /**
     * 将更新抽离出来，希望能够使用统一的代码来进行所有cp的更新
     */
    public void update(ConfigEnum.Provider provider, String updateCpBookId) {
        List<BatchBook> batchBooks = getBooks(provider, updateCpBookId);
        /**
         * 如果当前书籍批次中有书
         */
        if (CollectionUtils.isNotEmpty(batchBooks)) {

            for (BatchBook batchBook : batchBooks) {
                /**
                 * 抓取书籍信息并更新书籍信息
                 */

                Book bookDto = updateBook(batchBook);

                if (bookDto != null && bookDto.getId() != null) {
                    updateChapters(provider, batchBook, bookDto);

                }
            }
        }

    }

    public void update(ConfigEnum.Provider provider, List<String> cpBookIds) {
        List<BatchBook> batchBooks = getBooks(provider, cpBookIds);
        /**
         * 如果当前书籍批次中有书
         */
        if (CollectionUtils.isNotEmpty(batchBooks)) {

            for (BatchBook batchBook : batchBooks) {
                /**
                 * 抓取书籍信息并更新书籍信息
                 */

                log.debug("bookId:"+batchBook.getCpBookId());

                Book bookDto = updateBook(batchBook);

                if (bookDto != null && bookDto.getId() != null) {
                    updateChapters(provider, batchBook, bookDto);
                }
            }
        }

    }

    /**
     * 对更新章节信息
     *
     * @param provider
     * @param batchBook
     * @param book
     */
    protected void updateChapters(ConfigEnum.Provider provider, BatchBook batchBook, Book book) {
        //数据库中存储的当前书的最后的CP章节ID
        String maxCpChapterId = null;

        Chapter lastChapter = getMaxCpBookChapter(book.getId());
        if (lastChapter != null) {
            /***
             * 获得上次书籍最后CP章节ID
             * 如果这本书是第一次抓取的话，是拿不到最后一章信息
             */
            maxCpChapterId = lastChapter.getCpChapterId();
        }

        //获取书籍分页数据
        List<CpChapter> cpChapters = getChapterList(batchBook.getCpBookId());
        //抓取章节计数器
        int updateChaptersNumber = book.getChapters();

        /**
         * 如果有当前书的最后CP章节ID
         * */
        if (StringUtils.isNotEmpty(maxCpChapterId)) {
            /**
             * 书籍章节更新流程
             */
            //多趣最后更新章节索引
            int lastCpChapterUpdateIndex = 0;
            //当前章节索引
            int currentIndex = 1;

            //如果章节列表信息不为空
            if (CollectionUtils.isNotEmpty(cpChapters)) {
                for (CpChapter cpChapter : cpChapters) {
                    if (maxCpChapterId.equals(cpChapter.getCpChapterId())) {
                        lastCpChapterUpdateIndex = currentIndex;
                        if (log.isDebugEnabled())
                            log.debug("数据库中最大CP章节ID:'{}'  当前抓取章节ID:'{}' 标题:'{}'", maxCpChapterId, cpChapter.getCpChapterId(), cpChapter.getCpChapterTitle());
                    }
                    //
                    if ((currentIndex > lastCpChapterUpdateIndex) && lastCpChapterUpdateIndex > 0) {
                        if (log.isDebugEnabled())
                            log.debug("多趣最后更新章节索引:'{}' 当前章节索引:'{}' 本次抓取章节总数:'{}' 当前章节ID:'{}' 标题:'{}'",
                                    lastCpChapterUpdateIndex, currentIndex, cpChapters.size(), cpChapter.getCpChapterId(), cpChapter.getCpChapterTitle());

                        if (trace.isInfoEnabled())
                            trace.info("抓取数据章节内容，多趣书籍ID:'{}' 章节:'{}' {}章节ID:'{}'",
                                    book.getId(), cpChapter.getCpChapterTitle(), provider.getComment(), cpChapter.getCpChapterId());

                        //保存章节
                        boolean saveChapterStatus = saveChapter(batchBook, book, cpChapter, updateChaptersNumber);
                        if(saveChapterStatus)
                            updateChaptersNumber++;
                    }

                    currentIndex++;
                }
                //更新抓取章节数
                updateChaptersCount(book.getId(), updateChaptersNumber);
            }
        } else {
            /**
             * 如果没有书的最后章节ID
             * 不进行判断
             * */
            if (CollectionUtils.isNotEmpty(cpChapters)) {
                //更新抓取章节数
                updateChaptersCount(book.getId(), updateChaptersNumber);

                for (CpChapter cpChapter : cpChapters) {

                    if (trace.isInfoEnabled())
                        trace.info("抓取数据章节内容，多趣书籍ID:'{}' 章节:'{}' {}章节ID:'{}'",
                                book.getId(), cpChapter.getCpChapterTitle(), provider.getComment(), cpChapter.getCpChapterId());
                    //保存章节
                    boolean saveChapterStatus = saveChapter(batchBook, book, cpChapter, updateChaptersNumber);
                    if(saveChapterStatus)
                        updateChaptersNumber++;
                }
                //更新抓取章节数
                updateChaptersCount(book.getId(), updateChaptersNumber);
            }
        }
    }

    /**
     * 更新书籍信息
     * @param batchBook
     * @return
     */
    protected Book updateBook(BatchBook batchBook){
        if(StringUtils.isNotEmpty(batchBook.getCpBookId())){
            CpBook cpBook =  getCpBookInfo(batchBook.getCpBookId());

            if (cpBook != null) {
                //从数据库中查询当前书是否已经抓取过
                Book bookDto = bookGeneralService.get(String.valueOf(batchBook.getCpBookId()),
                        batchBook.getProviderId());

                if (bookDto == null) {
                    Book book = transformBook(batchBook, cpBook);
                    boolean status = bookGeneralService.saveWithApi(book);
                    if (status) {
                        bookDto = new Book();
                        bookDto.setId(book.getId());
                        bookDto.setChapters(0);
                        if (trace.isInfoEnabled())
                            trace.info("书籍保存成功，多趣书籍ID:'" + bookDto.getId() + "'");
                    }
                }
                return bookDto;
            }
        }
        return null;
    }
    /**
     * 保存章节
     *
     * @param batchBook
     * @param book
     * @param cpChapter
     * @param updateChaptersNumber 当前更新的章节计数器
     */
    protected boolean saveChapter(BatchBook batchBook, Book book, CpChapter cpChapter, int updateChaptersNumber) {

        String content =  getCpChapterContent(batchBook.getCpBookId(), cpChapter);
        //如果章节内容不为空
        if (StringUtils.isNotEmpty(content)) {
            Chapter chapter = transformChapter(book, updateChaptersNumber, cpChapter);
            ResultDto resultDto = chapterGeneralService.saveWithApi(
                    chapter,
                    content,
                    book.getIsFee(),
                    book.getFeeChapter());
            if (resultDto.getSuccess()) {
                if (trace.isInfoEnabled())
                    trace.info("多趣书籍ID:'{}' 章节:'{}' 保存成功，多趣章节ID:'{}'",book.getId(), cpChapter.getCpChapterTitle(),chapter.getId());
            }
            return resultDto.getSuccess();
        }
        return false;
    }

    /**
     * 转换章节
     * @param book
     * @param updateChaptersNumber
     * @param cpChapter
     * @return
     */
    protected Chapter transformChapter(Book book,Integer updateChaptersNumber,CpChapter cpChapter){
        Chapter chapter = new Chapter();
        chapter.setBookId(book.getId());
        chapter.setCpChapterId(cpChapter.getCpChapterId());
        chapter.setName(cpChapter.getCpChapterTitle());
        if(StringUtils.isNotEmpty(cpChapter.getCpChapterVolume()))
            chapter.setVolume(cpChapter.getCpChapterVolume());
//        chapter.setCreateDate(DateUtils.timeString2Date(yllyChapter));

        //设置是否收费
        chapter.setIsFee(0);
        if (book.getFeeChapter() != null && book.getFeeChapter() > 0 && updateChaptersNumber != null) {
            if (book.getFeeChapter() <= updateChaptersNumber) {
                chapter.setIsFee(1);
            }
        }
        return chapter;
    }

    /**
     * 获取批次书单
     *
     * @param provider
     * @param initCpBookId
     * @return
     */
    public List<BatchBook> getBooks(ConfigEnum.Provider provider, String initCpBookId) {
        List<BatchBook> batchBooks = null;
        if (provider != null) {
            batchBooks = Lists.newArrayList();

            //获得provider信息
            Provider p = providerRepository.findOne(provider.getId());
            //provider是否可用
            if(p!= null && p.getStatus() == 1){
                BatchQueryDto query = new BatchQueryDto();
                query.setProviderId(p.getId());
                query.setIsUse(1);
                List<Batch> batches = batchRepository.find(query);
                if(CollectionUtils.isNotEmpty(batches)){
                    for(Batch batch:batches){
//                        batchBooks.addAll(batchBookGeneralService.find(batch.getId(), provider.getId(),initCpBookId));
                    }

                }
            }
        }
        return batchBooks;
    }

    public List<BatchBook> getBooks(ConfigEnum.Provider provider, List<String> cpBookIds) {
        List<BatchBook> batchBooks = null;
        if (provider != null) {
            batchBooks = Lists.newArrayList();

            BatchBook batchBook = null;
            for (String id : cpBookIds) {
                batchBook = new BatchBook();
                batchBook.setCpBookId(id);
                batchBook.setProviderId(provider.getId());
                batchBooks.add(batchBook);
            }
        }
        return batchBooks;
    }

    /**
     * 根据书籍ID获取最后更新章节信息
     *
     * @param bookId
     * @return Chapter
     */
    protected Chapter getMaxCpBookChapter(Long bookId) {
        return chapterGeneralService.getMaxOrderChapter(bookId);
    }

    /**
     * 更新书籍的抓取章节数
     *
     * @param bookId
     * @param chapters
     */
    protected void updateChaptersCount(Long bookId, Integer chapters) {
        bookGeneralService.updateChaptersAmount(bookId, chapters);
    }

}
