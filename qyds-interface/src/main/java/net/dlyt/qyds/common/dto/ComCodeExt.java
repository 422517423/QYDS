package net.dlyt.qyds.common.dto;

/**
 * Created by pc on 2016/7/25.
 */
public class ComCodeExt extends ComCode {
    private String userName;
    private String loginId;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }
}
