package cn.aiyuedu.bs.back.dto;

import cn.aiyuedu.bs.common.dto.ResultDto;
import cn.aiyuedu.bs.dao.dto.BookQueryDto;

import java.util.List;

/**
 * Description:
 *
 * @author yz.wu
 */
public class BookTagDto extends ResultDto {

    private List<BookQueryDto> bookQueryDtos;

    public List<BookQueryDto> getBookQueryDtos() {
        return bookQueryDtos;
    }

    public void setBookQueryDtos(List<BookQueryDto> bookQueryDtos) {
        this.bookQueryDtos = bookQueryDtos;
    }
}
