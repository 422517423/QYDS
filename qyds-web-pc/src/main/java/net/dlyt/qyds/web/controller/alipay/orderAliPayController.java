package net.dlyt.qyds.web.controller.alipay;

import com.alibaba.fastjson.JSONObject;
import net.dlyt.qyds.common.dto.OrdMaster;
import net.dlyt.qyds.common.form.PayForm;
import net.dlyt.qyds.web.common.AliPay.util.Util;
import net.dlyt.qyds.web.common.AlipayConfig;
import net.dlyt.qyds.web.common.Constants;
import net.dlyt.qyds.web.service.OrdAliPayService;
import net.dlyt.qyds.web.service.OrdMasterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by wenxuechao on 16/8/27.
 */
@Controller
@RequestMapping("/alipay")
public class orderAliPayController {

    // 支付宝接口网关
    private static final String GATEWAY_URL = "https://mapi.alipay.com/gateway.do";

    // 即时到帐
    private static final String SERVICE_JSDZ = "create_direct_pay_by_user";

    // 支付类型
    private static final String PAY_TYPE = "1";

    @Autowired
    private OrdAliPayService ordAliPayService;

    @Autowired
    private OrdMasterService ordMasterService;

    /**
     * 阿里支付即时到账
     * @param orderId
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping("orderAliPay")
//    public @ResponseBody
    public void payment(@RequestParam(required = true)String orderId, HttpServletRequest request, HttpServletResponse response) throws Exception
    {
//    JSONObject orderAliPay(@RequestParam(required = true)String orderId, HttpServletRequest request, HttpServletResponse response){
        JSONObject json = new JSONObject();
        try{

            String result = "";
            if(orderId.startsWith("COUPON")){
                result = ordAliPayService.checkCoupponOrderInfo(orderId);
            }else{
                result = ordAliPayService.checkOrderInfo(orderId);
            }

//            //校验订单信息
//            String result = ordAliPayService.checkOrderInfo(orderId);
            if(!orderId.startsWith("COUPON")){
                ordMasterService.checkSecActivityOrderInfo(orderId, null);
            }

            if(!"NODATA".equals(result) && !"ZEROMONEY".equals(result)){
                PayForm payForm = new PayForm();
                //订单ID
                payForm.setOrderId(orderId);
                //付款类型 支付宝即时到账:21
                payForm.setPayType("21");
                //返回地址 同步请求地址
                payForm.setReturnUrl(request.getScheme() + "://"
                        + request.getServerName() + ":" + request.getServerPort()
                        + request.getContextPath()
                        + "/alipay/aliPaySynNotfy.json");
                //通知地址 异步请求地址
                payForm.setNotifyUrl(request.getScheme() + "://"
                        + request.getServerName() + ":" + request.getServerPort()
                        + request.getContextPath()
                        + "/alipay/aliPayAsynNotfy.json");
                //订单编号
                payForm.setOrderCode(result);
                //展示地址
                payForm.setShowUrl("");
                try {

                    String strResponseWriter;
                    // 即时到账
                    strResponseWriter = this.makeHtmlPageGetJsdz(payForm);

                    PrintWriter out = response.getWriter();
                    out.println(new String(strResponseWriter.getBytes("UTF-8"),
                            "ISO-8859-1"));
                    return;
                } catch (Exception e) {
                    // 支付失败页面
                    response.sendRedirect("");
                }
            }else{
                // 支付失败页面
                response.sendRedirect("");
            }
        }catch(Exception e){
            // 支付失败页面
            response.sendRedirect("");
            throw e;
        }
    }

    /**
     * 建立即时到帐请求，以表单HTML形式构造（默认）
     *
     * @param payForm
     *            请求参数数组
     * @return 提交表单HTML文本
     * @throws Exception
     */
    public String makeHtmlPageGetJsdz(PayForm payForm) {
        Map<String, String> mapPara = null;
        try {
            mapPara = getParaMapJsdz(payForm);
        } catch (Exception e) {
            e.printStackTrace();
        }
        StringBuffer sbRtn = new StringBuffer();
        sbRtn.append("<html>");
        sbRtn.append("<head>");
        sbRtn.append("<meta http-equiv='Content-Type' content='text/html; charset=UTF-8'>");
//        sbRtn.append("<title>支付宝纯即时到帐接口</title>");
        sbRtn.append("</head>");
        sbRtn.append(
                "<form id=\"alipaysubmit\" name=\"alipaysubmit\" action=\"")
                .append(GATEWAY_URL).append("?_input_charset=")
                .append(AlipayConfig.input_charset)
                .append("\" method=\"post\">");

        for (String key : mapPara.keySet()) {
            sbRtn.append("<input type=\"hidden\" name=\"").append(key)
                    .append("\" value=\"").append(mapPara.get(key))
                    .append("\"/>");
        }

        // submit按钮控件请不要含有name属性
        sbRtn.append("</form>");
        sbRtn.append("<script>document.forms['alipaysubmit'].submit();</script>");
        sbRtn.append("</html>");
        return sbRtn.toString();
    }

    // 即时到帐
    private Map<String, String> getParaMapJsdz(PayForm form) throws Exception {
        // 合作者身份ID
        String partner = AlipayConfig.partner;
        if (Util.isNullString(partner)) {
            throw new Exception("和作者身份未设定");
        }

        // 字符编码格式
        String input_charset = AlipayConfig.input_charset;
        input_charset = Util.isNullString(input_charset) ? "utf-8"
                : input_charset;

        // 签名方式
        String sign_type = AlipayConfig.sign_type;
        sign_type = Util.isNullString(sign_type) ? "MD5" : sign_type;

        // 支付宝服务
        String service = SERVICE_JSDZ;
        // 支付类型
        String payment_type = PAY_TYPE;

        // 数字格式化
        DecimalFormat format = new DecimalFormat("0.00");

        // 页面跳转同步通知页面路径
        String return_url = form.getReturnUrl();
        // 服务器异步通知页面路径
        String notify_url = form.getNotifyUrl();
        // 商品展示地址
        String show_url = form.getShowUrl();

        OrdMaster ordMaster = null;
        if(form.getOrderId().startsWith("COUPON")){
            ordMaster = ordAliPayService.getCouponOrderInfo(form.getOrderId());
        }else{
            ordMaster = ordAliPayService.getOrderInfo(form.getOrderId());
        }

        if (ordMaster == null) {
            throw new Exception("订单数据未取到");
        }
        //卖家支付宝帐户
        String seller_email = AlipayConfig.seller_email;
        // 商户订单号
        String out_trade_no = ordMaster.getOrderCode();
        if (Util.isNullString(out_trade_no)) {
            throw new Exception("订单编号未取到");
        }
        // 订单名称
        String subject =  ordMaster.getOrderCode();
        // 付款金额
        String total_fee = ordMaster.getPayInfact() != null ? String.valueOf(ordMaster.getPayInfact()) : "0.00";
        if (Util.isNullString(total_fee)) {
            throw new Exception("订单金额未取到");
        }

        // 订单描述
        String body = "";

        // 把请求参数打包成数组
        Map<String, String> paraMap = new HashMap<String, String>();
        paraMap.put("service", service);
        paraMap.put("partner", partner);
        paraMap.put("_input_charset", input_charset);
        paraMap.put("payment_type", payment_type);
        paraMap.put("return_url", return_url);
        paraMap.put("notify_url", notify_url);
        paraMap.put("seller_email", seller_email);
        paraMap.put("out_trade_no", out_trade_no);
        paraMap.put("subject", subject);
        paraMap.put("total_fee", total_fee);
        paraMap.put("body", body);
        paraMap.put("show_url", show_url);
        paraMap.put("it_b_pay","30m");

        // 签名结果与签名方式加入请求提交参数组中
        Map<String, String> sPara = Util.mapFilter(paraMap);
        sPara.put("sign", Util.getSign(sPara));
        sPara.put("sign_type", sign_type);

        return sPara;
    }
}
