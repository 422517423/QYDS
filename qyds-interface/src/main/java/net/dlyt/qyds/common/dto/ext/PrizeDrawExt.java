package net.dlyt.qyds.common.dto.ext;

import net.dlyt.qyds.common.dto.PrizeDraw;
import net.dlyt.qyds.common.dto.PrizeDrawConfig;
import net.dlyt.qyds.common.dto.PrizeGoods;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by cjk on 2016/12/15.
 */
public class PrizeDrawExt extends PrizeDraw{

    private List<PrizeGoods> prizeGoodsList = null;

    private String startTimeStr = null;

    private String endTimeStr = null;

    private String userId = null;

    //分页信息
    private String sEcho;

    //开始页面
    private int iDisplayStart;

    //每一页显示的项目
    private int iDisplayLength;

    //当前时间
    private Date currentTime;
    private String exchangeFlag;
    private BigDecimal exchangePoint;
    private String isLogin;
    private String isOrder;
    private BigDecimal orderAmount;

    public String getsEcho() {
        return sEcho;
    }

    public void setsEcho(String sEcho) {
        this.sEcho = sEcho;
    }

    public int getiDisplayStart() {
        return iDisplayStart;
    }

    public void setiDisplayStart(int iDisplayStart) {
        this.iDisplayStart = iDisplayStart;
    }

    public int getiDisplayLength() {
        return iDisplayLength;
    }

    public void setiDisplayLength(int iDisplayLength) {
        this.iDisplayLength = iDisplayLength;
    }

    public List<PrizeGoods> getPrizeGoodsList() {
        return prizeGoodsList;
    }

    public void setPrizeGoodsList(List<PrizeGoods> prizeGoodsList) {
        this.prizeGoodsList = prizeGoodsList;
    }

    public String getStartTimeStr() {
        return startTimeStr;
    }

    public void setStartTimeStr(String startTimeStr) {
        this.startTimeStr = startTimeStr;
    }

    public String getEndTimeStr() {
        return endTimeStr;
    }

    public void setEndTimeStr(String endTimeStr) {
        this.endTimeStr = endTimeStr;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public Date getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(Date currentTime) {
        this.currentTime = currentTime;
    }
}
