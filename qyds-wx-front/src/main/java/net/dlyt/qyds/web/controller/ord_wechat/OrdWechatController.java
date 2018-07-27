package net.dlyt.qyds.web.controller.ord_wechat;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import net.dlyt.qyds.common.dto.*;
import net.dlyt.qyds.common.dto.ext.MmbShoppingSKuExt;
import net.dlyt.qyds.common.form.OrdLogisticsForm;
import net.dlyt.qyds.common.form.OrdReturnWechatForm;
import net.dlyt.qyds.common.form.OrdSubmitForm;
import net.dlyt.qyds.common.form.OrdWechatForm;
import net.dlyt.qyds.web.common.Constants;
import net.dlyt.qyds.web.common.DataUtils;
import net.dlyt.qyds.web.quartz.QydsSchedulerFactory;
import net.dlyt.qyds.web.service.BnkMasterService;
import net.dlyt.qyds.web.service.OrdMasterService;
import net.dlyt.qyds.web.service.common.StringUtil;
import net.dlyt.qyds.web.service.exception.ExceptionBusiness;
import net.dlyt.qyds.web.service.exception.ExceptionErrorParam;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;


/**
 * @功能 微信端订单模块调用接口
 * Created by wenxuechao on 16/7/26.
 */
@Controller
@RequestMapping("/ord_wechat")
public class OrdWechatController {

    @Autowired
    private OrdMasterService ordMasterService;

    @Autowired
    private BnkMasterService bnkMasterService;

    /**
     * 根据买家ID获取订单列表信息
     * @param data
     * @return json
     */
    @RequestMapping("getOrderList")
    public @ResponseBody
    JSONObject getOrderList(String data) {
        OrdWechatForm form = (OrdWechatForm) JSON.parseObject(data, OrdWechatForm.class);

        JSONObject json = new JSONObject();

        try {
            //获取查询条件数据
            OrdMasterExt ordMasterExt = new OrdMasterExt();
            ordMasterExt.setMemberId(form.getMemberId());
            ordMasterExt.setOrderStatus(form.getOrderStatus());
            ordMasterExt.setPayStatus(form.getPayStatus());
            ordMasterExt.setDeliverStatus(form.getDeliverStatus());
            ordMasterExt.setNeedColumns(Integer.parseInt(form.getIDisplayLength()));
            ordMasterExt.setStartPoint(Integer.parseInt(form.getIDisplayStart()));

            List<OrdMasterExt> list = ordMasterService.getOrdListByUserId(ordMasterExt);
            //获取订单条数
            int ordCounts = ordMasterService.getAllDatasCount(ordMasterExt);

            if(list == null || list.size() == 0){
                json.put("aaData", null);
                json.put("iTotalRecords", 0);
                json.put("iTotalDisplayRecords", 0);
                json.put("resultCode", Constants.NORMAL);
            }else{
                JSONArray arrayMaster = new JSONArray();
                JSONObject jsonObject = null;
                for(OrdMasterExt ome : list){
                    jsonObject = new JSONObject();
                    jsonObject.put("orderId", ome.getOrderId());
                    jsonObject.put("orderCode", ome.getOrderCode());
                    jsonObject.put("orderType", ome.getOrderType());
                    jsonObject.put("amountTotle", ome.getAmountTotle());
                    jsonObject.put("orderTime", DataUtils.formatTimeStampToYMD(ome.getOrderTime()));
                    //传递前台显示订单状态
                    String orderStatusName = orderStatusNameGet(ome.getOrderStatus(),ome.getPayStatus(),ome.getDeliverStatus());
                    jsonObject.put("orderStatusName", orderStatusName);
                    jsonObject.put("orderStatus", ome.getOrderStatus());
                    jsonObject.put("payStatusCode", ome.getPayStatus());
                    jsonObject.put("deliverStatusCode", ome.getDeliverStatus());
                    jsonObject.put("payStatus", ome.getPayStatus());
                    jsonObject.put("shopName", ome.getShopName());
                    jsonObject.put("deliveryFree", ome.getDeliveryFree());
                    jsonObject.put("deliveryFee", ome.getDeliveryFee());
                    jsonObject.put("canDivide", ome.getCanDivide());
                    jsonObject.put("actionName", ome.getActionName());
                    jsonObject.put("couponMemberId", ome.getCouponMemberId());
                    jsonObject.put("amountCoupon", ome.getAmountCoupon());
                    jsonObject.put("couponId",list.get(0).getCouponMemberId());
                    jsonObject.put("coupon",list.get(0).getCoupon());
                    jsonObject.put("payInfact", ome.getPayInfact());
                    JSONArray arrayOrd = new JSONArray();
                    JSONObject jsonOrd = null;
                    for(OrdList ol : ome.getOrdList()){
                        jsonOrd = new JSONObject();
                        jsonOrd.put("detailId",ol.getDetailId());
                        jsonOrd.put("type",ol.getType());
                        jsonOrd.put("goodsId",ol.getGoodsId());
                        jsonOrd.put("goodsCode",ol.getGoodsCode());
                        if(ol.getImageUrlJson() != null && !"".equals(ol.getImageUrlJson())){
                            jsonOrd.put("imageUrlJson",ol.getImageUrlJson());
                        }else{
                            if(ol.getSku() != null && !"".equals(ol.getSku())){
                                List<Map<String, Object>> listMap = JSONArray.parseObject(ol.getSku(),ArrayList.class);
                                if(listMap != null && listMap.size() > 0){
                                    for(int i=0;i<listMap.size();i++){
                                        if(listMap.get(i).get("sku_img") != null){
                                            jsonOrd.put("imageUrlJson",((List)listMap.get(i).get("sku_img")).get(0));
                                            break;
                                        }
                                    }

                                }
                            }
                        }
                        jsonOrd.put("goodsName",ol.getGoodsName());
                        jsonOrd.put("sku",ol.getSku());
                        jsonOrd.put("price",ol.getPrice());
                        jsonOrd.put("actionName",ol.getActionName());
                        jsonOrd.put("priceDiscount",ol.getPriceDiscount());
                        jsonOrd.put("quantity",ol.getQuantity());
                        jsonOrd.put("amount",ol.getAmount());
                        jsonOrd.put("amountDiscount",ol.getAmountDiscount());
                        arrayOrd.add(jsonOrd);
                    }
                    jsonObject.put("ordList",arrayOrd);
                    arrayMaster.add(jsonObject);
                }

                json.put("aaData", arrayMaster);
                json.put("iTotalRecords", ordCounts);
                json.put("iTotalDisplayRecords", ordCounts);
                json.put("resultCode", Constants.NORMAL);
            }
        }catch(Exception e){
            json.put("resultCode", Constants.FAIL);
        }
        return json;
    }

    /**
     * 根据买家ID获取订单个数
     * @param data
     * @return json
     */
    @RequestMapping("getOrderCountByMemberId")
    @ResponseBody
    public JSONObject getOrderCountByMemberId(String data){
        OrdMasterExt ordMasterExt = JSON.parseObject(data, OrdMasterExt.class);
        return ordMasterService.getOrderCountByMemberId(ordMasterExt);

    }

    /**
     * 根据买家ID获取订单列表
     * @param data
     * @return json
     */
    @RequestMapping("getOrderListByMemberId")
    @ResponseBody
    public JSONObject getOrderListByMemberId(String data){
        OrdMasterExt ordMasterExt = JSON.parseObject(data, OrdMasterExt.class);
        return ordMasterService.getOrderListByMemberId(ordMasterExt);

    }

    /**
     * 根据订单状态,发货状态,付款状态判断订单状态文字信息,此段代码依据<订单流程.xlsx>
     * @param orderStatus
     * @param payStatus
     * @param deliverStatus
     * @return
     */
    private String orderStatusNameGet(String orderStatus, String payStatus, String deliverStatus) {
        String result = "";
        //订单未付款场合,传递待付款
        if ("10".equals(orderStatus) && "10".equals(deliverStatus) && "10".equals(payStatus)) {
            result = "待付款";
        } else if ("11".equals(orderStatus) && "10".equals(deliverStatus) && "10".equals(payStatus)) {
            result = "订单取消";
        } else if ("10".equals(orderStatus) && "10".equals(deliverStatus) && "20".equals(payStatus)) {
            result = "待发货";
        } else if ("20".equals(orderStatus) && "10".equals(deliverStatus) && "20".equals(payStatus)) {
            result = "申请退款中";
        } else if (("10".equals(orderStatus) || "23".equals(orderStatus)) && ("20".equals(deliverStatus) || "19".equals(deliverStatus)) && "20".equals(payStatus)) {
            result = "待收货";
        } else if ("21".equals(orderStatus) && "10".equals(deliverStatus) && "30".equals(payStatus)) {
            result = "退款中";
        } else if ("23".equals(orderStatus) && "10".equals(deliverStatus) && "20".equals(payStatus)) {
            result = "待发货";
        } else if ("10".equals(orderStatus) && "21".equals(deliverStatus) && "20".equals(payStatus)) {
            result = "已收货";
        } else if ("23".equals(orderStatus) && "21".equals(deliverStatus) && "20".equals(payStatus)) {
            result = "已收货";
        } else if ("22".equals(orderStatus) && "10".equals(deliverStatus) && "31".equals(payStatus)) {
            result = "退款完成";
        } else if ("90".equals(orderStatus) && "21".equals(deliverStatus) && "20".equals(payStatus)) {
            result = "订单完成";
        } else if ("30".equals(orderStatus) && "21".equals(deliverStatus) && "20".equals(payStatus)) {
            result = "退货申请中";
        } else if ("31".equals(orderStatus) && "30".equals(deliverStatus) && "20".equals(payStatus)) {
            result = "退货待收货";
        } else if ("33".equals(orderStatus) && "21".equals(deliverStatus) && "20".equals(payStatus)) {
            result = "退货驳回";
        } else if ("31".equals(orderStatus) && "31".equals(deliverStatus) && "30".equals(payStatus)) {
            result = "退款中";
        } else if ("32".equals(orderStatus) && "31".equals(deliverStatus) && "31".equals(payStatus)) {
            result = "退货完成";
        } else {
            result = "未知状态("+orderStatus+deliverStatus+payStatus+")";
        }
        return result;
    }


    /**
     * 根据订单id获取订单详细信息
     * @param data
     * @return
     */
    @RequestMapping("getOrderDetail")
    public @ResponseBody
    JSONObject getOrderDetail(String data) {

        OrdWechatForm form = (OrdWechatForm) JSON.parseObject(data, OrdWechatForm.class);

        JSONObject json = new JSONObject();

        try {
            //获取查询条件数据
            OrdMasterExt ordMasterExt = new OrdMasterExt();
            ordMasterExt.setOrderId(form.getOrderId());
            //根据订单id获取订单详细信息
            List<OrdMasterExt> list = ordMasterService.getOrdDetailByOrderId(ordMasterExt);
            if(list == null || list.size() == 0){
                json.put("resultCode", Constants.INVALID);
            }else{
                JSONObject jsonMaster = new JSONObject();
                //商品信息JSON
                JSONArray arrayOrd = new JSONArray();
                //物流信息JSON
                JSONArray arraySub = new JSONArray();
                //整理订单主信息
                jsonMaster.put("couponMasterList",list.get(0).getCouponMasterList());
                jsonMaster.put("orderId",list.get(0).getOrderId());
                jsonMaster.put("orderCode",list.get(0).getOrderCode());
                jsonMaster.put("orderType",list.get(0).getOrderType());
                jsonMaster.put("shopId",list.get(0).getShopId());
                jsonMaster.put("shopName",list.get(0).getShopName());
                jsonMaster.put("amountTotle",list.get(0).getAmountTotle());
                jsonMaster.put("actionId",list.get(0).getActionId());
                jsonMaster.put("actionName",list.get(0).getActionName());
                jsonMaster.put("amountDiscount",list.get(0).getAmountDiscount());
                jsonMaster.put("couponId",list.get(0).getCouponMemberId());
                jsonMaster.put("coupon",list.get(0).getCoupon());
                jsonMaster.put("couponMemberId",list.get(0).getCouponMemberId());
                jsonMaster.put("amountCoupon",list.get(0).getAmountCoupon());
                jsonMaster.put("pointCount",list.get(0).getPointCount());
                jsonMaster.put("amountPoint",list.get(0).getAmountPoint());
                jsonMaster.put("deliveryFree",list.get(0).getDeliveryFree());
                jsonMaster.put("deliveryFee",list.get(0).getDeliveryFee());
                jsonMaster.put("payDeliveryType",list.get(0).getPayDeliveryType());
                jsonMaster.put("payInfact",list.get(0).getPayInfact());
                jsonMaster.put("serviceFee",list.get(0).getServiceFee());
                jsonMaster.put("payType",list.get(0).getPayType());
                jsonMaster.put("message",list.get(0).getMessage());
                //传递前台显示订单状态
                String orderStatusName = orderStatusNameGet(list.get(0).getOrderStatus(),list.get(0).getPayStatus(),list.get(0).getDeliverStatus());
                jsonMaster.put("orderStatusName", orderStatusName);
                jsonMaster.put("orderStatus", list.get(0).getOrderStatus());
                jsonMaster.put("payStatusCode", list.get(0).getPayStatus());
                jsonMaster.put("deliverStatus", list.get(0).getDeliverStatus());
                jsonMaster.put("orderTime",list.get(0).getOrderTime());
                jsonMaster.put("payStatus",list.get(0).getPayStatus());
                jsonMaster.put("deliverTime",list.get(0).getDeliverTime());
                jsonMaster.put("evaluateStatus",list.get(0).getEvaluateStatus());
                jsonMaster.put("wantInvoice",list.get(0).getWantInvoice());
                jsonMaster.put("invoiceTitle",list.get(0).getInvoiceTitle());
                jsonMaster.put("invoiceAddress",list.get(0).getInvoiceAddress());
                jsonMaster.put("invoiceTel",list.get(0).getInvoiceTel());
                jsonMaster.put("invoiceTaxno",list.get(0).getInvoiceTaxno());
                jsonMaster.put("invoiceBank",list.get(0).getInvoiceBank());
                jsonMaster.put("invoiceAccount",list.get(0).getInvoiceAccount());
                jsonMaster.put("deliveryAddress",list.get(0).getDeliveryAddress());
                jsonMaster.put("deliveryContactor",list.get(0).getDeliveryContactor());
                jsonMaster.put("deliveryPhone",list.get(0).getDeliveryPhone());
                jsonMaster.put("deliveryPostcode",list.get(0).getDeliveryPostcode());
                jsonMaster.put("expressName",list.get(0).getExpressName());
                jsonMaster.put("expressNo",list.get(0).getExpressNo());
                jsonMaster.put("deliverType",list.get(0).getDeliverType());
                jsonMaster.put("storeName",list.get(0).getStoreName());
                jsonMaster.put("storePhone",list.get(0).getStorePhone());
                jsonMaster.put("storeAddress",list.get(0).getStoreAddress());
                jsonMaster.put("canReturn",list.get(0).getCanReturn());
                jsonMaster.put("canExchange",list.get(0).getCanExchange());
                jsonMaster.put("canDivide",list.get(0).getCanDivide());
                jsonMaster.put("cancelType",list.get(0).getCancelType());
                jsonMaster.put("comment",list.get(0).getComment());
                jsonMaster.put("noReturn",list.get(0).getNoReturn());
                //整理商品信息数据
                for(OrdList ol : list.get(0).getOrdList()){
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("detailId",ol.getDetailId());
                    jsonObject.put("type",ol.getType());
                    jsonObject.put("goodsCode",ol.getGoodsCode());
                    jsonObject.put("goodsId",ol.getGoodsId());
                    jsonObject.put("goodsName",ol.getGoodsName());
                    if(ol.getImageUrlJson() != null && !"".equals(ol.getImageUrlJson())){
                        jsonObject.put("imageUrlJson",ol.getImageUrlJson());
                    }else{
                        if(ol.getSku() != null && !"".equals(ol.getSku())){
                            List<Map<String, Object>> listMap = JSONArray.parseObject(ol.getSku(),ArrayList.class);
                            if(listMap != null && listMap.size() > 0){
                                for(int i=0;i<listMap.size();i++){
                                    if(listMap.get(i).get("sku_img") != null){
                                        jsonObject.put("imageUrlJson",((List)listMap.get(i).get("sku_img")).get(0));
                                        break;
                                    }
                                }

                            }
                        }
                    }
                    jsonObject.put("sku",ol.getSku());
                    jsonObject.put("price",ol.getPrice());
                    jsonObject.put("actionName",ol.getActionName());
                    jsonObject.put("priceDiscount",ol.getPriceDiscount());
                    jsonObject.put("quantity",ol.getQuantity());
                    jsonObject.put("amount",ol.getAmount());
                    jsonObject.put("amountDiscount",ol.getAmountDiscount());

                    if("30".equals(ol.getType())){
                        List<MmbShoppingSKuExt> sukList = ordMasterService.getSKUListByOrderDetailId(ol.getDetailId());
                        JSONArray arraySku = new JSONArray();
                        for (MmbShoppingSKuExt skuExt : sukList) {
                            JSONObject skuItem = new JSONObject();
                            skuItem.put("imageUrlJson",skuExt.getImageUrlJson());
                            skuItem.put("goodsName",skuExt.getGoodsName());
                            skuItem.put("colorName",skuExt.getColorName());
                            skuItem.put("skucontent",skuExt.getSkucontent());
                            skuItem.put("sizeName",skuExt.getSizeName());
                            skuItem.put("price",skuExt.getPrice());

                            arraySku.add(skuItem);
                        }

                        jsonObject.put("skuListInfo",arraySku);
                    }

                    arrayOrd.add(jsonObject);
                }
                jsonMaster.put("ordList",arrayOrd);

                //整理物流信息数据
                for(OrdSubList osl : list.get(0).getSubList()){
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("subOrderId",osl.getSubOrderId());
                    jsonObject.put("detailId",osl.getDetailId());
                    jsonObject.put("deliverStatus",osl.getDeliverStatus());
                    jsonObject.put("deliverTime",osl.getDeliverTime());
                    jsonObject.put("expressName",osl.getExpressName());
                    jsonObject.put("expressNo",osl.getExpressNo());
                    jsonObject.put("deliverType",osl.getDeliverType());
                    jsonObject.put("storeName",osl.getStoreName());
                    jsonObject.put("storePhone",osl.getStorePhone());
                    jsonObject.put("storeDeliveryName",osl.getStoreDeliveryName());
                    arraySub.add(jsonObject);
                }
                jsonMaster.put("deliverlist",arraySub);

                json.put("aaData", jsonMaster);
                json.put("resultCode", Constants.NORMAL);
            }
        }catch(Exception e){
            json.put("resultCode", Constants.FAIL);
        }

        return json;
    }

    /**
     * 买家确认收货
     * @功能位置 主订单确认收货按钮
     * @param data
     * @return
     */
    @RequestMapping("confirmReceiptInMaster")
    public @ResponseBody
    JSONObject confirmReceiptInMaster(String data){
        OrdMasterExt form = (OrdMasterExt) JSON.parseObject(data, OrdMasterExt.class);
        form.setUpdateUserId(form.getMemberId());
        return ordMasterService.confirmReceiptInMaster(form);
    }

    /**
     * 买家确认收货
     *
     * @param data memberId:会员ID
     *             orderId:订单编号 : 收货用标识符-门店自提
     *             expressNo:邮件编号 : 收货用标识符-物流送货
     * @return
     * @功能位置 物流列表单个物流确认收货
     */
    @RequestMapping("confirmReceived")
    public @ResponseBody
    JSONObject confirmReceiptInSub(String data){

        JSONObject json = new JSONObject();
        try {
            OrdLogisticsForm form = (OrdLogisticsForm) JSON.parseObject(data, OrdLogisticsForm.class);


            if(StringUtil.isEmpty(form.getMemberId())){
                throw new ExceptionErrorParam("缺少主键参数:会员信息");
            }

            if(StringUtil.isEmpty(form.getExpressNo()) && StringUtil.isEmpty(form.getOrderId())){
                throw new ExceptionErrorParam("缺少主键参数:订单号或邮件编号");
            }

            //确认收货结果集
            json = ordMasterService.confirmReceiptInSub(form);
            //判断是否全部收货,开启定时任务
            if(json != null && json.containsKey("orderCode")){
//                //开启定时任务
//                String id = UUID.randomUUID().toString();
//                QydsSchedulerFactory.doCancelReturnOrder(json.getString("orderCode"),id);
            }
        }catch(Exception e){
            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", e.getMessage());
        }
        return json;
    }

    /**
     * 买家删除订单
     * @功能位置 订单列表删除已完成订单
     * @触发条件 订单完成状态的订单允许删除订单
     * @param data
     * @return
     */
    @RequestMapping("deleteOrder")
    public @ResponseBody
    JSONObject deleteOrder(String data){
        OrdWechatForm form = (OrdWechatForm) JSON.parseObject(data, OrdWechatForm.class);
        JSONObject json = new JSONObject();

        try {
            //获取查询条件数据
            OrdMasterExt ordMasterExt = new OrdMasterExt();
            //获取订单id
            ordMasterExt.setOrderId(form.getOrderId());
            //获取用户id
            ordMasterExt.setMemberId(form.getMemberId());
            //删除订单结果集
            String result = ordMasterService.deleteOrder(ordMasterExt);
            //result为null场合,即未取到该条订单数据场合
            if(result == null){
                json.put("resultCode", Constants.INVALID);
            }else{
                json.put("resultCode", Constants.NORMAL);
            }
        }catch(Exception e){
            json.put("resultCode", Constants.FAIL);
        }
        return json;
    }

    /**
     * 买家取消订单
     * @功能位置 订单列表取消订单
     * @触发条件 只有订单状态为未完成,
     *          付款状态为未付款,
     *          发货状态为未发货
     * @param data
     * @return
     */
    @RequestMapping("cancelOrder")
    public @ResponseBody
    JSONObject cancelOrder(String data){
        OrdMasterExt ordMasterExt = (OrdMasterExt) JSON.parseObject(data, OrdMasterExt.class);
        return ordMasterService.cancelOrder(ordMasterExt);
    }


    /**
     * 买家主订单初始申请退货
     * @功能位置    主订单退货
     * @触发条件    1、没有活动的场合全单退货
     *             2、有活动的场合全单退货
     * @param data
     * @return
     */
    @RequestMapping("applyReturnGoods")
    public @ResponseBody
    JSONObject applyReturnGoods(String data){
        OrdReturnExchangeExt form = (OrdReturnExchangeExt) JSON.parseObject(data, OrdReturnExchangeExt.class);
        return ordMasterService.applyReturnGoods(form);
    }

    /**
     * 买家商品明细订单初始申请退货
     * @功能位置    子订单退货
     * @触发条件    没有活动的场合子单退货
     * @param data
     * @return
     */
    @RequestMapping("applyReturnSubGoods")
    public @ResponseBody
    JSONObject applyReturnSubGoods(String data){
        OrdReturnExchangeExt form = (OrdReturnExchangeExt) JSON.parseObject(data, OrdReturnExchangeExt.class);
        return ordMasterService.applyReturnSubGoods(form);
    }

    /**
     * 买家查阅退货信息
     * @功能位置    主订单查阅
     * @触发条件    退货场合
     * @param data
     * @return
     */
    @RequestMapping("viewApplyReturnGoods")
    public @ResponseBody
    JSONObject viewApplyReturnGoods(String data){
        OrdReturnWechatForm form = (OrdReturnWechatForm) JSON.parseObject(data, OrdReturnWechatForm.class);
        JSONObject json = new JSONObject();
        try {
            //获取查询条件数据
            OrdReturnExchangeExt ordReturnExchangeExt = new OrdReturnExchangeExt();
            //获取订单id
            ordReturnExchangeExt.setOrderId(form.getOrderId());
            //获取用户id
            ordReturnExchangeExt.setMemberId(form.getMemberId());
            //退货信息结果集
            List<OrdReturnExchangeExt> list = ordMasterService.viewApplyReturnGoods(ordReturnExchangeExt);
            if(list == null || list.size() == 0){
                json.put("resultCode", Constants.INVALID);
            }else{
                JSONObject jsonSub = new JSONObject();
                //退货信息集合
                JSONArray arraySub = new JSONArray();
                for(OrdReturnExchangeExt ore : list){
                    jsonSub.put("rexOrderId",ore.getRexOrderId());
                    jsonSub.put("orderId",ore.getOrderId());
                    jsonSub.put("subOrderId",ore.getSubOrderId());
                    jsonSub.put("orderCode",ore.getOrderCode());
                    jsonSub.put("memberId",ore.getMemberId());
                    jsonSub.put("rexType",ore.getRexType());
                    jsonSub.put("rexMode",ore.getRexMode());
                    jsonSub.put("rexPoint",ore.getRexPoint());
                    jsonSub.put("rexStoreName",ore.getRexStoreName());
                    jsonSub.put("rexStatus",ore.getRexStatus());
                    jsonSub.put("applyTime",ore.getApplyTime());
                    jsonSub.put("applyComment",ore.getApplyComment());
                    jsonSub.put("applyAnswerTime",ore.getApplyAnswerTime());
                    jsonSub.put("applyAnswerComment",ore.getApplyAnswerComment());
                    jsonSub.put("returnGoodsTime",ore.getReturnGoodsTime());
                    jsonSub.put("returnExpressName",ore.getReturnExpressName());
                    jsonSub.put("returnExpressNo",ore.getReturnExpressNo());
                    jsonSub.put("returnDeliveryFee",ore.getReturnDeliveryFee());
                    jsonSub.put("returnPayDelivery",ore.getReturnPayDelivery());
                    jsonSub.put("rebackExpressName",ore.getRebackExpressName());
                    jsonSub.put("rebackExpressNo",ore.getRebackExpressNo());
                    jsonSub.put("rebackDeliveryFee",ore.getRebackDeliveryFee());
                    jsonSub.put("rebackPayDelivery",ore.getRebackPayDelivery());
                    jsonSub.put("rebackAcceptTime",ore.getRebackAcceptTime());
                    jsonSub.put("refundStatus",ore.getRefundStatus());
                    jsonSub.put("refundGoods",ore.getRefundGoods());
                    jsonSub.put("deliveryFee",ore.getDeliveryFee());
                    jsonSub.put("refundInfact",ore.getRefundInfact());
                    jsonSub.put("rexOrderId",ore.getRexOrderId());
                    jsonSub.put("receiptAccount",ore.getReceiptAccount());
                    jsonSub.put("refundTime",ore.getRefundTime());
                    arraySub.add(jsonSub);
                }
                json.put("aaData", arraySub);
                json.put("resultCode", Constants.NORMAL);
            }
        }catch(Exception e){
            json.put("resultCode", Constants.FAIL);
        }
        return json;
    }


    /**
     * 买家主订单审批通过后申请退货
     * @功能位置    主订单退货
     * @param data
     * @return
     */
    @RequestMapping("reSubmitReturnGoodsByOrderId")
    public @ResponseBody
    JSONObject reSubmitReturnGoodsByOrderId(String data){
        OrdReturnWechatForm form = (OrdReturnWechatForm) JSON.parseObject(data, OrdReturnWechatForm.class);
        JSONObject json = new JSONObject();
        try {
            //获取查询条件数据
            OrdReturnExchangeExt ordReturnExchangeExt = new OrdReturnExchangeExt();
            //订单ID
            ordReturnExchangeExt.setOrderId(form.getOrderId());
            //退换货地点
            ordReturnExchangeExt.setRexPoint(form.getRexPoint());
            //退换货状态
            ordReturnExchangeExt.setRexStatus("21");
            //退换货门店id
            ordReturnExchangeExt.setRexStoreId(form.getRexStoreId());
            //退换货门店名称
            ordReturnExchangeExt.setRexStoreName(form.getRexStoreName());
            //退货发货人ID
            ordReturnExchangeExt.setReturnGoodsMemberid(form.getMemberId());
            //退货快递公司ID
            ordReturnExchangeExt.setReturnExpressId(form.getReturnExpressId());
            //退货快递公司名称
            ordReturnExchangeExt.setReturnExpressName(form.getReturnExpressName());
            //退货快递单号
            ordReturnExchangeExt.setReturnExpressNo(form.getReturnExpressNo());
            //退款状态 10 等待退款
            ordReturnExchangeExt.setRefundStatus("10");
            //主订单退货结果集
            String result = ordMasterService.reSubmitReturnGoodsByOrderId(ordReturnExchangeExt);
            //result为null场合,即未取到该条订单数据场合
            if(result == null){
                json.put("resultCode", Constants.INVALID);
            }else{
                json.put("resultCode", Constants.NORMAL);
            }
        }catch(Exception e){
            json.put("resultCode", Constants.FAIL);
        }
        return json;
    }

    /**
     * 买家立即购买非套装订单确认
     * @功能位置    商品详情
     * @触发条件    点击立即购买
     * @param data
     * @return
     */
    @RequestMapping("checkConfirmOrderFromGoods")
    public @ResponseBody
    JSONObject checkConfirmOrderFromGoods(String data){
        OrdWechatForm form = (OrdWechatForm) JSON.parseObject(data, OrdWechatForm.class);
        JSONObject json = new JSONObject();
        try {
            //获取查询条件数据
            OrdMasterExt ordMasterExt = new OrdMasterExt();
            //获取用户id
            ordMasterExt.setMemberId(form.getMemberId());
            //获取商品ID
            ordMasterExt.setGoodsId(form.getGoodsId());
            //商品类型
            ordMasterExt.setType(form.getType());
            //获取商品SKUID
            ordMasterExt.setGoodsSkuId(form.getGoodsSkuId());
            //获取商品购买数量
            ordMasterExt.setQuantity(form.getQuantity());
            //获取订单详情信息
            ordMasterService.checkConfirmOrderFromGoods(ordMasterExt);

            json.put("resultCode", Constants.NORMAL);

        }catch (ExceptionBusiness eb){
            json.put("resultCode", Constants.INVALID);
            json.put("resultMessage", eb.getMessage());
        }catch(Exception e){
            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", "服务异常，请刷新重试");
        }
        return json;
    }

    /**
     * 买家立即购买非套装订单确认
     * @功能位置    商品详情
     * @触发条件    点击立即购买
     * @param data
     * @return
     */
    @RequestMapping("confirmOrderFromGoods")
    public @ResponseBody
    JSONObject confirmOrderFromGoods(String data){
        OrdWechatForm form = (OrdWechatForm) JSON.parseObject(data, OrdWechatForm.class);
        JSONObject json = new JSONObject();
        try {
            //获取查询条件数据
            OrdMasterExt ordMasterExt = new OrdMasterExt();
            //获取用户id
            ordMasterExt.setMemberId(form.getMemberId());
            //获取商品ID
            ordMasterExt.setGoodsId(form.getGoodsId());
            //商品类型
            ordMasterExt.setType(form.getType());
            //获取商品SKUID
            ordMasterExt.setGoodsSkuId(form.getGoodsSkuId());
            //获取商品购买数量
            ordMasterExt.setQuantity(form.getQuantity());
            //获取订单详情信息
            OrdConfirmExt ordConfirmExt = ordMasterService.confirmOrderFromGoods(ordMasterExt);
            //数据不存在场合,业务异常
            if(ordConfirmExt == null){
                json.put("resultCode", Constants.INVALID);
                json.put("resultMessage", "商品已下架或删除");
            }else{
//                //确认订单主信息
//                JSONObject jsonConfirm = new JSONObject();
//                //会员收货地址
//                JSONArray arrayMmbAddress = new JSONArray();
//                //订单活动信息
//                JSONArray arrayAction = new JSONArray();
//                //商品配送方式
//                JSONArray arrayDeliverType = new JSONArray();
//                //信息整理
//                jsonConfirm.put("goodsId",ordConfirmOrderExt.getGoodsId());
//                jsonConfirm.put("goodsName",ordConfirmOrderExt.getGoodsName());
//                jsonConfirm.put("skucontent",ordConfirmOrderExt.getSkucontent());
//                jsonConfirm.put("price",ordConfirmOrderExt.getPrice());
//                jsonConfirm.put("quantity",ordConfirmOrderExt.getQuantity());
//                jsonConfirm.put("shopId",ordConfirmOrderExt.getShopId());
//                jsonConfirm.put("shopName",ordConfirmOrderExt.getShopName());
//                jsonConfirm.put("skuId",ordConfirmOrderExt.getSkuId());
//                jsonConfirm.put("coloreName",ordConfirmOrderExt.getColoreName());
//                jsonConfirm.put("sizeName",ordConfirmOrderExt.getSizeName());
//                //地址信息
//                for(MmbAddress ma : ordConfirmOrderExt.getMmbAddressList()){
//                    JSONObject mmbAddress = new JSONObject();
//                    mmbAddress.put("addressId",ma.getAddressId());
//                    mmbAddress.put("isDefault",ma.getIsDefault());
//                    mmbAddress.put("districtidProvince",ma.getDistrictidProvince());
//                    mmbAddress.put("districtidCity",ma.getDistrictidCity());
//                    mmbAddress.put("districtidDistrict",ma.getDistrictidDistrict());
//                    mmbAddress.put("address",ma.getAddress());
//                    mmbAddress.put("postcode",ma.getPostcode());
//                    mmbAddress.put("contactor",ma.getContactor());
//                    mmbAddress.put("phone",ma.getPhone());
//                    arrayMmbAddress.add(mmbAddress);
//                }
//                jsonConfirm.put("mmbAddressList",arrayMmbAddress);
//                //活动信息
//                for(ActMaster am : ordConfirmOrderExt.getActMasterList()){
//                    JSONObject actMaster = new JSONObject();
//                    //TODO 等接口信息返回
//                    arrayAction.add(actMaster);
//                }
//                jsonConfirm.put("actMasterList",arrayAction);
//                //TODO 添加优惠券接口获取信息
//
//
//
//                //配送方式
//                for(ComCode cc : ordConfirmOrderExt.getDeliverType()){
//                    JSONObject deliverType = new JSONObject();
//                    deliverType.put("value",cc.getValue());
//                    deliverType.put("displayCn",cc.getDisplayCn());
//                    arrayDeliverType.add(deliverType);
//                }
//                jsonConfirm.put("deliverType",arrayDeliverType);
                json.put("results", ordConfirmExt);
                json.put("resultCode", Constants.NORMAL);
            }

        }catch(Exception e){
            json.put("resultCode", Constants.FAIL);
        }
        return json;
    }


    /**
     * 买家立即购买套装订单确认
     * @功能位置    商品详情
     * @触发条件    点击立即购买
     * @param data
     * @return
     */
    @RequestMapping("confirmOrderFromSuitGoods")
    public @ResponseBody
    JSONObject confirmOrderFromSuitGoods(String data){
        OrdWechatForm form = (OrdWechatForm) JSON.parseObject(data, OrdWechatForm.class);
        JSONObject json = new JSONObject();
        //[{"goodsId":"1001","goodsSkuId":"100001"},{"goodsId":"1002","goodsSkuId":"100002"}]
        // form.setGdIdAndSkuIdJson("");
        try {
            //获取查询条件数据
            OrdMasterExt ordMasterExt = new OrdMasterExt();
            //获取用户id
            ordMasterExt.setMemberId(form.getMemberId());
            //获取商品ID
            ordMasterExt.setGoodsId(form.getGoodsId());
            //获取商品SKUID
            ordMasterExt.setGdIdAndSkuIdJson(form.getGdIdAndSkuIdJson());
            //获取商品购买数量
            ordMasterExt.setQuantity(form.getQuantity());
            //获取订单详情信息
            OrdConfirmExt ordConfirmExt = ordMasterService.confirmOrderFromSuitGoods(ordMasterExt);
            //数据不存在场合,业务异常
            if(ordConfirmExt == null){
                json.put("resultCode", Constants.INVALID);
                json.put("resultMessage", "商品已下架或删除");
            }else{
//                //确认订单主信息
//                JSONObject jsonConfirm = new JSONObject();
//                //套装商品信息
//                JSONArray arrayGoodsInfo = new JSONArray();
//                //会员收货地址
//                JSONArray arrayMmbAddress = new JSONArray();
//                //订单活动信息
//                JSONArray arrayAction = new JSONArray();
//                //商品配送方式
//                JSONArray arrayDeliverType = new JSONArray();
//                //主信息
//                jsonConfirm.put("quantity",ordConfirmOrderExt.getQuantity());
//                jsonConfirm.put("shopId",ordConfirmOrderExt.getShopId());
//                jsonConfirm.put("shopName",ordConfirmOrderExt.getShopName());
//                //商品信息
//                for(OrdConfirmOrderUnitExt unit : ordConfirmOrderExt.getOrdConfirmOrderUnitExtList()){
//                    JSONObject goodsInfo = new JSONObject();
//                    goodsInfo.put("goodsId",unit.getGoodsId());
//                    goodsInfo.put("goodsName",unit.getGoodsName());
//                    goodsInfo.put("skucontent",unit.getSkucontent());
//                    goodsInfo.put("price",unit.getPrice());
//                    goodsInfo.put("skuId",unit.getSkuId());
//                    goodsInfo.put("coloreName",unit.getColorName());
//                    goodsInfo.put("sizeName",unit.getSizeName());
//                    arrayGoodsInfo.add(goodsInfo);
//                }
//                jsonConfirm.put("ordConfirmOrderUnitExtList",arrayGoodsInfo);
//                //地址信息
//                for(MmbAddress ma : ordConfirmOrderExt.getMmbAddressList()){
//                    JSONObject mmbAddress = new JSONObject();
//                    mmbAddress.put("addressId",ma.getAddressId());
//                    mmbAddress.put("isDefault",ma.getIsDefault());
//                    mmbAddress.put("districtidProvince",ma.getDistrictidProvince());
//                    mmbAddress.put("districtidCity",ma.getDistrictidCity());
//                    mmbAddress.put("districtidDistrict",ma.getDistrictidDistrict());
//                    mmbAddress.put("address",ma.getAddress());
//                    mmbAddress.put("postcode",ma.getPostcode());
//                    mmbAddress.put("contactor",ma.getContactor());
//                    mmbAddress.put("phone",ma.getPhone());
//                    arrayMmbAddress.add(mmbAddress);
//                }
//                jsonConfirm.put("mmbAddressList",arrayMmbAddress);
//                //活动信息
//                for(ActMaster am : ordConfirmOrderExt.getActMasterList()){
//                    JSONObject actMaster = new JSONObject();
//                    //TODO 等接口信息返回
//                    arrayAction.add(actMaster);
//                }
//                jsonConfirm.put("actMasterList",arrayAction);
//                //配送方式
//                for(ComCode cc : ordConfirmOrderExt.getDeliverType()){
//                    JSONObject deliverType = new JSONObject();
//                    deliverType.put("value",cc.getValue());
//                    deliverType.put("displayCn",cc.getDisplayCn());
//                    arrayDeliverType.add(deliverType);
//                }
//                jsonConfirm.put("deliverType",arrayDeliverType);

                json.put("results", ordConfirmExt);
                json.put("resultCode", Constants.NORMAL);
            }
        }catch (ExceptionBusiness eb){
            json.put("resultCode", Constants.INVALID);
            json.put("resultMessage", eb.getMessage());
        }catch(Exception e){
            json.put("resultCode", Constants.FAIL);
        }
        return json;
    }

    /**
     * 买家购物车订单确认
     * @功能位置    我的购物车
     * @触发条件    购物车选择确认订单
     * @param data
     * @return
     */
    @RequestMapping("checkConfirmOrderFromBag")
    public @ResponseBody
    JSONObject checkConfirmOrderFromBag(String data){
        OrdWechatForm form = (OrdWechatForm) JSON.parseObject(data, OrdWechatForm.class);
        JSONObject json = new JSONObject();
        try {
            //获取查询条件数据
            OrdMasterExt ordMasterExt = new OrdMasterExt();
            //获取用户id
            ordMasterExt.setMemberId(form.getMemberId());
            //购物车编号
            ordMasterExt.setBagNoArray(form.getBagNoArray());
            //获取订单详情信息
            ordMasterService.checkConfirmOrderFromBag(ordMasterExt);

            json.put("resultCode", Constants.NORMAL);

        }catch (ExceptionBusiness eb){
            json.put("resultCode", Constants.INVALID);
            json.put("resultMessage", eb.getMessage());
        }catch(Exception e){
            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", "服务异常，请刷新重试");
        }
        return json;
    }

    /**
     * 买家购物车订单确认
     * @功能位置    我的购物车
     * @触发条件    购物车选择确认订单
     * @param data
     * @return
     */
    @RequestMapping("confirmOrderFromBag")
    public @ResponseBody
    JSONObject confirmOrderFromBag(String data){
        OrdWechatForm form = (OrdWechatForm) JSON.parseObject(data, OrdWechatForm.class);
        JSONObject json = new JSONObject();
        //bagNoArray = "100,101,102"
        try {
            //获取查询条件数据
            OrdMasterExt ordMasterExt = new OrdMasterExt();
            //获取用户id
            ordMasterExt.setMemberId(form.getMemberId());
            //购物车编号
            ordMasterExt.setBagNoArray(form.getBagNoArray());
            //获取订单详情信息
            OrdConfirmExt ordConfirmExt = ordMasterService.confirmOrderFromBag(ordMasterExt);
            if(ordConfirmExt == null){
                json.put("resultCode", Constants.INVALID);
                json.put("resultMessage", "商品已下架或删除");
            }else{
//                //确认订单主信息
//                JSONObject jsonConfirm = new JSONObject();
//                //商品信息
//                JSONArray arrayGoodsInfo = new JSONArray();
//                //会员收货地址
//                JSONArray arrayMmbAddress = new JSONArray();
//                //订单活动信息
//                JSONArray arrayAction = new JSONArray();
//                //商品配送方式
//                JSONArray arrayDeliverType = new JSONArray();
//                //地址信息
//                for(MmbAddress ma : ordConfirmExt.getMmbAddressList()){
//                    JSONObject mmbAddress = new JSONObject();
//                    mmbAddress.put("addressId",ma.getAddressId());
//                    mmbAddress.put("isDefault",ma.getIsDefault());
//                    mmbAddress.put("districtidProvince",ma.getDistrictidProvince());
//                    mmbAddress.put("districtidCity",ma.getDistrictidCity());
//                    mmbAddress.put("districtidDistrict",ma.getDistrictidDistrict());
//                    mmbAddress.put("address",ma.getAddress());
//                    mmbAddress.put("postcode",ma.getPostcode());
//                    mmbAddress.put("contactor",ma.getContactor());
//                    mmbAddress.put("phone",ma.getPhone());
//                    arrayMmbAddress.add(mmbAddress);
//                }
//                jsonConfirm.put("mmbAddressList",arrayMmbAddress);
//                //活动信息
//                for(ActMaster am : ordConfirmExt.getActMasterList()){
//                    JSONObject actMaster = new JSONObject();
//                    //TODO 等接口信息返回
//                    arrayAction.add(actMaster);
//                }
//                jsonConfirm.put("actMasterList",arrayAction);
//                //配送方式
//                for(ComCode cc : ordConfirmExt.getDeliverType()){
//                    JSONObject deliverType = new JSONObject();
//                    deliverType.put("value",cc.getValue());
//                    deliverType.put("displayCn",cc.getDisplayCn());
//                    arrayDeliverType.add(deliverType);
//                }
//                jsonConfirm.put("deliverType",arrayDeliverType);
//                //商品信息
//                for(OrdConfirmOrderExt ooe : ordConfirmExt.getGoodsInfo()){
//                    JSONObject goodsInfo = new JSONObject();
//                    goodsInfo.put("goodsId",ooe.getGoodsId());
//                    goodsInfo.put("quantity",ooe.getQuantity());
//                    JSONArray arrayUnit = new JSONArray();
//                    for(OrdConfirmOrderUnitExt ooue : ooe.getOrdConfirmOrderUnitExtList()){
//                        JSONObject jsonUnit = new JSONObject();
//                        jsonUnit.put("goodsId",ooue.getGoodsId());
//                        jsonUnit.put("goodsName",ooue.getGoodsName());
//                        jsonUnit.put("skucontent",ooue.getSkucontent());
//                        jsonUnit.put("price",ooue.getPrice());
//                        jsonUnit.put("skuId",ooue.getSkuId());
//                        jsonUnit.put("coloreName",ooue.getColoreName());
//                        jsonUnit.put("sizeName",ooue.getSizeName());
//                        arrayUnit.add(jsonUnit);
//                    }
//                    goodsInfo.put("ordConfirmOrderUnitExtList",arrayUnit);
//                    arrayGoodsInfo.add(goodsInfo);
//                }
//                jsonConfirm.put("goodsInfo",arrayGoodsInfo);

                json.put("results", ordConfirmExt);
                json.put("resultCode", Constants.NORMAL);
            }
        }catch (ExceptionBusiness eb){
            json.put("resultCode", Constants.INVALID);
            json.put("resultMessage", eb.getMessage());
        }catch(Exception e){
            json.put("resultCode", Constants.FAIL);
        }
        return json;
    }


    /**
     * 买家提交订单
     * @功能位置    提交订单
     * @触发条件
     * @param data
     * @return
     */
    @RequestMapping("submitOrder")
    public @ResponseBody
    JSONObject submitOrder(String data){
        OrdSubmitForm form = (OrdSubmitForm) JSON.parseObject(data, OrdSubmitForm.class);
        JSONObject json = new JSONObject();
        OrdMaster result = null;
        try {
            OrdMasterExt ordMasterExt = new OrdMasterExt();
            BeanUtils.copyProperties(form, ordMasterExt);
            ordMasterExt.setShopId(Constants.ORGID);
            //提交至生成订单
            result = ordMasterService.submitOrder(ordMasterExt);
            //result为null场合,即未取到该条订单数据场合
            if(result == null){
                json.put("resultCode", Constants.INVALID);
            }else{
                json.put("resultCode", Constants.NORMAL);
                json.put("results", result.getOrderCode());
                //开启定时任务
                String id = UUID.randomUUID().toString();
                QydsSchedulerFactory.doCancelUnnpayOrder(result.getOrderCode(),id);
            }
        }catch (ExceptionBusiness eb){
            json.put("resultCode", Constants.INVALID);
            json.put("resultMessage", eb.getMessage());
        }catch(Exception e){
            json.put("resultCode", Constants.FAIL);
        }
        return json;
    }


    /**
     * 订单物流信息查询
     * @功能位置    物流信息
     * @触发条件   订单详细查看物流信息
     * @param data orderId:订单ID
     * @return
     */
    @RequestMapping("queryLogisticsInfo")
    public @ResponseBody
    JSONObject queryLogisticsInfo(String data){
        OrdSubmitForm form = (OrdSubmitForm) JSON.parseObject(data, OrdSubmitForm.class);
        return ordMasterService.queryLogisticsInfo(form.getOrderId());
    }


    /**
     * 获取门店信息
     *
     * @return
     */
    @RequestMapping("getOrgList")
    public @ResponseBody
    JSONObject getOrgList(String data) {
        return bnkMasterService.getOrgList(data);
    }

    /**
     * 门店地址列表取得
     *
     * @param data
     * @return
     */
    @RequestMapping("getOrgAddressList")
    @ResponseBody
    public JSONObject getAddressList(String data) {
        JSONObject json = new JSONObject();
        try {
            ErpStore store = (ErpStore) JSON.parseObject(data, ErpStore.class);
            json = bnkMasterService.getOrgAddressList(store);
        } catch (Exception e) {
            json.put("resultCode", Constants.FAIL);
        }
        return json;

    }

    /**
     * 订单状态数量取得
     *
     * @param data
     * @return
     */
    @RequestMapping("getOrderCountByStatus")
    @ResponseBody
    public JSONObject getOrderCountByStatus(String data) {
        JSONObject json;
        try {
            MmbMaster form = (MmbMaster) JSON.parseObject(data, MmbMaster.class);

            OrdMasterExt ordMasterExt = new OrdMasterExt();
            ordMasterExt.setMemberId(form.getMemberId());

            //待付款数量
            ordMasterExt.setOrderStatus("10");
            ordMasterExt.setPayStatus("10");
            ordMasterExt.setDeliverStatus("");
            int notCount = ordMasterService.getAllDatasCount(ordMasterExt);

            //未发货数量
            ordMasterExt.setOrderStatus("");
            ordMasterExt.setPayStatus("20");
            ordMasterExt.setDeliverStatus("10");
            int notDeliverCount = ordMasterService.getAllDatasCount(ordMasterExt);

            //待收货数量
            ordMasterExt.setOrderStatus("");
            ordMasterExt.setPayStatus("");
            ordMasterExt.setDeliverStatus("20");
            int receiveCount = ordMasterService.getAllDatasCount(ordMasterExt);

            json = new JSONObject();
            json.put("notCount", notCount);
            json.put("notDeliverCount", notDeliverCount);
            json.put("receiveCount", receiveCount);

            json.put("resultCode", Constants.NORMAL);

        } catch (Exception e) {
            json = new JSONObject();
            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", e.getMessage());
        }
        return json;

    }

    /**
     * 买家主订单申请退款
     * @功能位置    主订单退款
     * @触发条件    1、订单付款成功
     *             2、未发货
     * @param data
     * @return
     */
    @RequestMapping("applyRefund")
    public @ResponseBody
    JSONObject applyRefund(String data){
        JSONObject json = new JSONObject();
        try {
            json = ordMasterService.applyRefund(data);
        }catch(Exception e){
            json.put("resultCode", Constants.FAIL);
            json.put("message", e.getMessage());
        }
        return json;
    }

    /**
     * 根据买家ID获取线下订单个数
     * @param data
     * @return json
     */
    @RequestMapping("getOrderListOffLineByMemberId")
    @ResponseBody
    public JSONObject getOrderListOffLineByMemberId(String data){
        OrdMasterExt ordMasterExt = JSON.parseObject(data, OrdMasterExt.class);
        return ordMasterService.getOrderListOffLineByMemberId(ordMasterExt);

    }
}
