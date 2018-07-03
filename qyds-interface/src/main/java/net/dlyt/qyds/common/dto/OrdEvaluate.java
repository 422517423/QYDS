package net.dlyt.qyds.common.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class OrdEvaluate implements Serializable {
    private String evaluateId;

    private String type;

    private String memberId;

    private String orderId;

    private String scoreJson;

    private BigDecimal scoreAll;

    private BigDecimal scoreService;

    private BigDecimal scoreQuality;

    private BigDecimal scoreDelivery;

    private BigDecimal scoreSpeed;

    private String content;

    private Date evaluateTime;

    private String answerContent;

    private String answerUserId;

    private Date answerTime;

    private String deleted;

    private String updateUserId;

    private Date updateTime;

    private String insertUserId;

    private Date insertTime;

    public String getEvaluateId() {
        return evaluateId;
    }

    public void setEvaluateId(String evaluateId) {
        this.evaluateId = evaluateId == null ? null : evaluateId.trim();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId == null ? null : memberId.trim();
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId == null ? null : orderId.trim();
    }

    public String getScoreJson() {
        return scoreJson;
    }

    public void setScoreJson(String scoreJson) {
        this.scoreJson = scoreJson == null ? null : scoreJson.trim();
    }

    public BigDecimal getScoreAll() {
        return scoreAll;
    }

    public void setScoreAll(BigDecimal scoreAll) {
        this.scoreAll = scoreAll;
    }

    public BigDecimal getScoreService() {
        return scoreService;
    }

    public void setScoreService(BigDecimal scoreService) {
        this.scoreService = scoreService;
    }

    public BigDecimal getScoreQuality() {
        return scoreQuality;
    }

    public void setScoreQuality(BigDecimal scoreQuality) {
        this.scoreQuality = scoreQuality;
    }

    public BigDecimal getScoreDelivery() {
        return scoreDelivery;
    }

    public void setScoreDelivery(BigDecimal scoreDelivery) {
        this.scoreDelivery = scoreDelivery;
    }

    public BigDecimal getScoreSpeed() {
        return scoreSpeed;
    }

    public void setScoreSpeed(BigDecimal scoreSpeed) {
        this.scoreSpeed = scoreSpeed;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    public Date getEvaluateTime() {
        return evaluateTime;
    }

    public void setEvaluateTime(Date evaluateTime) {
        this.evaluateTime = evaluateTime;
    }

    public String getAnswerContent() {
        return answerContent;
    }

    public void setAnswerContent(String answerContent) {
        this.answerContent = answerContent == null ? null : answerContent.trim();
    }

    public String getAnswerUserId() {
        return answerUserId;
    }

    public void setAnswerUserId(String answerUserId) {
        this.answerUserId = answerUserId == null ? null : answerUserId.trim();
    }

    public Date getAnswerTime() {
        return answerTime;
    }

    public void setAnswerTime(Date answerTime) {
        this.answerTime = answerTime;
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