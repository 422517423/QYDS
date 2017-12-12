package net.dlyt.qyds.common.dto;

import java.io.Serializable;
import java.util.Date;

public class PrizeDraw implements Serializable {
    private static final long serialVersionUID = 1L;
    private String prizeDrawId;
    private String prizeDrawName;
    private Date startTime;
    private Date endTime;
    private String drawType;
    private String canRepeatWin;
    private String comment;
    private String isValid;
    private String deleted;
    private String updateUserId;
    private Date updateTime;
    private String insertUserId;
    private Date insertTime;

    public String getPrizeDrawId() {
        return prizeDrawId;
    }

    public void setPrizeDrawId(String prizeDrawId) {
        this.prizeDrawId = prizeDrawId == null ? null : prizeDrawId.trim();
    }

    public String getPrizeDrawName() {
        return prizeDrawName;
    }

    public void setPrizeDrawName(String prizeDrawName) {
        this.prizeDrawName = prizeDrawName == null ? null : prizeDrawName.trim();
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getDrawType() {
        return drawType;
    }

    public void setDrawType(String drawType) {
        this.drawType = drawType == null ? null : drawType.trim();
    }

    public String getCanRepeatWin() {
        return canRepeatWin;
    }

    public void setCanRepeatWin(String canRepeatWin) {
        this.canRepeatWin = canRepeatWin == null ? null : canRepeatWin.trim();
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment == null ? null : comment.trim();
    }

    public String getIsValid() {
        return isValid;
    }

    public void setIsValid(String isValid) {
        this.isValid = isValid == null ? null : isValid.trim();
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