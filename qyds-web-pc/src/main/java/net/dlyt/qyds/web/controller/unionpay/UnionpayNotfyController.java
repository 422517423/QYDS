package net.dlyt.qyds.web.controller.unionpay;

import com.alibaba.fastjson.JSONObject;
import net.dlyt.qyds.common.dto.AliPayReturnModel;
import net.dlyt.qyds.common.dto.ext.CouponOrderExt;
import net.dlyt.qyds.web.common.unionpay.DemoBase;
import net.dlyt.qyds.web.common.unionpay.sdk.AcpService;
import net.dlyt.qyds.web.common.unionpay.sdk.LogUtil;
import net.dlyt.qyds.web.common.unionpay.sdk.SDKConstants;
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
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;
import java.util.Map.Entry;

/**
 * Created by congkeyan on 17/3/17.
 */
@Controller
@RequestMapping("/unionpay")
public class UnionpayNotfyController {

    private static final org.slf4j.Logger Log = LoggerFactory.getLogger(UnionpayNotfyController.class);

    @Autowired
    private OrdAliPayService ordAliPayService;
    @Autowired
    private CouponOrderService couponOrderService;

    /**
     * 获取请求参数中所有的信息
     *
     * @param request
     * @return
     */
    public static Map<String, String> getAllRequestParam(final HttpServletRequest request) {
        Map<String, String> res = new HashMap<String, String>();
        Enumeration<?> temp = request.getParameterNames();
        if (null != temp) {
            while (temp.hasMoreElements()) {
                String en = (String) temp.nextElement();
                String value = request.getParameter(en);
                res.put(en, value);
                //在报文上送时，如果字段的值为空，则不上送<下面的处理为在获取所有参数数据时，判断若值为空，则删除这个字段>
                //System.out.println("ServletUtil类247行  temp数据的键=="+en+"     值==="+value);
                if (null == res.get(en) || "".equals(res.get(en))) {
                    res.remove(en);
                }
            }
        }
        return res;
    }

    /**
     * 阿里支付(异步回调)
     * @param request
     * @param response
     */
    @RequestMapping("unionPayAsynNotfy")
    @ResponseBody
    public void unionPayAsynNotfy(HttpServletRequest request, HttpServletResponse response){


        LogUtil.writeLog("BackRcvResponse接收后台通知开始");
        try{
            String encoding = request.getParameter(SDKConstants.param_encoding);
            // 获取银联通知服务器发送的后台通知参数
            Map<String, String> reqParam = getAllRequestParam(request);

            LogUtil.printRequestLog(reqParam);

            Map<String, String> valideData = null;
            if (null != reqParam && !reqParam.isEmpty()) {
                Iterator<Entry<String, String>> it = reqParam.entrySet().iterator();
                valideData = new HashMap<String, String>(reqParam.size());
                while (it.hasNext()) {
                    Entry<String, String> e = it.next();
                    String key = (String) e.getKey();
                    String value = (String) e.getValue();
                    value = new String(value.getBytes(encoding), encoding);
                    valideData.put(key, value);
                }
            }

            //重要！验证签名前不要修改reqParam中的键值对的内容，否则会验签不过
            if (!AcpService.validate(valideData, encoding)) {
                LogUtil.writeLog("验证签名结果[失败].");
                //验签失败，需解决验签问题

            } else {
                LogUtil.writeLog("验证签名结果[成功].");
                //【注：为了安全验签成功才应该写商户的成功处理逻辑】交易成功，更新商户订单状态
                String respCode =valideData.get("respCode"); //获取应答码，收到后台通知了respCode的值一般是00，可以不需要根据这个应答码判断。

                if("00".equals(respCode)){
                    Map<String, String> mParam = new HashMap<String, String>();
                    mParam.put("orderCode", valideData.get("orderId"));
                    mParam.put("total_fee", valideData.get("txnAmt"));//单位‘元’ 0.00

                    if(valideData.get("orderId").startsWith("COUPON")){
                        //购买优惠劵
                        CouponOrderExt couponOrderExt = new CouponOrderExt();
                        couponOrderExt.setOrderId(valideData.get("orderId"));
                        couponOrderExt.setPayInfact(new BigDecimal(valideData.get("txnAmt")));
                        couponOrderExt.setPayType("10");
                        JSONObject jsonObject = couponOrderService.paySuccess(couponOrderExt);
                        if("00".equals(jsonObject.getString("resultCode"))){
                            couponOrderService.sendCoupon(couponOrderExt);
                        }
                    }else{
                        ordAliPayService.paySuccess(mParam);
                    }
                }

            }
            LogUtil.writeLog("BackRcvResponse接收后台通知结束");
            //返回给银联服务器http 200  状态码
            response.getWriter().print("ok");
        }catch (Exception e){
            Log.info(e.getMessage());
        }
    }

    /**
     * 阿里支付(同步回调)
     * @param request
     * @param response
     */
    @RequestMapping("unionPaySynNotfy")
    @ResponseBody
    public void unionPaySynNotfy(HttpServletRequest request, HttpServletResponse response){
        Log.info("############################### 即时到账回调接口 Start #############################");
        Log.info(request.getRequestURI());
        Log.info("############################### 即时到账回调接口 End #############################");

        try{

            LogUtil.writeLog("FrontRcvResponse前台接收报文返回开始");

            String encoding = request.getParameter(SDKConstants.param_encoding);
            LogUtil.writeLog("返回报文中encoding=[" + encoding + "]");
            Map<String, String> respParam = getAllRequestParam(request);

            // 打印请求报文
            LogUtil.printRequestLog(respParam);

            Map<String, String> valideData = null;
            StringBuffer page = new StringBuffer();
            if (null != respParam && !respParam.isEmpty()) {
                Iterator<Entry<String, String>> it = respParam.entrySet()
                        .iterator();
                valideData = new HashMap<String, String>(respParam.size());
                while (it.hasNext()) {
                    Entry<String, String> e = it.next();
                    String key = (String) e.getKey();
                    String value = (String) e.getValue();
                    value = new String(value.getBytes(encoding), encoding);
                    page.append("<tr><td width=\"30%\" align=\"right\">" + key
                            + "(" + key + ")</td><td>" + value + "</td></tr>");
                    valideData.put(key, value);
                }
            }
            if (!AcpService.validate(valideData, encoding)) {
                page.append("<tr><td width=\"30%\" align=\"right\">验证签名结果</td><td>失败</td></tr>");
                LogUtil.writeLog("验证签名结果[失败].");
            } else {
                page.append("<tr><td width=\"30%\" align=\"right\">验证签名结果</td><td>成功</td></tr>");
                LogUtil.writeLog("验证签名结果[成功].");
                System.out.println(valideData.get("orderId")); //其他字段也可用类似方式获取

                Map<String, String> mParam = new HashMap<String, String>();
                mParam.put("orderCode", valideData.get("orderId"));
                mParam.put("total_fee", valideData.get("txnAmt"));//单位‘元’ 0.00

                if(valideData.get("orderId").startsWith("COUPON")){
                    //购买优惠劵
                    CouponOrderExt couponOrderExt = new CouponOrderExt();
                    couponOrderExt.setOrderId(valideData.get("orderId"));
                    couponOrderExt.setPayInfact(new BigDecimal(valideData.get("txnAmt")));
                    couponOrderExt.setPayType("10");
                    JSONObject jsonObject = couponOrderService.paySuccess(couponOrderExt);
                    if("00".equals(jsonObject.getString("resultCode"))){
                        couponOrderService.sendCoupon(couponOrderExt);
                    }
                }else{
                    ordAliPayService.paySuccess(mParam);
                }

            }
            LogUtil.writeLog("FrontRcvResponse前台接收报文返回结束");
            // 支付成功跳转画面
            // TODO
            response.sendRedirect("https://www.dealuna.com/qyds-web-pc/qyds/#/personalCenter/personalCenter/orderList");

        }catch(Exception e){
            Log.info(e.getMessage());
        }
    }
}
