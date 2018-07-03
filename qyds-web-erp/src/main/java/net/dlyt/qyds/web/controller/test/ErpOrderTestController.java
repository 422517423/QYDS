package net.dlyt.qyds.web.controller.test;

import com.alibaba.fastjson.JSONObject;
import net.dlyt.qyds.web.common.Constants;
import net.dlyt.qyds.web.common.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by zlh on 2016/7/30.
 */
@Controller
@RequestMapping("/erp_order_test")
public class ErpOrderTestController {

    @RequestMapping("master.json")
    @ResponseBody
    public JSONObject master(@RequestParam(required = false)String data){
        JSONObject result = new JSONObject();
        try {
            //发送 POST 请求
            result = HttpRequest.sendErpPost("/erp_order/master", data);
        } catch (Exception e) {
            result.put("resultCode",Constants.FAIL);
            result.put("message", e.getMessage());
        }

        return result;
    }

    @RequestMapping("sub.json")
    @ResponseBody
    public JSONObject sub(@RequestParam(required = false)String data){
        JSONObject result = new JSONObject();
        try {
            //发送 POST 请求
            result = HttpRequest.sendErpPost("/erp_order/sub", data);
        } catch (Exception e) {
            result.put("resultCode",Constants.FAIL);
            result.put("message", e.getMessage());
        }

        return result;
    }
}
