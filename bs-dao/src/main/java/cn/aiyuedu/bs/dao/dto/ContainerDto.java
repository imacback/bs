package cn.aiyuedu.bs.dao.dto;

import cn.aiyuedu.bs.common.global.Operation;
import cn.aiyuedu.bs.dao.entity.Container;
import org.msgpack.annotation.Message;

/**
 * Description:
 *
 * @author yz.wu
 */
@Message
public class ContainerDto extends Container implements Operation {

    private String creatorName;
    private String editorName;

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public String getEditorName() {
        return editorName;
    }

    public void setEditorName(String editorName) {
        this.editorName = editorName;
    }
}
