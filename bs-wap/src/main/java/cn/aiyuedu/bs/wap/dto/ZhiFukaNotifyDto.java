package cn.aiyuedu.bs.wap.dto;

import cn.aiyuedu.bs.wap.Constants;

/**
 * Description:
 *
 * @author yz.wu
 */
public class ZhiFukaNotifyDto {

    private Integer state;
    private String sd51no;
    private String sdcustomno;
    private Double ordermoney;
    private String mark;
    private String sign;
    private String resign;
    private String des;

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("state=").append(state)
            .append("&customerid=").append(Constants.ZHIFUKA_ID)
            .append("&sd51no=").append(sd51no)
            .append("&sdcustomno=").append(sdcustomno)
            .append("&ordermoney=").append(ordermoney)
            .append("&cardno=").append(Constants.ZHIFUKA_CARDNO)
            .append("&mark=").append(mark)
            .append("&sign=").append(sign)
            .append("&resign=").append(resign)
            .append("&des=").append(des);
        return sb.toString();
    }

    public String getSignString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("customerid=").append(Constants.ZHIFUKA_ID)
                .append("&sd51no=").append(sd51no)
                .append("&sdcustomno=").append(sdcustomno)
                .append("&mark=").append(mark)
                .append("&key=").append(Constants.ZHIFUKA_KEY);
        return sb.toString();
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getSd51no() {
        return sd51no;
    }

    public void setSd51no(String sd51no) {
        this.sd51no = sd51no;
    }

    public String getSdcustomno() {
        return sdcustomno;
    }

    public void setSdcustomno(String sdcustomno) {
        this.sdcustomno = sdcustomno;
    }

    public Double getOrdermoney() {
        return ordermoney;
    }

    public void setOrdermoney(Double ordermoney) {
        this.ordermoney = ordermoney;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getResign() {
        return resign;
    }

    public void setResign(String resign) {
        this.resign = resign;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }
}
