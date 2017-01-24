
package cn.aiyuedu.bs.front.controller.resp;


import javax.xml.bind.annotation.XmlRootElement;

/**
 * Description:
 *
 * @author Scott
 */
public class RespBase {
    protected boolean success=false;
    protected String msg;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}