package net.dlyt.qyds.common.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class ErpReturnList implements Serializable {
    private BigDecimal amount;

    private String colorCode;

    private String no;

    private String ticketSubno;

    private Date actionType;

    private BigDecimal discount;

    private BigDecimal goodsNo;

    private BigDecimal size;

    private BigDecimal price;

    private BigDecimal count;

    private String actionCode;

    private String actionFlg;

    private Date goodsCode;

    private String goodsNameCn;

    private String comment;

    private String deleted;

    private String updateUserId;

    private Date updateTime;

    private String insertUserId;

    private Date insertTime;

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getColorCode() {
        return colorCode;
    }

    public void setColorCode(String colorCode) {
        this.colorCode = colorCode == null ? null : colorCode.trim();
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no == null ? null : no.trim();
    }

    public String getTicketSubno() {
        return ticketSubno;
    }

    public void setTicketSubno(String ticketSubno) {
        this.ticketSubno = ticketSubno == null ? null : ticketSubno.trim();
    }

    public Date getActionType() {
        return actionType;
    }

    public void setActionType(Date actionType) {
        this.actionType = actionType;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    public BigDecimal getGoodsNo() {
        return goodsNo;
    }

    public void setGoodsNo(BigDecimal goodsNo) {
        this.goodsNo = goodsNo;
    }

    public BigDecimal getSize() {
        return size;
    }

    public void setSize(BigDecimal size) {
        this.size = size;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getCount() {
        return count;
    }

    public void setCount(BigDecimal count) {
        this.count = count;
    }

    public String getActionCode() {
        return actionCode;
    }

    public void setActionCode(String actionCode) {
        this.actionCode = actionCode == null ? null : actionCode.trim();
    }

    public String getActionFlg() {
        return actionFlg;
    }

    public void setActionFlg(String actionFlg) {
        this.actionFlg = actionFlg == null ? null : actionFlg.trim();
    }

    public Date getGoodsCode() {
        return goodsCode;
    }

    public void setGoodsCode(Date goodsCode) {
        this.goodsCode = goodsCode;
    }

    public String getGoodsNameCn() {
        return goodsNameCn;
    }

    public void setGoodsNameCn(String goodsNameCn) {
        this.goodsNameCn = goodsNameCn == null ? null : goodsNameCn.trim();
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