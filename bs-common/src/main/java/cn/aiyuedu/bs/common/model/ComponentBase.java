package cn.aiyuedu.bs.common.model;

import com.alibaba.fastjson.annotation.JSONField;
import org.msgpack.annotation.Message;
import org.springframework.data.annotation.Transient;

import java.util.List;

/**
 * Description:
 * @author yz.wu
 */
@Message
public class ComponentBase {

    private Integer id;
    private Integer containerId;
    private String name;
    private Integer typeId;
    private String icon;
    private String fontColor;
    private String title;
    @Transient
    @JSONField(serialize = true)
    private List<ComponentDataGroupBase> groups;
    @Transient
    @JSONField(serialize = true)
    private List<ComponentDataBase> data;
    @Transient
    @JSONField(serialize = true)
    private ComponentDataBase entry;
    @Transient
    @JSONField(serialize = true)
    private List<BookBase> books;

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

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<ComponentDataGroupBase> getGroups() {
        return groups;
    }

    public void setGroups(List<ComponentDataGroupBase> groups) {
        this.groups = groups;
    }

    public List<ComponentDataBase> getData() {
        return data;
    }

    public void setData(List<ComponentDataBase> data) {
        this.data = data;
    }

    public ComponentDataBase getEntry() {
        return entry;
    }

    public void setEntry(ComponentDataBase entry) {
        this.entry = entry;
    }

    public Integer getContainerId() {
        return containerId;
    }

    public void setContainerId(Integer containerId) {
        this.containerId = containerId;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getFontColor() {
        return fontColor;
    }

    public void setFontColor(String fontColor) {
        this.fontColor = fontColor;
    }

    public List<BookBase> getBooks() {
        return books;
    }

    public void setBooks(List<BookBase> books) {
        this.books = books;
    }
}
