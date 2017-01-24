package cn.aiyuedu.bs.common.model;

import java.util.List;

/**
 * Description:
 *
 * @author yz.wu
 */
public class SiteBase {

    private Integer id;
    private String name;
    private Integer homeContainerId;
    private Integer platformId;
    private List<String> versions;
    private Integer isDitch;
    private List<String> ditches;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getHomeContainerId() {
        return homeContainerId;
    }

    public void setHomeContainerId(Integer homeContainerId) {
        this.homeContainerId = homeContainerId;
    }

    public Integer getPlatformId() {
        return platformId;
    }

    public void setPlatformId(Integer platformId) {
        this.platformId = platformId;
    }

    public List<String> getVersions() {
        return versions;
    }

    public void setVersions(List<String> versions) {
        this.versions = versions;
    }

    public Integer getIsDitch() {
        return isDitch;
    }

    public void setIsDitch(Integer isDitch) {
        this.isDitch = isDitch;
    }

    public List<String> getDitches() {
        return ditches;
    }

    public void setDitches(List<String> ditches) {
        this.ditches = ditches;
    }
}
