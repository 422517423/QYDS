package net.dlyt.qyds.common.dto.ext;

import net.dlyt.qyds.common.dto.MmbShoppingSku;

import java.math.BigDecimal;

/**
 * Created by YiLian on 16/8/5.
 */
public class MmbShoppingSKuExt extends MmbShoppingSku{

    // 商品品牌ID
    private String brandId;
    // 商品分类ID
    private String goodsTypeId;
    // 商品名称
    private String goodsName;
    // 商品路径
    private String goodsTypeNamePath;
    // SKU图片路径
    private String imageUrlJson;
    // SKU内容Json
    private String skucontent;
    // 颜色code
    private String colorCode;
    // 颜色名称
    private String colorName;
    // 市场价格
    private BigDecimal price;
    // 安全库存
    private Integer safeBank;
    // 尺寸code
    private String sizeCode;
    // 尺寸名称
    private String sizeName;
    // SKU删除标记
    private String skuDeleted;
    // 商品删除标记
    private String goodsDeleted;
    // 商品上架标记
    private String isOnsell;
    // 最新库存
    private Integer newCount;

    public String getSkucontent() {
        return skucontent;
    }

    public void setSkucontent(String skucontent) {
        this.skucontent = skucontent;
    }

    public String getColorCode() {
        return colorCode;
    }

    public void setColorCode(String colorCode) {
        this.colorCode = colorCode;
    }

    public String getColorName() {
        return colorName;
    }

    public void setColorName(String colorName) {
        this.colorName = colorName;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }


    public String getSizeCode() {
        return sizeCode;
    }

    public void setSizeCode(String sizeCode) {
        this.sizeCode = sizeCode;
    }

    public String getSizeName() {
        return sizeName;
    }

    public void setSizeName(String sizeName) {
        this.sizeName = sizeName;
    }

    public String getSkuDeleted() {
        return skuDeleted;
    }

    public void setSkuDeleted(String skuDeleted) {
        this.skuDeleted = skuDeleted;
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

    public Integer getSafeBank() {
        return safeBank;
    }

    public void setSafeBank(Integer safeBank) {
        this.safeBank = safeBank;
    }

    public Integer getNewCount() {
        return newCount;
    }

    public void setNewCount(Integer newCount) {
        this.newCount = newCount;
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

    public String getImageUrlJson() {
        return imageUrlJson;
    }

    public void setImageUrlJson(String imageUrlJson) {
        this.imageUrlJson = imageUrlJson;
    }
}
