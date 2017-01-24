package cn.aiyuedu.bs.dao.dto;

import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * Description:
 *
 * @author yz.wu
 */
public class BookQueryDto {

    private Long id;
    private List<Long> excludeIds;
    private List<Long> ids;
    private String name;
    private String author;
    private Integer checkLevel;
    private Integer chapters;
    private Integer dayPublishChapters;
    private Integer publishChapters;
    private Date updateChapterDate;
    private Integer isSerial;
    private Integer isFee;
    private Integer status;
    private Integer categoryId;
    private List<Integer> categoryIds;
    private Integer operatePlatformId;
    private Integer feePlatformId;
    private Integer tagSexId;
    private Integer tagClassifyId;
    private Integer tagStoryId;
    private Integer tagContentId;
    private List<Integer> tagContentIds;
    private Integer tagSupplyId;
    private List<Integer> tagSupplyIds;
    private Integer batchId;
    private Integer providerId;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startEditDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endEditDate;
    private Integer start;
    private Integer limit;
    private String smallPic;
    private Integer isDesc;
    private Integer orderType;//排序，默认1:id, 2:viewCount

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Integer getIsSerial() {
        return isSerial;
    }

    public void setIsSerial(Integer isSerial) {
        this.isSerial = isSerial;
    }

    public Integer getTagSexId() {
        return tagSexId;
    }

    public void setTagSexId(Integer tagSexId) {
        this.tagSexId = tagSexId;
    }

    public Integer getTagClassifyId() {
        return tagClassifyId;
    }

    public void setTagClassifyId(Integer tagClassifyId) {
        this.tagClassifyId = tagClassifyId;
    }

    public Integer getProviderId() {
        return providerId;
    }

    public void setProviderId(Integer providerId) {
        this.providerId = providerId;
    }

    public Date getStartEditDate() {
        return startEditDate;
    }

    public void setStartEditDate(Date startEditDate) {
        this.startEditDate = startEditDate;
    }

    public Date getEndEditDate() {
        return endEditDate;
    }

    public void setEndEditDate(Date endEditDate) {
        this.endEditDate = endEditDate;
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

    public Integer getCheckLevel() {
        return checkLevel;
    }

    public void setCheckLevel(Integer checkLevel) {
        this.checkLevel = checkLevel;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getTagSupplyId() {
        return tagSupplyId;
    }

    public void setTagSupplyId(Integer tagSupplyId) {
        this.tagSupplyId = tagSupplyId;
    }

    public Integer getTagStoryId() {
        return tagStoryId;
    }

    public void setTagStoryId(Integer tagStoryId) {
        this.tagStoryId = tagStoryId;
    }

    public List<Long> getIds() {
        return ids;
    }

    public void setIds(List<Long> ids) {
        this.ids = ids;
    }

    public Integer getDayPublishChapters() {
        return dayPublishChapters;
    }

    public void setDayPublishChapters(Integer dayPublishChapters) {
        this.dayPublishChapters = dayPublishChapters;
    }

    public Integer getPublishChapters() {
        return publishChapters;
    }

    public void setPublishChapters(Integer publishChapters) {
        this.publishChapters = publishChapters;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public Integer getChapters() {
        return chapters;
    }

    public void setChapters(Integer chapters) {
        this.chapters = chapters;
    }

    public Integer getTagContentId() {
        return tagContentId;
    }

    public void setTagContentId(Integer tagContentId) {
        this.tagContentId = tagContentId;
    }

    public List<Integer> getTagContentIds() {
        return tagContentIds;
    }

    public void setTagContentIds(List<Integer> tagContentIds) {
        this.tagContentIds = tagContentIds;
    }

    public List<Integer> getTagSupplyIds() {
        return tagSupplyIds;
    }

    public void setTagSupplyIds(List<Integer> tagSupplyIds) {
        this.tagSupplyIds = tagSupplyIds;
    }

    public List<Integer> getCategoryIds() {
        return categoryIds;
    }

    public void setCategoryIds(List<Integer> categoryIds) {
        this.categoryIds = categoryIds;
    }

    public Integer getBatchId() {
        return batchId;
    }

    public void setBatchId(Integer batchId) {
        this.batchId = batchId;
    }

    public String getSmallPic() {
        return smallPic;
    }

    public void setSmallPic(String smallPic) {
        this.smallPic = smallPic;
    }

    public Integer getIsDesc() {
        return isDesc;
    }

    public void setIsDesc(Integer isDesc) {
        this.isDesc = isDesc;
    }

    public Integer getOrderType() {
        return orderType;
    }

    public void setOrderType(Integer orderType) {
        this.orderType = orderType;
    }

    public List<Long> getExcludeIds() {
        return excludeIds;
    }

    public void setExcludeIds(List<Long> excludeIds) {
        this.excludeIds = excludeIds;
    }

    public Date getUpdateChapterDate() {
        return updateChapterDate;
    }

    public void setUpdateChapterDate(Date updateChapterDate) {
        this.updateChapterDate = updateChapterDate;
    }

    public Integer getOperatePlatformId() {
        return operatePlatformId;
    }

    public void setOperatePlatformId(Integer operatePlatformId) {
        this.operatePlatformId = operatePlatformId;
    }

    public Integer getFeePlatformId() {
        return feePlatformId;
    }

    public void setFeePlatformId(Integer feePlatformId) {
        this.feePlatformId = feePlatformId;
    }

    public Integer getIsFee() {
        return isFee;
    }

    public void setIsFee(Integer isFee) {
        this.isFee = isFee;
    }
}
