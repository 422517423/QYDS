package net.dlyt.qyds.common.dto;

import java.io.Serializable;

/**
 * Created by wenxuechao on 16/8/27.
 */
public class AliPayReturnModel implements Serializable {

    private String id; // ID
    private String buyer_email; // 买家账户
    private String buyer_id; // 买家账户ID
    private String discount; // 折扣
    private String gmt_create; // 交易创建时间
    private String gmt_logistics_modify; // 物流修改时间
    private String gmt_payment; // 支付时间
    private String is_success; // 是否成功
    private String is_total_fee_adjust; // 是否修改价格
    private String logistics_fee; // 邮费
    private String logistics_payment; // 邮费支付方
    private String logistics_type; // 邮寄方式
    private String notify_id; // 通知ID
    private String notify_time; // 通知时间
    private String notify_type; // 通知种类
    private String out_trade_no; // 原交易号
    private String payment_type; // 支付种类
    private String price; // 价格
    private String quantity; // 数量
    private String receive_address; // 收货地址
    private String receive_mobile; // 收货人手机
    private String receive_name; // 收货人姓名
    private String receive_zip; // 收货邮政编码
    private String seller_actions; // 卖家动作
    private String seller_email; // 卖家账户
    private String seller_id; // 卖家账户ID
    private String subject; // 订单标题
    private String total_fee; // 总金额
    private String trade_no; // 支付宝交易号
    private String trade_status; // 交易状态
    private String use_coupon; // 赠券
    private String sign; // 签名
    private String sign_type; // 签名种类
    private String request_type; // 请求类型
    private String createtime; // 纪录创建时间
    private String refund_status; // 退款状态
    private String gmt_refund;// 交易退款时间

    private String batch_no; // 退款批次号
    private String success_num; // 退款成功总数
    private String result_details;// 退款结果明细

    public String getRefund_status() {
        return refund_status;
    }

    public void setRefund_status(String refund_status) {
        this.refund_status = refund_status;
    }

    public String getGmt_refund() {
        return gmt_refund;
    }

    public void setGmt_refund(String gmt_refund) {
        this.gmt_refund = gmt_refund;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBuyer_email() {
        return buyer_email;
    }

    public void setBuyer_email(String buyer_email) {
        this.buyer_email = buyer_email;
    }

    public String getBuyer_id() {
        return buyer_id;
    }

    public void setBuyer_id(String buyer_id) {
        this.buyer_id = buyer_id;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getGmt_create() {
        return gmt_create;
    }

    public void setGmt_create(String gmt_create) {
        this.gmt_create = gmt_create;
    }

    public String getGmt_logistics_modify() {
        return gmt_logistics_modify;
    }

    public void setGmt_logistics_modify(String gmt_logistics_modify) {
        this.gmt_logistics_modify = gmt_logistics_modify;
    }

    public String getGmt_payment() {
        return gmt_payment;
    }

    public void setGmt_payment(String gmt_payment) {
        this.gmt_payment = gmt_payment;
    }

    public String getIs_success() {
        return is_success;
    }

    public void setIs_success(String is_success) {
        this.is_success = is_success;
    }

    public String getIs_total_fee_adjust() {
        return is_total_fee_adjust;
    }

    public void setIs_total_fee_adjust(String is_total_fee_adjust) {
        this.is_total_fee_adjust = is_total_fee_adjust;
    }

    public String getLogistics_fee() {
        return logistics_fee;
    }

    public void setLogistics_fee(String logistics_fee) {
        this.logistics_fee = logistics_fee;
    }

    public String getLogistics_payment() {
        return logistics_payment;
    }

    public void setLogistics_payment(String logistics_payment) {
        this.logistics_payment = logistics_payment;
    }

    public String getLogistics_type() {
        return logistics_type;
    }

    public void setLogistics_type(String logistics_type) {
        this.logistics_type = logistics_type;
    }

    public String getNotify_id() {
        return notify_id;
    }

    public void setNotify_id(String notify_id) {
        this.notify_id = notify_id;
    }

    public String getNotify_time() {
        return notify_time;
    }

    public void setNotify_time(String notify_time) {
        this.notify_time = notify_time;
    }

    public String getNotify_type() {
        return notify_type;
    }

    public void setNotify_type(String notify_type) {
        this.notify_type = notify_type;
    }

    public String getOut_trade_no() {
        return out_trade_no;
    }

    public void setOut_trade_no(String out_trade_no) {
        this.out_trade_no = out_trade_no;
    }

    public String getPayment_type() {
        return payment_type;
    }

    public void setPayment_type(String payment_type) {
        this.payment_type = payment_type;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getReceive_address() {
        return receive_address;
    }

    public void setReceive_address(String receive_address) {
        this.receive_address = receive_address;
    }

    public String getReceive_mobile() {
        return receive_mobile;
    }

    public void setReceive_mobile(String receive_mobile) {
        this.receive_mobile = receive_mobile;
    }

    public String getReceive_name() {
        return receive_name;
    }

    public void setReceive_name(String receive_name) {
        this.receive_name = receive_name;
    }

    public String getReceive_zip() {
        return receive_zip;
    }

    public void setReceive_zip(String receive_zip) {
        this.receive_zip = receive_zip;
    }

    public String getSeller_actions() {
        return seller_actions;
    }

    public void setSeller_actions(String seller_actions) {
        this.seller_actions = seller_actions;
    }

    public String getSeller_email() {
        return seller_email;
    }

    public void setSeller_email(String seller_email) {
        this.seller_email = seller_email;
    }

    public String getSeller_id() {
        return seller_id;
    }

    public void setSeller_id(String seller_id) {
        this.seller_id = seller_id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getTotal_fee() {
        return total_fee;
    }

    public void setTotal_fee(String total_fee) {
        this.total_fee = total_fee;
    }

    public String getTrade_no() {
        return trade_no;
    }

    public void setTrade_no(String trade_no) {
        this.trade_no = trade_no;
    }

    public String getTrade_status() {
        return trade_status;
    }

    public void setTrade_status(String trade_status) {
        this.trade_status = trade_status;
    }

    public String getUse_coupon() {
        return use_coupon;
    }

    public void setUse_coupon(String use_coupon) {
        this.use_coupon = use_coupon;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getSign_type() {
        return sign_type;
    }

    public void setSign_type(String sign_type) {
        this.sign_type = sign_type;
    }

    public String getRequest_type() {
        return request_type;
    }

    public void setRequest_type(String request_type) {
        this.request_type = request_type;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public String getBatch_no() {
        return batch_no;
    }

    public void setBatch_no(String batch_no) {
        this.batch_no = batch_no;
    }

    public String getSuccess_num() {
        return success_num;
    }

    public void setSuccess_num(String success_num) {
        this.success_num = success_num;
    }

    public String getResult_details() {
        return result_details;
    }

    public void setResult_details(String result_details) {
        this.result_details = result_details;
    }
}
