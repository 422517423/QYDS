package net.dlyt.qyds.web.controller.alipay;

import com.alibaba.fastjson.JSONObject;
import net.dlyt.qyds.common.dto.AliPayReturnModel;
import net.dlyt.qyds.common.dto.ext.CouponOrderExt;
import net.dlyt.qyds.web.common.Constants;
import net.dlyt.qyds.web.service.CouponOrderService;
import net.dlyt.qyds.web.service.OrdAliPayService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by wenxuechao on 16/8/27.
 */
@Controller
@RequestMapping("/alipay")
public class AliPayNotfyController {

    private static final org.slf4j.Logger Log = LoggerFactory.getLogger(AliPayNotfyController.class);

    // 阿里支付接口请求方式 异步请求
    public static final String ALI_REQUEST_ASYNC = "ASYNC";

    @Autowired
    private OrdAliPayService ordAliPayService;
    @Autowired
    private CouponOrderService couponOrderService;

    /*测试*/
    /**
     * 阿里支付(异步回调)
     * @param request
     * @param response
     */
    @RequestMapping("aliPayAsynNotfy")
    public void aliPayAsynNotfyTest(HttpServletRequest request, HttpServletResponse response){
        Log.info("############################### 即时到账异步接口 Start #############################");
        Log.info(request.getRequestURI());
        Log.info("############################### 即时到账异步接口 End #############################");

        try{
            Map<String, String> mParam = new HashMap<String, String>();
            mParam.put("orderCode", "20171219091321024616");
            mParam.put("total_fee","0.01");//单位‘元’ 0.00
            try{
                ordAliPayService.paySuccess(mParam);
            }catch (Exception e){
                Log.info(e.getMessage());
            }
            response.getWriter().print("success");

        }catch(Exception e){
            Log.info(e.getMessage());
        }
    }
 /*测试*/


    /**
     * 阿里支付(异步回调)
     * @param request
     * @param response
     */
    @RequestMapping("aliPayAsynNotfyEee")
    @ResponseBody
    public void aliPayAsynNotfy(HttpServletRequest request, HttpServletResponse response){
        Log.info("############################### 即时到账异步接口 Start #############################");
        Log.info(request.getRequestURI());
        Log.info("############################### 即时到账异步接口 End #############################");

        try{
            if (checkAliRequest(request.getParameter("notify_id"), request.getParameter("seller_id"))) {
                AliPayReturnModel model = setAlipayReturnModel(request, ALI_REQUEST_ASYNC);
                Map<String, String> mParam = new HashMap<String, String>();
                mParam.put("orderCode", model.getOut_trade_no());
                mParam.put("total_fee", model.getTotal_fee());//单位‘元’ 0.00
                Log.info("############################### orderCode :" + model.getOut_trade_no());
                try{
                    if(model.getOut_trade_no().startsWith("COUPON")){
                        //购买优惠劵
                        CouponOrderExt couponOrderExt = new CouponOrderExt();
                        couponOrderExt.setOrderId(model.getOut_trade_no());
                        couponOrderExt.setPayInfact(new BigDecimal(model.getTotal_fee()));
                        couponOrderExt.setPayType("10");
                        JSONObject jsonObject = couponOrderService.paySuccess(couponOrderExt);
                        if("00".equals(jsonObject.getString("resultCode"))){
                            couponOrderService.sendCoupon(couponOrderExt);
                        }
                    }else{
                        ordAliPayService.paySuccess(mParam);
                    }

                }catch (Exception e){
                    Log.info(e.getMessage());
                }
                response.getWriter().print("success");
            }
        }catch(Exception e){
            Log.info(e.getMessage());
        }
    }

    /**
     * 阿里支付(同步回调)
     * @param request
     * @param response
     */
    @RequestMapping("aliPaySynNotfy")
    @ResponseBody
    public void aliPaySynNotfy(HttpServletRequest request, HttpServletResponse response){
        Log.info("############################### 即时到账异步接口 Start #############################");
        Log.info(request.getRequestURI());
        Log.info("############################### 即时到账异步接口 End #############################");

        try{
            if (checkAliRequest(request.getParameter("notify_id"), request.getParameter("seller_id"))) {
                AliPayReturnModel model = setAlipayReturnModel(request, ALI_REQUEST_ASYNC);
                Map<String, String> mParam = new HashMap<String, String>();
                mParam.put("orderCode", model.getOut_trade_no());
                mParam.put("total_fee", model.getTotal_fee());//单位‘元’ 0.00
                Log.info("############################### orderCode :" + model.getOut_trade_no());
                try{
                    if(model.getOut_trade_no().startsWith("COUPON")){
                        //购买优惠劵
                        CouponOrderExt couponOrderExt = new CouponOrderExt();
                        couponOrderExt.setOrderId(model.getOut_trade_no());
                        couponOrderExt.setPayInfact(new BigDecimal(model.getTotal_fee()));
                        couponOrderExt.setPayType("10");
                        JSONObject jsonObject = couponOrderService.paySuccess(couponOrderExt);
                        if("00".equals(jsonObject.getString("resultCode"))){
                            couponOrderService.sendCoupon(couponOrderExt);
                        }
                    }else{
                        ordAliPayService.paySuccess(mParam);
                    }
                }catch (Exception e){
                    Log.info(e.getMessage());
                }
                // 支付成功跳转画面
                response.sendRedirect("https://www.dealuna.com/qyds-web-pc/qyds/#/personalCenter/personalCenter/orderList");
            }
        }catch(Exception e){
            Log.info(e.getMessage());
        }
    }

    /**
     * Null的话转成空串
     */
    private String isNull(Object obj) {
        if (obj != null) {
            return obj.toString();
        } else {
            return "";
        }
    }

    /**
     * 验证是否为阿里请求
     * @throws Exception
     *
     */
    private boolean checkAliRequest(String notify_id, String partner) {

        String strUrl = "http://notify.alipay.com/trade/notify_query.do?partner="
                + partner + "&notify_id=" + notify_id;
        Log.info("#URL# " + strUrl + " #############################");
        String strResponse = "";
        URL url;
        try {
            url = new URL(strUrl);
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream(), "UTF-8"));
            String temp = "";
            while ((temp = in.readLine()) != null)/* 将输入流以字节的形式读取并写入buffer中 */
            {
                strResponse += temp;
            }
            in.close();
            connection.disconnect();
        } catch (Exception e) {
            Log.info("#Exception# " + e.getMessage()
                    + " #############################");
            e.printStackTrace();
        }

        boolean isResult = Boolean.parseBoolean(strResponse);
        Log.info("############################### 阿里请求验证结果 Start #############################");
        Log.info(String.valueOf(isResult));
        Log.info("############################### 阿里请求验证结果 End #############################");
        return isResult;
    }

    /**
     * 阿里支付回调数据模型设置
     *
     * @param request
     *            请求 sync 异步／同步区分
     *
     * @return 阿里支付回调数据模型
     *
     */
    private AliPayReturnModel setAlipayReturnModel(HttpServletRequest request, String sync) {
        AliPayReturnModel model = new AliPayReturnModel();
        model.setTrade_status(isNull(request.getParameter("trade_status")));
        model.setOut_trade_no(isNull(request.getParameter("out_trade_no")));
        model.setPrice(isNull(request.getParameter("price")));
        model.setBuyer_email(isNull(request.getParameter("buyer_email")));
        model.setBuyer_id(isNull(request.getParameter("buyer_id")));
        model.setDiscount(isNull(request.getParameter("discount")));
        model.setGmt_create(isNull(request.getParameter("gmt_create")));
        model.setGmt_logistics_modify(isNull(request
                .getParameter("gmt_logistics_modify")));
        model.setGmt_payment(isNull(request.getParameter("gmt_payment")));
        model.setIs_success(isNull(request.getParameter("is_success")));
        model.setIs_total_fee_adjust(isNull(request
                .getParameter("is_total_fee_adjust")));
        model.setLogistics_fee(isNull(request.getParameter("logistics_fee")));
        model.setLogistics_payment(isNull(request
                .getParameter("logistics_payment")));
        model.setLogistics_type(isNull(request.getParameter("logistics_type")));
        model.setNotify_id(isNull(request.getParameter("notify_id")));
        model.setNotify_time(isNull(request.getParameter("notify_time")));
        model.setNotify_type(isNull(request.getParameter("notify_type")));
        model.setPayment_type(isNull(request.getParameter("payment_type")));
        model.setQuantity(isNull(request.getParameter("quantity")));
        model.setReceive_address(isNull(request.getParameter("receive_address")));
        model.setReceive_mobile(isNull(request.getParameter("receive_mobile")));
        model.setReceive_name(isNull(request.getParameter("receive_name")));
        model.setReceive_zip(isNull(request.getParameter("receive_zip")));
        model.setSeller_actions(isNull(request.getParameter("seller_actions")));
        model.setSeller_email(isNull(request.getParameter("seller_email")));
        model.setSeller_id(isNull(request.getParameter("seller_id")));
        model.setSubject(isNull(request.getParameter("subject")));
        model.setTotal_fee(isNull(request.getParameter("total_fee")));
        model.setTrade_no(isNull(request.getParameter("trade_no")));
        model.setUse_coupon(isNull(request.getParameter("use_coupon")));
        model.setSign(isNull(request.getParameter("sign")));
        model.setSign_type(isNull(request.getParameter("sign_type")));
        model.setRequest_type(sync);
        model.setCreatetime("");
        return model;
    }
}
