package cn.aiyuedu.bs.dao.dto;

import cn.aiyuedu.bs.common.global.Operation;
import cn.aiyuedu.bs.dao.entity.Site;

/**
 * Description:
 *
 * @author yz.wu
 */
public class SiteDto extends Site implements Operation {

    private String homeContainerName;
    private String creatorName;
    private String editorName;

    public String getHomeContainerName() {
        return homeContainerName;
    }

    public void setHomeContainerName(String homeContainerName) {
        this.homeContainerName = homeContainerName;
    }

    @Override
    public String getCreatorName() {
        return creatorName;
    }

    @Override
    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    @Override
    public String getEditorName() {
        return editorName;
    }

    @Override
    public void setEditorName(String editorName) {
        this.editorName = editorName;
    }
}
