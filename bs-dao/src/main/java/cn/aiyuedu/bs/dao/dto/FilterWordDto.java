package cn.aiyuedu.bs.dao.dto;

import cn.aiyuedu.bs.common.global.Operation;
import cn.aiyuedu.bs.dao.entity.FilterWord;

/**
 * Description:
 *
 * @author yz.wu
 */
public class FilterWordDto extends FilterWord implements Operation {

    private String creatorName;
    private String editorName;

    public String getCreatorName() {
        return creatorName;
    }

    public String getEditorName() {
        return editorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public void setEditorName(String editorName) {
        this.editorName = editorName;
    }
}
