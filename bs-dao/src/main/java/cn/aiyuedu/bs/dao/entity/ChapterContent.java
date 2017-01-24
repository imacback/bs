package cn.aiyuedu.bs.dao.entity;

import cn.aiyuedu.bs.common.model.ChapterContentBase;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Description:
 *
 * @author yz.wu
 */
@Document(collection = "chapterContent")
public class ChapterContent extends ChapterContentBase {

    public ChapterContent() {
        super();
    }

    public ChapterContent(Long chapterId, Long bookId, String content) {
        super(chapterId, bookId, content);
    }
}
