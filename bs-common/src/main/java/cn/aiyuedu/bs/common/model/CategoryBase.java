package cn.aiyuedu.bs.common.model;

import com.alibaba.fastjson.annotation.JSONField;
import org.msgpack.annotation.Message;

import java.util.List;

/**
 * Description:
 *
 * @author yz.wu
 */
@Message
public class CategoryBase {

    private Integer id;
    private String name;
    private String logo;
    private String recommend;
    private Integer orderId;

    @JSONField(serialize = false)
    private List<CategoryBase> children;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getRecommend() {
        return recommend;
    }

    public void setRecommend(String recommend) {
        this.recommend = recommend;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public List<CategoryBase> getChildren() {
        return children;
    }

    public void setChildren(List<CategoryBase> children) {
        this.children = children;
    }
}
