package cn.aiyuedu.bs.front.vo;

import java.util.ArrayList;
import java.util.List;

/**
 * Description:
 *
 * @author Scott
 */
public class RankVo  {
    String id;
    String name;
    private List<BookVo> list = new ArrayList<>();

    public List<BookVo> getList() {
        return list;
    }

    public void setList(List<BookVo> list) {
        this.list = list;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
