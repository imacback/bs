package cn.aiyuedu.bs.dao.dto;

import java.util.List;

/**
 * Created by Thinkpad on 2015/1/1.
 */
public class BatchBookQueryDto {

    private Long id;
    //标示主键ID是否按"!="在数据库中查询
    private Integer isNEId;
    private Integer batchId;
    private List<Integer> batchIdList;
    private String cpBookId;
    private Integer providerId;
    private String bookName;
    //标识名称是否按Like在数据库中查询
    private Integer isLikeBookName;
    private String author;

    private Integer start;
    private Integer limit;
    private Integer isDesc;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getIsNEId() {
        return isNEId;
    }

    public void setIsNEId(Integer isNEId) {
        this.isNEId = isNEId;
    }

    public Integer getBatchId() {
        return batchId;
    }

    public void setBatchId(Integer batchId) {
        this.batchId = batchId;
    }

    public List<Integer> getBatchIdList() {
        return batchIdList;
    }

    public void setBatchIdList(List<Integer> batchIdList) {
        this.batchIdList = batchIdList;
    }

    public String getCpBookId() {
        return cpBookId;
    }

    public void setCpBookId(String cpBookId) {
        this.cpBookId = cpBookId;
    }

    public Integer getProviderId() {
        return providerId;
    }

    public void setProviderId(Integer providerId) {
        this.providerId = providerId;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public Integer getIsLikeBookName() {
        return isLikeBookName;
    }

    public void setIsLikeBookName(Integer isLikeBookName) {
        this.isLikeBookName = isLikeBookName;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

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
