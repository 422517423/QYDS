package net.dlyt.qyds.common.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class GdsSku implements Serializable {
    private String goodsSkuId;

    private String goodsId;

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

    private String imageUrlJson;

    private BigDecimal price;

    private Integer safeBank;

    private String comment;

    private String deleted;

    private String updateUserId;

    private Date updateTime;

    private String insertUserId;

    private Date insertTime;

    public String getGoodsSkuId() {
        return goodsSkuId;
    }

    public void setGoodsSkuId(String goodsSkuId) {
        this.goodsSkuId = goodsSkuId == null ? null : goodsSkuId.trim();
    }

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId == null ? null : goodsId.trim();
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

    public String getImageUrlJson() {
        return imageUrlJson;
    }

    public void setImageUrlJson(String imageUrlJson) {
        this.imageUrlJson = imageUrlJson == null ? null : imageUrlJson.trim();
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getSafeBank() {
        return safeBank;
    }

    public void setSafeBank(Integer safeBank) {
        this.safeBank = safeBank;
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