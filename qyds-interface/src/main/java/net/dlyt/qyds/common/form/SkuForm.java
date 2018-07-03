package net.dlyt.qyds.common.form;

import net.dlyt.qyds.common.dto.GdsMaster;

/**
 * Created by cjk on 16/8/4.
 */
public class SkuForm {
    //分页信息
    private String sEcho;

    //开始页面
    private int iDisplayStart;

    //每一页显示的项目
    private int iDisplayLength;

    private String goodsId = null;
    private String type = null;
    private String brandId = null;
    private String goodsTypeId = null;
    private String isOnsell = null;
    private String goodsName = null;
    private String goodsCode = null;
    private String goodsTypeNamePath = null;
    private String skuid = null;
    private String skucontent = null;
    private String colorCode = null;
    private String colorName = null;
    private float price = 0;
    private String safeBank = null;
    private String sizeCode = null;
    private String sizeName = null;
    private String erpimg = null;
    private String newCount = null;
    private String memberId = null;

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public String getSkuid() {
        return skuid;
    }

    public void setSkuid(String skuid) {
        this.skuid = skuid;
    }

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

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getSafeBank() {
        return safeBank;
    }

    public void setSafeBank(String safeBank) {
        this.safeBank = safeBank;
    }

    public String getsEcho() {
        return sEcho;
    }

    public void setsEcho(String sEcho) {
        this.sEcho = sEcho;
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

    public String getIsOnsell() {
        return isOnsell;
    }

    public void setIsOnsell(String isOnsell) {
        this.isOnsell = isOnsell;
    }

    public String getGoodsCode() {
        return goodsCode;
    }

    public void setGoodsCode(String goodsCode) {
        this.goodsCode = goodsCode;
    }

    public String getErpimg() {
        return erpimg;
    }

    public void setErpimg(String erpimg) {
        this.erpimg = erpimg;
    }

    public String getNewCount() {
        return newCount;
    }

    public void setNewCount(String newCount) {
        this.newCount = newCount;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }
}
