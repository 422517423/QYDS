package net.dlyt.qyds.common.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class PrizeDrawConfig implements Serializable {
    private static final long serialVersionUID = 1L;
    private String prizeDrawId;
    private String exchangeFlag;
    private BigDecimal exchangePoint;
    private String isLogin;
    private String isOrder;
    private BigDecimal orderAmount;

    public String getPrizeDrawId() {
        return prizeDrawId;
    }

    public void setPrizeDrawId(String prizeDrawId) {
        this.prizeDrawId = prizeDrawId == null ? null : prizeDrawId.trim();
    }

    public String getExchangeFlag() {
        return exchangeFlag;
    }

    public void setExchangeFlag(String exchangeFlag) {
        this.exchangeFlag = exchangeFlag == null ? null : exchangeFlag.trim();
    }

    public BigDecimal getExchangePoint() {
        return exchangePoint;
    }

    public void setExchangePoint(BigDecimal exchangePoint) {
        this.exchangePoint = exchangePoint;
    }

    public String getIsLogin() {
        return isLogin;
    }

    public void setIsLogin(String isLogin) {
        this.isLogin = isLogin == null ? null : isLogin.trim();
    }

    public String getIsOrder() {
        return isOrder;
    }

    public void setIsOrder(String isOrder) {
        this.isOrder = isOrder == null ? null : isOrder.trim();
    }

    public BigDecimal getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(BigDecimal orderAmount) {
        this.orderAmount = orderAmount;
    }
}