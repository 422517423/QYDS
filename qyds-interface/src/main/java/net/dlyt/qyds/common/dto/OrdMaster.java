package net.dlyt.qyds.common.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class OrdMaster implements Serializable {
    private String orderId;

    private String orderCode;

    private String orderType;

    private String memberId;

    private String shopId;

    private BigDecimal amountTotle;

    private String actionId;

    private String actionName;

    private BigDecimal amountDiscount;

    private String couponId;

    private BigDecimal amountCoupon;

    private BigDecimal pointCount;

    private BigDecimal amountPoint;

    private String deliveryFree;

    private BigDecimal deliveryFee;

    private String payDeliveryType;

    private BigDecimal payInfact;

    private BigDecimal serviceFee;

    private BigDecimal amountShop;

    private String payType;

    private String payAccount;

    private BigDecimal amount;

    private String receiptAccount;

    private String message;

    private String orderStatus;

    private Date orderTime;

    private String payStatus;

    private Date payTime;

    private String deliverStatus;

    private Date deliverTime;

    private Date receiptTime;

    private String evaluateStatus;

    private Date evaluateTime;

    private String wantInvoice;

    private String invoiceType;

    private String invoiceTitle;

    private String invoiceAddress;

    private String invoiceTel;

    private String invoiceTaxno;

    private String invoiceBank;

    private String invoiceAccount;

    private String invoiceNo;

    private String districtidProvince;

    private String districtidCity;

    private String districtidDistrict;

    private String deliveryAddress;

    private String deliveryContactor;

    private String deliveryPhone;

    private String deliveryPostcode;

    private String expressId;

    private String expressName;

    private String expressNo;

    private String deliverType;

    private String erpStoreId;

    private String storeName;

    private String storePhone;

    private String storeDeliveryId;

    private String storeDeliveryName;

    private String canReturn;

    private String canExchange;

    private String canDivide;

    private String cancelType;

    private String comment;

    private String deleted;

    private String updateUserId;

    private Date updateTime;

    private String insertUserId;

    private Date insertTime;

    private String erpSendStatus;

    private Integer exchangePointCount;

    private String dispatchStatus;

    private String tradeStatus;

    private String helpBuy;

    public String getHelpBuy() {
        return helpBuy;
    }

    public void setHelpBuy(String helpBuy) {
        this.helpBuy = helpBuy == null ? null : helpBuy.trim();
    }

    private static final long serialVersionUID = 1L;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId == null ? null : orderId.trim();
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode == null ? null : orderCode.trim();
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType == null ? null : orderType.trim();
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId == null ? null : memberId.trim();
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId == null ? null : shopId.trim();
    }

    public BigDecimal getAmountTotle() {
        return amountTotle;
    }

    public void setAmountTotle(BigDecimal amountTotle) {
        this.amountTotle = amountTotle;
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

    public BigDecimal getAmountDiscount() {
        return amountDiscount;
    }

    public void setAmountDiscount(BigDecimal amountDiscount) {
        this.amountDiscount = amountDiscount;
    }

    public String getCouponId() {
        return couponId;
    }

    public void setCouponId(String couponId) {
        this.couponId = couponId == null ? null : couponId.trim();
    }

    public BigDecimal getAmountCoupon() {
        return amountCoupon;
    }

    public void setAmountCoupon(BigDecimal amountCoupon) {
        this.amountCoupon = amountCoupon;
    }

    public BigDecimal getPointCount() {
        return pointCount;
    }

    public void setPointCount(BigDecimal pointCount) {
        this.pointCount = pointCount;
    }

    public BigDecimal getAmountPoint() {
        return amountPoint;
    }

    public void setAmountPoint(BigDecimal amountPoint) {
        this.amountPoint = amountPoint;
    }

    public String getDeliveryFree() {
        return deliveryFree;
    }

    public void setDeliveryFree(String deliveryFree) {
        this.deliveryFree = deliveryFree == null ? null : deliveryFree.trim();
    }

    public BigDecimal getDeliveryFee() {
        return deliveryFee;
    }

    public void setDeliveryFee(BigDecimal deliveryFee) {
        this.deliveryFee = deliveryFee;
    }

    public String getPayDeliveryType() {
        return payDeliveryType;
    }

    public void setPayDeliveryType(String payDeliveryType) {
        this.payDeliveryType = payDeliveryType == null ? null : payDeliveryType.trim();
    }

    public BigDecimal getPayInfact() {
        return payInfact;
    }

    public void setPayInfact(BigDecimal payInfact) {
        this.payInfact = payInfact;
    }

    public BigDecimal getServiceFee() {
        return serviceFee;
    }

    public void setServiceFee(BigDecimal serviceFee) {
        this.serviceFee = serviceFee;
    }

    public BigDecimal getAmountShop() {
        return amountShop;
    }

    public void setAmountShop(BigDecimal amountShop) {
        this.amountShop = amountShop;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType == null ? null : payType.trim();
    }

    public String getPayAccount() {
        return payAccount;
    }

    public void setPayAccount(String payAccount) {
        this.payAccount = payAccount == null ? null : payAccount.trim();
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getReceiptAccount() {
        return receiptAccount;
    }

    public void setReceiptAccount(String receiptAccount) {
        this.receiptAccount = receiptAccount == null ? null : receiptAccount.trim();
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message == null ? null : message.trim();
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus == null ? null : orderStatus.trim();
    }

    public Date getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(Date orderTime) {
        this.orderTime = orderTime;
    }

    public String getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(String payStatus) {
        this.payStatus = payStatus == null ? null : payStatus.trim();
    }

    public Date getPayTime() {
        return payTime;
    }

    public void setPayTime(Date payTime) {
        this.payTime = payTime;
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

    public Date getReceiptTime() {
        return receiptTime;
    }

    public void setReceiptTime(Date receiptTime) {
        this.receiptTime = receiptTime;
    }

    public String getEvaluateStatus() {
        return evaluateStatus;
    }

    public void setEvaluateStatus(String evaluateStatus) {
        this.evaluateStatus = evaluateStatus == null ? null : evaluateStatus.trim();
    }

    public Date getEvaluateTime() {
        return evaluateTime;
    }

    public void setEvaluateTime(Date evaluateTime) {
        this.evaluateTime = evaluateTime;
    }

    public String getWantInvoice() {
        return wantInvoice;
    }

    public void setWantInvoice(String wantInvoice) {
        this.wantInvoice = wantInvoice == null ? null : wantInvoice.trim();
    }

    public String getInvoiceType() {
        return invoiceType;
    }

    public void setInvoiceType(String invoiceType) {
        this.invoiceType = invoiceType == null ? null : invoiceType.trim();
    }

    public String getInvoiceTitle() {
        return invoiceTitle;
    }

    public void setInvoiceTitle(String invoiceTitle) {
        this.invoiceTitle = invoiceTitle == null ? null : invoiceTitle.trim();
    }

    public String getInvoiceAddress() {
        return invoiceAddress;
    }

    public void setInvoiceAddress(String invoiceAddress) {
        this.invoiceAddress = invoiceAddress == null ? null : invoiceAddress.trim();
    }

    public String getInvoiceTel() {
        return invoiceTel;
    }

    public void setInvoiceTel(String invoiceTel) {
        this.invoiceTel = invoiceTel == null ? null : invoiceTel.trim();
    }

    public String getInvoiceTaxno() {
        return invoiceTaxno;
    }

    public void setInvoiceTaxno(String invoiceTaxno) {
        this.invoiceTaxno = invoiceTaxno == null ? null : invoiceTaxno.trim();
    }

    public String getInvoiceBank() {
        return invoiceBank;
    }

    public void setInvoiceBank(String invoiceBank) {
        this.invoiceBank = invoiceBank == null ? null : invoiceBank.trim();
    }

    public String getInvoiceAccount() {
        return invoiceAccount;
    }

    public void setInvoiceAccount(String invoiceAccount) {
        this.invoiceAccount = invoiceAccount == null ? null : invoiceAccount.trim();
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo == null ? null : invoiceNo.trim();
    }

    public String getDistrictidProvince() {
        return districtidProvince;
    }

    public void setDistrictidProvince(String districtidProvince) {
        this.districtidProvince = districtidProvince == null ? null : districtidProvince.trim();
    }

    public String getDistrictidCity() {
        return districtidCity;
    }

    public void setDistrictidCity(String districtidCity) {
        this.districtidCity = districtidCity == null ? null : districtidCity.trim();
    }

    public String getDistrictidDistrict() {
        return districtidDistrict;
    }

    public void setDistrictidDistrict(String districtidDistrict) {
        this.districtidDistrict = districtidDistrict == null ? null : districtidDistrict.trim();
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

    public String getCanDivide() {
        return canDivide;
    }

    public void setCanDivide(String canDivide) {
        this.canDivide = canDivide == null ? null : canDivide.trim();
    }

    public String getCancelType() {
        return cancelType;
    }

    public void setCancelType(String cancelType) {
        this.cancelType = cancelType == null ? null : cancelType.trim();
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

    public Integer getExchangePointCount() {
        return exchangePointCount;
    }

    public void setExchangePointCount(Integer exchangePointCount) {
        this.exchangePointCount = exchangePointCount;
    }

    public String getDispatchStatus() {
        return dispatchStatus;
    }

    public void setDispatchStatus(String dispatchStatus) {
        this.dispatchStatus = dispatchStatus == null ? null : dispatchStatus.trim();
    }

    public String getTradeStatus() {
        return tradeStatus;
    }

    public void setTradeStatus(String tradeStatus) {
        this.tradeStatus = tradeStatus == null ? null : tradeStatus.trim();
    }
}