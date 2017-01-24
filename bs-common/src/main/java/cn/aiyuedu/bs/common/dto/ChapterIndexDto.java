package cn.aiyuedu.bs.common.dto;

import cn.aiyuedu.bs.common.model.ChapterBase;

/**
 * Description:
 *
 * @author yz.wu
 */
public class ChapterIndexDto {

    private ChapterBase chapterBase;
    private String text;

    public ChapterIndexDto(){}

    public ChapterIndexDto(ChapterBase chapterBase, String text) {
        this.chapterBase = chapterBase;
        this.text = text;
    }

    public ChapterBase getChapterBase() {
        return chapterBase;
    }

    public void setChapterBase(ChapterBase chapterBase) {
        this.chapterBase = chapterBase;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
