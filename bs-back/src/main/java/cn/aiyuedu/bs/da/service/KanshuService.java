package cn.aiyuedu.bs.da.service;

import cn.aiyuedu.bs.da.model.CpBook;
import cn.aiyuedu.bs.da.model.CpChapter;
import cn.aiyuedu.bs.da.model.kanshu.KanshuBook;
import cn.aiyuedu.bs.da.service.AbstractService;
import cn.aiyuedu.bs.da.worker.KanshuWorker;
import cn.aiyuedu.bs.dao.entity.BatchBook;
import cn.aiyuedu.bs.dao.entity.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by tonydeng on 14/10/28.
 */
@Service("kanshuService")
public class KanshuService extends AbstractService {
    @Autowired
    private KanshuWorker kanshuWorker;

    @Override
    public List<Integer> getCpBookIds() {
        return kanshuWorker.getCpBookIds();
    }

    @Override
    public CpBook getCpBookInfo(String cpBookId) {
        return kanshuWorker.getBookInfo(cpBookId);
    }

    @Override
    protected Book transformBook(BatchBook batchBook, CpBook cpBook) {
        KanshuBook kanshuBook = (KanshuBook) cpBook;
        Book book = new Book();
        book.setProviderId(batchBook.getProviderId());
        book.setCpBookId(batchBook.getCpBookId());
        book.setName(kanshuBook.getBookName());
        book.setAuthor(kanshuBook.getAuthor());
        book.setMemo(kanshuBook.getDetail());
        book.setBatchId(batchBook.getBatchId());
        switch (kanshuBook.getBookStatus()) {
            case 0: //kanshu的bookstatus为0则是连载
                book.setIsSerial(1);
                break;
            case 1:
                book.setIsSerial(0);
                break;
        }
        //设置书封图片
        uploadImage(book,batchBook,cpBook);
        return book;
    }


    @Override
    public List<CpChapter> getChapterList(String cpBookId) {
        return kanshuWorker.getChapterList(cpBookId);
    }

    @Override
    protected String getCpChapterContent(String cpBookId, CpChapter cpChapter) {
        return kanshuWorker.getChapterContent(cpBookId,cpChapter.getCpChapterId());
    }
}
