package net.dlyt.qyds.web.service;

import com.alibaba.fastjson.JSONObject;

/**
 * Created by YiLian on 16/8/18.
 */
public interface DashboardService {
    /**
     * 未发货订单数量
     *
     * @return
     */
    JSONObject countPendingDeliveryOrder();

    /**
     * 待退货订单数量
     *
     * @return
     */
    JSONObject countPendingRejectedOrder();

    /**
     * 上架商品数量
     *
     * @return
     */
    JSONObject countGoodsOnSell();

    /**
     * 注册会员数量
     *
     * @return
     */
    JSONObject countMember();

    /**
     * 订单数量(金额)趋势
     *
     * @return
     */
    JSONObject getOrderCountList();

    /**
     * 会员等级分布
     *
     * @return
     */
    JSONObject getMemberLevelList();

    /**
     * 会员注册绑定新增趋势
     *
     * @return
     */
    JSONObject getMemberAddList();

    /**
     * 销量排行前十商品
     *
     * @return
     */
    JSONObject getGoodsTopList();

    /**
     * 待指派门店订单数量统计
     * @return
     */
    JSONObject countPendingDispatchOrder();
}
