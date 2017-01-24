package cn.aiyuedu.bs.dao.dto;

import cn.aiyuedu.bs.common.global.Operation;
import cn.aiyuedu.bs.dao.entity.Category;

/**
 * Description:
 *
 * @author yz.wu
 */
public class CategoryDto extends Category implements Operation {

    private String creatorName;
    private String editorName;
    private String parentName;
    private String tagClassifyName;
    private String tagSexName;
    private String tagContentNames;
    private String tagSupplyNames;

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

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public String getTagContentNames() {
        return tagContentNames;
    }

    public void setTagContentNames(String tagContentNames) {
        this.tagContentNames = tagContentNames;
    }

    public String getTagSupplyNames() {
        return tagSupplyNames;
    }

    public void setTagSupplyNames(String tagSupplyNames) {
        this.tagSupplyNames = tagSupplyNames;
    }

    public String getTagClassifyName() {
        return tagClassifyName;
    }

    public void setTagClassifyName(String tagClassifyName) {
        this.tagClassifyName = tagClassifyName;
    }

    public String getTagSexName() {
        return tagSexName;
    }

    public void setTagSexName(String tagSexName) {
        this.tagSexName = tagSexName;
    }
}
