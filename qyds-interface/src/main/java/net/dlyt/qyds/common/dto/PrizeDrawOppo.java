package net.dlyt.qyds.common.dto;

import java.io.Serializable;
import java.util.Date;

public class PrizeDrawOppo implements Serializable {
    private static final long serialVersionUID = 1L;
    private String prizeDrawOppoId;
    private String memberId;
    private String prizeDrawId;
    private String isDrawed;
    private String isWin;
    private String prizeName;
    private String prizeImage;
    private String prizeDesc;
    private String deleted;
    private String updateUserId;
    private Date updateTime;
    private String insertUserId;
    private Date insertTime;
    private String deliveryStatus;
    private String deliveryComment;

    public String getPrizeDrawOppoId() {
        return prizeDrawOppoId;
    }

    public void setPrizeDrawOppoId(String prizeDrawOppoId) {
        this.prizeDrawOppoId = prizeDrawOppoId == null ? null : prizeDrawOppoId.trim();
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId == null ? null : memberId.trim();
    }

    public String getPrizeDrawId() {
        return prizeDrawId;
    }

    public void setPrizeDrawId(String prizeDrawId) {
        this.prizeDrawId = prizeDrawId == null ? null : prizeDrawId.trim();
    }

    public String getIsDrawed() {
        return isDrawed;
    }

    public void setIsDrawed(String isDrawed) {
        this.isDrawed = isDrawed == null ? null : isDrawed.trim();
    }

    public String getIsWin() {
        return isWin;
    }

    public void setIsWin(String isWin) {
        this.isWin = isWin == null ? null : isWin.trim();
    }

    public String getPrizeName() {
        return prizeName;
    }

    public void setPrizeName(String prizeName) {
        this.prizeName = prizeName == null ? null : prizeName.trim();
    }

    public String getPrizeImage() {
        return prizeImage;
    }

    public void setPrizeImage(String prizeImage) {
        this.prizeImage = prizeImage == null ? null : prizeImage.trim();
    }

    public String getPrizeDesc() {
        return prizeDesc;
    }

    public void setPrizeDesc(String prizeDesc) {
        this.prizeDesc = prizeDesc == null ? null : prizeDesc.trim();
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

    public String getDeliveryStatus() {
        return deliveryStatus;
    }

    public void setDeliveryStatus(String deliveryStatus) {
        this.deliveryStatus = deliveryStatus == null ? null : deliveryStatus.trim();
    }

    public String getDeliveryComment() {
        return deliveryComment;
    }

    public void setDeliveryComment(String deliveryComment) {
        this.deliveryComment = deliveryComment == null ? null : deliveryComment.trim();
    }
}