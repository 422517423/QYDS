package net.dlyt.qyds.web.service;

import net.dlyt.qyds.common.dto.OrdMaster;
import net.dlyt.qyds.common.form.PayForm;

import java.util.Map;

/**
 * Created by wenxuechao on 16/8/27.
 */
public interface OrdAliPayService {

    /**
     * 校验订单信息
     * @param orderId
     * @return
     */
    String checkOrderInfo(String orderId);

    String checkCoupponOrderInfo(String orderId);

    /**
     * 获取订单信息
     * @param orderId
     * @return
     */
    OrdMaster getOrderInfo(String orderId);

    OrdMaster getCouponOrderInfo(String orderId);

    /**
     * 支付成功回调
     * @param mParam
     */
    void paySuccess(Map<String, String> mParam);
}
