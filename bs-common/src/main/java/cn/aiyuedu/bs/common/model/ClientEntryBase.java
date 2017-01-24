package cn.aiyuedu.bs.common.model;

import org.msgpack.annotation.Message;

/**
 * Created by Wangpeitao on 2014/9/25.
 */
@Message
public class ClientEntryBase {
    private Integer id;
    private Integer entryType;
    private Integer platformId;
    private String versions;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getEntryType() {
        return entryType;
    }

    public void setEntryType(Integer entryType) {
        this.entryType = entryType;
    }

    public Integer getPlatformId() {
        return platformId;
    }

    public void setPlatformId(Integer platformId) {
        this.platformId = platformId;
    }

    public String getVersions() {
        return versions;
    }

    public void setVersions(String versions) {
        this.versions = versions;
    }
}
