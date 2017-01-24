package cn.aiyuedu.bs.dao.dto;

import cn.aiyuedu.bs.common.global.Operation;
import cn.aiyuedu.bs.dao.entity.Book;
import org.msgpack.annotation.Message;

/**
 * Description:
 *
 * @author yz.wu
 */
@Message
public class BookDto extends Book implements Operation {

    private String providerName;
    private String creatorName;
    private String editorName;
    private String tagClassifyName;
    private String tagSexName;
    private String tagStoryName;
    private String tagContentNames;
    private String tagSupplyNames;
    private String authorizeStartDate;
    private String authorizeEndDate;

    public String getProviderName() {
        return providerName;
    }

    public void setProviderName(String providerName) {
        this.providerName = providerName;
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

    public String getTagStoryName() {
        return tagStoryName;
    }

    public void setTagStoryName(String tagStoryName) {
        this.tagStoryName = tagStoryName;
    }

    public String getAuthorizeStartDate() {
        return authorizeStartDate;
    }

    public void setAuthorizeStartDate(String authorizeStartDate) {
        this.authorizeStartDate = authorizeStartDate;
    }

    public String getAuthorizeEndDate() {
        return authorizeEndDate;
    }

    public void setAuthorizeEndDate(String authorizeEndDate) {
        this.authorizeEndDate = authorizeEndDate;
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
}
