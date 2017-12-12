package net.dlyt.qyds.common.dto;

import java.io.Serializable;
import java.util.List;

/**
 * Created by wenxuechao on 16/8/5.
 */
public class OrdConfirmOrderUnitExt implements Serializable {

    //商品ID
    private String goodsId;
    //商品名称
    private String goodsName;
    //SKU信息
    private String skucontent;
    //市场价格
    private String price;
    //商品SKU
    private String skuId;
    //颜色名称
    private String colorName;
    //尺寸名称
    private String sizeName;
    //商品图片
    private String imageUrlJson;
    //购物车用数量
    private String quantity;
    //单品活动ID
    private String actGoodsId;
    //活动名称
    private String actionName;
    //优惠后价格
    private String priceDiscount;

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

    public String getSkucontent() {
        return skucontent;
    }

    public void setSkucontent(String skucontent) {
        this.skucontent = skucontent;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getSkuId() {
        return skuId;
    }

    public void setSkuId(String skuId) {
        this.skuId = skuId;
    }

    public String getColorName() {
        return colorName;
    }

    public void setColorName(String colorName) {
        this.colorName = colorName;
    }

    public String getSizeName() {
        return sizeName;
    }

    public void setSizeName(String sizeName) {
        this.sizeName = sizeName;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
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

    public String getPriceDiscount() {
        return priceDiscount;
    }

    public void setPriceDiscount(String priceDiscount) {
        this.priceDiscount = priceDiscount;
    }

    public String getImageUrlJson() {
        return imageUrlJson;
    }

    public void setImageUrlJson(String imageUrlJson) {
        this.imageUrlJson = imageUrlJson;
    }
}
