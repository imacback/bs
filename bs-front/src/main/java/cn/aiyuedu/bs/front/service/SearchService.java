package cn.aiyuedu.bs.front.service;


import cn.aiyuedu.bs.common.orm.Page;
import cn.aiyuedu.bs.front.vo.BookVo;

import java.util.List;

/**
 * Description:
 *
 * @author Scott
 */
//@Service
public abstract class SearchService {

   abstract public Page<BookVo> list(String  words, int pageNum, int pageSize) ;
   abstract public List<BookVo> linkList() ;
   abstract public List<BookVo> picList() ;

}
