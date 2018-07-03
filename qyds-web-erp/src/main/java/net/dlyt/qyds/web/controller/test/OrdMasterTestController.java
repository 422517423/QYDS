package net.dlyt.qyds.web.controller.test;


import com.alibaba.fastjson.JSONObject;
import net.dlyt.qyds.web.common.Constants;
import net.dlyt.qyds.web.common.EncryptUtil;
import net.dlyt.qyds.web.common.HttpRequest;
import net.dlyt.qyds.web.common.StringUtil;
import net.dlyt.qyds.web.service.OrdMasterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by zlh on 16/8/12.
 */
@Controller
@RequestMapping("/order_test")
public class OrdMasterTestController {


    @Autowired
    private OrdMasterService ordMasterService;

    /**
     * 根据店铺ID获取订单列表信息
     * @param data
     * @return json
     */
    @RequestMapping("sent")
    public @ResponseBody
    JSONObject sentOrder(@RequestParam(required = false)String data){
        JSONObject json = new JSONObject();
        try {
            json = HttpRequest.sendErpPost("/order/sent", data);
        }catch(Exception e){
            json.put("resultCode", Constants.FAIL);
            json.put("message", e.getMessage());
        }
        return json;
    }

    /**
     * 根据店铺ID获取订单列表信息
     * @param data
     * @return json
     */
    @RequestMapping("sent2")
    public @ResponseBody
    JSONObject sentOrder2(@RequestParam(required = false)String data){
        JSONObject json = new JSONObject();
        try {
            String key = StringUtil.getErpKey(data);
            data = EncryptUtil.encrypt(data);
            json = ordMasterService.sendOrder(StringUtil.getErpData(data,key));
        }catch(Exception e){
            json.put("resultCode", Constants.FAIL);
            json.put("message", e.getMessage());
        }
        return json;
    }
}
