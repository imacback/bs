package cn.aiyuedu.bs.common.dto;

import cn.aiyuedu.bs.common.model.ChapterBase;

/**
 * Description:
 *
 * @author yz.wu
 */
public class ChapterBaseDto {

    private ChapterBase chapterBase;
    private String text;

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
