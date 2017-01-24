package cn.aiyuedu.bs.common.dto;


import org.springframework.util.StringUtils;

import java.io.Serializable;

/**
 * 用户信息，api与service模块公用
 *
 * @author Scott
 */
public class ClientInfo implements Serializable {
    private String cid;//
    private Integer uid;
    private String site;
    private String os;
    private String app;
    private String model;//
    private String root;
    private String imei;//
    private Integer width;
    private String os_version;
    private Integer height;
    private String os_id;
    private String brand;
    private String imsi;//
    private String dpi;
    private Integer channel;//
    private String operator;
    private String ip;
    private String sessionid;

    /**
     * 版本号由三部分组成：1.1.00.12
     * 1.系统平台（Android、Symbian、J2ME），具体含义如下：
     * 2. 客户端版本号 1.00：客户端版本号（1：为大版本  0：新功能  0：功能优化、BUG修复）
     * 3. 渠道号
     *
     * @return
     */
    public ClientParam clientParam() {
        ClientParam out = new ClientParam();
        try {
            String app = getApp();
            String[] arr = app.split("\\.");
            out.setChannelId(Integer.valueOf(arr[3]));
            out.setPlatform(Integer.valueOf(arr[0]));
            out.setVersion(arr[1]+"."+arr[2]);
            out.setVersion1(arr[1]);
            out.setVersion2(arr[2]);
        } catch (Throwable e) {
        } finally {
            return out;
        }
    }

    static public class ClientParam {
        Integer platform;
        String version;
        String version1;
        String version2;
        Integer channelId;


        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public String getVersion1() {
            return version1;
        }

        public void setVersion1(String version1) {
            this.version1 = version1;
        }

        public String getVersion2() {
            return version2;
        }

        public void setVersion2(String version2) {
            this.version2 = version2;
        }

        public Integer getPlatform() {
            return platform;
        }

        public void setPlatform(Integer platform) {
            this.platform = platform;
        }

        public Integer getChannelId() {
            return channelId;
        }

        public void setChannelId(Integer channelId) {
            this.channelId = channelId;
        }
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public String getApp() {
        return app;
    }

    public void setApp(String app) {
        this.app = app;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getRoot() {
        return root;
    }

    public void setRoot(String root) {
        this.root = root;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public String getOs_id() {
        return os_id;
    }

    public void setOs_id(String os_id) {
        this.os_id = os_id;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getImsi() {
        return imsi;
    }

    public void setImsi(String imsi) {
        this.imsi = imsi;
    }

    public String getDpi() {
        return dpi;
    }

    public void setDpi(String dpi) {
        this.dpi = dpi;
    }


    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getOs_version() {
        return os_version;
    }

    public void setOs_version(String os_version) {
        this.os_version = os_version;
    }

    public Integer getChannel() {
        return channel;
    }

    public void setChannel(Integer channel) {
        this.channel = channel;
    }

    public String getSessionid() {
        return sessionid;
    }

    public void setSessionid(String sessionid) {
        this.sessionid = sessionid;
    }
}
