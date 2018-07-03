package net.dlyt.qyds.common.dto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by wenxuechao on 16/7/23.
 */
public class OrdReturnExchangeExt extends OrdReturnExchange {

    //分页信息
    private int  sEcho;
    //开始页面
    private int iDisplayStart;

    //每一页显示的项目
    private int iDisplayLength;

    private String rexMemberName;
    //登录用户的id
    private String userId;

    //商品明细订单
    private String detailId;

    //退货商品个数
    private String returnCount;

    //退货状态
    private String rexStatusCode;

    //退换货方式
    private String rexModeCode;

    private BigDecimal amountTotle;
    private BigDecimal amountDiscount;
    private BigDecimal amountCoupon;
    private BigDecimal pointCount;
    private BigDecimal amountPoint;
    private BigDecimal payInfact;
    private Date orderTime;
    private Date payTime;
    private Date deliverTime;
    //订单状态
    private String orderStatus;
    private String payStatus;
    private String deliverStatus;
    private String orderStatusName;
    private String payStatusName;
    private String deliverStatusName;
    private String rexStatusName;
    private String rexTypeName;
    private String rexModeName;
    private String rexPointName;
    private String refundStatusName;

    public BigDecimal getAmountCoupon() {
        return amountCoupon;
    }

    public void setAmountCoupon(BigDecimal amountCoupon) {
        this.amountCoupon = amountCoupon;
    }

    public String getOrderStatusName() {
        return orderStatusName;
    }

    public void setOrderStatusName(String orderStatusName) {
        this.orderStatusName = orderStatusName;
    }

    public String getPayStatusName() {
        return payStatusName;
    }

    public void setPayStatusName(String payStatusName) {
        this.payStatusName = payStatusName;
    }

    public String getDeliverStatusName() {
        return deliverStatusName;
    }

    public void setDeliverStatusName(String deliverStatusName) {
        this.deliverStatusName = deliverStatusName;
    }

    public String getRexStatusName() {
        return rexStatusName;
    }

    public void setRexStatusName(String rexStatusName) {
        this.rexStatusName = rexStatusName;
    }

    public String getRexTypeName() {
        return rexTypeName;
    }

    public void setRexTypeName(String rexTypeName) {
        this.rexTypeName = rexTypeName;
    }

    public String getRexModeName() {
        return rexModeName;
    }

    public void setRexModeName(String rexModeName) {
        this.rexModeName = rexModeName;
    }

    public String getRexPointName() {
        return rexPointName;
    }

    public void setRexPointName(String rexPointName) {
        this.rexPointName = rexPointName;
    }

    public String getRefundStatusName() {
        return refundStatusName;
    }

    public void setRefundStatusName(String refundStatusName) {
        this.refundStatusName = refundStatusName;
    }

    public BigDecimal getAmountTotle() {
        return amountTotle;
    }

    public void setAmountTotle(BigDecimal amountTotle) {
        this.amountTotle = amountTotle;
    }

    public BigDecimal getAmountDiscount() {
        return amountDiscount;
    }

    public void setAmountDiscount(BigDecimal amountDiscount) {
        this.amountDiscount = amountDiscount;
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

    public BigDecimal getPayInfact() {
        return payInfact;
    }

    public void setPayInfact(BigDecimal payInfact) {
        this.payInfact = payInfact;
    }

    public Date getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(Date orderTime) {
        this.orderTime = orderTime;
    }

    public Date getPayTime() {
        return payTime;
    }

    public void setPayTime(Date payTime) {
        this.payTime = payTime;
    }

    public Date getDeliverTime() {
        return deliverTime;
    }

    public void setDeliverTime(Date deliverTime) {
        this.deliverTime = deliverTime;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(String payStatus) {
        this.payStatus = payStatus;
    }

    public String getDeliverStatus() {
        return deliverStatus;
    }

    public void setDeliverStatus(String deliverStatus) {
        this.deliverStatus = deliverStatus;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDetailId() {
        return detailId;
    }

    public void setDetailId(String detailId) {
        this.detailId = detailId;
    }

    public String getReturnCount() {
        return returnCount;
    }

    public void setReturnCount(String returnCount) {
        this.returnCount = returnCount;
    }

    public String getRexStatusCode() {
        return rexStatusCode;
    }

    public void setRexStatusCode(String rexStatusCode) {
        this.rexStatusCode = rexStatusCode;
    }

    public String getRexModeCode() {
        return rexModeCode;
    }

    public void setRexModeCode(String rexModeCode) {
        this.rexModeCode = rexModeCode;
    }

    public int getsEcho() {
        return sEcho;
    }

    public void setsEcho(int sEcho) {
        this.sEcho = sEcho;
    }

    public int getiDisplayLength() {
        return iDisplayLength;
    }

    public void setiDisplayLength(int iDisplayLength) {
        this.iDisplayLength = iDisplayLength;
    }

    public int getiDisplayStart() {
        return iDisplayStart;
    }

    public void setiDisplayStart(int iDisplayStart) {
        this.iDisplayStart = iDisplayStart;
    }

    public String getRexMemberName() {
        return rexMemberName;
    }

    public void setRexMemberName(String rexMemberName) {
        this.rexMemberName = rexMemberName;
    }
}
