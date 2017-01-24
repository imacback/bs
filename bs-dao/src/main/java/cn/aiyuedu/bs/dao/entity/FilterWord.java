package cn.aiyuedu.bs.dao.entity;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

@Document(collection = "filterWord")
public class FilterWord {

    private Integer id;
    @Field(value = "wrd")
    private String word;
    @Indexed
    @Field(value = "len")
    private Integer length;
    @Field(value = "lev")
    private Integer level;
    @Field(value = "rst")
    private Integer replaceStrategyType;
    @Field(value = "rpw")
    private String replaceWord;
    @Field(value = "act")
    private Integer allowComment;
    @Indexed
    @Field(value = "sts")
    private Integer status;

    @Field(value = "cdt")
    private Date createDate;
    @Field(value = "cid")
    private Integer creatorId;
    @Field(value = "edt")
    private Date editDate;
    @Field(value = "eid")
    private Integer editorId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getReplaceStrategyType() {
        return replaceStrategyType;
    }

    public void setReplaceStrategyType(Integer replaceStrategyType) {
        this.replaceStrategyType = replaceStrategyType;
    }

    public String getReplaceWord() {
        return replaceWord;
    }

    public void setReplaceWord(String replaceWord) {
        this.replaceWord = replaceWord;
    }

    public Integer getAllowComment() {
        return allowComment;
    }

    public void setAllowComment(Integer allowComment) {
        this.allowComment = allowComment;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Integer getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Integer creatorId) {
        this.creatorId = creatorId;
    }

    public Date getEditDate() {
        return editDate;
    }

    public void setEditDate(Date editDate) {
        this.editDate = editDate;
    }

    public Integer getEditorId() {
        return editorId;
    }

    public void setEditorId(Integer editorId) {
        this.editorId = editorId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }
}
