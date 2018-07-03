package net.dlyt.qyds.common.dto;

import java.io.Serializable;
import java.util.Date;

public class BnkRecords implements Serializable {
    private Integer recordId;

    private String shopId;

    private String goodsType;

    private String typeType;

    private String goodsId;

    private String goodsCode;

    private String erpGoodsCode;

    private String sku;

    private String erpSku;

    private String erpStoreId;

    private String bankType;

    private Integer inoutCount;

    private Date inoutTime;

    private String comment;

    private String deleted;

    private String updateUserId;

    private Date updateTime;

    private String insertUserId;

    private Date insertTime;

    private String erpSendStatus;

    private static final long serialVersionUID = 1L;

    public Integer getRecordId() {
        return recordId;
    }

    public void setRecordId(Integer recordId) {
        this.recordId = recordId;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId == null ? null : shopId.trim();
    }

    public String getGoodsType() {
        return goodsType;
    }

    public void setGoodsType(String goodsType) {
        this.goodsType = goodsType == null ? null : goodsType.trim();
    }

    public String getTypeType() {
        return typeType;
    }

    public void setTypeType(String typeType) {
        this.typeType = typeType == null ? null : typeType.trim();
    }

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId == null ? null : goodsId.trim();
    }

    public String getGoodsCode() {
        return goodsCode;
    }

    public void setGoodsCode(String goodsCode) {
        this.goodsCode = goodsCode == null ? null : goodsCode.trim();
    }

    public String getErpGoodsCode() {
        return erpGoodsCode;
    }

    public void setErpGoodsCode(String erpGoodsCode) {
        this.erpGoodsCode = erpGoodsCode == null ? null : erpGoodsCode.trim();
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku == null ? null : sku.trim();
    }

    public String getErpSku() {
        return erpSku;
    }

    public void setErpSku(String erpSku) {
        this.erpSku = erpSku == null ? null : erpSku.trim();
    }

    public String getErpStoreId() {
        return erpStoreId;
    }

    public void setErpStoreId(String erpStoreId) {
        this.erpStoreId = erpStoreId == null ? null : erpStoreId.trim();
    }

    public String getBankType() {
        return bankType;
    }

    public void setBankType(String bankType) {
        this.bankType = bankType == null ? null : bankType.trim();
    }

    public Integer getInoutCount() {
        return inoutCount;
    }

    public void setInoutCount(Integer inoutCount) {
        this.inoutCount = inoutCount;
    }

    public Date getInoutTime() {
        return inoutTime;
    }

    public void setInoutTime(Date inoutTime) {
        this.inoutTime = inoutTime;
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

    public String getErpSendStatus() {
        return erpSendStatus;
    }

    public void setErpSendStatus(String erpSendStatus) {
        this.erpSendStatus = erpSendStatus == null ? null : erpSendStatus.trim();
    }
}