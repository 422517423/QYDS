package net.dlyt.qyds.common.dto;

import java.io.Serializable;
import java.util.Date;

public class GdsMaster implements Serializable {
    private String goodsId;

    private String shopId;

    private String type;

    private String brandId;

    private String goodsTypeId;

    private String goodsTypeIdPath;

    private String goodsTypeCode;

    private String goodsTypeCodePath;

    private String erpStyleNo;

    private String goodsCode;

    private String goodsName;

    private String erpGoodsCode;

    private String erpGoodsName;

    private String maintainStatus;

    private String isOnsell;

    private String isWaste;

    private String deleted;

    private String updateUserId;

    private Date updateTime;

    private String insertUserId;

    private Date insertTime;

    private String goodsTypeNamePath;

    private String erpSendTypeStatus;

    private String goodsBrandTypeId;

    private String goodsBrandTypeIdPath;

    private String goodsBrandTypeCode;

    private String goodsBrandTypeCodePath;

    private String goodsBrandTypeNamePath;

    private Short sort;

    public int getSales() {
        return sales;
    }

    public void setSales(int sales) {
        this.sales = sales;
    }

    //2018.05.16添加销量字段
    private int  sales;

    private static final long serialVersionUID = 1L;

    private String cmsGoodsTypeId;

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId == null ? null : goodsId.trim();
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId == null ? null : shopId.trim();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }

    public String getBrandId() {
        return brandId;
    }

    public void setBrandId(String brandId) {
        this.brandId = brandId == null ? null : brandId.trim();
    }

    public String getGoodsTypeId() {
        return goodsTypeId;
    }

    public void setGoodsTypeId(String goodsTypeId) {
        this.goodsTypeId = goodsTypeId == null ? null : goodsTypeId.trim();
    }

    public String getGoodsTypeIdPath() {
        return goodsTypeIdPath;
    }

    public void setGoodsTypeIdPath(String goodsTypeIdPath) {
        this.goodsTypeIdPath = goodsTypeIdPath == null ? null : goodsTypeIdPath.trim();
    }

    public String getGoodsTypeCode() {
        return goodsTypeCode;
    }

    public void setGoodsTypeCode(String goodsTypeCode) {
        this.goodsTypeCode = goodsTypeCode == null ? null : goodsTypeCode.trim();
    }

    public String getGoodsTypeCodePath() {
        return goodsTypeCodePath;
    }

    public void setGoodsTypeCodePath(String goodsTypeCodePath) {
        this.goodsTypeCodePath = goodsTypeCodePath == null ? null : goodsTypeCodePath.trim();
    }

    public String getErpStyleNo() {
        return erpStyleNo;
    }

    public void setErpStyleNo(String erpStyleNo) {
        this.erpStyleNo = erpStyleNo == null ? null : erpStyleNo.trim();
    }

    public String getGoodsCode() {
        return goodsCode;
    }

    public void setGoodsCode(String goodsCode) {
        this.goodsCode = goodsCode == null ? null : goodsCode.trim();
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName == null ? null : goodsName.trim();
    }

    public String getErpGoodsCode() {
        return erpGoodsCode;
    }

    public void setErpGoodsCode(String erpGoodsCode) {
        this.erpGoodsCode = erpGoodsCode == null ? null : erpGoodsCode.trim();
    }

    public String getErpGoodsName() {
        return erpGoodsName;
    }

    public void setErpGoodsName(String erpGoodsName) {
        this.erpGoodsName = erpGoodsName == null ? null : erpGoodsName.trim();
    }

    public String getMaintainStatus() {
        return maintainStatus;
    }

    public void setMaintainStatus(String maintainStatus) {
        this.maintainStatus = maintainStatus == null ? null : maintainStatus.trim();
    }

    public String getIsOnsell() {
        return isOnsell;
    }

    public void setIsOnsell(String isOnsell) {
        this.isOnsell = isOnsell == null ? null : isOnsell.trim();
    }

    public String getIsWaste() {
        return isWaste;
    }

    public void setIsWaste(String isWaste) {
        this.isWaste = isWaste == null ? null : isWaste.trim();
    }

    public String getDeleted() {
        return deleted;
    }

    public void setDeleted(String deleted) {
        this.deleted = deleted == null ? null : deleted.trim();
    }

    public String getUpdateUserId() {
        return updateUserId;
    }

    public void setUpdateUserId(String updateUserId) {
        this.updateUserId = updateUserId == null ? null : updateUserId.trim();
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getInsertUserId() {
        return insertUserId;
    }

    public void setInsertUserId(String insertUserId) {
        this.insertUserId = insertUserId == null ? null : insertUserId.trim();
    }

    public Date getInsertTime() {
        return insertTime;
    }

    public void setInsertTime(Date insertTime) {
        this.insertTime = insertTime;
    }

    public String getGoodsTypeNamePath() {
        return goodsTypeNamePath;
    }

    public void setGoodsTypeNamePath(String goodsTypeNamePath) {
        this.goodsTypeNamePath = goodsTypeNamePath == null ? null : goodsTypeNamePath.trim();
    }

    public String getErpSendTypeStatus() {
        return erpSendTypeStatus;
    }

    public void setErpSendTypeStatus(String erpSendTypeStatus) {
        this.erpSendTypeStatus = erpSendTypeStatus == null ? null : erpSendTypeStatus.trim();
    }

    public String getGoodsBrandTypeId() {
        return goodsBrandTypeId;
    }

    public void setGoodsBrandTypeId(String goodsBrandTypeId) {
        this.goodsBrandTypeId = goodsBrandTypeId == null ? null : goodsBrandTypeId.trim();
    }

    public String getGoodsBrandTypeIdPath() {
        return goodsBrandTypeIdPath;
    }

    public void setGoodsBrandTypeIdPath(String goodsBrandTypeIdPath) {
        this.goodsBrandTypeIdPath = goodsBrandTypeIdPath == null ? null : goodsBrandTypeIdPath.trim();
    }

    public String getGoodsBrandTypeCode() {
        return goodsBrandTypeCode;
    }

    public void setGoodsBrandTypeCode(String goodsBrandTypeCode) {
        this.goodsBrandTypeCode = goodsBrandTypeCode == null ? null : goodsBrandTypeCode.trim();
    }

    public String getGoodsBrandTypeCodePath() {
        return goodsBrandTypeCodePath;
    }

    public void setGoodsBrandTypeCodePath(String goodsBrandTypeCodePath) {
        this.goodsBrandTypeCodePath = goodsBrandTypeCodePath == null ? null : goodsBrandTypeCodePath.trim();
    }

    public String getGoodsBrandTypeNamePath() {
        return goodsBrandTypeNamePath;
    }

    public Short getSort() {
        return sort;
    }

    public void setSort(Short sort) {
        this.sort = sort;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public void setGoodsBrandTypeNamePath(String goodsBrandTypeNamePath) {
        this.goodsBrandTypeNamePath = goodsBrandTypeNamePath == null ? null : goodsBrandTypeNamePath.trim();
    }

    public String getCmsGoodsTypeId() {
        return cmsGoodsTypeId;
    }

    public void setCmsGoodsTypeId(String cmsGoodsTypeId) {
        this.cmsGoodsTypeId = cmsGoodsTypeId;
    }
}
