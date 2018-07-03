package net.dlyt.qyds.common.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class OrdHistory implements Serializable {
    private String seqOrderId;

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

    private String payReqJson;

    private String payRespJson;

    private String deliverStatus;

    private Date deliverTime;

    private Date receiptTime;

    private String evaluateStatus;

    private Date evaluateTime;

    private String wantInvoice;

    private String invoiceJson;

    private String deliveryJson;

    private String expressJson;

    private String invoiceAccount;

    private String erpStoreid;

    private String storename;

    private String storePhone;

    private String storeDeliveryid;

    private String storeDeliveryname;

    private String canReturn;

    private String canExchange;

    private String canDivide;

    private String cancelType;

    private String insertUserId;

    private Date insertTime;

    public String getSeqOrderId() {
        return seqOrderId;
    }

    public void setSeqOrderId(String seqOrderId) {
        this.seqOrderId = seqOrderId == null ? null : seqOrderId.trim();
    }

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
        this.couponId = couponId;
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

    public String getPayReqJson() {
        return payReqJson;
    }

    public void setPayReqJson(String payReqJson) {
        this.payReqJson = payReqJson == null ? null : payReqJson.trim();
    }

    public String getPayRespJson() {
        return payRespJson;
    }

    public void setPayRespJson(String payRespJson) {
        this.payRespJson = payRespJson == null ? null : payRespJson.trim();
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

    public String getInvoiceJson() {
        return invoiceJson;
    }

    public void setInvoiceJson(String invoiceJson) {
        this.invoiceJson = invoiceJson == null ? null : invoiceJson.trim();
    }

    public String getDeliveryJson() {
        return deliveryJson;
    }

    public void setDeliveryJson(String deliveryJson) {
        this.deliveryJson = deliveryJson == null ? null : deliveryJson.trim();
    }

    public String getExpressJson() {
        return expressJson;
    }

    public void setExpressJson(String expressJson) {
        this.expressJson = expressJson == null ? null : expressJson.trim();
    }

    public String getInvoiceAccount() {
        return invoiceAccount;
    }

    public void setInvoiceAccount(String invoiceAccount) {
        this.invoiceAccount = invoiceAccount == null ? null : invoiceAccount.trim();
    }

    public String getErpStoreid() {
        return erpStoreid;
    }

    public void setErpStoreid(String erpStoreid) {
        this.erpStoreid = erpStoreid == null ? null : erpStoreid.trim();
    }

    public String getStorename() {
        return storename;
    }

    public void setStorename(String storename) {
        this.storename = storename == null ? null : storename.trim();
    }

    public String getStorePhone() {
        return storePhone;
    }

    public void setStorePhone(String storePhone) {
        this.storePhone = storePhone == null ? null : storePhone.trim();
    }

    public String getStoreDeliveryid() {
        return storeDeliveryid;
    }

    public void setStoreDeliveryid(String storeDeliveryid) {
        this.storeDeliveryid = storeDeliveryid == null ? null : storeDeliveryid.trim();
    }

    public String getStoreDeliveryname() {
        return storeDeliveryname;
    }

    public void setStoreDeliveryname(String storeDeliveryname) {
        this.storeDeliveryname = storeDeliveryname == null ? null : storeDeliveryname.trim();
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