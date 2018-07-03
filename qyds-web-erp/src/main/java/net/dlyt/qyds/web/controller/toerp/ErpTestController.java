package net.dlyt.qyds.web.controller.toerp;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import net.dlyt.qyds.web.common.Constants;
import net.dlyt.qyds.web.service.ErpSendService;
import net.dlyt.qyds.web.service.common.ErpSendUtil;
import net.dlyt.qyds.web.service.erp.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import static net.dlyt.qyds.web.service.common.ErpKeyUtil.*;

/**
 * Created by zlh on 2016/7/30.
 */
@Controller
@RequestMapping("/erptest")
public class ErpTestController {

    private final Logger log = LoggerFactory.getLogger(ErpTestController.class);
    private static Service service;
    private static ServiceSoap soap;
    @Resource
    private ErpSendService sendService;

    @RequestMapping("GoodsUpdate")
    public @ResponseBody
    JSONObject goodsUpdate(@RequestParam(required = false)String data){
        JSONObject map = new JSONObject();
        try{
            Goods goods = JSON.parseObject(data, Goods.class);
            BaseDate date = new BaseDate();
            date.setGoods(goods);
            initService();
            String result = soap.goodsUpdate(getKeyGoodsUpdate(date),date);
            if (result.equals("00")) {
                map.put("resultCode", Constants.NORMAL);
            } else if (result.equals("11")) {
                map.put("resultCode", Constants.FAIL);
                map.put("message", "ERP数据库连接失败");
            } else if (result.equals("12")) {
                map.put("resultCode", Constants.FAIL);
                map.put("message", "ERP数据库更新失败");
            } else if (result.equals("13")) {
                map.put("resultCode", Constants.FAIL);
                map.put("message", "ERP数据重复");
            } else if (result.equals("21")) {
                map.put("resultCode", Constants.FAIL);
                map.put("message", "ERP验证失败");
            } else {
                map.put("resultCode", Constants.FAIL);
                map.put("message", "ERP未知错误");
            }
        }catch(Exception e){
            map.put("resultCode", Constants.FAIL);
            map.put("message", e.getMessage());
        }
        return map;
    }

    @RequestMapping("VIPUpdate")
    public @ResponseBody
    JSONObject vipUpdate(@RequestParam(required = false)String data){
        JSONObject map = new JSONObject();
        try{
            Vip vip = JSON.parseObject(data, Vip.class);
            BaseDate date = new BaseDate();
            date.setVip(vip);
            initService();
            String result = soap.vipUpdate(getKeyVIPUpdate(date),date);
            log.debug("result code:"+result,date);
            if (result.equals("00")) {
                map.put("resultCode", Constants.NORMAL);
            } else if (result.equals("11")) {
                map.put("resultCode", Constants.FAIL);
                map.put("message", "ERP数据库连接失败");
            } else if (result.equals("12")) {
                map.put("resultCode", Constants.FAIL);
                map.put("message", "ERP数据库更新失败");
            } else if (result.equals("21")) {
                map.put("resultCode", Constants.FAIL);
                map.put("message", "ERP验证失败");
            } else {
                map.put("resultCode", Constants.FAIL);
                map.put("message", "ERP未知错误");
            }
        }catch(Exception e){
            map.put("resultCode", Constants.FAIL);
            map.put("message", e.getMessage());
        }
        return map;
    }

    @RequestMapping("VIPPointUpdate")
    public @ResponseBody
    JSONObject VIPPointUpdate(@RequestParam(required = false)String data){
        JSONObject map = new JSONObject();
        try{
            VipPoint point = JSON.parseObject(data,VipPoint.class);
            BaseDate date = new BaseDate();
            date.setVipPoint(point);
            initService();
            String result = soap.vipPointUpdate(getKeyVIPPointUpdate(date),date);
            if (result.equals("00")) {
                map.put("resultCode", Constants.NORMAL);
            } else if (result.equals("11")) {
                map.put("resultCode", Constants.FAIL);
                map.put("message", "ERP数据库连接失败");
            } else if (result.equals("12")) {
                map.put("resultCode", Constants.FAIL);
                map.put("message", "ERP数据库更新失败");
            } else if (result.equals("21")) {
                map.put("resultCode", Constants.FAIL);
                map.put("message", "ERP验证失败");
            } else {
                map.put("resultCode", Constants.FAIL);
                map.put("message", "ERP未知错误");
            }
        }catch(Exception e){
            map.put("resultCode", Constants.FAIL);
            map.put("message", e.getMessage());
        }
        return map;
    }

    @RequestMapping("CouponSendUpdate")
    public @ResponseBody
    JSONObject couponSendUpdate(@RequestParam(required = false)String data){
        JSONObject map = new JSONObject();
        try{
            CouponSend couponSend = JSON.parseObject(data,CouponSend.class);

            Coupon coupon = new Coupon();
            coupon.setCouponSend(couponSend);
            initService();
            String result = soap.couponSendUpdate(getKeyCouponSendUpdate(coupon),coupon);
            if (result.equals("00")) {
                map.put("resultCode", Constants.NORMAL);
            } else if (result.equals("11")) {
                map.put("resultCode", Constants.FAIL);
                map.put("message", "ERP数据库连接失败");
            } else if (result.equals("12")) {
                map.put("resultCode", Constants.FAIL);
                map.put("message", "ERP数据库更新失败");
            } else if (result.equals("21")) {
                map.put("resultCode", Constants.FAIL);
                map.put("message", "ERP验证失败");
            } else {
                map.put("resultCode", Constants.FAIL);
                map.put("message", "ERP未知错误");
            }
        }catch(Exception e){
            map.put("resultCode", Constants.FAIL);
            map.put("message", e.getMessage());
        }
        return map;
    }

    @RequestMapping("CouponissueUpdate")
    public @ResponseBody
    JSONObject couponissueUpdate(@RequestParam(required = false)String master,@RequestParam(required = false)String list) {
        JSONObject map = new JSONObject();
        try {
            Coupon coupon = new Coupon();

            CouponIssue couponIssue = JSON.parseObject(master, CouponIssue.class);
            coupon.setCouponIssue(couponIssue);

            List<CouponSku> l = JSON.parseArray(list, CouponSku.class);
            ArrayOfCouponSku ll = new ArrayOfCouponSku();
            ll.setCouponSku(l);
            coupon.setCouponSku(ll);

            initService();
            String result = soap.couponissueUpdate(getKeyCouponissueUpdate(coupon),coupon);
            if (result.equals("00")) {
                map.put("resultCode", Constants.NORMAL);
            } else if (result.equals("11")) {
                map.put("resultCode", Constants.FAIL);
                map.put("message", "ERP数据库连接失败");
            } else if (result.equals("12")) {
                map.put("resultCode", Constants.FAIL);
                map.put("message", "ERP数据库更新失败");
            } else if (result.equals("13")) {
                map.put("resultCode", Constants.FAIL);
                map.put("message", couponIssue.getCouponId()+"代金券重复");
            } else if (result.equals("21")) {
                map.put("resultCode", Constants.FAIL);
                map.put("message", "ERP验证失败");
            } else {
                map.put("resultCode", Constants.FAIL);
                map.put("message", "ERP未知错误");
            }
        } catch (Exception e) {
            map.put("resultCode", Constants.FAIL);
            map.put("message", e.getMessage());
        }
        return map;
    }

    @RequestMapping("CouponUsedUpdate")
    public @ResponseBody
    JSONObject couponUsedUpdate(@RequestParam(required = false)String data){
        JSONObject map = new JSONObject();
        try{
            CouponUsed couponUsed = JSON.parseObject(data,CouponUsed.class);

            Coupon coupon = new Coupon();
            coupon.setCouponUsed(couponUsed);
            initService();
            String result = soap.couponUsedUpdate(getKeyCouponUsedUpdate(coupon),coupon);
            if (result.equals("00")) {
                map.put("resultCode", Constants.NORMAL);
            } else if (result.equals("11")) {
                map.put("resultCode", Constants.FAIL);
                map.put("message", "ERP数据库连接失败");
            } else if (result.equals("12")) {
                map.put("resultCode", Constants.FAIL);
                map.put("message", "ERP数据库更新失败");
            } else if (result.equals("21")) {
                map.put("resultCode", Constants.FAIL);
                map.put("message", "ERP验证失败");
            } else {
                map.put("resultCode", Constants.FAIL);
                map.put("message", "ERP未知错误");
            }
        }catch(Exception e){
            map.put("resultCode", Constants.FAIL);
            map.put("message", e.getMessage());
        }
        return map;
    }

    @RequestMapping("SaleInput")
    public @ResponseBody
    JSONObject saleInput(@RequestParam(required = false)String master,@RequestParam(required = false)String list){
        JSONObject map = new JSONObject();
        try{
            Orders order = new Orders();
            ArrayOfSaleList ll = new ArrayOfSaleList();
            List<SaleList> l = JSON.parseArray(list,SaleList.class);
            ll.setSaleList(l);
            SaleMaster m = JSON.parseObject(master,SaleMaster.class);
            m.setQuantity(String.valueOf(l.size()));
            order.setSaleMaster(m);
            order.setSaleList(ll);
            initService();
            String result = soap.saleInput(getKeyOrderInput(order),order);
            log.debug("ERP SaleInput result code:"+result+",param:"+JSON.toJSONString(order));

            if (result.equals("00")) {
                map.put("resultCode", Constants.NORMAL);
            } else if (result.equals("11")) {
                map.put("resultCode", Constants.FAIL);
                map.put("message", "ERP数据库连接失败");
            } else if (result.equals("12")) {
                map.put("resultCode", Constants.FAIL);
                map.put("message", "ERP数据库更新失败");
            } else if (result.equals("13")) {
                map.put("resultCode", Constants.FAIL);
                map.put("message", m.getOrderId()+"订单重复");
            } else if (result.equals("21")) {
                map.put("resultCode", Constants.FAIL);
                map.put("message", "ERP验证失败");
            } else {
                map.put("resultCode", Constants.FAIL);
                map.put("message", "ERP未知错误");
            }
        }catch(Exception e){
            map.put("resultCode", Constants.FAIL);
            map.put("message", e.getMessage());
        }
        return map;
    }

    @RequestMapping("ReturnInput")
    public @ResponseBody
    JSONObject returnInput(@RequestParam(required = false)String master,@RequestParam(required = false)String list) {
        JSONObject map = new JSONObject();
        try {
            Orders order = new Orders();
            ArrayOfSaleList ll = new ArrayOfSaleList();
            List<SaleList> l = JSON.parseArray(list.replace("\\r\\n", "\\n"), SaleList.class);
            ll.setSaleList(l);
            order.setSaleList(ll);
            SaleMaster m = JSON.parseObject(master.replace("\\r\\n", "\\n"), SaleMaster.class);
            m.setQuantity(String.valueOf(l.size()));
            order.setSaleMaster(m);
            initService();
            String result = soap.returnInput(getKeyOrderInput(order),order);
            if (result.equals("00")) {
                map.put("resultCode", Constants.NORMAL);
            } else if (result.equals("11")) {
                map.put("resultCode", Constants.FAIL);
                map.put("message", "ERP数据库连接失败");
            } else if (result.equals("12")) {
                map.put("resultCode", Constants.FAIL);
                map.put("message", "ERP数据库更新失败");
            } else if (result.equals("13")) {
                map.put("resultCode", Constants.FAIL);
                map.put("message", m.getOrderId()+"订单重复");
            } else if (result.equals("21")) {
                map.put("resultCode", Constants.FAIL);
                map.put("message", "ERP验证失败");
            } else {
                map.put("resultCode", Constants.FAIL);
                map.put("message", "ERP未知错误");
            }
        } catch (Exception e) {
            map.put("resultCode", Constants.FAIL);
            map.put("message", e.getMessage());
        }
        return map;
    }

    @RequestMapping("SaleInputById")
    public @ResponseBody
    JSONObject saleInputById(@RequestParam(required = false)String id){
        JSONObject map = new JSONObject();
        try{
            map = sendService.sendOrderById(id);
        }catch(Exception e){
            map.put("resultCode", Constants.FAIL);
            map.put("message", e.getMessage());
        }
        return map;
    }

    @RequestMapping("SaleInputDelete")
    public @ResponseBody
    JSONObject saleInput(@RequestParam(required = false)String data){
        JSONObject map = new JSONObject();
        try{
            Orders order = JSON.parseObject(data, Orders.class);
            initService();
            String result = soap.saleInput(getKeyOrderInput(order),order);
            log.debug("ERP SaleInput result code:"+result,order);
            if (result.equals("00")) {
                map.put("resultCode", Constants.NORMAL);
            } else if (result.equals("11")) {
                map.put("resultCode", Constants.FAIL);
                map.put("message", "ERP数据库连接失败");
            } else if (result.equals("12")) {
                map.put("resultCode", Constants.FAIL);
                map.put("message", "ERP数据库更新失败");
            } else if (result.equals("21")) {
                map.put("resultCode", Constants.FAIL);
                map.put("message", "ERP验证失败");
            } else {
                map.put("resultCode", Constants.FAIL);
                map.put("message", "ERP未知错误");
            }
        }catch(Exception e){
            map.put("resultCode", Constants.FAIL);
            map.put("message", e.getMessage());
        }
        return map;
    }

    @RequestMapping("ServerUpdateExe")
    public @ResponseBody
    JSONObject serverUpdateExe(@RequestParam(required = false)String exeType){
        JSONObject map = new JSONObject();
        try{
            initService();
            String result = soap.serverUpdateExe(exeType);
            if (result.equals("00")) {
                map.put("resultCode", Constants.NORMAL);
            } else if (result.equals("11")) {
                map.put("resultCode", Constants.FAIL);
                map.put("message", "ERP数据库连接失败");
            } else if (result.equals("12")) {
                map.put("resultCode", Constants.FAIL);
                map.put("message", "ERP数据库更新失败");
            } else if (result.equals("21")) {
                map.put("resultCode", Constants.FAIL);
                map.put("message", "ERP验证失败");
            } else {
                map.put("resultCode", Constants.FAIL);
                map.put("message", "ERP未知错误");
            }
        }catch(Exception e){
            map.put("resultCode", Constants.FAIL);
            map.put("message", e.getMessage());
        }
        return map;
    }

    @RequestMapping("SendSms")
    public @ResponseBody
    JSONObject sendSms(@RequestParam(required = false)String data){
        JSONObject map = new JSONObject();
        try{
            SmsSend sms = JSON.parseObject(data,SmsSend.class);
            initService();
            SmsResult result = soap.sendSms(sms);
            if (result.equals("00")) {
                map.put("resultCode", Constants.NORMAL);
            } else if (result.equals("11")) {
                map.put("resultCode", Constants.FAIL);
                map.put("message", "ERP数据库连接失败");
            } else if (result.equals("12")) {
                map.put("resultCode", Constants.FAIL);
                map.put("message", "ERP数据库更新失败");
            } else if (result.equals("21")) {
                map.put("resultCode", Constants.FAIL);
                map.put("message", "ERP验证失败");
            } else {
                map.put("resultCode", Constants.FAIL);
                map.put("message", "ERP未知错误");
            }
        }catch(Exception e){
            map.put("resultCode", Constants.FAIL);
            map.put("message", e.getMessage());
        }
        return map;
    }
    private void initService() throws MalformedURLException {
        if(service==null)service = new net.dlyt.qyds.web.service.erp.Service(new URL(ErpSendUtil.WSDL_LOCATION));
        if(soap==null)soap = service.getServiceSoap();
    }

    @RequestMapping("VIPUsedUpdate")
    public @ResponseBody
    JSONObject vipUsedUpdate(@RequestParam(required = false)String data){
        JSONObject map = new JSONObject();
        try{
            Vip vip = JSON.parseObject(data, Vip.class);
            BaseDate date = new BaseDate();
            date.setVip(vip);
            initService();
            String result = soap.vipUsedUpdate(getKeyVIPUsedUpdate(date),date);
            log.debug("ERP VIPUsedUpdate result code:"+result,date);
            if (result.equals("00")) {
                map.put("resultCode", Constants.NORMAL);
            } else if (result.equals("11")) {
                map.put("resultCode", Constants.FAIL);
                map.put("message", "ERP数据库连接失败");
            } else if (result.equals("12")) {
                map.put("resultCode", Constants.FAIL);
                map.put("message", "ERP数据库更新失败");
            } else if (result.equals("21")) {
                map.put("resultCode", Constants.FAIL);
                map.put("message", "ERP验证失败");
            } else {
                map.put("resultCode", Constants.FAIL);
                map.put("message", "ERP未知错误");
            }
        }catch(Exception e){
            map.put("resultCode", Constants.FAIL);
            map.put("message", e.getMessage());
        }
        return map;
    }
}
