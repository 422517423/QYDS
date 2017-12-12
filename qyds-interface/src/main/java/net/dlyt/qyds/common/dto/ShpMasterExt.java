package net.dlyt.qyds.common.dto;

/**
 * Created by pc on 2016/7/25.
 */
public class ShpMasterExt extends ShpMaster {
    private String loginId;
    private String userName;

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
