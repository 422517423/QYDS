package net.dlyt.qyds.common.dto;

import java.io.Serializable;
import java.util.List;

/**
 * Created by wenxuechao on 16/8/5.
 */
public class OrdConfirmOrderExt implements Serializable {

    //买家收货地址信息
    private List<MmbAddress> mmbAddressList;
    //商品ID
    private String goodsId;
    //商品名称
    private String goodsName;
    //商品类别
    private String type;
    //SKU信息
    private String skucontent;
    //市场价格
    private String price;
    //购买数量
    private String quantity;
    //店铺ID
    private String shopId;
    //店铺名称
    private String shopName;
    //可参与活动列表
    private List<ActMaster> actMasterList;
    //发货方式  10.电商发货，20.门店自提
    private List<ComCode> deliverType;
    //商品SKUID
    private String skuId;
    //颜色名称
    private String coloreName;
    //尺寸名称
    private String sizeName;
    //套装内各商品信息
    private List<OrdConfirmOrderUnitExt> ordConfirmOrderUnitExtList;
    //商品信息
    private List<OrdConfirmOrderExt> goodsInfo;

    public List<MmbAddress> getMmbAddressList() {
        return mmbAddressList;
    }

    public void setMmbAddressList(List<MmbAddress> mmbAddressList) {
        this.mmbAddressList = mmbAddressList;
    }

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

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public List<ActMaster> getActMasterList() {
        return actMasterList;
    }

    public void setActMasterList(List<ActMaster> actMasterList) {
        this.actMasterList = actMasterList;
    }

    public List<ComCode> getDeliverType() {
        return deliverType;
    }

    public void setDeliverType(List<ComCode> deliverType) {
        this.deliverType = deliverType;
    }

    public String getSkuId() {
        return skuId;
    }

    public void setSkuId(String skuId) {
        this.skuId = skuId;
    }

    public String getColoreName() {
        return coloreName;
    }

    public void setColoreName(String coloreName) {
        this.coloreName = coloreName;
    }

    public String getSizeName() {
        return sizeName;
    }

    public void setSizeName(String sizeName) {
        this.sizeName = sizeName;
    }

    public List<OrdConfirmOrderUnitExt> getOrdConfirmOrderUnitExtList() {
        return ordConfirmOrderUnitExtList;
    }

    public void setOrdConfirmOrderUnitExtList(List<OrdConfirmOrderUnitExt> ordConfirmOrderUnitExtList) {
        this.ordConfirmOrderUnitExtList = ordConfirmOrderUnitExtList;
    }

    public List<OrdConfirmOrderExt> getGoodsInfo() {
        return goodsInfo;
    }

    public void setGoodsInfo(List<OrdConfirmOrderExt> goodsInfo) {
        this.goodsInfo = goodsInfo;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
