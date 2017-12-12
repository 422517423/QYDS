package net.dlyt.qyds.web.controller.forerp;

import com.alibaba.fastjson.JSONObject;
import net.dlyt.qyds.web.common.StringUtil;
import net.dlyt.qyds.web.service.ErpBrandService;
import net.dlyt.qyds.web.service.ErpCouponService;
import net.dlyt.qyds.web.service.ErpSendService;
import net.dlyt.qyds.web.service.common.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * Created by zlh on 2016/9/4.
 */
@Controller
@RequestMapping("/erp_coupon")
public class ErpCouponController {

    @Resource
    private ErpCouponService service;
    protected final Logger log = LoggerFactory.getLogger(ErpCouponController.class);

    @RequestMapping("issue.json")
    @ResponseBody
    public JSONObject issue(@RequestParam("data") String data, @RequestParam(required = false)String key){
        JSONObject map = new JSONObject();
        try{
            data = StringUtil.getErpData(data,key);
            log.debug("param:"+ data);
            map = service.receiveCoupon(data);
        }catch(Exception e){
            map.put("resultCode", Constants.FAIL);
            map.put("message", e.getMessage());
        }
        log.debug("result:"+map);
        return map;
    }

    @RequestMapping("sku.json")
    @ResponseBody
    public JSONObject sku(@RequestParam("data") String data, @RequestParam(required = false)String key){
        JSONObject map = new JSONObject();
        try{
            data = StringUtil.getErpData(data,key);
            log.debug("param:"+ data);
            map = service.receiveCouponSku(data);
        }catch(Exception e){
            map.put("resultCode", Constants.FAIL);
            map.put("message", e.getMessage());
        }
        log.debug("result:"+map);
        return map;
    }

    @RequestMapping("send.json")
    @ResponseBody
    public JSONObject send(@RequestParam("data") String data, @RequestParam(required = false)String key){
        JSONObject map = new JSONObject();
        try{
            data = StringUtil.getErpData(data,key);
            log.debug("param:"+ data);
            map = service.receiveCouponMember(data);
        }catch(Exception e){
            map.put("resultCode", Constants.FAIL);
            map.put("message", e.getMessage());
        }
        log.debug("result:"+map);
        return map;
    }

    @RequestMapping("used.json")
    @ResponseBody
    public JSONObject used(@RequestParam("data") String data, @RequestParam(required = false)String key){
        JSONObject map = new JSONObject();
        try{
            data = StringUtil.getErpData(data,key);
            log.debug("param:"+ data);
            map = service.receiveCouponUsed(data);
        }catch(Exception e){
            map.put("resultCode", Constants.FAIL);
            map.put("message", e.getMessage());
        }
        log.debug("result:"+map);
        return map;
    }

    @RequestMapping("check.json")
    @ResponseBody
    public JSONObject check(@RequestParam("data") String data, @RequestParam(required = false)String key){
        JSONObject map = new JSONObject();
        try{
            data = StringUtil.getErpData(data,key);
            log.debug("param:"+ data);
            map = service.checkCoupon(data);
        }catch(Exception e){
            map.put("resultCode", Constants.FAIL);
            map.put("message", e.getMessage());
        }
        log.debug("result:"+map);
        return map;
    }
}
