package net.dlyt.qyds.web.controller.erp;

import com.alibaba.fastjson.JSONObject;
import net.dlyt.qyds.web.service.ErpBrandService;
import net.dlyt.qyds.web.service.ErpSendService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * Created by zlh on 2016/7/30.
 */
@Controller
@RequestMapping("/erp_send")
public class ErpSendController {

    /**
     * 发送指定的商品分类
     *
     */
    @Resource
    private ErpSendService service;

    /**
     * 发送指定的商品分类
     *
     */
    @RequestMapping("sendGoodsTypeById")
    public @ResponseBody
    JSONObject sendGoodsTypeById(String goodId){
        return service.sendGoodsTypeById(goodId);
    }

    /**
     * 发送指定的会员信息
     *
     */
    @RequestMapping("sendMemberById")
    public @ResponseBody
    JSONObject sendMemberById(String memberId){
        return service.sendMemberById(memberId);
    }

    /**
     * 发送指定的会员积分记录
     *
     */
    @RequestMapping("sendPointRecordById")
    public @ResponseBody
    JSONObject sendPointRecordById(@RequestParam("recordNo") int recordNo){
        return service.sendPointRecordById(recordNo);
    }

    /**
     * 发送指定的新订单
     *
     */
    @RequestMapping("sendOrderById")
    public @ResponseBody
    JSONObject sendOrderById(String orderId){
        return service.sendOrderById(orderId);
    }

    /**
     * 发送指定的退货订单
     *
     */
    @RequestMapping("sendReturnOrderByOrderId")
    public @ResponseBody
    JSONObject sendReturnOrderByOrderId(String orderId){
        return service.sendReturnOrderByOrderId(orderId);
    }

    /**
     * 发送指定的退货子订单
     *
     */
    @RequestMapping("sendReturnOrderBySubOrderId")
    public @ResponseBody
    JSONObject sendReturnOrderBySubOrderId(String subOrderId){
        return service.sendReturnOrderBySubOrderId(subOrderId);
    }

    /**
     * 发送指定的优惠券
     *
     */
    @RequestMapping("sendCouponById")
    public @ResponseBody
    JSONObject sendCouponById(String couponId){
        return service.sendCouponById(couponId);
    }

    /**
     * 发送指定的优惠券绑定SKU
     *
     */
//    @RequestMapping("sendCouponSkuById")
//    public @ResponseBody
//    JSONObject sendCouponSkuById(String couponGoodsId){
//        return service.sendCouponSkuById(couponGoodsId);
//    }

    /**
     * 发送指定的会员优惠券
     *
     */
    @RequestMapping("sendCouponMemberById")
    public @ResponseBody
    JSONObject sendCouponMemberById(String couponMemberId){
        return service.sendCouponMemberById(couponMemberId);
    }

    /**
     * 发送指定的已使用优惠券
     *
     */
    @RequestMapping("sendCouponUsedById")
    public @ResponseBody
    JSONObject sendCouponUsedById(String couponMemberId){
        return service.sendCouponUsedById(couponMemberId);
    }
}
