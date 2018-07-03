package net.dlyt.qyds.common.dto;

import java.io.Serializable;
import java.util.Date;

public class PamsNode implements Serializable {
    private Integer seq;

    private String nodeId;

    private String orgId;

    private String nodeName;

    private Short minDay;

    private Short standardDay;

    private Short maxDay;

    private String isValid;

    private String pamdRoleId;

    private String createUser;

    private Date createTime;

    private String updateUser;

    private Date updateTime;

    public Integer getSeq() {
        return seq;
    }

    public void setSeq(Integer seq) {
        this.seq = seq;
    }

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId == null ? null : nodeId.trim();
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId == null ? null : orgId.trim();
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName == null ? null : nodeName.trim();
    }

    public Short getMinDay() {
        return minDay;
    }

    public void setMinDay(Short minDay) {
        this.minDay = minDay;
    }

    public Short getStandardDay() {
        return standardDay;
    }

    public void setStandardDay(Short standardDay) {
        this.standardDay = standardDay;
    }

    public Short getMaxDay() {
        return maxDay;
    }

    public void setMaxDay(Short maxDay) {
        this.maxDay = maxDay;
    }

    public String getIsValid() {
        return isValid;
    }

    public void setIsValid(String isValid) {
        this.isValid = isValid == null ? null : isValid.trim();
    }

    public String getPamdRoleId() {
        return pamdRoleId;
    }

    public void setPamdRoleId(String pamdRoleId) {
        this.pamdRoleId = pamdRoleId == null ? null : pamdRoleId.trim();
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser == null ? null : createUser.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser == null ? null : updateUser.trim();
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}