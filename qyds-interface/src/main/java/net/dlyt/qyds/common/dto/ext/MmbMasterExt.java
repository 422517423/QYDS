package net.dlyt.qyds.common.dto.ext;

import net.dlyt.qyds.common.dto.MmbMaster;

/**
 * Created by C_Nagai on 2016/7/27.
 */
public class MmbMasterExt extends MmbMaster {

    //需要多少行
    private int needColumns;

    //起点位置
    private int startPoint;

    //总行数
    private int count;

    // 会员类型
    private String typeName;

    // 性别
    private String sexName;

    // 会员级别
    private String memberLevelName;

    // 推荐人
    private String referrerName;

    // 创建者
    private String insertUserName;

    private String birthdayStart;

    private String serverId;
    //开始日期
    private String startDate;
    //结束日期
    private String endDate;

    //是否是总部内购组成员
    private String isSaller;


    //操作员ID
    private String operate;
    //商户ID
    private String storeid;
    //子商户ID
    private String storesubid;

    public String getIsSaller() {
        return isSaller;
    }

    public void setIsSaller(String isSaller) {
        this.isSaller = isSaller;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public int getNeedColumns() {
        return needColumns;
    }

    public void setNeedColumns(int needColumns) {
        this.needColumns = needColumns;
    }

    public int getStartPoint() {
        return startPoint;
    }

    public void setStartPoint(int startPoint) {
        this.startPoint = startPoint;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getSexName() {
        return sexName;
    }

    public void setSexName(String sexName) {
        this.sexName = sexName;
    }

    public String getMemberLevelName() {
        return memberLevelName;
    }

    public void setMemberLevelName(String memberLevelName) {
        this.memberLevelName = memberLevelName;
    }

    public String getInsertUserName() {
        return insertUserName;
    }

    public void setInsertUserName(String insertUserName) {
        this.insertUserName = insertUserName;
    }

    public String getReferrerName() {
        return referrerName;
    }

    public void setReferrerName(String referrerName) {
        this.referrerName = referrerName;
    }

    public String getBirthdayStart() {
        return birthdayStart;
    }

    public void setBirthdayStart(String birthdayStart) {
        this.birthdayStart = birthdayStart;
    }

    public String getServerId() {
        return serverId;
    }

    public void setServerId(String serverId) {
        this.serverId = serverId;
    }

    public String getOperate() {
        return operate;
    }

    public void setOperate(String operate) {
        this.operate = operate;
    }

    public String getStoreid() {
        return storeid;
    }

    public void setStoreid(String storeid) {
        this.storeid = storeid;
    }

    public String getStoresubid() {
        return storesubid;
    }

    public void setStoresubid(String storesubid) {
        this.storesubid = storesubid;
    }
}
