package net.dlyt.qyds.common.dto.ext;

import net.dlyt.qyds.common.dto.MmbPointRecord;

public class MmbPointRecordExt extends MmbPointRecord {

    /**
     *  积分规则名称
     */
    private String ruleName;
    private String typeName;
    private String telephone;

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    /**
     *  积分规则名称
     */
    private String memberCode;

    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    public String getMemberCode() {
        return memberCode;
    }

    public void setMemberCode(String memberCode) {
        this.memberCode = memberCode;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
}
