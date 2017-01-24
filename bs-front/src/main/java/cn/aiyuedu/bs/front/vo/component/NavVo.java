package cn.aiyuedu.bs.front.vo.component;

import java.util.ArrayList;
import java.util.List;

/**
 * Description:
 *
 * @author Scott
 */
public class NavVo extends ComponentBaseVo {
    private List<ComponentBaseVo> group  = new ArrayList<>();

    public List<ComponentBaseVo> getGroup() {
        return group;
    }

    public void setGroup(List<ComponentBaseVo> group) {
        this.group = group;
    }
}
