package net.dlyt.qyds.common.dto;

import java.io.Serializable;
import java.util.Date;

public class GdsColoreimage implements Serializable {
    private String goodsColoreId;

    private String goodsId;

    private String coloreCode;

    private String coloreName;

    private String erpGoodsCode;

    private String erpColoreCode;

    private String imageUrlJson;

    private String comment;

    private String deleted;

    private String updateUserId;

    private Date updateTime;

    private String insertUserId;

    private Date insertTime;

    private String imageUrl;

    private static final long serialVersionUID = 1L;

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getGoodsColoreId() {
        return goodsColoreId;
    }

    public void setGoodsColoreId(String goodsColoreId) {
        this.goodsColoreId = goodsColoreId == null ? null : goodsColoreId.trim();
    }

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId == null ? null : goodsId.trim();
    }

    public String getColoreCode() {
        return coloreCode;
    }

    public void setColoreCode(String coloreCode) {
        this.coloreCode = coloreCode == null ? null : coloreCode.trim();
    }

    public String getColoreName() {
        return coloreName;
    }

    public void setColoreName(String coloreName) {
        this.coloreName = coloreName == null ? null : coloreName.trim();
    }

    public String getErpGoodsCode() {
        return erpGoodsCode;
    }

    public void setErpGoodsCode(String erpGoodsCode) {
        this.erpGoodsCode = erpGoodsCode == null ? null : erpGoodsCode.trim();
    }

    public String getErpColoreCode() {
        return erpColoreCode;
    }

    public void setErpColoreCode(String erpColoreCode) {
        this.erpColoreCode = erpColoreCode == null ? null : erpColoreCode.trim();
    }

    public String getImageUrlJson() {
        return imageUrlJson;
    }

    public void setImageUrlJson(String imageUrlJson) {
        this.imageUrlJson = imageUrlJson == null ? null : imageUrlJson.trim();
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
}