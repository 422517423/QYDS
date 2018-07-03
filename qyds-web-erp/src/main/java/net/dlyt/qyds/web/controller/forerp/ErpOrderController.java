package net.dlyt.qyds.web.controller.forerp;

import com.alibaba.fastjson.JSONObject;
import net.dlyt.qyds.web.common.Constants;
import net.dlyt.qyds.web.common.StringUtil;
import net.dlyt.qyds.web.service.ErpOrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * Created by zlh on 2016/7/30.
 */
@Controller
@RequestMapping("/erp_order")
public class ErpOrderController {

    @Resource
    private ErpOrderService service;
    protected final Logger log = LoggerFactory.getLogger(ErpOrderController.class);

    @RequestMapping("master.json")
    @ResponseBody
    public JSONObject master(@RequestParam(required = false)String data,@RequestParam(required = false)String key){
        JSONObject result = new JSONObject();
        try {
            data = StringUtil.getErpData(data,key);
            log.debug("param:"+ data);
            result = service.inputMaster(data);
        } catch (Exception e) {
            result.put("resultCode", Constants.FAIL);
            result.put("message", e.getMessage());
        }
        log.debug("result:"+result);
        return result;
    }

    @RequestMapping("sub.json")
    @ResponseBody
    public JSONObject sub(@RequestParam(required = false)String data,@RequestParam(required = false)String key){
        JSONObject result = new JSONObject();
        try {
            data = StringUtil.getErpData(data,key);
            log.debug("param:"+ data);
            result = service.inputSub(data);
        } catch (Exception e) {
            result.put("resultCode", Constants.FAIL);
            result.put("message", e.getMessage());
        }
        log.debug("result:"+result);
        return result;
    }
}
