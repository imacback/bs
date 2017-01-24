package cn.aiyuedu.bs.back.dto;

import cn.aiyuedu.bs.common.Constants.ChapterStatus;
import cn.aiyuedu.bs.dao.entity.Chapter;
import com.google.common.collect.Sets;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;
import java.util.Set;

/**
 * Description:
 *
 * @author yz.wu
 */
public class ChapterUpdateDto {

    private Set<Long> chapterIds = Sets.newHashSet();
    private Set<Integer> orderIds = Sets.newHashSet();

    private Integer newStatus;
    private Integer oldStatus;

    private Integer newFilteredWords;
    private Integer oldFilteredWords;

    private Integer newOrderId;
    private Integer oldOrderId;

    private Integer maxOrderId;
    private Integer minOrderId;

    public ChapterUpdateDto(){}

    public ChapterUpdateDto(Chapter _new, Chapter _old, Integer maxOrderId, Integer minOrderId) {
        chapterIds.add(_new.getId());
        orderIds.add(_new.getOrderId());

        if (_new.getStatus() != null) {
            this.newStatus = _new.getStatus();
        } else {
            this.newStatus = _old.getStatus();
        }
        this.newFilteredWords = _new.getFilteredWords();
        this.newOrderId = _new.getOrderId();

        this.oldStatus = _old.getStatus();
        this.oldFilteredWords = _old.getFilteredWords();
        this.oldOrderId = _old.getOrderId();

        this.maxOrderId = maxOrderId;
        this.minOrderId = minOrderId;
    }

    public Integer getNewStatus() {
        return newStatus;
    }

    public void setNewStatus(Integer newStatus) {
        this.newStatus = newStatus;
    }

    public Integer getOldStatus() {
        return oldStatus;
    }

    public void setOldStatus(Integer oldStatus) {
        this.oldStatus = oldStatus;
    }

    public Integer getNewFilteredWords() {
        return newFilteredWords;
    }

    public void setNewFilteredWords(Integer newFilteredWords) {
        this.newFilteredWords = newFilteredWords;
    }

    public Integer getOldFilteredWords() {
        return oldFilteredWords;
    }

    public void setOldFilteredWords(Integer oldFilteredWords) {
        this.oldFilteredWords = oldFilteredWords;
    }

    public Integer getNewOrderId() {
        return newOrderId;
    }

    public void setNewOrderId(Integer newOrderId) {
        this.newOrderId = newOrderId;
    }

    public Integer getOldOrderId() {
        return oldOrderId;
    }

    public void setOldOrderId(Integer oldOrderId) {
        this.oldOrderId = oldOrderId;
    }

    public Integer getMaxOrderId() {
        return maxOrderId;
    }

    public void setMaxOrderId(Integer maxOrderId) {
        this.maxOrderId = maxOrderId;
    }

    public Integer getMinOrderId() {
        return minOrderId;
    }

    public void setMinOrderId(Integer minOrderId) {
        this.minOrderId = minOrderId;
    }

    public Set<Long> getChapterIds() {
        return chapterIds;
    }

    public void setChapterIds(Set<Long> chapterIds) {
        this.chapterIds = chapterIds;
    }

    public Set<Integer> getOrderIds() {
        return orderIds;
    }

    public void setOrderIds(Set<Integer> orderIds) {
        this.orderIds = orderIds;
    }

    /**
     * 是否需要重新计算偏移量
     * @return
     */
    public boolean resum() {
        if (newStatus == ChapterStatus.Online.getId()) {
            if (newOrderId != oldOrderId ||
                    newFilteredWords != oldFilteredWords ||
                    (newStatus != oldStatus && newOrderId < maxOrderId))
            return true;
        }

        return false;
    }

    /**
     * 是否需要重新排序
     * @return
     */
    public boolean reorder() {
        if (newStatus == ChapterStatus.Offline.getId()) { //下线
            if (newStatus != oldStatus) {
                return true;
            }
        } else if (newStatus == ChapterStatus.Online.getId()) { //上线
            if (newStatus == oldStatus) {
                if (newOrderId != oldOrderId || newFilteredWords != oldFilteredWords) {
                    return true;
                }
            } else {
                if (newOrderId != oldOrderId) {
                    return true;
                }
            }
        } else if (newStatus == ChapterStatus.Audited.getId()) { //审核
            if (newOrderId != oldOrderId) {
                return true;
            }
        } else if (newStatus == ChapterStatus.Saved.getId()) { //入库
            if (newOrderId != oldOrderId) {
                return true;
            }
        }

        return false;
    }

    public void addChapterIds(List<Long> chapterIds) {
        if (CollectionUtils.isNotEmpty(chapterIds)) {
            for (Long id : chapterIds) {
                this.chapterIds.add(id);
            }
        }
    }

    public void addOrderIds(List<Integer> ids) {
        if (CollectionUtils.isNotEmpty(ids)) {
            for (Integer id : ids) {
                this.orderIds.add(id);
            }
        }
    }
}
