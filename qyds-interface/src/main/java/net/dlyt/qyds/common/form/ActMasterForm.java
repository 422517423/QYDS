package net.dlyt.qyds.common.form;

import net.dlyt.qyds.common.dto.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by cjk on 16/8/1.
 */
public class ActMasterForm extends ActMaster {
    private String applyUserName = null;
    private String approveUserName = null;
    private String approveStatusCn = null;
    private String approveStatusEn = null;
    private String actitionTypeCn = null;
    private String actitionTypeEn = null;
    private String activityType = null;
    private String sendCouponId = null;
    private int sendPoint;
    private List<GdsMasterExt> sendGoodsList = null;
    private String sendGoodsId = null;
    private List<ActGoodsForm> goodsList = null;
    private List<ActMemberForm> memberList = null;
    private List<ActMasterForm> subActivityList = null;
    private String startTimeStr = null;
    private String endTimeStr = null;

    private String startDateTimeStr = null;
    private String endDateTimeStr = null;

    private String actitionType = null;

    private float originPrice;

    private float newPrice;

    private int point = 0;

    private float cutPrice;

    //分页信息
    private String sEcho;

    //开始页面
    private int iDisplayStart;

    //每一页显示的项目
    private int iDisplayLength;

    private String tempName;

    // 检索条件用活动ID
    private  String searchIds;

    private int hour;
    private int minute;

    private String activityGoodsSellYear;

    private String activityGoodsSeasonCode;

    private String activityGoodsErpBrand;

    private String activityGoodsLineCode;

    // 20180103
    private  String paramValue;

    public String getParamValue() {
        return paramValue;
    }

    public void setParamValue(String paramValue) {
        this.paramValue = paramValue;
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

    public String getsEcho() {
        return sEcho;
    }

    public void setsEcho(String sEcho) {
        this.sEcho = sEcho;
    }


    public String getApproveStatusEn() {
        return approveStatusEn;
    }

    public void setApproveStatusEn(String approveStatusEn) {
        this.approveStatusEn = approveStatusEn;
    }


    public String getApproveStatusCn() {
        return approveStatusCn;
    }

    public void setApproveStatusCn(String approveStatusCn) {
        this.approveStatusCn = approveStatusCn;
    }

    public String getApplyUserName() {
        return applyUserName;
    }

    public void setApplyUserName(String applyUserName) {
        this.applyUserName = applyUserName;
    }

    public String getApproveUserName() {
        return approveUserName;
    }

    public void setApproveUserName(String approveUserName) {
        this.approveUserName = approveUserName;
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

    public String getActitionType() {
        return actitionType;
    }

    public void setActitionType(String actitionType) {
        this.actitionType = actitionType;
    }

    public String getActitionTypeCn() {
        return actitionTypeCn;
    }

    public void setActitionTypeCn(String actitionTypeCn) {
        this.actitionTypeCn = actitionTypeCn;
    }

    public String getActitionTypeEn() {
        return actitionTypeEn;
    }

    public void setActitionTypeEn(String actitionTypeEn) {
        this.actitionTypeEn = actitionTypeEn;
    }

    public List<ActGoodsForm> getGoodsList() {
        return goodsList;
    }

    public void setGoodsList(List<ActGoodsForm> goodsList) {
        this.goodsList = goodsList;
    }

    public List<ActMemberForm> getMemberList() {
        return memberList;
    }

    public void setMemberList(List<ActMemberForm> memberList) {
        this.memberList = memberList;
    }

    public float getOriginPrice() {
        return originPrice;
    }

    public void setOriginPrice(float originPrice) {
        this.originPrice = originPrice;
    }

    public float getNewPrice() {
        return newPrice;
    }

    public void setNewPrice(float newPrice) {
        this.newPrice = newPrice;
    }

    public String getActivityType() {
        return activityType;
    }

    public void setActivityType(String activityType) {
        this.activityType = activityType;
    }

    public String getSendCouponId() {
        return sendCouponId;
    }

    public void setSendCouponId(String sendCouponId) {
        this.sendCouponId = sendCouponId;
    }

    public float getCutPrice() {
        return cutPrice;
    }

    public void setCutPrice(float cutPrice) {
        this.cutPrice = cutPrice;
    }

    public List<ActMasterForm> getSubActivityList() {
        return subActivityList;
    }

    public void setSubActivityList(List<ActMasterForm> subActivityList) {
        this.subActivityList = subActivityList;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public String getSendGoodsId() {
        return sendGoodsId;
    }

    public void setSendGoodsId(String sendGoodsId) {
        this.sendGoodsId = sendGoodsId;
    }

    public List<GdsMasterExt> getSendGoodsList() {
        return sendGoodsList;
    }

    public void setSendGoodsList(List<GdsMasterExt> sendGoodsList) {
        this.sendGoodsList = sendGoodsList;
    }

    @Override
    public String getTempName() {
        return tempName;
    }

    @Override
    public void setTempName(String tempName) {
        this.tempName = tempName;
    }

    public int getSendPoint() {
        return sendPoint;
    }

    public void setSendPoint(int sendPoint) {
        this.sendPoint = sendPoint;
    }

    public String getStartDateTimeStr() {
        return startDateTimeStr;
    }

    public void setStartDateTimeStr(String startDateTimeStr) {
        this.startDateTimeStr = startDateTimeStr;
    }

    public String getEndDateTimeStr() {
        return endDateTimeStr;
    }

    public void setEndDateTimeStr(String endDateTimeStr) {
        this.endDateTimeStr = endDateTimeStr;
    }

    public String getSearchIds() {
        return searchIds;
    }

    public void setSearchIds(String searchIds) {
        this.searchIds = searchIds;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public String getActivityGoodsSellYear() {
        return activityGoodsSellYear;
    }

    public void setActivityGoodsSellYear(String activityGoodsSellYear) {
        this.activityGoodsSellYear = activityGoodsSellYear;
    }

    public String getActivityGoodsSeasonCode() {
        return activityGoodsSeasonCode;
    }

    public void setActivityGoodsSeasonCode(String activityGoodsSeasonCode) {
        this.activityGoodsSeasonCode = activityGoodsSeasonCode;
    }

    public String getActivityGoodsErpBrand() {
        return activityGoodsErpBrand;
    }

    public void setActivityGoodsErpBrand(String activityGoodsErpBrand) {
        this.activityGoodsErpBrand = activityGoodsErpBrand;
    }

    public String getActivityGoodsLineCode() {
        return activityGoodsLineCode;
    }

    public void setActivityGoodsLineCode(String activityGoodsLineCode) {
        this.activityGoodsLineCode = activityGoodsLineCode;
    }
}
