package net.dlyt.qyds.common.dto;

import java.io.Serializable;
import java.util.Date;

public class ErpBankRecord implements Serializable {
    private Integer recordid;

    private String erpGoodsCode;

    private String erpSku;

    private String erpStoreid;

    private String banktype;

    private Integer inoutCount;

    private Date inoutTime;

    private String comment;

    private String deleted;

    private String updateUserId;

    private Date updateTime;

    private String insertUserId;

    private Date insertTime;

    public Integer getRecordid() {
        return recordid;
    }

    public void setRecordid(Integer recordid) {
        this.recordid = recordid;
    }

    public String getErpGoodsCode() {
        return erpGoodsCode;
    }

    public void setErpGoodsCode(String erpGoodsCode) {
        this.erpGoodsCode = erpGoodsCode == null ? null : erpGoodsCode.trim();
    }

    public String getErpSku() {
        return erpSku;
    }

    public void setErpSku(String erpSku) {
        this.erpSku = erpSku == null ? null : erpSku.trim();
    }

    public String getErpStoreid() {
        return erpStoreid;
    }

    public void setErpStoreid(String erpStoreid) {
        this.erpStoreid = erpStoreid == null ? null : erpStoreid.trim();
    }

    public String getBanktype() {
        return banktype;
    }

    public void setBanktype(String banktype) {
        this.banktype = banktype == null ? null : banktype.trim();
    }

    public Integer getInoutCount() {
        return inoutCount;
    }

    public void setInoutCount(Integer inoutCount) {
        this.inoutCount = inoutCount;
    }

    public Date getInoutTime() {
        return inoutTime;
    }

    public void setInoutTime(Date inoutTime) {
        this.inoutTime = inoutTime;
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