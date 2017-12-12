package net.dlyt.qyds.common.dto;

import java.io.Serializable;
import java.util.Date;

public class PrizeDrawRecord implements Serializable {
    private static final long serialVersionUID = 1L;
    private String prizeDrawRecordId;
    private String userId;
    private String prizeDrawId;
    private String isWin;
    private String isNotify;
    private String comment;
    private String deleted;
    private String updateUserId;
    private Date updateTime;
    private String insertUserId;
    private Date insertTime;
    private String prizeGoodsId;
    private String prizeDrawOppoId;

    public String getPrizeDrawRecordId() {
        return prizeDrawRecordId;
    }

    public void setPrizeDrawRecordId(String prizeDrawRecordId) {
        this.prizeDrawRecordId = prizeDrawRecordId == null ? null : prizeDrawRecordId.trim();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    public String getPrizeDrawId() {
        return prizeDrawId;
    }

    public void setPrizeDrawId(String prizeDrawId) {
        this.prizeDrawId = prizeDrawId == null ? null : prizeDrawId.trim();
    }

    public String getIsWin() {
        return isWin;
    }

    public void setIsWin(String isWin) {
        this.isWin = isWin == null ? null : isWin.trim();
    }

    public String getIsNotify() {
        return isNotify;
    }

    public void setIsNotify(String isNotify) {
        this.isNotify = isNotify == null ? null : isNotify.trim();
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

    public String getPrizeGoodsId() {
        return prizeGoodsId;
    }

    public void setPrizeGoodsId(String prizeGoodsId) {
        this.prizeGoodsId = prizeGoodsId == null ? null : prizeGoodsId.trim();
    }

    public String getPrizeDrawOppoId() {
        return prizeDrawOppoId;
    }

    public void setPrizeDrawOppoId(String prizeDrawOppoId) {
        this.prizeDrawOppoId = prizeDrawOppoId == null ? null : prizeDrawOppoId.trim();
    }
}