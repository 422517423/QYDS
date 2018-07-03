package net.dlyt.qyds.common.form;

import java.math.BigDecimal;
import java.util.Date;
import java.util.zip.Inflater;

/**
 * Created by YiLian on 16/7/28.
 */
public class MmbPointRecordForm {


    //分页信息
    private int  sEcho;

    //开始页面
    private int iDisplayStart;

    //每一页显示的项目
    private int iDisplayLength;

    /**
     * 会员ID
     */
    private String memberId;

    /**
     * 积分来源(订单ID)
     */
    private String scoreSource;

    /**
     * 积分规则ID
     */
    private String ruleId;

    /**
     * 积分消费ID
     */
    private String worthId;

    /**
     * 消费现金数额
     *
     * 只取整数处理
     *
     */
    private BigDecimal cash;

    /**
     * 积分抵值金额
     *
     * 只取整数处理
     */
    private BigDecimal worthCash;

    /**
     * 抵值积分
     *
     * 只取整数处理
     */
    private Integer worthPoint;

    /**
     * 换购积分规则ID
     */
    private String exchangeId;

    /**
     * 换购积分
     *
     * 只取整数处理
     */
    private Integer exchangePoint;

    /**
     * 赠送积分
     *
     * 只取整数处理
     */
    private Integer presentPoint;

    /**
     * 更新者
     */
    private String updateUserId;


    /**
     * 当前页数
     * 分页用:从0开始
     */
    private Integer currentPage = 0;

    public int getsEcho() {
        return sEcho;
    }

    public void setsEcho(int sEcho) {
        this.sEcho = sEcho;
    }

    public int getiDisplayStart() {
        return iDisplayStart;
    }

    public void setiDisplayStart(int iDisplayStart) {
        this.iDisplayStart = iDisplayStart;
    }

    public int getiDisplayLength() {
        return iDisplayLength;
    }

    public void setiDisplayLength(int iDisplayLength) {
        this.iDisplayLength = iDisplayLength;
    }

    public Integer getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    public BigDecimal getCash() {
        return cash;
    }

    public void setCash(BigDecimal cash) {
        this.cash = cash;
    }

    public BigDecimal getWorthCash() {
        return worthCash;
    }

    public void setWorthCash(BigDecimal worthCash) {
        this.worthCash = worthCash;
    }

    public Integer getWorthPoint() {
        return worthPoint;
    }

    public void setWorthPoint(Integer worthPoint) {
        this.worthPoint = worthPoint;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getScoreSource() {
        return scoreSource;
    }

    public void setScoreSource(String scoreSource) {
        this.scoreSource = scoreSource;
    }

    public String getRuleId() {
        return ruleId;
    }

    public void setRuleId(String ruleId) {
        this.ruleId = ruleId;
    }

    public String getWorthId() {
        return worthId;
    }

    public void setWorthId(String worthId) {
        this.worthId = worthId;
    }

    public String getUpdateUserId() {
        return updateUserId;
    }

    public void setUpdateUserId(String updateUserId) {
        this.updateUserId = updateUserId;
    }

    public String getExchangeId() {
        return exchangeId;
    }

    public void setExchangeId(String exchangeId) {
        this.exchangeId = exchangeId;
    }

    public Integer getExchangePoint() {
        return exchangePoint;
    }

    public void setExchangePoint(Integer exchangePoint) {
        this.exchangePoint = exchangePoint;
    }

    public Integer getPresentPoint() {
        return presentPoint;
    }

    public void setPresentPoint(Integer presentPoint) {
        this.presentPoint = presentPoint;
    }
}
