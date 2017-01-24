package cn.aiyuedu.bs.dao.dto;

import cn.aiyuedu.bs.common.global.Operation;
import cn.aiyuedu.bs.dao.entity.AdminUser;

/**
 * Description:
 * 
 * @author yz.wu
 */
public class AdminUserDto extends AdminUser implements Operation {

    private static final long serialVersionUID = 4550175793584035325L;

    private String roleName;
    private String creatorName;
    private String editorName;

    public String getRoleName() {
	return roleName;
    }

    public String getCreatorName() {
	return creatorName;
    }

    public String getEditorName() {
	return editorName;
    }

    public void setRoleName(String roleName) {
	this.roleName = roleName;
    }

    public void setCreatorName(String creatorName) {
	this.creatorName = creatorName;
    }

    public void setEditorName(String editorName) {
	this.editorName = editorName;
    }
}
