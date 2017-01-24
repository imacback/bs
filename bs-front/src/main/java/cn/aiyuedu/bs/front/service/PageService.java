package cn.aiyuedu.bs.front.service;


import cn.aiyuedu.bs.common.model.ContainerBase;
import cn.aiyuedu.bs.front.vo.page.PageBaseVo;

/**
 * Description:
 *
 * @author Scott
 */
//@Service
public abstract class PageService {

    public abstract PageBaseVo getPage(String pageId) ;

    public abstract PageBaseVo getPage(ContainerBase ctn);
}
