package net.dlyt.qyds.common.dto;

import java.io.Serializable;

public class PamsRoleKey implements Serializable {
    private Integer roleId;

    private String orgId;

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId == null ? null : orgId.trim();
    }
}