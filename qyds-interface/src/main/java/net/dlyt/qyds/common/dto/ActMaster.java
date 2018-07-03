package net.dlyt.qyds.common.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class ActMaster implements Serializable {
    private static final long serialVersionUID = 1L;
    private String activityId;
    private String shopId;
    private String tempId;
    private String tempName;
    private String activityCode;
    private String comment;
    private String level;
    private String additionalDiscount;
    private Date startTime;
    private Date endTime;
    private String unit;
    private String goodsType;
    private String memberType;
    private String isOriginPrice;
    private String hasSubActivity;
    private String canReturn;
    private String canExchange;
    private String limitCount;
    private String applyUserId;
    private Date applyTime;
    private String applyContent;
    private String approveUserId;
    private Date approveTime;
    private String approveContent;
    private String approveStatus;
    private String deleted;
    private String updateUserId;
    private Date updateTime;
    private String insertUserId;
    private Date insertTime;
    private String activityName;
    private String isMemberActivity;
    private Integer needPoint;
    private BigDecimal needFee;
    private Integer sort;
    private String isValid;
    private String goodsTypeValue;

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId == null ? null : activityId.trim();
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId == null ? null : shopId.trim();
    }

    public String getTempId() {
        return tempId;
    }

    public void setTempId(String tempId) {
        this.tempId = tempId == null ? null : tempId.trim();
    }

    public String getTempName() {
        return tempName;
    }

    public void setTempName(String tempName) {
        this.tempName = tempName == null ? null : tempName.trim();
    }

    public String getActivityCode() {
        return activityCode;
    }

    public void setActivityCode(String activityCode) {
        this.activityCode = activityCode == null ? null : activityCode.trim();
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment == null ? null : comment.trim();
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level == null ? null : level.trim();
    }

    public String getAdditionalDiscount() {
        return additionalDiscount;
    }

    public void setAdditionalDiscount(String additionalDiscount) {
        this.additionalDiscount = additionalDiscount == null ? null : additionalDiscount.trim();
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit == null ? null : unit.trim();
    }

    public String getGoodsType() {
        return goodsType;
    }

    public void setGoodsType(String goodsType) {
        this.goodsType = goodsType == null ? null : goodsType.trim();
    }

    public String getMemberType() {
        return memberType;
    }

    public void setMemberType(String memberType) {
        this.memberType = memberType == null ? null : memberType.trim();
    }

    public String getIsOriginPrice() {
        return isOriginPrice;
    }

    public void setIsOriginPrice(String isOriginPrice) {
        this.isOriginPrice = isOriginPrice == null ? null : isOriginPrice.trim();
    }

    public String getHasSubActivity() {
        return hasSubActivity;
    }

    public void setHasSubActivity(String hasSubActivity) {
        this.hasSubActivity = hasSubActivity == null ? null : hasSubActivity.trim();
    }

    public String getCanReturn() {
        return canReturn;
    }

    public void setCanReturn(String canReturn) {
        this.canReturn = canReturn == null ? null : canReturn.trim();
    }

    public String getCanExchange() {
        return canExchange;
    }

    public void setCanExchange(String canExchange) {
        this.canExchange = canExchange == null ? null : canExchange.trim();
    }

    public String getLimitCount() {
        return limitCount;
    }

    public void setLimitCount(String limitCount) {
        this.limitCount = limitCount == null ? null : limitCount.trim();
    }

    public String getApplyUserId() {
        return applyUserId;
    }

    public void setApplyUserId(String applyUserId) {
        this.applyUserId = applyUserId == null ? null : applyUserId.trim();
    }

    public Date getApplyTime() {
        return applyTime;
    }

    public void setApplyTime(Date applyTime) {
        this.applyTime = applyTime;
    }

    public String getApplyContent() {
        return applyContent;
    }

    public void setApplyContent(String applyContent) {
        this.applyContent = applyContent == null ? null : applyContent.trim();
    }

    public String getApproveUserId() {
        return approveUserId;
    }

    public void setApproveUserId(String approveUserId) {
        this.approveUserId = approveUserId == null ? null : approveUserId.trim();
    }

    public Date getApproveTime() {
        return approveTime;
    }

    public void setApproveTime(Date approveTime) {
        this.approveTime = approveTime;
    }

    public String getApproveContent() {
        return approveContent;
    }

    public void setApproveContent(String approveContent) {
        this.approveContent = approveContent == null ? null : approveContent.trim();
    }

    public String getApproveStatus() {
        return approveStatus;
    }

    public void setApproveStatus(String approveStatus) {
        this.approveStatus = approveStatus == null ? null : approveStatus.trim();
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

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName == null ? null : activityName.trim();
    }

    public String getIsMemberActivity() {
        return isMemberActivity;
    }

    public void setIsMemberActivity(String isMemberActivity) {
        this.isMemberActivity = isMemberActivity == null ? null : isMemberActivity.trim();
    }

    public Integer getNeedPoint() {
        return needPoint;
    }

    public void setNeedPoint(Integer needPoint) {
        this.needPoint = needPoint;
    }

    public BigDecimal getNeedFee() {
        return needFee;
    }

    public void setNeedFee(BigDecimal needFee) {
        this.needFee = needFee;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public String getIsValid() {
        return isValid;
    }

    public void setIsValid(String isValid) {
        this.isValid = isValid == null ? null : isValid.trim();
    }

    public String getGoodsTypeValue() {
        return goodsTypeValue;
    }

    public void setGoodsTypeValue(String goodsTypeValue) {
        this.goodsTypeValue = goodsTypeValue == null ? null : goodsTypeValue.trim();
    }
}