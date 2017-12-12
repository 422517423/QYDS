package net.dlyt.qyds.common.dto;

import java.io.Serializable;
import java.util.Date;

public class CouponGoods implements Serializable {
    private static final long serialVersionUID = 1L;
    private String couponGoodsId;
    private String shopId;
    private String couponId;
    private String goodsType;
    private String goodsId;
    private String skuId;
    private String deleted;
    private String updateUserId;
    private Date updateTime;
    private String insertUserId;
    private Date insertTime;
    private String erpSendStatus;
    private String goodsTypeValue;

    public String getCouponGoodsId() {
        return couponGoodsId;
    }

    public void setCouponGoodsId(String couponGoodsId) {
        this.couponGoodsId = couponGoodsId == null ? null : couponGoodsId.trim();
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId == null ? null : shopId.trim();
    }

    public String getCouponId() {
        return couponId;
    }

    public void setCouponId(String couponId) {
        this.couponId = couponId == null ? null : couponId.trim();
    }

    public String getGoodsType() {
        return goodsType;
    }

    public void setGoodsType(String goodsType) {
        this.goodsType = goodsType == null ? null : goodsType.trim();
    }

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId == null ? null : goodsId.trim();
    }

    public String getSkuId() {
        return skuId;
    }

    public void setSkuId(String skuId) {
        this.skuId = skuId == null ? null : skuId.trim();
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

    public String getGoodsTypeValue() {
        return goodsTypeValue;
    }

    public void setGoodsTypeValue(String goodsTypeValue) {
        this.goodsTypeValue = goodsTypeValue == null ? null : goodsTypeValue.trim();
    }
}