package cn.aiyuedu.bs.back.dto;

import cn.aiyuedu.bs.dao.entity.DistributeBook;

/**
 * Created by Thinkpad on 2014/11/18.
 */
public class DistributeBookTagDto extends DistributeBook {

    private  String bookName;
    private  String author;
    private  String distributeName;

    public String getDistributeName() {
        return distributeName;
    }

    public void setDistributeName(String distributeName) {
        this.distributeName = distributeName;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }
}
