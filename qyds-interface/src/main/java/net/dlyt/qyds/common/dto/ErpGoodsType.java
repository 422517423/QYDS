package net.dlyt.qyds.common.dto;

import java.io.Serializable;
import java.util.Date;

public class ErpGoodsType implements Serializable {
    private String typeCode;

    private String topTypeCode;

    private String topTypeNameEn;

    private String topTypeNameCn;

    private String typeNameEn;

    private String typeNameCn;

    private String comment;

    private String deleted;

    private String updateUserId;

    private Date updateTime;

    private String insertUserId;

    private Date insertTime;

    public String getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode == null ? null : typeCode.trim();
    }

    public String getTopTypeCode() {
        return topTypeCode;
    }

    public void setTopTypeCode(String topTypeCode) {
        this.topTypeCode = topTypeCode == null ? null : topTypeCode.trim();
    }

    public String getTopTypeNameEn() {
        return topTypeNameEn;
    }

    public void setTopTypeNameEn(String topTypeNameEn) {
        this.topTypeNameEn = topTypeNameEn == null ? null : topTypeNameEn.trim();
    }

    public String getTopTypeNameCn() {
        return topTypeNameCn;
    }

    public void setTopTypeNameCn(String topTypeNameCn) {
        this.topTypeNameCn = topTypeNameCn == null ? null : topTypeNameCn.trim();
    }

    public String getTypeNameEn() {
        return typeNameEn;
    }

    public void setTypeNameEn(String typeNameEn) {
        this.typeNameEn = typeNameEn == null ? null : typeNameEn.trim();
    }

    public String getTypeNameCn() {
        return typeNameCn;
    }

    public void setTypeNameCn(String typeNameCn) {
        this.typeNameCn = typeNameCn == null ? null : typeNameCn.trim();
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