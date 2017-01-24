package cn.aiyuedu.bs.back.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Thinkpad on 2014/10/30.
 */
public class UploadExcelResultDto {
    private List contentList;
    private boolean success;
    private String info;

    public List getContentList() {
        return contentList;
    }

    public void setContentList(List contentList) {
        this.contentList = contentList;
    }

    public boolean getSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
