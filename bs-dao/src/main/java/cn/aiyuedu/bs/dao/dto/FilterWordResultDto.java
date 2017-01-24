package cn.aiyuedu.bs.dao.dto;

import cn.aiyuedu.bs.dao.entity.FilterWord;

/**
 * Description:
 *
 * @author yz.wu
 */
public class FilterWordResultDto {

    private FilterWord filterWord;
    private Integer count;

    public FilterWordResultDto(){}

    public FilterWordResultDto(FilterWord filterWord, Integer count) {
        this.filterWord = filterWord;
        this.count = count;
    }

    public FilterWord getFilterWord() {
        return filterWord;
    }

    public void setFilterWord(FilterWord filterWord) {
        this.filterWord = filterWord;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
