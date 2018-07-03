package net.dlyt.qyds.common.dto;

import java.io.Serializable;
import java.util.Date;

public class GdsType implements Serializable {
    private String goodsTypeId;

    private String shopId;

    private String goodsTypeIdParent;

    private String type;

    private String goodsTypeCode;

    private String erpStyleNo;

    private String goodsTypeNameEn;

    private String goodsTypeNameCn;

    private String goodsTypeFullNameEn;

    private String goodsTypeFullNameCn;

    private String comment;

    private String deleted;

    private String updateUserId;

    private Date updateTime;

    private String insertUserId;

    private Date insertTime;

    private Integer sort;

    private static final long serialVersionUID = 1L;

    public String getGoodsTypeId() {
        return goodsTypeId;
    }

    public void setGoodsTypeId(String goodsTypeId) {
        this.goodsTypeId = goodsTypeId == null ? null : goodsTypeId.trim();
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId == null ? null : shopId.trim();
    }

    public String getGoodsTypeIdParent() {
        return goodsTypeIdParent;
    }

    public void setGoodsTypeIdParent(String goodsTypeIdParent) {
        this.goodsTypeIdParent = goodsTypeIdParent == null ? null : goodsTypeIdParent.trim();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }

    public String getGoodsTypeCode() {
        return goodsTypeCode;
    }

    public void setGoodsTypeCode(String goodsTypeCode) {
        this.goodsTypeCode = goodsTypeCode == null ? null : goodsTypeCode.trim();
    }

    public String getErpStyleNo() {
        return erpStyleNo;
    }

    public void setErpStyleNo(String erpStyleNo) {
        this.erpStyleNo = erpStyleNo == null ? null : erpStyleNo.trim();
    }

    public String getGoodsTypeNameEn() {
        return goodsTypeNameEn;
    }

    public void setGoodsTypeNameEn(String goodsTypeNameEn) {
        this.goodsTypeNameEn = goodsTypeNameEn == null ? null : goodsTypeNameEn.trim();
    }

    public String getGoodsTypeNameCn() {
        return goodsTypeNameCn;
    }

    public void setGoodsTypeNameCn(String goodsTypeNameCn) {
        this.goodsTypeNameCn = goodsTypeNameCn == null ? null : goodsTypeNameCn.trim();
    }

    public String getGoodsTypeFullNameEn() {
        return goodsTypeFullNameEn;
    }

    public void setGoodsTypeFullNameEn(String goodsTypeFullNameEn) {
        this.goodsTypeFullNameEn = goodsTypeFullNameEn == null ? null : goodsTypeFullNameEn.trim();
    }

    public String getGoodsTypeFullNameCn() {
        return goodsTypeFullNameCn;
    }

    public void setGoodsTypeFullNameCn(String goodsTypeFullNameCn) {
        this.goodsTypeFullNameCn = goodsTypeFullNameCn == null ? null : goodsTypeFullNameCn.trim();
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment == null ? null : comment.trim();
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

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }
}