package net.dlyt.qyds.common.dto;

import java.io.Serializable;
import java.util.Date;

public class MmbAddress implements Serializable {
    private String addressId;

    private String memberId;

    private Short sort;

    private String isDefault;

    private String districtidProvince;

    private String districtidCity;

    private String districtidDistrict;

    private String districtidStreet;

    private String address;

    private String postcode;

    private String contactor;

    private String phone;

    private String deleted;

    private String updateUserId;

    private Date updateTime;

    private String insertUserId;

    private Date insertTime;

    private static final long serialVersionUID = 1L;

    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId == null ? null : addressId.trim();
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId == null ? null : memberId.trim();
    }

    public Short getSort() {
        return sort;
    }

    public void setSort(Short sort) {
        this.sort = sort;
    }

    public String getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(String isDefault) {
        this.isDefault = isDefault == null ? null : isDefault.trim();
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

    public String getDistrictidStreet() {
        return districtidStreet;
    }

    public void setDistrictidStreet(String districtidStreet) {
        this.districtidStreet = districtidStreet == null ? null : districtidStreet.trim();
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address == null ? null : address.trim();
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode == null ? null : postcode.trim();
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