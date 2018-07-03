package net.dlyt.qyds.common.dto;

import java.io.Serializable;
import java.util.Date;

public class ComSetting implements Serializable {
    private static final long serialVersionUID = 1L;
    private String comSettingId;
    private String firstBuyActivity;
    private String firstBuyCoupon;
    private String deleted;
    private String updateUserId;
    private Date updateTime;
    private String insertUserId;
    private Date insertTime;

    public String getComSettingId() {
        return comSettingId;
    }

    public void setComSettingId(String comSettingId) {
        this.comSettingId = comSettingId == null ? null : comSettingId.trim();
    }

    public String getFirstBuyActivity() {
        return firstBuyActivity;
    }

    public void setFirstBuyActivity(String firstBuyActivity) {
        this.firstBuyActivity = firstBuyActivity == null ? null : firstBuyActivity.trim();
    }

    public String getFirstBuyCoupon() {
        return firstBuyCoupon;
    }

    public void setFirstBuyCoupon(String firstBuyCoupon) {
        this.firstBuyCoupon = firstBuyCoupon == null ? null : firstBuyCoupon.trim();
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
}