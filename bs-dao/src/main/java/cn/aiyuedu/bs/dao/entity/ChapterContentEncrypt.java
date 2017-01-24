package cn.aiyuedu.bs.dao.entity;

import cn.aiyuedu.bs.common.model.ChapterContentBase;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Description:
 *
 * @author yz.wu
 */
@Document(collection = "chapterContentEncrypt")
public class ChapterContentEncrypt  extends ChapterContentBase {

    public ChapterContentEncrypt() {
        super();
    }

    public ChapterContentEncrypt(Long chapterId, Long bookId, String content) {
        super(chapterId, bookId, content);
    }
}
