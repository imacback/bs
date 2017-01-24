package cn.aiyuedu.bs.dao.dto;

import java.util.List;
import java.util.Map;

/**
 * Description:
 *
 * @author yz.wu
 */
public class FilterResultDto {
    private String text;                                    //过滤后内容
    private String originText;                              //原文内容
    private boolean replaced;                               //是否有替换内容
    private boolean isPassed;
    private Integer chineseLength;
    private Map<Integer, List<FilterWordResultDto>> result; //过滤结果，Map<Integer{敏感词类型id}, FilterWordResultDto>>

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean getReplaced() {
        return replaced;
    }

    public void setReplaced(boolean replaced) {
        this.replaced = replaced;
    }

    public String getOriginText() {
        return originText;
    }

    public void setOriginText(String originText) {
        this.originText = originText;
    }

    public boolean getIsPassed() {
        return isPassed;
    }

    public void setIsPassed(boolean isPassed) {
        this.isPassed = isPassed;
    }

    public Integer getChineseLength() {
        return chineseLength;
    }

    public void setChineseLength(Integer chineseLength) {
        this.chineseLength = chineseLength;
    }

    public Map<Integer, List<FilterWordResultDto>> getResult() {
        return result;
    }

    public void setResult(Map<Integer, List<FilterWordResultDto>> result) {
        this.result = result;
    }
}
