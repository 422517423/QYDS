package net.dlyt.qyds.web.service;

import com.alibaba.fastjson.JSONObject;

/**
 * Created by zlh on 2016/9/4.
 */
public interface ErpCouponService {

    /**
     * 接受ERP的优惠券
     *
     */
    JSONObject receiveCoupon(String data);

    /**
     * 接受ERP的优惠券绑定SKU
     *
     */
    JSONObject receiveCouponSku(String data);

    /**
     * 接受ERP的会员优惠券
     *
     */
    JSONObject receiveCouponMember(String data);

    /**
     * 接受ERP的已使用优惠券
     *
     */
    JSONObject receiveCouponUsed(String data);

    /**
     * 验证ERP的要使用优惠券
     *
     */
    JSONObject checkCoupon(String data);
}
