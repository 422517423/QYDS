package net.dlyt.qyds.common.dto.ext;

import net.dlyt.qyds.common.dto.MmbShoppingBag;
import net.dlyt.qyds.common.form.ActMasterForm;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by YiLian on 16/8/2.
 */
public class MmbShoppingBagExt extends MmbShoppingBag {

    // SKU-ID
    private String skuId;
    // 分类
    private String type;
    // 图片链接
    private String imageUrlJson;
    // 商品品牌ID
    private String brandId;
    // 商品分类ID
    private String goodsTypeId;
    // 商品名称
    private String goodsName;
    // 商品路径
    private String goodsTypeNamePath;

    // 商品删除标记
    private String goodsDeleted;
    // 商品上架标记
    private String isOnsell;

    // 商品SKU详细信息
    private List<MmbShoppingSKuExt> skuList;

    private List<ActMasterForm> activityList;

    public String getImageUrlJson() {
        return imageUrlJson;
    }

    public void setImageUrlJson(String imageUrlJson) {
        this.imageUrlJson = imageUrlJson;
    }

    public String getBrandId() {
        return brandId;
    }

    public void setBrandId(String brandId) {
        this.brandId = brandId;
    }

    public String getGoodsTypeId() {
        return goodsTypeId;
    }

    public void setGoodsTypeId(String goodsTypeId) {
        this.goodsTypeId = goodsTypeId;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getGoodsTypeNamePath() {
        return goodsTypeNamePath;
    }

    public void setGoodsTypeNamePath(String goodsTypeNamePath) {
        this.goodsTypeNamePath = goodsTypeNamePath;
    }

    public String getGoodsDeleted() {
        return goodsDeleted;
    }

    public void setGoodsDeleted(String goodsDeleted) {
        this.goodsDeleted = goodsDeleted;
    }

    public String getIsOnsell() {
        return isOnsell;
    }

    public void setIsOnsell(String isOnsell) {
        this.isOnsell = isOnsell;
    }

    public List<MmbShoppingSKuExt> getSkuList() {
        return skuList;
    }

    public void setSkuList(List<MmbShoppingSKuExt> skuList) {
        this.skuList = skuList;
    }

    public String getSkuId() {
        return skuId;
    }

    public void setSkuId(String skuId) {
        this.skuId = skuId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<ActMasterForm> getActivityList() {
        return activityList;
    }

    public void setActivityList(List<ActMasterForm> activityList) {
        this.activityList = activityList;
    }
}
