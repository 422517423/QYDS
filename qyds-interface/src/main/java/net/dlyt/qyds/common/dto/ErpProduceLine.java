package net.dlyt.qyds.common.dto;

import java.io.Serializable;
import java.util.Date;

public class ErpProduceLine implements Serializable {
    private String lineCode;

    private String lineNameEn;

    private String lineNameCn;

    private String comment;

    private String deleted;

    private String updateUserId;

    private Date updateTime;

    private String insertUserId;

    private Date insertTime;

    public String getLineCode() {
        return lineCode;
    }

    public void setLineCode(String lineCode) {
        this.lineCode = lineCode == null ? null : lineCode.trim();
    }

    public String getLineNameEn() {
        return lineNameEn;
    }

    public void setLineNameEn(String lineNameEn) {
        this.lineNameEn = lineNameEn == null ? null : lineNameEn.trim();
    }

    public String getLineNameCn() {
        return lineNameCn;
    }

    public void setLineNameCn(String lineNameCn) {
        this.lineNameCn = lineNameCn == null ? null : lineNameCn.trim();
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