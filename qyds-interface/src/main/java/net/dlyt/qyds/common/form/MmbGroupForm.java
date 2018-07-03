package net.dlyt.qyds.common.form;

import net.dlyt.qyds.common.dto.MmbGroup;

import java.util.List;

/**
 * Created by YiLian on 16/7/29.
 */
public class MmbGroupForm extends MmbGroup {

    //分页信息
    private String sEcho;

    //开始页面
    private int iDisplayStart;

    //每一页显示的项目
    private int iDisplayLength;

    //追加电话号码查询
    private String telephone;

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
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

    private String memberType;
    private String memberLevelId;
    private String memberName;
    private String memberStatus;

    public String getMemberType() {
        return memberType;
    }

    public void setMemberType(String memberType) {
        this.memberType = memberType;
    }

    public String getMemberLevelId() {
        return memberLevelId;
    }

    public void setMemberLevelId(String memberLevelId) {
        this.memberLevelId = memberLevelId;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getMemberStatus() {
        return memberStatus;
    }

    public void setMemberStatus(String memberStatus) {
        this.memberStatus = memberStatus;
    }

    private List<String> addList;
    private List<String> delList;

    public List<String> getAddList() {
        return addList;
    }

    public void setAddList(List<String> addList) {
        this.addList = addList;
    }

    public List<String> getDelList() {
        return delList;
    }

    public void setDelList(List<String> delList) {
        this.delList = delList;
    }

    private String iTimeStart;

    private String iTimeEnd;

    private String provinceCode;

    private String cityCode;

    private String districtCode;

    public String getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getDistrictCode() {
        return districtCode;
    }

    public void setDistrictCode(String districtCode) {
        this.districtCode = districtCode;
    }

    public String getiTimeStart() {
        return iTimeStart;
    }

    public void setiTimeStart(String iTimeStart) {
        this.iTimeStart = iTimeStart;
    }

    public String getiTimeEnd() {
        return iTimeEnd;
    }

    public void setiTimeEnd(String iTimeEnd) {
        this.iTimeEnd = iTimeEnd;
    }
}
