package net.dlyt.qyds.common.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class OrdLogisticStatus implements Serializable {
    private static final long serialVersionUID = 1L;
    // 物流状态id
    private String ordLogisticId;
    // 订单id
    private String orderId;
    // 物流公司ID（YTO）
    private String logisticProviderId;
    // vip客户标识(客户编号)
    private String clientId;
    // 运单号
    private String mailNo;
    // 物流号
    private String txLogisticId;
    // 通知类型STATUS：物流状态
    private String infoType;
    // ACCEPT 接单 UNACCEPT 不接单GOT 已收件 NOT_SEND 揽收失败 ARRIVAL 已收入 DEPARTURE 已发出 PACKAGE 已打包 UNPACK 已拆包 SENT_SCAN 派件 SIGNED 签收成功 FAILED 签收失败
    private String infoContent;
    // 备注或失败原因（值为中文原因或备注）
    private String remark;
    // 揽收重量
    private String weight;
    // 签收人
    private String signedName;
    // 该状态操作人员，签收、派送、揽件
    private String deliveryName;
    // 事件发生时间
    private Date acceptTime;
    // 联系方式（包括手机，电话等）
    private String contactInfo;
    // erp发送状态(10发送成功 20发送失败)
    private String erpSendStatus;
    // erp发送更新时间
    private Date updateTime;

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
    public String getErpSendStatus() {
        return erpSendStatus;
    }

    public void setErpSendStatus(String erpSendStatus) {
        this.erpSendStatus = erpSendStatus;
    }
    public String getOrdLogisticId() {
        return ordLogisticId;
    }

    public void setOrdLogisticId(String ordLogisticId) {
        this.ordLogisticId = ordLogisticId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getLogisticProviderId() {
        return logisticProviderId;
    }

    public void setLogisticProviderId(String logisticProviderId) {
        this.logisticProviderId = logisticProviderId;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getMailNo() {
        return mailNo;
    }

    public void setMailNo(String mailNo) {
        this.mailNo = mailNo;
    }

    public String getTxLogisticId() {
        return txLogisticId;
    }

    public void setTxLogisticId(String txLogisticId) {
        this.txLogisticId = txLogisticId;
    }

    public String getInfoType() {
        return infoType;
    }

    public void setInfoType(String infoType) {
        this.infoType = infoType;
    }

    public String getInfoContent() {
        return infoContent;
    }

    public void setInfoContent(String infoContent) {
        this.infoContent = infoContent;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getSignedName() {
        return signedName;
    }

    public void setSignedName(String signedName) {
        this.signedName = signedName;
    }

    public String getDeliveryName() {
        return deliveryName;
    }

    public void setDeliveryName(String deliveryName) {
        this.deliveryName = deliveryName;
    }

    public Date getAcceptTime() {
        return acceptTime;
    }

    public void setAcceptTime(Date acceptTime) {
        this.acceptTime = acceptTime;
    }

    public String getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }
}