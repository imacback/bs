package cn.aiyuedu.bs.dao.dto;

import cn.aiyuedu.bs.common.global.Operation;
import cn.aiyuedu.bs.dao.entity.Component;
import org.msgpack.annotation.Message;

/**
 * Description:
 *
 * @author yz.wu
 */
@Message
public class ComponentDto extends Component implements Operation {

    private String containerName;
    private String typeName;
    private String creatorName;
    private String editorName;
    private Integer dataGroup;
    private Integer dataLimit;

    public String getContainerName() {
        return containerName;
    }

    public void setContainerName(String containerName) {
        this.containerName = containerName;
    }

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

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public Integer getDataGroup() {
        return dataGroup;
    }

    public void setDataGroup(Integer dataGroup) {
        this.dataGroup = dataGroup;
    }

    public Integer getDataLimit() {
        return dataLimit;
    }

    public void setDataLimit(Integer dataLimit) {
        this.dataLimit = dataLimit;
    }
}
