package net.dlyt.qyds.common.dto;

import java.io.Serializable;
import java.util.Date;

public class GdsSell implements Serializable {
    private String goodsId;

    private String erpGoodsCode;

    private String propertySellJson;

    private Integer limitCount;

    private String recommendJson;

    private String matingJson;

    private Integer safeBank;

    private String onsellPlanDate;

    private String offsellPlanDate;

    private String onsellInfactTime;

    private String offsellInfactTime;

    private String offsellReason;

    private String comment;

    private String deleted;

    private String updateUserId;

    private Date updateTime;

    private String insertUserId;

    private Date insertTime;

    private static final long serialVersionUID = 1L;

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId == null ? null : goodsId.trim();
    }

    public String getErpGoodsCode() {
        return erpGoodsCode;
    }

    public void setErpGoodsCode(String erpGoodsCode) {
        this.erpGoodsCode = erpGoodsCode == null ? null : erpGoodsCode.trim();
    }

    public String getPropertySellJson() {
        return propertySellJson;
    }

    public void setPropertySellJson(String propertySellJson) {
        this.propertySellJson = propertySellJson == null ? null : propertySellJson.trim();
    }

    public Integer getLimitCount() {
        return limitCount;
    }

    public void setLimitCount(Integer limitCount) {
        this.limitCount = limitCount;
    }

    public String getRecommendJson() {
        return recommendJson;
    }

    public void setRecommendJson(String recommendJson) {
        this.recommendJson = recommendJson == null ? null : recommendJson.trim();
    }

    public String getMatingJson() {
        return matingJson;
    }

    public void setMatingJson(String matingJson) {
        this.matingJson = matingJson == null ? null : matingJson.trim();
    }

    public Integer getSafeBank() {
        return safeBank;
    }

    public void setSafeBank(Integer safeBank) {
        this.safeBank = safeBank;
    }

    public String getOnsellPlanDate() {
        return onsellPlanDate;
    }

    public void setOnsellPlanDate(String onsellPlanDate) {
        this.onsellPlanDate = onsellPlanDate == null ? null : onsellPlanDate.trim();
    }

    public String getOffsellPlanDate() {
        return offsellPlanDate;
    }

    public void setOffsellPlanDate(String offsellPlanDate) {
        this.offsellPlanDate = offsellPlanDate == null ? null : offsellPlanDate.trim();
    }

    public String getOnsellInfactTime() {
        return onsellInfactTime;
    }

    public void setOnsellInfactTime(String onsellInfactTime) {
        this.onsellInfactTime = onsellInfactTime == null ? null : onsellInfactTime.trim();
    }

    public String getOffsellInfactTime() {
        return offsellInfactTime;
    }

    public void setOffsellInfactTime(String offsellInfactTime) {
        this.offsellInfactTime = offsellInfactTime == null ? null : offsellInfactTime.trim();
    }

    public String getOffsellReason() {
        return offsellReason;
    }

    public void setOffsellReason(String offsellReason) {
        this.offsellReason = offsellReason == null ? null : offsellReason.trim();
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
}