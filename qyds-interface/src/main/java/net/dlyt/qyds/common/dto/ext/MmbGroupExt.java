package net.dlyt.qyds.common.dto.ext;

import net.dlyt.qyds.common.dto.MmbGroup;

/**
 * Created by YiLian on 16/7/29.
 */
public class MmbGroupExt extends MmbGroup {

    private String typeName;

    private String insertUserName;

    private String updateUserName;

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

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
