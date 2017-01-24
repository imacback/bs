package cn.aiyuedu.bs.front.pojo;

import java.io.Serializable;


public class StaticLog implements Serializable {
    //cookie的用户信息
    private String platform; //    1.	平台
    private String version; //    2.	版本
    private String channelId; //    3.	渠道
    private String uid; //    4.	用户ID
    private String model;  //    5.	机型
    private String os;  //    6.	系统
    private String width;  //    7.	分辨率宽
    private String height;  //    8.	分辨率高
    private String imei;  //    9.	IMEI
    private String imsi;  //    10.	IMSI
    private String ip;  //    11.	IP
    //request的信息
    private String refer;  //    12.	ReferUrl
    private String currUrl;  //    13.	请求Url
    private String operation;  //    14.	操作标识
    private String objId;   //    15.	对象ID
    private String gotoUrl;   //    16.	跳转URL
    private String level;   //    17.	页面级别
    private String sourcePath;   //    18.	来源路径：如“/pg_1/cp_1/bk_1/lv_1/”。每层的格式为“来源类型_来源ID”，类型如下：
    private String serverVersion;   //    19.	服务端版本

    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();
        b.append("[")
                .append(platform).append("] [")
                .append(version).append("] [")
                .append(channelId).append("] [")
                .append(uid).append("] [")
                .append(model).append("] [")
                .append(os).append("] [")
                .append(width).append("] [")
                .append(height).append("] [")
                .append(imei).append("] [")
                .append(imsi).append("] [")
                .append(ip).append("] [")
                .append(refer).append("] [")
                .append(currUrl).append("] [")
                .append(operation).append("] [")
                .append(objId).append("] [")
                .append(gotoUrl).append("] [")
                .append(level).append("] [")
                .append(sourcePath).append("] [")
                .append(serverVersion).append("]");
        return b.toString();
    }

    public String getServerVersion() {
        return serverVersion;
    }

    public void setServerVersion(String serverVersion) {
        this.serverVersion = serverVersion;
    }

    public String getPlatform() {
        return platform;
    }

    public String getVersion() {
        return version;
    }

    public String getChannelId() {
        return channelId;
    }

    public String getUid() {
        return uid;
    }

    public String getModel() {
        return model;
    }

    public String getOs() {
        return os;
    }

    public String getWidth() {
        return width;
    }

    public String getHeight() {
        return height;
    }

    public String getImei() {
        return imei;
    }

    public String getImsi() {
        return imsi;
    }

    public String getIp() {
        return ip;
    }

    public String getRefer() {
        return refer;
    }

    public String getCurrUrl() {
        return currUrl;
    }

    public String getOperation() {
        return operation;
    }

    public String getObjId() {
        return objId;
    }

    public String getGotoUrl() {
        return gotoUrl;
    }

    public void setGotoUrl(String gotoUrl) {
        this.gotoUrl = gotoUrl;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getSourcePath() {
        return sourcePath;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public void setImsi(String imsi) {
        this.imsi = imsi;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void setRefer(String refer) {
        this.refer = refer;
    }

    public void setCurrUrl(String currUrl) {
        this.currUrl = currUrl;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public void setObjId(String objId) {
        this.objId = objId;
    }

    public void setSourcePath(String sourcePath) {
        this.sourcePath = sourcePath;
    }
}
