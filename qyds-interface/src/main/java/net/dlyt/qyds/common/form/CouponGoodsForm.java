package net.dlyt.qyds.common.form;

import net.dlyt.qyds.common.dto.CouponGoods;

/**
 * Created by cjk on 16/8/5.
 */
public class CouponGoodsForm extends CouponGoods {
    private String goodsName = null;
    private String goodsCode = null;
    private String colorName = null;
    private String sizeName = null;
    private String skucontent = null;
    private String type = null;

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
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

    public String getSkucontent() {
        return skucontent;
    }

    public void setSkucontent(String skucontent) {
        this.skucontent = skucontent;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getGoodsCode() {
        return goodsCode;
    }

    public void setGoodsCode(String goodsCode) {
        this.goodsCode = goodsCode;
    }
}
