package net.dlyt.qyds.common.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class OrdTransferList implements Serializable {
    private static final long serialVersionUID = 1L;
    private String orderTransferId;
    private String subOrderId;
    private String orderId;
    private String transferStatus;
    private String goodsType;
    private String goodsId;
    private String goodsCode;
    private String goodsName;
    private String coloreCode;
    private String coloreName;
    private String sizeCode;
    private String sizeName;
    private String sku;
    private String erpSku;
    private String erpStyleNo;
    private String erpColoreCode;
    private String erpColoreName;
    private String erpSizeCode;
    private String erpSizeName;
    private BigDecimal price;
    private BigDecimal priceDiscount;
    private BigDecimal priceShare;
    private Integer quantity;
    private BigDecimal amount;
    private BigDecimal amountDiscount;
    private String applyStore;
    private String applyUser;
    private Date applyTime;
    private String applyAddress;
    private String applyContactor;
    private String applyPhone;
    private String applyPostcode;
    private String dispatchUser;
    private Date dispatchTime;
    private String dispatchStore;
    private String refuseStore;
    private String refuseUser;
    private Date refuseTime;
    private String acceptUser;
    private Date acceptTime;
    private String deliveryUser;
    private Date deliveryTime;
    private String deliveryAddress;
    private String deliveryContactor;
    private String deliveryPhone;
    private String deliveryPostcode;
    private String expressId;
    private String expressName;
    private String expressNo;
    private String receiveUser;
    private Date receiveTime;
    private String erpSendStatusDelivery;
    private String erpSendStatusReceive;
    private String deleted;
    private String updateUserId;
    private Date updateTime;
    private String insertUserId;
    private Date insertTime;

    public String getOrderTransferId() {
        return orderTransferId;
    }

    public void setOrderTransferId(String orderTransferId) {
        this.orderTransferId = orderTransferId == null ? null : orderTransferId.trim();
    }

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

    public String getTransferStatus() {
        return transferStatus;
    }

    public void setTransferStatus(String transferStatus) {
        this.transferStatus = transferStatus == null ? null : transferStatus.trim();
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

    public BigDecimal getPriceDiscount() {
        return priceDiscount;
    }

    public void setPriceDiscount(BigDecimal priceDiscount) {
        this.priceDiscount = priceDiscount;
    }

    public BigDecimal getPriceShare() {
        return priceShare;
    }

    public void setPriceShare(BigDecimal priceShare) {
        this.priceShare = priceShare;
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

    public String getApplyStore() {
        return applyStore;
    }

    public void setApplyStore(String applyStore) {
        this.applyStore = applyStore == null ? null : applyStore.trim();
    }

    public String getApplyUser() {
        return applyUser;
    }

    public void setApplyUser(String applyUser) {
        this.applyUser = applyUser == null ? null : applyUser.trim();
    }

    public Date getApplyTime() {
        return applyTime;
    }

    public void setApplyTime(Date applyTime) {
        this.applyTime = applyTime;
    }

    public String getApplyAddress() {
        return applyAddress;
    }

    public void setApplyAddress(String applyAddress) {
        this.applyAddress = applyAddress == null ? null : applyAddress.trim();
    }

    public String getApplyContactor() {
        return applyContactor;
    }

    public void setApplyContactor(String applyContactor) {
        this.applyContactor = applyContactor == null ? null : applyContactor.trim();
    }

    public String getApplyPhone() {
        return applyPhone;
    }

    public void setApplyPhone(String applyPhone) {
        this.applyPhone = applyPhone == null ? null : applyPhone.trim();
    }

    public String getApplyPostcode() {
        return applyPostcode;
    }

    public void setApplyPostcode(String applyPostcode) {
        this.applyPostcode = applyPostcode == null ? null : applyPostcode.trim();
    }

    public String getDispatchUser() {
        return dispatchUser;
    }

    public void setDispatchUser(String dispatchUser) {
        this.dispatchUser = dispatchUser == null ? null : dispatchUser.trim();
    }

    public Date getDispatchTime() {
        return dispatchTime;
    }

    public void setDispatchTime(Date dispatchTime) {
        this.dispatchTime = dispatchTime;
    }

    public String getDispatchStore() {
        return dispatchStore;
    }

    public void setDispatchStore(String dispatchStore) {
        this.dispatchStore = dispatchStore == null ? null : dispatchStore.trim();
    }

    public String getRefuseStore() {
        return refuseStore;
    }

    public void setRefuseStore(String refuseStore) {
        this.refuseStore = refuseStore == null ? null : refuseStore.trim();
    }

    public String getRefuseUser() {
        return refuseUser;
    }

    public void setRefuseUser(String refuseUser) {
        this.refuseUser = refuseUser == null ? null : refuseUser.trim();
    }

    public Date getRefuseTime() {
        return refuseTime;
    }

    public void setRefuseTime(Date refuseTime) {
        this.refuseTime = refuseTime;
    }

    public String getAcceptUser() {
        return acceptUser;
    }

    public void setAcceptUser(String acceptUser) {
        this.acceptUser = acceptUser == null ? null : acceptUser.trim();
    }

    public Date getAcceptTime() {
        return acceptTime;
    }

    public void setAcceptTime(Date acceptTime) {
        this.acceptTime = acceptTime;
    }

    public String getDeliveryUser() {
        return deliveryUser;
    }

    public void setDeliveryUser(String deliveryUser) {
        this.deliveryUser = deliveryUser == null ? null : deliveryUser.trim();
    }

    public Date getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(Date deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress == null ? null : deliveryAddress.trim();
    }

    public String getDeliveryContactor() {
        return deliveryContactor;
    }

    public void setDeliveryContactor(String deliveryContactor) {
        this.deliveryContactor = deliveryContactor == null ? null : deliveryContactor.trim();
    }

    public String getDeliveryPhone() {
        return deliveryPhone;
    }

    public void setDeliveryPhone(String deliveryPhone) {
        this.deliveryPhone = deliveryPhone == null ? null : deliveryPhone.trim();
    }

    public String getDeliveryPostcode() {
        return deliveryPostcode;
    }

    public void setDeliveryPostcode(String deliveryPostcode) {
        this.deliveryPostcode = deliveryPostcode == null ? null : deliveryPostcode.trim();
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

    public String getReceiveUser() {
        return receiveUser;
    }

    public void setReceiveUser(String receiveUser) {
        this.receiveUser = receiveUser == null ? null : receiveUser.trim();
    }

    public Date getReceiveTime() {
        return receiveTime;
    }

    public void setReceiveTime(Date receiveTime) {
        this.receiveTime = receiveTime;
    }

    public String getErpSendStatusDelivery() {
        return erpSendStatusDelivery;
    }

    public void setErpSendStatusDelivery(String erpSendStatusDelivery) {
        this.erpSendStatusDelivery = erpSendStatusDelivery == null ? null : erpSendStatusDelivery.trim();
    }

    public String getErpSendStatusReceive() {
        return erpSendStatusReceive;
    }

    public void setErpSendStatusReceive(String erpSendStatusReceive) {
        this.erpSendStatusReceive = erpSendStatusReceive == null ? null : erpSendStatusReceive.trim();
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