package net.dlyt.qyds.common.dto;

import java.io.Serializable;
import java.util.Date;

public class ErpGoodsSize extends ErpGoodsSizeKey implements Serializable {
    private Integer sort;

    private String typeName;

    private String sizeNameEn;

    private String sizeNameCn;

    private String sizeFullNameEn;

    private String sizeFullNameCn;

    private String comment;

    private String deleted;

    private String updateUserId;

    private Date updateTime;

    private String insertUserId;

    private Date insertTime;

    private Short bnkNoLimit;

    private Short bnkLessLimit;

    private static final long serialVersionUID = 1L;

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName == null ? null : typeName.trim();
    }

    public String getSizeNameEn() {
        return sizeNameEn;
    }

    public void setSizeNameEn(String sizeNameEn) {
        this.sizeNameEn = sizeNameEn == null ? null : sizeNameEn.trim();
    }

    public String getSizeNameCn() {
        return sizeNameCn;
    }

    public void setSizeNameCn(String sizeNameCn) {
        this.sizeNameCn = sizeNameCn == null ? null : sizeNameCn.trim();
    }

    public String getSizeFullNameEn() {
        return sizeFullNameEn;
    }

    public void setSizeFullNameEn(String sizeFullNameEn) {
        this.sizeFullNameEn = sizeFullNameEn == null ? null : sizeFullNameEn.trim();
    }

    public String getSizeFullNameCn() {
        return sizeFullNameCn;
    }

    public void setSizeFullNameCn(String sizeFullNameCn) {
        this.sizeFullNameCn = sizeFullNameCn == null ? null : sizeFullNameCn.trim();
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

    public Short getBnkNoLimit() {
        return bnkNoLimit;
    }

    public void setBnkNoLimit(Short bnkNoLimit) {
        this.bnkNoLimit = bnkNoLimit;
    }

    public Short getBnkLessLimit() {
        return bnkLessLimit;
    }

    public void setBnkLessLimit(Short bnkLessLimit) {
        this.bnkLessLimit = bnkLessLimit;
    }
}