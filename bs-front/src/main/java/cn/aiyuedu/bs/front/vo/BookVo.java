package cn.aiyuedu.bs.front.vo;


import cn.aiyuedu.bs.common.model.BookBase;

import java.util.List;

/**
 * Description:
 *
 * @author Scott
 */
public class BookVo extends BookBase {
    private String categoryName;
    private String providerName;
    private String cpbookEntry;
    private String cpbookAction;
    private String apkPackageName;
    private String cpbookExtra;
    private String shortMemo;
    private String tinyMemo;
    private List<? extends CommentBaseDto> comments;

    public List<? extends CommentBaseDto> getComments() {
        return comments;
    }

    public void setComments(List<? extends CommentBaseDto> comments) {
        this.comments = comments;
    }

    public BookVo() {
    }
    public BookVo(Long id, String name, String author, String summary, String smallPic) {
        setId(id);
        setName(name);
        setAuthor(author);
//        setSummary(summary);
        setSmallPic(smallPic);
        setLargePic(smallPic);
    }

    public String getCpbookExtra() {
        return cpbookExtra;
    }

    public void setCpbookExtra(String cpbookExtra) {
        this.cpbookExtra = cpbookExtra;
    }

    public String getCpbookEntry() {
        return cpbookEntry;
    }

    public void setCpbookEntry(String cpbookEntry) {
        this.cpbookEntry = cpbookEntry;
    }

    public String getCpbookAction() {
        return cpbookAction;
    }

    public void setCpbookAction(String cpbookAction) {
        this.cpbookAction = cpbookAction;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getProviderName() {
        return providerName;
    }

    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }

    public String getShortMemo() {
        return shortMemo;
    }

    public void setShortMemo(String shortMemo) {
        this.shortMemo = shortMemo;
    }

    public String getTinyMemo() {
        return tinyMemo;
    }

    public void setTinyMemo(String tinyMemo) {
        this.tinyMemo = tinyMemo;
    }

    public String getApkPackageName() {
        return apkPackageName;
    }

    public void setApkPackageName(String apkPackageName) {
        this.apkPackageName = apkPackageName;
    }
}
