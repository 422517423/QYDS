package net.dlyt.qyds.common.dto;

import java.io.Serializable;
import java.util.Date;

public class CouponMember implements Serializable {
    private String couponMemberId;

    private String shopId;

    private String couponId;

    private String memberId;

    private String orderId;

    private String status;

    private Date startTime;

    private Date endTime;

    private Date sendTime;

    private Date usedTime;

    private String deleted;

    private String updateUserId;

    private Date updateTime;

    private String insertUserId;

    private Date insertTime;

    private String erpSendStatus;

    private String erpSendUsedStatus;

    private static final long serialVersionUID = 1L;

    public String getCouponMemberId() {
        return couponMemberId;
    }

    public void setCouponMemberId(String couponMemberId) {
        this.couponMemberId = couponMemberId == null ? null : couponMemberId.trim();
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

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId == null ? null : memberId.trim();
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId == null ? null : orderId.trim();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Date getSendTime() {
        return sendTime;
    }

    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }

    public Date getUsedTime() {
        return usedTime;
    }

    public void setUsedTime(Date usedTime) {
        this.usedTime = usedTime;
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

    public String getErpSendUsedStatus() {
        return erpSendUsedStatus;
    }

    public void setErpSendUsedStatus(String erpSendUsedStatus) {
        this.erpSendUsedStatus = erpSendUsedStatus == null ? null : erpSendUsedStatus.trim();
    }
}