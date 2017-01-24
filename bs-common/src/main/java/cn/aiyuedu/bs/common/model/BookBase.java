package cn.aiyuedu.bs.common.model;

import org.apache.commons.lang3.StringUtils;
import org.msgpack.annotation.Ignore;
import org.msgpack.annotation.Message;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;

import java.util.Date;
import java.util.List;

/**
 * Description:
 *
 * @author yz.wu
 */
@Message
public class BookBase {

    private Long id;
    private String name;
    private String author;
    @Indexed
    private Integer tagStoryId;                     //故事属性
    private List<Integer> categoryIds;              //分类 ids
    @Indexed
    private Integer isSerial;
    private Integer words;
    private Integer chapters;
    @Indexed
    private Integer checkLevel;
    private Integer publishChapters;
    private Date updateChapterDate;
    private Long updateChapterId;
    private String updateChapter;
    private String smallPic;
    private String largePic;
    @Ignore
    private String originMemo;
    private String memo;
    private String shortRecommend;
    private String longRecommend;
    @Indexed
    private Integer providerId;
    @Indexed
    private Integer isFee;//0=免费，1=收费
    private Integer isWholeFee;
    private List<Integer> operatePlatformIds;
    private List<Integer> feePlatformIds;
    private List<Integer> feeTypeIds;
    private Integer feeChapter;//收费起始章节
    private Integer wholePrice;
    @Indexed
    private Integer status;//
    private Date createDate;
    @Indexed
    private Integer viewCount;

    @Indexed
    private Integer tagClassifyId;                  //类别属性
    @Indexed
    private Integer tagSexId;                       //性别属性
    private List<Integer> tagContentIds;            //内容属性
    private List<Integer> tagSupplyIds;             //补充属性

    @Transient
    private String shortMemo;

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

    public Integer getTagStoryId() {
        return tagStoryId;
    }

    public void setTagStoryId(Integer tagStoryId) {
        this.tagStoryId = tagStoryId;
    }

    public List<Integer> getCategoryIds() {
        return categoryIds;
    }

    public void setCategoryIds(List<Integer> categoryIds) {
        this.categoryIds = categoryIds;
    }

    public Integer getIsSerial() {
        return isSerial;
    }

    public void setIsSerial(Integer isSerial) {
        this.isSerial = isSerial;
    }

    public Integer getWords() {
        return words;
    }

    public void setWords(Integer words) {
        this.words = words;
    }

    public Integer getChapters() {
        return chapters;
    }

    public void setChapters(Integer chapters) {
        this.chapters = chapters;
    }

    public Integer getCheckLevel() {
        return checkLevel;
    }

    public void setCheckLevel(Integer checkLevel) {
        this.checkLevel = checkLevel;
    }

    public Integer getPublishChapters() {
        return publishChapters;
    }

    public void setPublishChapters(Integer publishChapters) {
        this.publishChapters = publishChapters;
    }

    public Date getUpdateChapterDate() {
        return updateChapterDate;
    }

    public void setUpdateChapterDate(Date updateChapterDate) {
        this.updateChapterDate = updateChapterDate;
    }

    public Long getUpdateChapterId() {
        return updateChapterId;
    }

    public void setUpdateChapterId(Long updateChapterId) {
        this.updateChapterId = updateChapterId;
    }

    public String getUpdateChapter() {
        return updateChapter;
    }

    public void setUpdateChapter(String updateChapter) {
        this.updateChapter = updateChapter;
    }

    public String getSmallPic() {
        return smallPic;
    }

    public void setSmallPic(String smallPic) {
        this.smallPic = smallPic;
    }

    public String getLargePic() {
        return largePic;
    }

    public void setLargePic(String largePic) {
        this.largePic = largePic;
    }

    public String getOriginMemo() {
        return originMemo;
    }

    public void setOriginMemo(String originMemo) {
        this.originMemo = originMemo;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getShortRecommend() {
        return shortRecommend;
    }

    public void setShortRecommend(String shortRecommend) {
        this.shortRecommend = shortRecommend;
    }

    public String getLongRecommend() {
        return longRecommend;
    }

    public void setLongRecommend(String longRecommend) {
        this.longRecommend = longRecommend;
    }

    public Integer getProviderId() {
        return providerId;
    }

    public void setProviderId(Integer providerId) {
        this.providerId = providerId;
    }

    public Integer getIsFee() {
        return isFee;
    }

    public void setIsFee(Integer isFee) {
        this.isFee = isFee;
    }

    public Integer getIsWholeFee() {
        return isWholeFee;
    }

    public void setIsWholeFee(Integer isWholeFee) {
        this.isWholeFee = isWholeFee;
    }

    public List<Integer> getOperatePlatformIds() {
        return operatePlatformIds;
    }

    public void setOperatePlatformIds(List<Integer> operatePlatformIds) {
        this.operatePlatformIds = operatePlatformIds;
    }

    public List<Integer> getFeePlatformIds() {
        return feePlatformIds;
    }

    public void setFeePlatformIds(List<Integer> feePlatformIds) {
        this.feePlatformIds = feePlatformIds;
    }

    public List<Integer> getFeeTypeIds() {
        return feeTypeIds;
    }

    public void setFeeTypeIds(List<Integer> feeTypeIds) {
        this.feeTypeIds = feeTypeIds;
    }

    public Integer getFeeChapter() {
        return feeChapter;
    }

    public void setFeeChapter(Integer feeChapter) {
        this.feeChapter = feeChapter;
    }

    public Integer getWholePrice() {
        return wholePrice;
    }

    public void setWholePrice(Integer wholePrice) {
        this.wholePrice = wholePrice;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Integer getViewCount() {
        return viewCount;
    }

    public void setViewCount(Integer viewCount) {
        this.viewCount = viewCount;
    }

    public Integer getTagClassifyId() {
        return tagClassifyId;
    }

    public void setTagClassifyId(Integer tagClassifyId) {
        this.tagClassifyId = tagClassifyId;
    }

    public Integer getTagSexId() {
        return tagSexId;
    }

    public void setTagSexId(Integer tagSexId) {
        this.tagSexId = tagSexId;
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

    public String getShortMemo() {
        if (StringUtils.isNotBlank(memo)) {
            return this.memo.length() <= 150 ? this.memo : this.memo.substring(0, 150) + "...";
        }

        return shortMemo;
    }

    public void setShortMemo(String shortMemo) {
        this.shortMemo = shortMemo;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("BookBase{");
        sb.append("id=").append(id);
        sb.append(", name='").append(name).append('\'');
        sb.append(", author='").append(author).append('\'');
        sb.append(", tagStoryId=").append(tagStoryId);
        sb.append(", categoryIds=").append(categoryIds);
        sb.append(", isSerial=").append(isSerial);
        sb.append(", words=").append(words);
        sb.append(", chapters=").append(chapters);
        sb.append(", checkLevel=").append(checkLevel);
        sb.append(", publishChapters=").append(publishChapters);
        sb.append(", updateChapterDate=").append(updateChapterDate);
        sb.append(", updateChapterId=").append(updateChapterId);
        sb.append(", updateChapter='").append(updateChapter).append('\'');
        sb.append(", smallPic='").append(smallPic).append('\'');
        sb.append(", largePic='").append(largePic).append('\'');
        sb.append(", originMemo='").append(originMemo).append('\'');
        sb.append(", memo='").append(memo).append('\'');
        sb.append(", shortRecommend='").append(shortRecommend).append('\'');
        sb.append(", longRecommend='").append(longRecommend).append('\'');
        sb.append(", providerId=").append(providerId);
        sb.append(", isFee=").append(isFee);
        sb.append(", isWholeFee=").append(isWholeFee);
        sb.append(", operatePlatformIds=").append(operatePlatformIds);
        sb.append(", feePlatformIds=").append(feePlatformIds);
        sb.append(", feeTypeIds=").append(feeTypeIds);
        sb.append(", feeChapter=").append(feeChapter);
        sb.append(", wholePrice=").append(wholePrice);
        sb.append(", status=").append(status);
        sb.append(", createDate=").append(createDate);
        sb.append(", viewCount=").append(viewCount);
        sb.append(", tagClassifyId=").append(tagClassifyId);
        sb.append(", tagSexId=").append(tagSexId);
        sb.append(", tagContentIds=").append(tagContentIds);
        sb.append(", tagSupplyIds=").append(tagSupplyIds);
        sb.append('}');
        return sb.toString();
    }
}
