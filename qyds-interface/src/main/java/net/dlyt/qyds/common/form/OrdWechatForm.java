package net.dlyt.qyds.common.form;

import net.dlyt.qyds.common.dto.OrdMaster;

/**
 * Created by wenxuechao on 16/7/26.
 */
public class OrdWechatForm extends OrdMaster {

    //分页信息
    private String  sEcho;
    //开始页面
    private String iDisplayStart;

    //每一页显示的项目
    private String iDisplayLength;

    //子订单ID
    private String subOrderId;

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

    // 20180109会员手机号(被代买会员手机号)
    private String memberPhone;

    public String getMemberPhone() {
        return memberPhone;
    }

    public void setMemberPhone(String memberPhone) {
        this.memberPhone = memberPhone;
    }

    public String getIDisplayStart() {
        return iDisplayStart;
    }

    public void setIDisplayStart(String iDisplayStart) {
        this.iDisplayStart = iDisplayStart;
    }

    public String getIDisplayLength() {
        return iDisplayLength;
    }

    public void setIDisplayLength(String iDisplayLength) {
        this.iDisplayLength = iDisplayLength;
    }

    public String getsEcho() {
        return sEcho;
    }

    public void setsEcho(String sEcho) {
        this.sEcho = sEcho;
    }

    public String getSubOrderId() {
        return subOrderId;
    }

    public void setSubOrderId(String subOrderId) {
        this.subOrderId = subOrderId;
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

    public String[] getBagNoArray() {
        return bagNoArray;
    }

    public void setBagNoArray(String[] bagNoArray) {
        this.bagNoArray = bagNoArray;
    }
}
