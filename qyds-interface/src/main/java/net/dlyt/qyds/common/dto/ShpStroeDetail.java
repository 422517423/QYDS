package net.dlyt.qyds.common.dto;

import java.io.Serializable;
import java.util.Date;

public class ShpStroeDetail implements Serializable {
    private String orgId;

    private String erpStoreId;

    private String searchKey;

    private String imageUrl;

    private String contactor;

    private String phone;

    private String districtidProvince;

    private String districtidCity;

    private String districtidDistrict;

    private String address;

    private String longitude;

    private String latitude;

    private String introduceHtml;

    private String deleted;

    private String updateUserId;

    private Date updateTime;

    private String insertUserId;

    private Date insertTime;

    private static final long serialVersionUID = 1L;

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId == null ? null : orgId.trim();
    }

    public String getErpStoreId() {
        return erpStoreId;
    }

    public void setErpStoreId(String erpStoreId) {
        this.erpStoreId = erpStoreId == null ? null : erpStoreId.trim();
    }

    public String getSearchKey() {
        return searchKey;
    }

    public void setSearchKey(String searchKey) {
        this.searchKey = searchKey == null ? null : searchKey.trim();
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl == null ? null : imageUrl.trim();
    }

    public String getContactor() {
        return contactor;
    }

    public void setContactor(String contactor) {
        this.contactor = contactor == null ? null : contactor.trim();
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    public String getDistrictidProvince() {
        return districtidProvince;
    }

    public void setDistrictidProvince(String districtidProvince) {
        this.districtidProvince = districtidProvince == null ? null : districtidProvince.trim();
    }

    public String getDistrictidCity() {
        return districtidCity;
    }

    public void setDistrictidCity(String districtidCity) {
        this.districtidCity = districtidCity == null ? null : districtidCity.trim();
    }

    public String getDistrictidDistrict() {
        return districtidDistrict;
    }

    public void setDistrictidDistrict(String districtidDistrict) {
        this.districtidDistrict = districtidDistrict == null ? null : districtidDistrict.trim();
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address == null ? null : address.trim();
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude == null ? null : longitude.trim();
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude == null ? null : latitude.trim();
    }

    public String getIntroduceHtml() {
        return introduceHtml;
    }

    public void setIntroduceHtml(String introduceHtml) {
        this.introduceHtml = introduceHtml == null ? null : introduceHtml.trim();
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