package net.dlyt.qyds.common.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class OrdList implements Serializable {
    private String detailId;

    private String orderId;

    private String type;

    private String goodsCode;

    private String goodsId;

    private String imageUrlJson;

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

    private String deleted;

    private String updateUserId;

    private Date updateTime;

    private String insertUserId;

    private Date insertTime;

    private String isGift;

    private static final long serialVersionUID = 1L;

    public String getDetailId() {
        return detailId;
    }

    public void setDetailId(String detailId) {
        this.detailId = detailId == null ? null : detailId.trim();
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId == null ? null : orderId.trim();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }

    public String getGoodsCode() {
        return goodsCode;
    }

    public void setGoodsCode(String goodsCode) {
        this.goodsCode = goodsCode == null ? null : goodsCode.trim();
    }

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId == null ? null : goodsId.trim();
    }

    public String getImageUrlJson() {
        return imageUrlJson;
    }

    public void setImageUrlJson(String imageUrlJson) {
        this.imageUrlJson = imageUrlJson == null ? null : imageUrlJson.trim();
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

    public String getIsGift() {
        return isGift;
    }

    public void setIsGift(String isGift) {
        this.isGift = isGift == null ? null : isGift.trim();
    }
}