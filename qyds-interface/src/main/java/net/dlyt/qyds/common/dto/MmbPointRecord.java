package net.dlyt.qyds.common.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class MmbPointRecord implements Serializable {
    private Integer recordNo;

    private String memberId;

    private String type;

    private String ruleId;

    private Integer point;

    private Date pointTime;

    private String scoreSource;

    private String deleted;

    private String updateUserId;

    private Date updateTime;

    private String insertUserId;

    private Date insertTime;

    private Integer pointSurplus;

    private BigDecimal pointRatio;

    private Integer pointCash;

    private String erpSendStatus;

    private static final long serialVersionUID = 1L;

    public Integer getRecordNo() {
        return recordNo;
    }

    public void setRecordNo(Integer recordNo) {
        this.recordNo = recordNo;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId == null ? null : memberId.trim();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }

    public String getRuleId() {
        return ruleId;
    }

    public void setRuleId(String ruleId) {
        this.ruleId = ruleId == null ? null : ruleId.trim();
    }

    public Integer getPoint() {
        return point;
    }

    public void setPoint(Integer point) {
        this.point = point;
    }

    public Date getPointTime() {
        return pointTime;
    }

    public void setPointTime(Date pointTime) {
        this.pointTime = pointTime;
    }

    public String getScoreSource() {
        return scoreSource;
    }

    public void setScoreSource(String scoreSource) {
        this.scoreSource = scoreSource == null ? null : scoreSource.trim();
    }

    public String getDeleted() {
        return deleted;
    }

    public void setDeleted(String deleted) {
        this.deleted = deleted == null ? null : deleted.trim();
    }

    public String getUpdateUserId() {
        return updateUserId;
    }

    public void setUpdateUserId(String updateUserId) {
        this.updateUserId = updateUserId == null ? null : updateUserId.trim();
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getInsertUserId() {
        return insertUserId;
    }

    public void setInsertUserId(String insertUserId) {
        this.insertUserId = insertUserId == null ? null : insertUserId.trim();
    }

    public Date getInsertTime() {
        return insertTime;
    }

    public void setInsertTime(Date insertTime) {
        this.insertTime = insertTime;
    }

    public Integer getPointSurplus() {
        return pointSurplus;
    }

    public void setPointSurplus(Integer pointSurplus) {
        this.pointSurplus = pointSurplus;
    }

    public BigDecimal getPointRatio() {
        return pointRatio;
    }

    public void setPointRatio(BigDecimal pointRatio) {
        this.pointRatio = pointRatio;
    }

    public Integer getPointCash() {
        return pointCash;
    }

    public void setPointCash(Integer pointCash) {
        this.pointCash = pointCash;
    }

    public String getErpSendStatus() {
        return erpSendStatus;
    }

    public void setErpSendStatus(String erpSendStatus) {
        this.erpSendStatus = erpSendStatus == null ? null : erpSendStatus.trim();
    }
}