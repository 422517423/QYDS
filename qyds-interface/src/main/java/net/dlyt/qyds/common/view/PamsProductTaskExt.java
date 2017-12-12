package net.dlyt.qyds.common.view;

import net.dlyt.qyds.common.dto.PamsProductTask;

/**
 * Created by panda on 16/7/13.
 */
public class PamsProductTaskExt extends PamsProductTask{

    //持续期间
    private String duration;

    //可执行角色
    private String pamdRoleId;

    //分配人名字
    private String userName;

    public String getPamdRoleId() {
        return pamdRoleId;
    }

    public void setPamdRoleId(String pamdRoleId) {
        this.pamdRoleId = pamdRoleId;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
