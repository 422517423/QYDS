package net.dlyt.qyds.common.dto.ext;

import net.dlyt.qyds.common.dto.CouponMaster;
import net.dlyt.qyds.common.form.ActGoodsForm;
import net.dlyt.qyds.common.form.CouponGoodsForm;

import java.util.List;

/**
 * Created by cjk on 16/8/6.
 */
public class CouponMasterExt extends CouponMaster {
    private String applyUserName = null;
    private String approveUserName = null;
    private String couponTypeCn = null;
    private String couponTypeEn = null;
    private String approveStatusCn = null;
    private String approveStatusEn = null;
    private String startTimeStr = null;
    private String endTimeStr = null;
    private String sendStartTimeStr = null;
    private String sendEndTimeStr = null;
    private String isGeted = null;
    private String couponMemberId = null;
    private String status = null;
    private String isOverdue = null;
    private List<CouponGoodsForm> goodsList = null;
    private String distributeTypeCn = null;
    private String distributeTypeEn = null;
    private String couponScopeCn = null;
    private String couponScopeEn = null;
    private String couponStyleCn = null;
    private String couponStyleEn = null;

    private String activityGoodsSellYear;

    private String activityGoodsSeasonCode;

    private String activityGoodsErpBrand;

    private String activityGoodsLineCode;

    //分页信息
    private String sEcho;

    //开始页面
    private int iDisplayStart;

    //每一页显示的项目
    private int iDisplayLength;

    private int usedCount;
    private int unuseCount;
    private int allCount;
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

    public String getDistributeTypeCn() {
        return distributeTypeCn;
    }

    public void setDistributeTypeCn(String distributeTypeCn) {
        this.distributeTypeCn = distributeTypeCn;
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

    public String getApproveStatusCn() {
        return approveStatusCn;
    }

    public void setApproveStatusCn(String approveStatusCn) {
        this.approveStatusCn = approveStatusCn;
    }

    public String getApproveStatusEn() {
        return approveStatusEn;
    }

    public void setApproveStatusEn(String approveStatusEn) {
        this.approveStatusEn = approveStatusEn;
    }

    public String getCouponTypeCn() {
        return couponTypeCn;
    }

    public void setCouponTypeCn(String couponTypeCn) {
        this.couponTypeCn = couponTypeCn;
    }

    public String getCouponTypeEn() {
        return couponTypeEn;
    }

    public void setCouponTypeEn(String couponTypeEn) {
        this.couponTypeEn = couponTypeEn;
    }

    public String getIsGeted() {
        return isGeted;
    }

    public void setIsGeted(String isGeted) {
        this.isGeted = isGeted;
    }

    public String getCouponMemberId() {
        return couponMemberId;
    }

    public void setCouponMemberId(String couponMemberId) {
        this.couponMemberId = couponMemberId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIsOverdue() {
        return isOverdue;
    }

    public void setIsOverdue(String isOverdue) {
        this.isOverdue = isOverdue;
    }

    public List<CouponGoodsForm> getGoodsList() {
        return goodsList;
    }

    public void setGoodsList(List<CouponGoodsForm> goodsList) {
        this.goodsList = goodsList;
    }

    public String getSendStartTimeStr() {
        return sendStartTimeStr;
    }

    public void setSendStartTimeStr(String sendStartTimeStr) {
        this.sendStartTimeStr = sendStartTimeStr;
    }

    public String getSendEndTimeStr() {
        return sendEndTimeStr;
    }

    public void setSendEndTimeStr(String sendEndTimeStr) {
        this.sendEndTimeStr = sendEndTimeStr;
    }

    public String getDistributeTypeEn() {
        return distributeTypeEn;
    }

    public void setDistributeTypeEn(String distributeTypeEn) {
        this.distributeTypeEn = distributeTypeEn;
    }

    public String getCouponScopeCn() {
        return couponScopeCn;
    }

    public void setCouponScopeCn(String couponScopeCn) {
        this.couponScopeCn = couponScopeCn;
    }

    public String getCouponScopeEn() {
        return couponScopeEn;
    }

    public void setCouponScopeEn(String couponScopeEn) {
        this.couponScopeEn = couponScopeEn;
    }

    public String getCouponStyleCn() {
        return couponStyleCn;
    }

    public void setCouponStyleCn(String couponStyleCn) {
        this.couponStyleCn = couponStyleCn;
    }

    public String getCouponStyleEn() {
        return couponStyleEn;
    }

    public void setCouponStyleEn(String couponStyleEn) {
        this.couponStyleEn = couponStyleEn;
    }

    public int getUsedCount() {
        return usedCount;
    }

    public void setUsedCount(int usedCount) {
        this.usedCount = usedCount;
    }

    public int getUnuseCount() {
        return unuseCount;
    }

    public void setUnuseCount(int unuseCount) {
        this.unuseCount = unuseCount;
    }

    public int getAllCount() {
        return allCount;
    }

    public void setAllCount(int allCount) {
        this.allCount = allCount;
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
