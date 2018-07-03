package net.dlyt.qyds.common.view;

import net.dlyt.qyds.common.dto.PamsFlow;

/**
 * Created by panda on 16/7/8.
 */
public class PamsFlowExt extends PamsFlow {

    //创建者姓名
    private String createUserName;

    public String getCreateUserName() {
        return createUserName;
    }

    public void setCreateUserName(String createUserName) {
        this.createUserName = createUserName;
    }
}
