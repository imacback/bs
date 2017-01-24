package cn.aiyuedu.bs.wap.dto;

import cn.aiyuedu.bs.common.model.BookBase;

/**
 * Description:
 *
 * @author yz.wu
 */
public class BookDto extends BookBase {

    private String categoryName;
    private String providerName;
    private String cpbookEntry;
    private String cpbookAction;
    private String apkPackageName;
    private String cpbookExtra;
    private String tinyMemo;

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

    public String getApkPackageName() {
        return apkPackageName;
    }

    public void setApkPackageName(String apkPackageName) {
        this.apkPackageName = apkPackageName;
    }

    public String getCpbookExtra() {
        return cpbookExtra;
    }

    public void setCpbookExtra(String cpbookExtra) {
        this.cpbookExtra = cpbookExtra;
    }

    public String getTinyMemo() {
        return tinyMemo;
    }

    public void setTinyMemo(String tinyMemo) {
        this.tinyMemo = tinyMemo;
    }

}
