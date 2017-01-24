package cn.aiyuedu.bs.dao.entity;

import cn.aiyuedu.bs.common.model.ChapterContentBase;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Description:
 *
 * @author yz.wu
 */
@Document(collection = "chapterContentFilter")
public class ChapterContentFilter extends ChapterContentBase {

    public ChapterContentFilter() {
        super();
    }

    public ChapterContentFilter(Long chapterId, Long bookId, String content) {
        super(chapterId, bookId, content);
    }
}
