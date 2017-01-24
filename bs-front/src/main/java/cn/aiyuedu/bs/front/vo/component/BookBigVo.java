package cn.aiyuedu.bs.front.vo.component;


import cn.aiyuedu.bs.front.vo.BookVo;

import java.util.ArrayList;
import java.util.List;

/**
 * Description:
 *
 * @author Scott
 */
public class BookBigVo extends ComponentBaseVo {
    List<BookVo> bookList = new ArrayList<>();

    public List<BookVo> getBookList() {
        return bookList;
    }

    public void setBookList(List<BookVo> bookList) {
        this.bookList = bookList;
    }

}
