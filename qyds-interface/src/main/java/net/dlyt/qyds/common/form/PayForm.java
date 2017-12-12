package net.dlyt.qyds.common.form;

import java.io.Serializable;

/**
 * Created by wenxuechao on 16/8/27.
 */
public class PayForm implements Serializable {

    // 付款类型
    private String payType;

    // 订单ID
    private String orderId;

    // 订单编码
    private String orderCode;

    // 支付宝交易号
    private String tradeNo;

    // 返回地址
    private String returnUrl;

    // 通知地址
    private String notifyUrl;

    // 展示地址
    private String showUrl;

    /**
     * 付款类型取得<BR>
     *
     * @return payType
     */
    public String getPayType() {
        return payType;
    }

    /**
     * 付款类型赋值<BR>
     *
     * @param  payType
     */
    public void setPayType(String payType) {
        this.payType = payType;
    }

    /**
     * 订单ID取得<BR>
     *
     * @return orderId
     */
    public String getOrderId() {
        return orderId;
    }

    /**
     * 订单ID赋值<BR>
     *
     * @param  orderId
     */
    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    /**
     * 订单编码取得<BR>
     *
     * @return orderCode
     */
    public String getOrderCode() {
        return orderCode;
    }

    /**
     * 订单编码赋值<BR>
     *
     * @param  orderCode
     */
    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    /**
     * 支付宝交易号取得<BR>
     *
     * @return tradeNo
     */
    public String getTradeNo() {
        return tradeNo;
    }

    /**
     * 支付宝交易号赋值<BR>
     *
     * @param  tradeNo
     */
    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    /**
     * 返回地址取得<BR>
     *
     * @return returnUrl
     */
    public String getReturnUrl() {
        return returnUrl;
    }

    /**
     * 返回地址赋值<BR>
     *
     * @param  returnUrl
     */
    public void setReturnUrl(String returnUrl) {
        this.returnUrl = returnUrl;
    }

    /**
     * 通知地址取得<BR>
     *
     * @return notifyUrl
     */
    public String getNotifyUrl() {
        return notifyUrl;
    }

    /**
     * 通知地址赋值<BR>
     *
     * @param  notifyUrl
     */
    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    /**
     * 展示地址取得<BR>
     *
     * @return showUrl
     */
    public String getShowUrl() {
        return showUrl;
    }

    /**
     * 展示地址赋值<BR>
     *
     * @param  showUrl
     */
    public void setShowUrl(String showUrl) {
        this.showUrl = showUrl;
    }
}
