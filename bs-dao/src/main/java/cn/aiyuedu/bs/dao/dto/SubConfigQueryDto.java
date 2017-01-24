package cn.aiyuedu.bs.dao.dto;

/**
 * Created by Thinkpad on 2014/11/25.
 */
public class SubConfigQueryDto {
    private Integer start;
    private Integer limit;
    private Integer isDesc;

    public Integer getStart() {
        return start;
    }

    public void setStart(Integer start) {
        this.start = start;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public Integer getIsDesc() {
        return isDesc;
    }

    public void setIsDesc(Integer isDesc) {
        this.isDesc = isDesc;
    }
}
