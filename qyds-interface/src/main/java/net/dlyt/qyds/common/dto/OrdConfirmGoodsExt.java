package net.dlyt.qyds.common.dto;

import net.dlyt.qyds.common.form.ActMasterForm;

import java.io.Serializable;
import java.util.List;

/**
 * Created by wenxuechao on 16/8/13.
 */
public class OrdConfirmGoodsExt implements Serializable {

    private boolean isNew;

    public boolean getIsNew() {
        return isNew;
    }

    public void setIsNew(boolean isNew) {
        this.isNew = isNew;
    }

    private boolean flag;

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    //商品ID
    private String goodsId;
    //商品名称
    private String goodsName;
    //商品类别
    private String type;
    //市场价格
    private String price;
    //购买数量
    private int quantity;
    //单品活动ID
    private String actGoodsId;
    //活动名称
    private String actionName;
    //优惠后价格
    private float priceDiscount;
    //商品图片
    private String imageUrlJson;
    //套装内各商品信息
    private List<OrdConfirmOrderUnitExt> ordConfirmOrderUnitExtList;

    private List<OrdSkuInfoExt> skuInfo;

    private ActMasterForm activity = null;

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public List<OrdConfirmOrderUnitExt> getOrdConfirmOrderUnitExtList() {
        return ordConfirmOrderUnitExtList;
    }

    public void setOrdConfirmOrderUnitExtList(List<OrdConfirmOrderUnitExt> ordConfirmOrderUnitExtList) {
        this.ordConfirmOrderUnitExtList = ordConfirmOrderUnitExtList;
    }

    public String getActGoodsId() {
        return actGoodsId;
    }

    public void setActGoodsId(String actGoodsId) {
        this.actGoodsId = actGoodsId;
    }

    public String getActionName() {
        return actionName;
    }

    public void setActionName(String actionName) {
        this.actionName = actionName;
    }

    public float getPriceDiscount() {
        return priceDiscount;
    }

    public void setPriceDiscount(float priceDiscount) {
        this.priceDiscount = priceDiscount;
    }

    public String getImageUrlJson() {
        return imageUrlJson;
    }

    public void setImageUrlJson(String imageUrlJson) {
        this.imageUrlJson = imageUrlJson;
    }

    public ActMasterForm getActivity() {
        return activity;
    }

    public void setActivity(ActMasterForm activity) {
        this.activity = activity;
    }

    public void setSkuInfo(List<OrdSkuInfoExt> skuInfo) {
        this.skuInfo = skuInfo;
    }

    public List<OrdSkuInfoExt> getSkuInfo() {
        return skuInfo;
    }
}
