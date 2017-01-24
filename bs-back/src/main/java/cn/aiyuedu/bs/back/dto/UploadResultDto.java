package cn.aiyuedu.bs.back.dto;

import java.io.File;
import java.util.Map;

/**
 * Description:
 *
 * @author yz.wu
 */
public class UploadResultDto {

    private String url;
    private Map<String, String> urlMap;
    private File file;
    private boolean success;
    private String info;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
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

    public Map<String, String> getUrlMap() {
        return urlMap;
    }

    public void setUrlMap(Map<String, String> urlMap) {
        this.urlMap = urlMap;
    }
}
