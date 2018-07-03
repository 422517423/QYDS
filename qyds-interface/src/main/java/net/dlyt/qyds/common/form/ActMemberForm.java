package net.dlyt.qyds.common.form;

import net.dlyt.qyds.common.dto.ActMember;

/**
 * Created by cjk on 16/8/5.
 */
public class ActMemberForm extends ActMember {
    private String memberName = null;

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }
}
