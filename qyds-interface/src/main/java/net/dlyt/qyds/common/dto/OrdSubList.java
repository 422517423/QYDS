package net.dlyt.qyds.common.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class OrdSubList implements Serializable {
    private String subOrderId;

    private String orderId;

    private String detailId;

    private String type;

    private String goodsId;

    private String goodsCode;

    private String goodsName;

    private String sku;

    private String coloreCode;

    private String coloreName;

    private String sizeCode;

    private String sizeName;

    private String erpSku;

    private String erpStyleNo;

    private String erpColoreCode;

    private String erpColoreName;

    private String erpSizeCode;

    private String erpSizeName;

    private BigDecimal price;

    private String actionId;

    private String actionName;

    private BigDecimal priceDiscount;

    private Integer quantity;

    private BigDecimal amount;

    private BigDecimal amountDiscount;

    private String deliverStatus;

    private Date deliverTime;

    private String expressId;

    private String expressName;

    private String expressNo;

    private String deliverType;

    private String erpStoreId;

    private String storeName;

    private String storePhone;

    private String storeDeliveryId;

    private String storeDeliveryName;

    private String deleted;

    private String updateUserId;

    private Date updateTime;

    private String insertUserId;

    private Date insertTime;

    private String skuId;

    private String dispatchStatus;

    private String dispatchStore;

    private BigDecimal priceShare;

    private String upSeasoning;

    private static final long serialVersionUID = 1L;

    public String getSubOrderId() {
        return subOrderId;
    }

    public void setSubOrderId(String subOrderId) {
        this.subOrderId = subOrderId == null ? null : subOrderId.trim();
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId == null ? null : orderId.trim();
    }

    public String getDetailId() {
        return detailId;
    }

    public void setDetailId(String detailId) {
        this.detailId = detailId == null ? null : detailId.trim();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type == null ? null : type.trim();
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

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName == null ? null : goodsName.trim();
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku == null ? null : sku.trim();
    }

    public String getColoreCode() {
        return coloreCode;
    }

    public void setColoreCode(String coloreCode) {
        this.coloreCode = coloreCode == null ? null : coloreCode.trim();
    }

    public String getColoreName() {
        return coloreName;
    }

    public void setColoreName(String coloreName) {
        this.coloreName = coloreName == null ? null : coloreName.trim();
    }

    public String getSizeCode() {
        return sizeCode;
    }

    public void setSizeCode(String sizeCode) {
        this.sizeCode = sizeCode == null ? null : sizeCode.trim();
    }

    public String getSizeName() {
        return sizeName;
    }

    public void setSizeName(String sizeName) {
        this.sizeName = sizeName == null ? null : sizeName.trim();
    }

    public String getErpSku() {
        return erpSku;
    }

    public void setErpSku(String erpSku) {
        this.erpSku = erpSku == null ? null : erpSku.trim();
    }

    public String getErpStyleNo() {
        return erpStyleNo;
    }

    public void setErpStyleNo(String erpStyleNo) {
        this.erpStyleNo = erpStyleNo == null ? null : erpStyleNo.trim();
    }

    public String getErpColoreCode() {
        return erpColoreCode;
    }

    public void setErpColoreCode(String erpColoreCode) {
        this.erpColoreCode = erpColoreCode == null ? null : erpColoreCode.trim();
    }

    public String getErpColoreName() {
        return erpColoreName;
    }

    public void setErpColoreName(String erpColoreName) {
        this.erpColoreName = erpColoreName == null ? null : erpColoreName.trim();
    }

    public String getErpSizeCode() {
        return erpSizeCode;
    }

    public void setErpSizeCode(String erpSizeCode) {
        this.erpSizeCode = erpSizeCode == null ? null : erpSizeCode.trim();
    }

    public String getErpSizeName() {
        return erpSizeName;
    }

    public void setErpSizeName(String erpSizeName) {
        this.erpSizeName = erpSizeName == null ? null : erpSizeName.trim();
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getActionId() {
        return actionId;
    }

    public void setActionId(String actionId) {
        this.actionId = actionId == null ? null : actionId.trim();
    }

    public String getActionName() {
        return actionName;
    }

    public void setActionName(String actionName) {
        this.actionName = actionName == null ? null : actionName.trim();
    }

    public BigDecimal getPriceDiscount() {
        return priceDiscount;
    }

    public void setPriceDiscount(BigDecimal priceDiscount) {
        this.priceDiscount = priceDiscount;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getAmountDiscount() {
        return amountDiscount;
    }

    public void setAmountDiscount(BigDecimal amountDiscount) {
        this.amountDiscount = amountDiscount;
    }

    public String getDeliverStatus() {
        return deliverStatus;
    }

    public void setDeliverStatus(String deliverStatus) {
        this.deliverStatus = deliverStatus == null ? null : deliverStatus.trim();
    }

    public Date getDeliverTime() {
        return deliverTime;
    }

    public void setDeliverTime(Date deliverTime) {
        this.deliverTime = deliverTime;
    }

    public String getExpressId() {
        return expressId;
    }

    public void setExpressId(String expressId) {
        this.expressId = expressId == null ? null : expressId.trim();
    }

    public String getExpressName() {
        return expressName;
    }

    public void setExpressName(String expressName) {
        this.expressName = expressName == null ? null : expressName.trim();
    }

    public String getExpressNo() {
        return expressNo;
    }

    public void setExpressNo(String expressNo) {
        this.expressNo = expressNo == null ? null : expressNo.trim();
    }

    public String getDeliverType() {
        return deliverType;
    }

    public void setDeliverType(String deliverType) {
        this.deliverType = deliverType == null ? null : deliverType.trim();
    }

    public String getErpStoreId() {
        return erpStoreId;
    }

    public void setErpStoreId(String erpStoreId) {
        this.erpStoreId = erpStoreId == null ? null : erpStoreId.trim();
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName == null ? null : storeName.trim();
    }

    public String getStorePhone() {
        return storePhone;
    }

    public void setStorePhone(String storePhone) {
        this.storePhone = storePhone == null ? null : storePhone.trim();
    }

    public String getStoreDeliveryId() {
        return storeDeliveryId;
    }

    public void setStoreDeliveryId(String storeDeliveryId) {
        this.storeDeliveryId = storeDeliveryId == null ? null : storeDeliveryId.trim();
    }

    public String getStoreDeliveryName() {
        return storeDeliveryName;
    }

    public void setStoreDeliveryName(String storeDeliveryName) {
        this.storeDeliveryName = storeDeliveryName == null ? null : storeDeliveryName.trim();
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

    public String getSkuId() {
        return skuId;
    }

    public void setSkuId(String skuId) {
        this.skuId = skuId == null ? null : skuId.trim();
    }

    public String getDispatchStatus() {
        return dispatchStatus;
    }

    public void setDispatchStatus(String dispatchStatus) {
        this.dispatchStatus = dispatchStatus == null ? null : dispatchStatus.trim();
    }

    public String getDispatchStore() {
        return dispatchStore;
    }

    public void setDispatchStore(String dispatchStore) {
        this.dispatchStore = dispatchStore == null ? null : dispatchStore.trim();
    }

    public BigDecimal getPriceShare() {
        return priceShare;
    }

    public void setPriceShare(BigDecimal priceShare) {
        this.priceShare = priceShare;
    }

    public String getUpSeasoning() {
        return upSeasoning;
    }

    public void setUpSeasoning(String upSeasoning) {
        this.upSeasoning = upSeasoning == null ? null : upSeasoning.trim();
    }
}