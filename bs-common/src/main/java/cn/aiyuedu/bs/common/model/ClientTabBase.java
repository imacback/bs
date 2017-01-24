package cn.aiyuedu.bs.common.model;

import org.msgpack.annotation.Message;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@Message
@XmlAccessorType(XmlAccessType.FIELD)
public class ClientTabBase {
    private Integer id;
	// **排序******/
    @XmlElement(name = "index")
	private String orderId;
	// **地址******/
	private String url;
	// **名称******/
    @XmlElement(name = "title")
	private String name;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
