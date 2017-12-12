package net.dlyt.qyds.web.controller.wechat;

import com.alibaba.fastjson.JSONObject;
import net.dlyt.qyds.common.dto.CouponOrder;
import net.dlyt.qyds.common.dto.ext.CouponOrderExt;
import net.dlyt.qyds.web.service.CouponOrderService;
import net.dlyt.qyds.web.service.WechatPayNotfyService;
import org.apache.http.HttpResponse;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.omg.CORBA.portable.ResponseHandler;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wenxuechao on 16/8/20.
 */
@Controller
@RequestMapping("/wechat")
public class WechatPayNotfyController {

    private static final org.slf4j.Logger Log = LoggerFactory.getLogger(WechatPayNotfyController.class);

    @Autowired
    private WechatPayNotfyService wechatPayNotfyService;
    @Autowired
    private CouponOrderService couponOrderService;

    /**
     * 微信支付回调
     * @throws IOException
     * @throws DocumentException
     */
    @RequestMapping("asyncWechatPay")
    @ResponseBody
    public void asyncWechatPay(HttpServletRequest request, HttpServletResponse response) throws IOException, DocumentException {
//        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        Log.info("############################### 微信支付异步接口 Start #############################");
        Log.info(request.getRequestURI());
        Log.info("############################### 微信支付异步接口 End #############################");

        // 解析结果存储在HashMap
        Map<String, String> map = new HashMap<String, String>();
        InputStream inputStream = request.getInputStream();
        // 读取输入流
        SAXReader reader = new SAXReader();
        Document document = reader.read(inputStream);
        // 得到xml根元素
        Element root = document.getRootElement();
        // 得到根元素的所有子节点
        List<Element> elementList = root.elements();

        // 遍历所有子节点
        for (Element e : elementList){
            map.put(e.getName(), e.getText());}
        // 释放资源
        inputStream.close();
        inputStream = null;

        String strReturnCode = map.get("return_code");
        Log.info("############################### return_code :" + strReturnCode);

        String strOrderCode = map.get("out_trade_no");
        String total_fee = map.get("total_fee");

        if (!"SUCCESS".equals(strReturnCode)) {
            return;
        }

        Map<String, String> mParam = new HashMap<String, String>();
        mParam.put("orderCode", strOrderCode);
        mParam.put("total_fee", total_fee);//单位‘分’
        Log.info("############################### orderCode :" + strOrderCode);
        Log.info("############################### total_fee :" + total_fee);
        try{
            if(strOrderCode.startsWith("COUPON")){
                //购买优惠劵
                CouponOrderExt couponOrderExt = new CouponOrderExt();
                couponOrderExt.setOrderId(strOrderCode);
                couponOrderExt.setPayInfact(new BigDecimal(total_fee));
                couponOrderExt.setPayType("20");
                JSONObject jsonObject = couponOrderService.paySuccess(couponOrderExt);
                if("00".equals(jsonObject.getString("resultCode"))){
                    couponOrderService.sendCoupon(couponOrderExt);
                }
            }else{
                wechatPayNotfyService.paySuccess(mParam);
            }
        }catch (Exception e){
            Log.info(e.getMessage());
        }
        response.getWriter().print("success");
    }
}
