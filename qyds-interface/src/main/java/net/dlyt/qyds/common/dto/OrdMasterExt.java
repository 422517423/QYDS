package net.dlyt.qyds.common.dto;

import net.dlyt.qyds.common.form.ActMasterForm;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Created by wenxuechao on 16/7/23.
 */
public class OrdMasterExt extends OrdMaster {

    //登录用户的名字
    private String loginUserName;

    //需要多少行
    private int needColumns;

    //起点位置
    private int startPoint;

    //总行数
    private int count;

    //登录用户的id
    private String userId;

    //店铺名
    private String shopName;

    //子订单数据
    private List<OrdSubList> subList;

    //订单商品明细数据
    private List<OrdList> ordList;

    //子订单ID
    private String subOrderId;

    //商品明细订单ID
    private String detailId;

    //商品ID
    private String goodsId;

    //商品SKUID
    private String goodsSkuId;

    //购买数量
    private String quantity;

    //商品类型
    private String type;

    //套装产品id及skuid
    private String gdIdAndSkuIdJson;

    //会员购物车编号
    private String[] bagNoArray;

    //商品信息JSON
    private String ordListJson;

    //提交订单传递的商品信息
    private List<OrdListExt> ordListExtList;

    //收货地址ID
    private String addressId;

    //退货方式
    private String rexMode;

    //退货状态
    private String rexStatus;

    //会员名称
    private String memberName;

    //会员电话
    private String telephone;

    private String couponMemberId;

    private String orderTypeCn;
    private String orderStatusCn;
    private String payTypeCn;
    private String deliverStatusCn;
    private String evaluateStatusCn;
    private String deliverTypeCn;
    private String cancelTypeCn;
    private String orderStatusName;

    private String newmemberId;

    private ActMasterForm activity = null;

    private CouponMaster coupon = null;

    private int allCount;
    private int waitPayCount;
    private int waitDeliveryCount;
    private int waitReceiveCount;
    private int completedCount;

    private String captcha = null;

    private String storeAddress;

    //门店自提的时候联系人的电话和姓名
    private String cname;
    private String ctel;

    //下单开始时间
    private String orderTimeStart;

    //下单结束时间
    private String orderTimeEnd;

    //已发货件数
    private int deliverCount;

    //优惠券名称
    private String couponName;
    //活动名称
    private String activityName;

    //excel子订单
    private List<OrdSubListExt> OrdSubListExtList;
    private String insertTimeString;

    // TODO: 2017/12/15 总消费金额 临时字段
    private BigDecimal allPrice;

    // TODO: 2018/1/22 客户申请退款总金额
    private BigDecimal rexPrice;

    // TODO: 2018/1/24 客户实际退款总金额
    private BigDecimal rexInfactPrice;

    private String salerName;

    public String getSalerTelephone() {
        return salerTelephone;
    }

    public void setSalerTelephone(String salerTelephone) {
        this.salerTelephone = salerTelephone;
    }

    private String salerTelephone;

    public String getSalerName() {
        return salerName;
    }

    public void setSalerName(String salerName) {
        this.salerName = salerName;
    }

    public BigDecimal getRexInfactPrice() {
        return rexInfactPrice;
    }

    public void setRexInfactPrice(BigDecimal rexInfactPrice) {
        this.rexInfactPrice = rexInfactPrice;
    }

    public BigDecimal getRexPrice() {
        return rexPrice;
    }

    public void setRexPrice(BigDecimal rexPrice) {
        this.rexPrice = rexPrice;
    }

    public BigDecimal getAllPrice() {
        return allPrice;
    }

    public void setAllPrice(BigDecimal allPrice) {
        this.allPrice = allPrice;
    }

    public List<OrdSubListExt> getOrdSubListExtList() {
        return OrdSubListExtList;
    }

    public void setOrdSubListExtList(List<OrdSubListExt> ordSubListExtList) {
        OrdSubListExtList = ordSubListExtList;
    }

    public String getCouponName() {
        return couponName;
    }

    public void setCouponName(String couponName) {
        this.couponName = couponName;
    }

    public int getDeliverCount() {
        return deliverCount;
    }

    public void setDeliverCount(int deliverCount) {
        this.deliverCount = deliverCount;
    }

    public String getOrderTimeStart() {
        return orderTimeStart;
    }

    public void setOrderTimeStart(String orderTimeStart) {
        this.orderTimeStart = orderTimeStart;
    }

    public String getOrderTimeEnd() {
        return orderTimeEnd;
    }

    public void setOrderTimeEnd(String orderTimeEnd) {
        this.orderTimeEnd = orderTimeEnd;
    }

    public String getLoginUserName() {
        return loginUserName;
    }

    public void setLoginUserName(String loginUserName) {
        this.loginUserName = loginUserName;
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<OrdSubList> getSubList() {
        return subList;
    }

    public void setSubList(List<OrdSubList> subList) {
        this.subList = subList;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public List<OrdList> getOrdList() {
        return ordList;
    }

    public void setOrdList(List<OrdList> ordList) {
        this.ordList = ordList;
    }

    public String getSubOrderId() {
        return subOrderId;
    }

    public void setSubOrderId(String subOrderId) {
        this.subOrderId = subOrderId;
    }

    public String getDetailId() {
        return detailId;
    }

    public void setDetailId(String detailId) {
        this.detailId = detailId;
    }

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public String getGoodsSkuId() {
        return goodsSkuId;
    }

    public void setGoodsSkuId(String goodsSkuId) {
        this.goodsSkuId = goodsSkuId;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getGdIdAndSkuIdJson() {
        return gdIdAndSkuIdJson;
    }

    public void setGdIdAndSkuIdJson(String gdIdAndSkuIdJson) {
        this.gdIdAndSkuIdJson = gdIdAndSkuIdJson;
    }

    public String[] getBagNoArray() {
        return bagNoArray;
    }

    public void setBagNoArray(String[] bagNoArray) {
        this.bagNoArray = bagNoArray;
    }

    public String getOrdListJson() {
        return ordListJson;
    }

    public void setOrdListJson(String ordListJson) {
        this.ordListJson = ordListJson;
    }

    public List<OrdListExt> getOrdListExtList() {
        return ordListExtList;
    }

    public void setOrdListExtList(List<OrdListExt> ordListExtList) {
        this.ordListExtList = ordListExtList;
    }

    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }

    public String getRexMode() {
        return rexMode;
    }

    public void setRexMode(String rexMode) {
        this.rexMode = rexMode;
    }

    public String getRexStatus() {
        return rexStatus;
    }

    public void setRexStatus(String rexStatus) {
        this.rexStatus = rexStatus;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getCouponMemberId() {
        return couponMemberId;
    }

    public void setCouponMemberId(String couponMemberId) {
        this.couponMemberId = couponMemberId;
    }

    public String getOrderTypeCn() {
        return orderTypeCn;
    }

    public void setOrderTypeCn(String orderTypeCn) {
        this.orderTypeCn = orderTypeCn;
    }

    public String getOrderStatusCn() {
        return orderStatusCn;
    }

    public void setOrderStatusCn(String orderStatusCn) {
        this.orderStatusCn = orderStatusCn;
    }

    public String getPayTypeCn() {
        return payTypeCn;
    }

    public void setPayTypeCn(String payTypeCn) {
        this.payTypeCn = payTypeCn;
    }

    public String getDeliverStatusCn() {
        return deliverStatusCn;
    }

    public void setDeliverStatusCn(String deliverStatusCn) {
        this.deliverStatusCn = deliverStatusCn;
    }

    public String getEvaluateStatusCn() {
        return evaluateStatusCn;
    }

    public void setEvaluateStatusCn(String evaluateStatusCn) {
        this.evaluateStatusCn = evaluateStatusCn;
    }

    public String getCancelTypeCn() {
        return cancelTypeCn;
    }

    public void setCancelTypeCn(String cancelTypeCn) {
        this.cancelTypeCn = cancelTypeCn;
    }

    public String getDeliverTypeCn() {
        return deliverTypeCn;
    }

    public void setDeliverTypeCn(String deliverTypeCn) {
        this.deliverTypeCn = deliverTypeCn;
    }

    public String getOrderStatusName() {
        return orderStatusName;
    }

    public void setOrderStatusName(String orderStatusName) {
        this.orderStatusName = orderStatusName;
    }

    public ActMasterForm getActivity() {
        return activity;
    }

    public void setActivity(ActMasterForm activity) {
        this.activity = activity;
    }

    public CouponMaster getCoupon() {
        return coupon;
    }

    public void setCoupon(CouponMaster coupon) {
        this.coupon = coupon;
    }

    public String getNewmemberId() {
        return newmemberId;
    }

    public void setNewmemberId(String newmemberId) {
        this.newmemberId = newmemberId;
    }

    public int getAllCount() {
        return allCount;
    }

    public void setAllCount(int allCount) {
        this.allCount = allCount;
    }

    public int getWaitPayCount() {
        return waitPayCount;
    }

    public void setWaitPayCount(int waitPayCount) {
        this.waitPayCount = waitPayCount;
    }

    public int getWaitDeliveryCount() {
        return waitDeliveryCount;
    }

    public void setWaitDeliveryCount(int waitDeliveryCount) {
        this.waitDeliveryCount = waitDeliveryCount;
    }

    public int getWaitReceiveCount() {
        return waitReceiveCount;
    }

    public void setWaitReceiveCount(int waitReceiveCount) {
        this.waitReceiveCount = waitReceiveCount;
    }

    public int getCompletedCount() {
        return completedCount;
    }

    public void setCompletedCount(int completedCount) {
        this.completedCount = completedCount;
    }

    public String getCaptcha() {
        return captcha;
    }

    public void setCaptcha(String captcha) {
        this.captcha = captcha;
    }

    public String getStoreAddress() {
        return storeAddress;
    }

    public void setStoreAddress(String storeAddress) {
        this.storeAddress = storeAddress;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public String getCtel() {
        return ctel;
    }

    public void setCtel(String ctel) {
        this.ctel = ctel;
    }

    public String getInsertTimeString() {
        return insertTimeString;
    }

    public void setInsertTimeString(String insertTimeString) {
        this.insertTimeString = insertTimeString;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }
}
