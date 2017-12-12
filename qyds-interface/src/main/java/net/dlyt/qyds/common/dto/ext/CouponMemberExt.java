package net.dlyt.qyds.common.dto.ext;

import net.dlyt.qyds.common.dto.CouponMember;
import net.dlyt.qyds.common.dto.OrdConfirmGoodsExt;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by cjk on 16/8/9.
 */
public class CouponMemberExt extends CouponMember {
    private float orderPrice = 0;
    private List<OrdConfirmGoodsExt> goodsInfo = null;

    //分页信息
    private int  sEcho;
    //开始页面
    private int iDisplayStart;

    //每一页显示的项目
    private int iDisplayLength;

    private String couponName;

    private String couponStyle;

    private String typeName;

    private String statusName;

    private Integer worth;

    private BigDecimal discount;

    private String telephone;

    private String hasOrderActivity;

    private String memberLevel;

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    public String getCouponStyle() {
        return couponStyle;
    }

    public void setCouponStyle(String couponStyle) {
        this.couponStyle = couponStyle;
    }

    public Integer getWorth() {
        return worth;
    }

    public void setWorth(Integer worth) {
        this.worth = worth;
    }

    public String getCouponName() {
        return couponName;
    }

    public void setCouponName(String couponName) {
        this.couponName = couponName;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public int getsEcho() {
        return sEcho;
    }

    public void setsEcho(int sEcho) {
        this.sEcho = sEcho;
    }

    public float getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(float orderPrice) {
        this.orderPrice = orderPrice;
    }

    public List<OrdConfirmGoodsExt> getGoodsInfo() {
        return goodsInfo;
    }

    public void setGoodsInfo(List<OrdConfirmGoodsExt> goodsInfo) {
        this.goodsInfo = goodsInfo;
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

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getHasOrderActivity() {
        return hasOrderActivity;
    }

    public void setHasOrderActivity(String hasOrderActivity) {
        this.hasOrderActivity = hasOrderActivity;
    }

    public String getMemberLevel() {
        return memberLevel;
    }

    public void setMemberLevel(String memberLevel) {
        this.memberLevel = memberLevel;
    }
}
