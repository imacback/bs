package cn.aiyuedu.bs.common.model;

import java.util.LinkedList;
import java.util.List;

public class ShelfBookBase  {
	private Integer chapters;
    private List<Long> list ;
    private LinkedList link  ;

    public LinkedList getLink() {
        return link;
    }

    public void setLink(LinkedList link) {
        this.link = link;
    }

    // private Link

    public List<Long> getList() {
        return list;
    }

    public void setList(List<Long> list) {
        this.list = list;
    }

    public Integer getChapters() {
		return chapters;
	}

	public void setChapters(Integer chapters) {
		this.chapters = chapters;
	}
	
}
