package net.dlyt.qyds.common.form;

import java.util.List;

public class MmbGoodsExtForm {
    private String memberLevelId = null;
    private List<MmbGoodsForm> goodsList;
    //分页信息
    private String sEcho;

    //开始页面
    private int iDisplayStart;

    //每一页显示的项目
    private int iDisplayLength;

    private String tempName;

    // 检索条件用活动ID
    private  String searchIds;

    public String getMemberLevelId() {
        return memberLevelId;
    }

    public void setMemberLevelId(String memberLevelId) {
        this.memberLevelId = memberLevelId;
    }

    public List<MmbGoodsForm> getGoodsList() {
        return goodsList;
    }

    public void setGoodsList(List<MmbGoodsForm> goodsList) {
        this.goodsList = goodsList;
    }

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

    public String getTempName() {
        return tempName;
    }

    public void setTempName(String tempName) {
        this.tempName = tempName;
    }

    public String getSearchIds() {
        return searchIds;
    }

    public void setSearchIds(String searchIds) {
        this.searchIds = searchIds;
    }
}
