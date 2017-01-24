package cn.aiyuedu.bs.common.global;

/**
 * Description:
 *
 * @author yz.wu
 */
public interface Operation {

    Integer getCreatorId();
    String getCreatorName();
    void setCreatorName(String creatorName);

    Integer getEditorId();
    String getEditorName();
    void setEditorName(String editorName);
}
