package cn.aiyuedu.bs.wap.dto;

import java.util.List;

/**
 * Created by Thinkpad on 2014/11/26.
 */
public class ChapterPageDto<T> {
    private Integer id;
    private Integer start;
    private Integer size;
    private Integer total;
    private Integer maxChapter;
    private Integer minChapter;
    private List<T> result;
    private Integer pages;
    private  boolean isDesc;
    private Long bookId;
    private String name;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public Integer getStart() {
        return start;
    }

    public void setStart(Integer start) {
        this.start = start;
    }

    public Integer getPages() {
        return pages;
    }

    public void setPages(Integer pages) {
        this.pages = pages;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getMaxChapter() {
        return maxChapter;
    }

    public void setMaxChapter(Integer maxChapter) {
        this.maxChapter = maxChapter;
    }

    public Integer getMinChapter() {
        return minChapter;
    }

    public void setMinChapter(Integer minChapter) {
        this.minChapter = minChapter;
    }

    public List<T> getResult() {
        return result;
    }

    public void setResult(List<T> result) {
        this.result = result;
    }

    public boolean getIsDesc() {
        return isDesc;
    }

    public void setIsDesc(boolean isDesc) {
        this.isDesc = isDesc;
    }
}
