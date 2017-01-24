package cn.aiyuedu.bs.front.vo;

import cn.aiyuedu.bs.common.model.CategoryBase;

import java.util.ArrayList;
import java.util.List;

/**
 * Description:
 *
 * @author Scott
 */
public class TypeVo extends CategoryBase implements Comparable<TypeVo> {
    private  List<TypeVo> list = new ArrayList<>();

    public List<TypeVo> getList() {
        return list;
    }

    public void setList(List<TypeVo> list) {
        this.list = list;
    }


    @Override
    public int compareTo(TypeVo o) {
        return 0;
    }

//    @Override
//    public int compareTo(TypeVo o) {
//        if (o.getOrderId()==this.getOrderId()){
//            return 0;
//        }else if(o.getOrderId()>this.getOrderId()){
//            return -1;
//        }else{
//            return 1;
//        }
//    }
}
