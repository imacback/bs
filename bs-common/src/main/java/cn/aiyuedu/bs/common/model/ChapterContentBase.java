package cn.aiyuedu.bs.common.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

/**
 * Description:
 *
 * @author yz.wu
 */
public class ChapterContentBase {

    @Id
    private Long chapterId;
    @Indexed
    private Long bookId;
    private String content;

    public ChapterContentBase() {}

    public ChapterContentBase(Long chapterId, Long bookId, String content) {
        this.chapterId = chapterId;
        this.bookId = bookId;
        this.content = content;
    }

    public Long getChapterId() {
        return chapterId;
    }

    public void setChapterId(Long chapterId) {
        this.chapterId = chapterId;
    }

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
