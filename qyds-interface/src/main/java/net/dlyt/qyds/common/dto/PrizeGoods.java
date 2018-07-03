package net.dlyt.qyds.common.dto;

import java.io.Serializable;
import java.util.Date;

public class PrizeGoods implements Serializable {
    private static final long serialVersionUID = 1L;
    private String prizeGoodsId;
    private String prizeDrawId;
    private String prizeGoodsName;
    private String prizeGoodsImage;
    private Integer prizeGoodsCount;
    private Integer prizeGoodsCountLeft;
    private Integer sort;
    private String prizeGoodsDesc;
    private String winPercent;
    private String deleted;
    private String updateUserId;
    private Date updateTime;
    private String insertUserId;
    private Date insertTime;

    public String getPrizeGoodsId() {
        return prizeGoodsId;
    }

    public void setPrizeGoodsId(String prizeGoodsId) {
        this.prizeGoodsId = prizeGoodsId == null ? null : prizeGoodsId.trim();
    }

    public String getPrizeDrawId() {
        return prizeDrawId;
    }

    public void setPrizeDrawId(String prizeDrawId) {
        this.prizeDrawId = prizeDrawId == null ? null : prizeDrawId.trim();
    }

    public String getPrizeGoodsName() {
        return prizeGoodsName;
    }

    public void setPrizeGoodsName(String prizeGoodsName) {
        this.prizeGoodsName = prizeGoodsName == null ? null : prizeGoodsName.trim();
    }

    public String getPrizeGoodsImage() {
        return prizeGoodsImage;
    }

    public void setPrizeGoodsImage(String prizeGoodsImage) {
        this.prizeGoodsImage = prizeGoodsImage == null ? null : prizeGoodsImage.trim();
    }

    public Integer getPrizeGoodsCount() {
        return prizeGoodsCount;
    }

    public void setPrizeGoodsCount(Integer prizeGoodsCount) {
        this.prizeGoodsCount = prizeGoodsCount;
    }

    public Integer getPrizeGoodsCountLeft() {
        return prizeGoodsCountLeft;
    }

    public void setPrizeGoodsCountLeft(Integer prizeGoodsCountLeft) {
        this.prizeGoodsCountLeft = prizeGoodsCountLeft;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public String getPrizeGoodsDesc() {
        return prizeGoodsDesc;
    }

    public void setPrizeGoodsDesc(String prizeGoodsDesc) {
        this.prizeGoodsDesc = prizeGoodsDesc == null ? null : prizeGoodsDesc.trim();
    }

    public String getWinPercent() {
        return winPercent;
    }

    public void setWinPercent(String winPercent) {
        this.winPercent = winPercent == null ? null : winPercent.trim();
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