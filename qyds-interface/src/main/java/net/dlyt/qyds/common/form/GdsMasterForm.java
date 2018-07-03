package net.dlyt.qyds.common.form;


import net.dlyt.qyds.common.dto.GdsMaster;

public class GdsMasterForm extends GdsMaster {

    //分页信息
    private String  sEcho;
    //开始页面
    private String iDisplayStart;

    //每一页显示的项目
    private String iDisplayLength;

    //商品id
    private String goodsIds;

    //上架的时候用
    private String pageFrom_sell;

    // 活动ID
    private String activityId;

    // cms维护商品分类
    private String cmsGoodsTypeId;

    //姓名
    private String userName;
    //电话
    private String telephone;

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

    public String getsEcho() {
        return sEcho;
    }

    public void setsEcho(String sEcho) {
        this.sEcho = sEcho;
    }

    public String getGoodsIds() {
        return goodsIds;
    }

    public void setGoodsIds(String goodsIds) {
        this.goodsIds = goodsIds;
    }

    public String getPageFrom_sell() {
        return pageFrom_sell;
    }

    public void setPageFrom_sell(String pageFrom_sell) {
        this.pageFrom_sell = pageFrom_sell;
    }

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    public String getCmsGoodsTypeId() {
        return cmsGoodsTypeId;
    }

    public void setCmsGoodsTypeId(String cmsGoodsTypeId) {
        this.cmsGoodsTypeId = cmsGoodsTypeId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }
}