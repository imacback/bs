package cn.aiyuedu.bs.front.vo.page;


import cn.aiyuedu.bs.front.vo.component.ComponentBaseVo;

import java.util.ArrayList;
import java.util.List;

/**
 * Description:
 *
 * @author Scott
 */
public class PageBaseVo {
    Integer id;
    String name;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    private List<? extends ComponentBaseVo> compList = new ArrayList<>();

    public List<? extends ComponentBaseVo> getCompList() {
        return compList;
    }

    public void setCompList(List<? extends ComponentBaseVo> compList) {
        this.compList = compList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
