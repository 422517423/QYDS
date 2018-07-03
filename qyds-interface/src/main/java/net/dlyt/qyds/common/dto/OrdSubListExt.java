package net.dlyt.qyds.common.dto;

/**
 * Created by wenxuechao on 16/8/15.
 */
public class OrdSubListExt extends OrdSubList {

    private String deliverStatusCode;

    public String getDeliverStatusCode() {
        return deliverStatusCode;
    }

    public void setDeliverStatusCode(String deliverStatusCode) {
        this.deliverStatusCode = deliverStatusCode;
    }


    private String skucontent;

    private String imageUrlJson;

    private String rexStatus = null;

    private String rexStatusName = null;

    private String rexOrderId = null;

    private String orgId = null;

    private String rexStoreId = null;

    private String rexStoreName = null;

    //需要多少行
    private int needColumns;

    //起点位置
    private int startPoint;

    //总行数
    private int count;

    //分页信息
    private String  sEcho;
    //开始页面
    private String iDisplayStart;

    //每一页显示的项目
    private String iDisplayLength;

    //每一页显示的项目
    private String erpSendStatus;

    //是否内购活动
    private String innerBuy;

    private String dispatchStoreName = null;

    private String insertTimeString;


    // 20171205 扩展字段
    // 发货门店地址
    private String shopProvince;
    // 发货门店地址
    private String shopCity;
    // 发货门店区域
    private String shopDistrict;
    // 发货门店地址
    private String shopAddress;

    public String getShopProvince() {
        return shopProvince;
    }

    public void setShopProvince(String shopProvince) {
        this.shopProvince = shopProvince;
    }

    public String getShopCity() { return shopCity; }

    public void setShopCity(String shopCity) {
        this.shopCity = shopCity;
    }

    public String getShopDistrict() {
        return shopDistrict;
    }

    public void setShopDistrict(String shopDistrict) { this.shopDistrict = shopDistrict; }

    public String getShopAddress() {
        return shopAddress;
    }

    public void setShopAddress(String shopAddress) {
        this.shopAddress = shopAddress;
    }
    public String getInsertTimeString() {
        return insertTimeString;
    }

    public void setInsertTimeString(String insertTimeString) {
        this.insertTimeString = insertTimeString;
    }

    public String getDispatchStoreName() {
        return dispatchStoreName;
    }

    public void setDispatchStoreName(String dispatchStoreName) {
        this.dispatchStoreName = dispatchStoreName;
    }

    public String getInnerBuy() {
        return innerBuy;
    }

    public void setInnerBuy(String innerBuy) {
        this.innerBuy = innerBuy;
    }

    public String getErpSendStatus() {
        return erpSendStatus;
    }

    public void setErpSendStatus(String erpSendStatus) {
        this.erpSendStatus = erpSendStatus;
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

    public String getsEcho() {
        return sEcho;
    }

    public void setsEcho(String sEcho) {
        this.sEcho = sEcho;
    }

    public String getiDisplayStart() {
        return iDisplayStart;
    }

    public void setiDisplayStart(String iDisplayStart) {
        this.iDisplayStart = iDisplayStart;
    }

    public String getiDisplayLength() {
        return iDisplayLength;
    }

    public void setiDisplayLength(String iDisplayLength) {
        this.iDisplayLength = iDisplayLength;
    }

    public String getSkucontent() {
        return skucontent;
    }

    public void setSkucontent(String skucontent) {
        this.skucontent = skucontent;
    }

    public String getImageUrlJson() {
        return imageUrlJson;
    }

    public void setImageUrlJson(String imageUrlJson) {
        this.imageUrlJson = imageUrlJson;
    }

    public String getRexStatus() {
        return rexStatus;
    }

    public void setRexStatus(String rexStatus) {
        this.rexStatus = rexStatus;
    }

    public String getRexStatusName() {
        return rexStatusName;
    }

    public void setRexStatusName(String rexStatusName) {
        this.rexStatusName = rexStatusName;
    }

    public String getRexOrderId() {
        return rexOrderId;
    }

    public void setRexOrderId(String rexOrderId) {
        this.rexOrderId = rexOrderId;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getRexStoreId() {
        return rexStoreId;
    }

    public void setRexStoreId(String rexStoreId) {
        this.rexStoreId = rexStoreId;
    }

    public String getRexStoreName() {
        return rexStoreName;
    }

    public void setRexStoreName(String rexStoreName) {
        this.rexStoreName = rexStoreName;
    }
}
