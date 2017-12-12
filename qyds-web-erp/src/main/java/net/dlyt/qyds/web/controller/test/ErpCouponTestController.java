package net.dlyt.qyds.web.controller.test;

import com.alibaba.fastjson.JSONObject;
import net.dlyt.qyds.web.common.HttpRequest;
import net.dlyt.qyds.web.common.StringUtil;
import net.dlyt.qyds.web.service.ErpCouponService;
import net.dlyt.qyds.web.service.common.Constants;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * Created by zlh on 2016/9/4.
 */
@Controller
@RequestMapping("/erp_coupon_test")
public class ErpCouponTestController {

    @Resource
    private ErpCouponService service;

    @RequestMapping("issue.json")
    @ResponseBody
    public JSONObject issue(@RequestParam("data") String data){
        JSONObject map = new JSONObject();
        try{
            map = HttpRequest.sendErpPost("/erp_coupon/issue", data);
        }catch(Exception e){
            map.put("resultCode", Constants.FAIL);
            map.put("message", e.getMessage());
        }
        return map;
    }

    @RequestMapping("sku.json")
    @ResponseBody
    public JSONObject sku(@RequestParam("data") String data){
        JSONObject map = new JSONObject();
        try{
            map = HttpRequest.sendErpPost("/erp_coupon/sku", data);
        }catch(Exception e){
            map.put("resultCode", Constants.FAIL);
            map.put("message", e.getMessage());
        }
        return map;
    }

    @RequestMapping("send.json")
    @ResponseBody
    public JSONObject send(@RequestParam("data") String data){
        JSONObject map = new JSONObject();
        try{
            map = HttpRequest.sendErpPost("/erp_coupon/send", data);
        }catch(Exception e){
            map.put("resultCode", Constants.FAIL);
            map.put("message", e.getMessage());
        }
        return map;
    }

    @RequestMapping("used.json")
    @ResponseBody
    public JSONObject used(@RequestParam("data") String data){
        JSONObject map = new JSONObject();
        try{
            map = HttpRequest.sendErpPost("/erp_coupon/used", data);
        }catch(Exception e){
            map.put("resultCode", Constants.FAIL);
            map.put("message", e.getMessage());
        }
        return map;
    }

    @RequestMapping("check.json")
    @ResponseBody
    public JSONObject check(@RequestParam("data") String data){
        JSONObject map = new JSONObject();
        try{
            map = HttpRequest.sendErpPost("/erp_coupon/check", data);
        }catch(Exception e){
            map.put("resultCode", Constants.FAIL);
            map.put("message", e.getMessage());
        }
        return map;
    }
}
