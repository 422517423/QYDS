package net.dlyt.qyds.common.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class OrdReturnExchange implements Serializable {
    private String rexOrderId;

    private String orderId;

    private String subOrderId;

    private String orderCode;

    private String memberId;

    private String shopId;

    private String rexType;

    private String rexMode;

    private String rexPoint;

    private String rexStoreId;

    private String rexStoreName;

    private String rexStatus;

    private String rexMemberId;

    private Date applyTime;

    private String applyComment;

    private String applyAnswerUserId;

    private Date applyAnswerTime;

    private String applyAnswerComment;

    private String returnGoodsMemberid;

    private Date returnGoodsTime;

    private String returnExpressId;

    private String returnExpressName;

    private String returnExpressNo;

    private String returnDeliveryFee;

    private String returnPayDelivery;

    private String returnAcceptUserId;

    private Date returnAcceptTime;

    private String rebackExpressId;

    private String rebackExpressName;

    private String rebackExpressNo;

    private String rebackDeliveryFee;

    private String rebackPayDelivery;

    private String rebackAcceptMemberId;

    private Date rebackAcceptTime;

    private String refundStatus;

    private BigDecimal refundGoods;

    private BigDecimal deliveryFee;

    private BigDecimal refundInfact;

    private String payAccount;

    private String receiptAccount;

    private String refundMemberId;

    private Date refundTime;

    private String deleted;

    private String updateUserId;

    private Date updateTime;

    private String insertUserId;

    private Date insertTime;

    private String erpSendStatus;

    private static final long serialVersionUID = 1L;

    public String getRexOrderId() {
        return rexOrderId;
    }

    public void setRexOrderId(String rexOrderId) {
        this.rexOrderId = rexOrderId == null ? null : rexOrderId.trim();
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId == null ? null : orderId.trim();
    }

    public String getSubOrderId() {
        return subOrderId;
    }

    public void setSubOrderId(String subOrderId) {
        this.subOrderId = subOrderId == null ? null : subOrderId.trim();
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode == null ? null : orderCode.trim();
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

    public String getRexType() {
        return rexType;
    }

    public void setRexType(String rexType) {
        this.rexType = rexType == null ? null : rexType.trim();
    }

    public String getRexMode() {
        return rexMode;
    }

    public void setRexMode(String rexMode) {
        this.rexMode = rexMode == null ? null : rexMode.trim();
    }

    public String getRexPoint() {
        return rexPoint;
    }

    public void setRexPoint(String rexPoint) {
        this.rexPoint = rexPoint == null ? null : rexPoint.trim();
    }

    public String getRexStoreId() {
        return rexStoreId;
    }

    public void setRexStoreId(String rexStoreId) {
        this.rexStoreId = rexStoreId == null ? null : rexStoreId.trim();
    }

    public String getRexStoreName() {
        return rexStoreName;
    }

    public void setRexStoreName(String rexStoreName) {
        this.rexStoreName = rexStoreName == null ? null : rexStoreName.trim();
    }

    public String getRexStatus() {
        return rexStatus;
    }

    public void setRexStatus(String rexStatus) {
        this.rexStatus = rexStatus == null ? null : rexStatus.trim();
    }

    public String getRexMemberId() {
        return rexMemberId;
    }

    public void setRexMemberId(String rexMemberId) {
        this.rexMemberId = rexMemberId == null ? null : rexMemberId.trim();
    }

    public Date getApplyTime() {
        return applyTime;
    }

    public void setApplyTime(Date applyTime) {
        this.applyTime = applyTime;
    }

    public String getApplyComment() {
        return applyComment;
    }

    public void setApplyComment(String applyComment) {
        this.applyComment = applyComment == null ? null : applyComment.trim();
    }

    public String getApplyAnswerUserId() {
        return applyAnswerUserId;
    }

    public void setApplyAnswerUserId(String applyAnswerUserId) {
        this.applyAnswerUserId = applyAnswerUserId == null ? null : applyAnswerUserId.trim();
    }

    public Date getApplyAnswerTime() {
        return applyAnswerTime;
    }

    public void setApplyAnswerTime(Date applyAnswerTime) {
        this.applyAnswerTime = applyAnswerTime;
    }

    public String getApplyAnswerComment() {
        return applyAnswerComment;
    }

    public void setApplyAnswerComment(String applyAnswerComment) {
        this.applyAnswerComment = applyAnswerComment == null ? null : applyAnswerComment.trim();
    }

    public String getReturnGoodsMemberid() {
        return returnGoodsMemberid;
    }

    public void setReturnGoodsMemberid(String returnGoodsMemberid) {
        this.returnGoodsMemberid = returnGoodsMemberid == null ? null : returnGoodsMemberid.trim();
    }

    public Date getReturnGoodsTime() {
        return returnGoodsTime;
    }

    public void setReturnGoodsTime(Date returnGoodsTime) {
        this.returnGoodsTime = returnGoodsTime;
    }

    public String getReturnExpressId() {
        return returnExpressId;
    }

    public void setReturnExpressId(String returnExpressId) {
        this.returnExpressId = returnExpressId == null ? null : returnExpressId.trim();
    }

    public String getReturnExpressName() {
        return returnExpressName;
    }

    public void setReturnExpressName(String returnExpressName) {
        this.returnExpressName = returnExpressName == null ? null : returnExpressName.trim();
    }

    public String getReturnExpressNo() {
        return returnExpressNo;
    }

    public void setReturnExpressNo(String returnExpressNo) {
        this.returnExpressNo = returnExpressNo == null ? null : returnExpressNo.trim();
    }

    public String getReturnDeliveryFee() {
        return returnDeliveryFee;
    }

    public void setReturnDeliveryFee(String returnDeliveryFee) {
        this.returnDeliveryFee = returnDeliveryFee == null ? null : returnDeliveryFee.trim();
    }

    public String getReturnPayDelivery() {
        return returnPayDelivery;
    }

    public void setReturnPayDelivery(String returnPayDelivery) {
        this.returnPayDelivery = returnPayDelivery == null ? null : returnPayDelivery.trim();
    }

    public String getReturnAcceptUserId() {
        return returnAcceptUserId;
    }

    public void setReturnAcceptUserId(String returnAcceptUserId) {
        this.returnAcceptUserId = returnAcceptUserId == null ? null : returnAcceptUserId.trim();
    }

    public Date getReturnAcceptTime() {
        return returnAcceptTime;
    }

    public void setReturnAcceptTime(Date returnAcceptTime) {
        this.returnAcceptTime = returnAcceptTime;
    }

    public String getRebackExpressId() {
        return rebackExpressId;
    }

    public void setRebackExpressId(String rebackExpressId) {
        this.rebackExpressId = rebackExpressId == null ? null : rebackExpressId.trim();
    }

    public String getRebackExpressName() {
        return rebackExpressName;
    }

    public void setRebackExpressName(String rebackExpressName) {
        this.rebackExpressName = rebackExpressName == null ? null : rebackExpressName.trim();
    }

    public String getRebackExpressNo() {
        return rebackExpressNo;
    }

    public void setRebackExpressNo(String rebackExpressNo) {
        this.rebackExpressNo = rebackExpressNo == null ? null : rebackExpressNo.trim();
    }

    public String getRebackDeliveryFee() {
        return rebackDeliveryFee;
    }

    public void setRebackDeliveryFee(String rebackDeliveryFee) {
        this.rebackDeliveryFee = rebackDeliveryFee == null ? null : rebackDeliveryFee.trim();
    }

    public String getRebackPayDelivery() {
        return rebackPayDelivery;
    }

    public void setRebackPayDelivery(String rebackPayDelivery) {
        this.rebackPayDelivery = rebackPayDelivery == null ? null : rebackPayDelivery.trim();
    }

    public String getRebackAcceptMemberId() {
        return rebackAcceptMemberId;
    }

    public void setRebackAcceptMemberId(String rebackAcceptMemberId) {
        this.rebackAcceptMemberId = rebackAcceptMemberId == null ? null : rebackAcceptMemberId.trim();
    }

    public Date getRebackAcceptTime() {
        return rebackAcceptTime;
    }

    public void setRebackAcceptTime(Date rebackAcceptTime) {
        this.rebackAcceptTime = rebackAcceptTime;
    }

    public String getRefundStatus() {
        return refundStatus;
    }

    public void setRefundStatus(String refundStatus) {
        this.refundStatus = refundStatus == null ? null : refundStatus.trim();
    }

    public BigDecimal getRefundGoods() {
        return refundGoods;
    }

    public void setRefundGoods(BigDecimal refundGoods) {
        this.refundGoods = refundGoods;
    }

    public BigDecimal getDeliveryFee() {
        return deliveryFee;
    }

    public void setDeliveryFee(BigDecimal deliveryFee) {
        this.deliveryFee = deliveryFee;
    }

    public BigDecimal getRefundInfact() {
        return refundInfact;
    }

    public void setRefundInfact(BigDecimal refundInfact) {
        this.refundInfact = refundInfact;
    }

    public String getPayAccount() {
        return payAccount;
    }

    public void setPayAccount(String payAccount) {
        this.payAccount = payAccount == null ? null : payAccount.trim();
    }

    public String getReceiptAccount() {
        return receiptAccount;
    }

    public void setReceiptAccount(String receiptAccount) {
        this.receiptAccount = receiptAccount == null ? null : receiptAccount.trim();
    }

    public String getRefundMemberId() {
        return refundMemberId;
    }

    public void setRefundMemberId(String refundMemberId) {
        this.refundMemberId = refundMemberId == null ? null : refundMemberId.trim();
    }

    public Date getRefundTime() {
        return refundTime;
    }

    public void setRefundTime(Date refundTime) {
        this.refundTime = refundTime;
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