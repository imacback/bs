package cn.aiyuedu.bs.da.model;

/**
 * Created by tonydeng on 14-10-17.
 */
public class CpChapter {
    private String cpChapterId;
    private String cpChapterTitle;
    private String cpChapterVolume;

    public String getCpChapterTitle() {
        return cpChapterTitle;
    }

    public void setCpChapterTitle(String cpChapterTitle) {
        this.cpChapterTitle = cpChapterTitle;
    }

    public String getCpChapterId() {

        return cpChapterId;
    }

    public void setCpChapterId(String cpChapterId) {
        this.cpChapterId = cpChapterId;
    }

    public String getCpChapterVolume() {
        return cpChapterVolume;
    }

    public void setCpChapterVolume(String cpChapterVolume) {
        this.cpChapterVolume = cpChapterVolume;
    }

    @Override
    public String toString() {
        return "CpChapter{" +
                "cpChapterId='" + getCpChapterId() + '\'' +
                ", cpChapterTitle='" + getCpChapterTitle() + '\'' +
                ", cpChapterVolume='" + getCpChapterVolume() + '\'' +
                '}';
    }
}
