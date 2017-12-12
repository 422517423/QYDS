package net.dlyt.qyds.common.dto;

import java.io.Serializable;
import java.util.Date;

public class ErpGoodsSeason implements Serializable {
    private String seasonCode;

    private String seasonNameEn;

    private String seasonNameCn;

    private String comment;

    private String deleted;

    private String updateUserId;

    private Date updateTime;

    private String insertUserId;

    private Date insertTime;

    public String getSeasonCode() {
        return seasonCode;
    }

    public void setSeasonCode(String seasonCode) {
        this.seasonCode = seasonCode == null ? null : seasonCode.trim();
    }

    public String getSeasonNameEn() {
        return seasonNameEn;
    }

    public void setSeasonNameEn(String seasonNameEn) {
        this.seasonNameEn = seasonNameEn == null ? null : seasonNameEn.trim();
    }

    public String getSeasonNameCn() {
        return seasonNameCn;
    }

    public void setSeasonNameCn(String seasonNameCn) {
        this.seasonNameCn = seasonNameCn == null ? null : seasonNameCn.trim();
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