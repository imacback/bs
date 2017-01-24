package cn.aiyuedu.bs.common.dto;

import java.io.Serializable;
import java.util.Map;

public class ResultDto implements Serializable {

	private static final long serialVersionUID = 7842620505046783135L;

	private boolean success;
    private boolean reload;
	private String info;
    private Map<String, String> callBack;

	public boolean getSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

    public boolean getReload() {
        return reload;
    }

    public void setReload(boolean reload) {
        this.reload = reload;
    }

    public Map<String, String> getCallBack() {
        return callBack;
    }

    public void setCallBack(Map<String, String> callBack) {
        this.callBack = callBack;
    }
}
