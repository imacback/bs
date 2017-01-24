package cn.aiyuedu.bs.front.service;


import cn.aiyuedu.bs.front.vo.TypeVo;

import java.util.List;

/**
 * Description:
 *
 * @author Scott
 */
//@Service
abstract  public class TypeService {
    abstract  public List<TypeVo> list1();
//    public TypeVo makeTypeVo(CategoryBase type){
//        TypeVo vo = new TypeVo();
//        vo.setId(type.getId());
//        vo.setName(type.getName());
//        return vo;
//    }
}
