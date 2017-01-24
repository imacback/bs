package cn.aiyuedu.bs.dao.dto;

import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.TreeMap;

/**
 * Created by Thinkpad on 2015/1/4.
 */
public class ClientShelfQueryDto {

    private Integer platformId;
    private String version;
    //标识版本是否按Like在数据库中查询
    private Integer isLikeVersion;
    private String ditchIds;
    //标识渠道ID是否按Like在数据库中查询
    private Integer isLikeDitchId;
    private String bookIds;
    //标识书籍ID是否按Like在数据库中查询
    private Integer isLikeBookId;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startCreateDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endCreateDate;
    private Integer status;

    private Integer start;
    private Integer limit;
    private Integer isDesc;
    //按put的顺序对相应的字段进行排序
    private LinkedHashMap<String, Sort.Direction> orderMap;

    public Integer getPlatformId() {
        return platformId;
    }

    public void setPlatformId(Integer platformId) {
        this.platformId = platformId;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Integer getIsLikeVersion() {
        return isLikeVersion;
    }

    public void setIsLikeVersion(Integer isLikeVersion) {
        this.isLikeVersion = isLikeVersion;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getDitchIds() {
        return ditchIds;
    }

    public void setDitchIds(String ditchIds) {
        this.ditchIds = ditchIds;
    }

    public Integer getIsLikeDitchId() {
        return isLikeDitchId;
    }

    public void setIsLikeDitchId(Integer isLikeDitchId) {
        this.isLikeDitchId = isLikeDitchId;
    }

    public String getBookIds() {
        return bookIds;
    }

    public void setBookIds(String bookIds) {
        this.bookIds = bookIds;
    }

    public Integer getIsLikeBookId() {
        return isLikeBookId;
    }

    public void setIsLikeBookId(Integer isLikeBookId) {
        this.isLikeBookId = isLikeBookId;
    }

    public Date getStartCreateDate() {
        return startCreateDate;
    }

    public void setStartCreateDate(Date startCreateDate) {
        this.startCreateDate = startCreateDate;
    }

    public Date getEndCreateDate() {
        return endCreateDate;
    }

    public void setEndCreateDate(Date endCreateDate) {
        this.endCreateDate = endCreateDate;
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

    public LinkedHashMap<String, Sort.Direction> getOrderMap() {
        return orderMap;
    }

    public void setOrderMap(LinkedHashMap<String, Sort.Direction> orderMap) {
        this.orderMap = orderMap;
    }
}
