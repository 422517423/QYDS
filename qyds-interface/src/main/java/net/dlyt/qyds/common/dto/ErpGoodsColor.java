package net.dlyt.qyds.common.dto;

import java.io.Serializable;
import java.util.Date;

public class ErpGoodsColor implements Serializable {
    private String colorCode;

    private String helpcode;

    private String colorNameEn;

    private String colorNameCn;

    private String comment;

    private String deleted;

    private String updateUserId;

    private Date updateTime;

    private String insertUserId;

    private Date insertTime;

    public String getColorCode() {
        return colorCode;
    }

    public void setColorCode(String colorCode) {
        this.colorCode = colorCode == null ? null : colorCode.trim();
    }

    public String getHelpcode() {
        return helpcode;
    }

    public void setHelpcode(String helpcode) {
        this.helpcode = helpcode == null ? null : helpcode.trim();
    }

    public String getColorNameEn() {
        return colorNameEn;
    }

    public void setColorNameEn(String colorNameEn) {
        this.colorNameEn = colorNameEn == null ? null : colorNameEn.trim();
    }

    public String getColorNameCn() {
        return colorNameCn;
    }

    public void setColorNameCn(String colorNameCn) {
        this.colorNameCn = colorNameCn == null ? null : colorNameCn.trim();
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