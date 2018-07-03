package net.dlyt.qyds.web.service;

import com.alibaba.fastjson.JSONObject;

/**
 * Created by zlh on 2016/9/4.
 */
public interface ErpSendService {

    /**
     * 发送未成功的商品分类
     *
     */
    JSONObject sendFailGoodsType();

    /**
     * 发送指定的商品分类
     *
     */
    JSONObject sendGoodsTypeById(String id);

    /**
     * 发送未成功的会员信息
     *
     */
    JSONObject sendFailMember();

    /**
     * 发送指定的会员信息
     *
     */
    JSONObject sendMemberById(String id);

    /**
     * 发送未成功的会员积分记录
     *
     */
    JSONObject sendFailPointRecord();

    /**
     * 发送指定的会员积分记录
     *
     */
    JSONObject sendPointRecordById(Integer id);

    /**
     * 发送未成功的优惠券
     *
     */
    JSONObject sendFailCoupon();

    /**
     * 发送指定的优惠券
     *
     */
    JSONObject sendFailCouponById(String id);

    /**
     * 发送指定的优惠券
     *
     */
    JSONObject sendCouponById(String id);

    /**
     * 发送指定的会员优惠券
     *
     */
    JSONObject sendFailCouponMember();

    /**
     * 发送指定的会员优惠券
     *
     */
    JSONObject sendFailCouponMemberById(String id);

    /**
     * 发送指定的会员优惠券
     *
     */
    JSONObject sendCouponMemberById(String id);

    /**
     * 发送未成功的已使用优惠券
     *
     */
    JSONObject sendFailCouponUsed();

    /**
     * 发送指定的已使用优惠券
     *
     */
    JSONObject sendFailCouponUsedById(String id);

    /**
     * 发送指定的已使用优惠券
     *
     */
    JSONObject sendCouponUsedById(String id);

    /**
     * 发送未成功的新订单
     *
     */
    JSONObject sendFailOrder();

    /**
     * 发送指定的新订单
     *
     */
    JSONObject sendOrderById(String id);

    /**
     * 发送未成功的退货订单
     *
     */
    JSONObject sendFailReturnOrder();

    /**
     * 发送指定的退货订单
     *
     */
    JSONObject sendReturnOrderByOrderId(String id);

    /**
     * 发送调货订单
     *
     */
    JSONObject sendFailBankUpdate();

    /**
     * 发送指定的调货订单
     *
     */
    JSONObject sendFailBankUpdateById(String id);

    /**
     * 发送指定的退货子订单
     *
     */
    JSONObject sendReturnOrderBySubOrderId(String id);

    /**
     * 发送指定的已使用优惠券
     *
     */
    JSONObject selectSendFailOrder();

    /**
     * 发送未成功的禁用会员
     *
     */
    JSONObject sendFailMemberUsed();

    /**
     * 20171225
     * 发送未成功的物流状态通知
     */
    JSONObject sendFailExpress();
}
