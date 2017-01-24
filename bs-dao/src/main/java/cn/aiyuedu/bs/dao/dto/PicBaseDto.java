package cn.aiyuedu.bs.dao.dto;

import cn.aiyuedu.bs.common.model.PicBase;

/**
 * Created by Thinkpad on 2014/9/24.
 */
public class PicBaseDto extends PicBase {
    private String base64;

    public String getBase64() {
        return base64;
    }

    public void setBase64(String base64) {
        this.base64 = base64;
    }
}
