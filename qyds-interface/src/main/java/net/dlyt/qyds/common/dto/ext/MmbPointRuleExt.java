package net.dlyt.qyds.common.dto.ext;

import net.dlyt.qyds.common.dto.MmbPointRule;

/**
 * Created by YiLian on 16/7/29.
 */
public class MmbPointRuleExt extends MmbPointRule {

    /**
     * 修改人
     */
    private String updateUserName;

    /**
     * 创建人
     */
    private String insertUserName;

    public String getUpdateUserName() {
        return updateUserName;
    }

    public void setUpdateUserName(String updateUserName) {
        this.updateUserName = updateUserName;
    }

    public String getInsertUserName() {
        return insertUserName;
    }

    public void setInsertUserName(String insertUserName) {
        this.insertUserName = insertUserName;
    }
}
