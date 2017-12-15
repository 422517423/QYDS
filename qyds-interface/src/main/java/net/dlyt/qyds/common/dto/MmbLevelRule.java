package net.dlyt.qyds.common.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class MmbLevelRule implements Serializable {
    private String memberLevelId;

    private String memberLevelCode;

    private String memberLevelName;

    private Integer pointLower;

    private Integer pointUpper;

    private BigDecimal discount;

    private BigDecimal pointRatio;

    private String comment;

    private String deleted;

    private String updateUserId;

    private Date updateTime;

    private String insertUserId;

    private Date insertTime;

    private Integer pointSingle;

    private static final long serialVersionUID = 1L;

    // TODO: 2017/12/14 累计消费
    private Integer pointCumulative;

    public Integer getPointCumulative() {
        return pointCumulative;
    }

    public void setPointCumulative(Integer pointCumulative) {
        this.pointCumulative = pointCumulative;
    }

    public String getMemberLevelId() {
        return memberLevelId;
    }


    public void setMemberLevelId(String memberLevelId) {

        this.memberLevelId = memberLevelId == null ? null : memberLevelId.trim();
    }

    public String getMemberLevelCode() {
        return memberLevelCode;
    }

    public void setMemberLevelCode(String memberLevelCode) {
        this.memberLevelCode = memberLevelCode == null ? null : memberLevelCode.trim();
    }

    public String getMemberLevelName() {
        return memberLevelName;
    }

    public void setMemberLevelName(String memberLevelName) {
        this.memberLevelName = memberLevelName == null ? null : memberLevelName.trim();
    }

    public Integer getPointLower() {
        return pointLower;
    }

    public void setPointLower(Integer pointLower) {
        this.pointLower = pointLower;
    }

    public Integer getPointUpper() {
        return pointUpper;
    }

    public void setPointUpper(Integer pointUpper) {
        this.pointUpper = pointUpper;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    public BigDecimal getPointRatio() {
        return pointRatio;
    }

    public void setPointRatio(BigDecimal pointRatio) {
        this.pointRatio = pointRatio;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment == null ? null : comment.trim();
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

    public Integer getPointSingle() {
        return pointSingle;
    }

    public void setPointSingle(Integer pointSingle) {
        this.pointSingle = pointSingle;
    }
}