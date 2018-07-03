package net.dlyt.qyds.common.dto.ext;

import net.dlyt.qyds.common.dto.MmbLevelRule;

/**
 * Created by C_Nagai on 2016/7/28.
 */
public class MmbLevelRuleExt extends MmbLevelRule {
    // 创建者
    private String insertUserName;

    // 更新者
    private String updateUserName;

    public void setMemberCount(int memberCount) {
        this.memberCount = memberCount;
    }

    public int getMemberCount() {
        return memberCount;
    }

    // 该级别会员数目统计
    private int memberCount;

    public String getInsertUserName() {
        return insertUserName;
    }

    public void setInsertUserName(String insertUserName) {
        this.insertUserName = insertUserName;
    }

    public String getUpdateUserName() {
        return updateUserName;
    }

    public void setUpdateUserName(String updateUserName) {
        this.updateUserName = updateUserName;
    }

}
