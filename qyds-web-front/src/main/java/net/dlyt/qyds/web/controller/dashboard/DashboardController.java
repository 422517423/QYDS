package net.dlyt.qyds.web.controller.dashboard;

import com.alibaba.fastjson.JSONObject;
import net.dlyt.qyds.web.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by YiLian on 16/8/18.
 */
@Controller
@RequestMapping("/dashboard")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;


    /**
     * 未发货订单数量
     *
     * @return
     */
    @RequestMapping("getPendingDispatchOrderCount")
    @ResponseBody
    public JSONObject countPendingDeliveryOrder() {
        return dashboardService.countPendingDispatchOrder();
    }

    /**
     * 待退货订单数量
     *
     * @return
     */
    @RequestMapping("getPendingRejectedOrderCount")
    @ResponseBody
    public JSONObject countPendingRejectedOrder() {
        return dashboardService.countPendingRejectedOrder();
    }

    /**
     * 上架商品数量
     *
     * @return
     */
    @RequestMapping("getGoodsOnSellCount")
    @ResponseBody
    public JSONObject countGoodsOnSell() {
        return dashboardService.countGoodsOnSell();
    }

    /**
     * 注册会员数量
     *
     * @return
     */
    @RequestMapping("getMemberCount")
    @ResponseBody
    public JSONObject countMember() {
        return dashboardService.countMember();
    }

    /**
     * 订单数量(金额)趋势
     *
     * @return
     */
    @RequestMapping("getOrderCountList")
    @ResponseBody
    public JSONObject getOrderCountList() {
        return dashboardService.getOrderCountList();
    }

    /**
     * 会员等级分布
     *
     * @return
     */
    @RequestMapping("getMemberLevelList")
    @ResponseBody
    public JSONObject getMemberLevelList() {
        return dashboardService.getMemberLevelList();
    }

    /**
     * 会员注册绑定新增趋势
     *
     * @return
     */
    @RequestMapping("getMemberAddList")
    @ResponseBody
    public JSONObject getMemberAddList() {
        return dashboardService.getMemberAddList();
    }

    /**
     * 销量排行前十商品
     *
     * @return
     */
    @RequestMapping("getGoodsTopList")
    @ResponseBody
    public JSONObject getGoodsTopList() {
        return dashboardService.getGoodsTopList();
    }

}
