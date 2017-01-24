package cn.aiyuedu.bs.back.dto;

/**
 * Created by webwyz on 14/10/21.
 */
public class ChapterReorderDto {

    private Long chapterId;
    private Integer oldOrderId;
    private Integer newOrderId;

    public ChapterReorderDto() {}

    public ChapterReorderDto(Long chapterId, Integer oldOrderId, Integer newOrderId) {
        this.chapterId = chapterId;
        this.oldOrderId = oldOrderId;
        this.newOrderId = newOrderId;
    }

    public Long getChapterId() {
        return chapterId;
    }

    public void setChapterId(Long chapterId) {
        this.chapterId = chapterId;
    }

    public Integer getOldOrderId() {
        return oldOrderId;
    }

    public void setOldOrderId(Integer oldOrderId) {
        this.oldOrderId = oldOrderId;
    }

    public Integer getNewOrderId() {
        return newOrderId;
    }

    public void setNewOrderId(Integer newOrderId) {
        this.newOrderId = newOrderId;
    }
}
