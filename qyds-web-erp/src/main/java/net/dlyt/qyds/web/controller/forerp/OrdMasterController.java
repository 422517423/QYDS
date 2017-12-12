package net.dlyt.qyds.web.controller.forerp;


import com.alibaba.fastjson.JSONObject;
import net.dlyt.qyds.web.common.Constants;
import net.dlyt.qyds.web.common.StringUtil;
import net.dlyt.qyds.web.service.OrdMasterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created by zlh on 16/8/12.
 */
@Controller
@RequestMapping("/order")
public class OrdMasterController {


    @Autowired
    private OrdMasterService ordMasterService;
    protected final Logger log = LoggerFactory.getLogger(ErpBankRecordController.class);

    /**
     * 发送订单信息
     * @param data
     * @return json
     */
//    @RequestMapping("sent")
//    public @ResponseBody
//    JSONObject sentOrder(@RequestParam(required = false)String data){
//        JSONObject json = new JSONObject();
//        try {
//            json = ordMasterService.sendOrder(data);
//        }catch(Exception e){
//            json.put("resultCode", Constants.FAIL);
//            json.put("message", e.getMessage());
//        }
//        return json;
//    }

    /**
     * 发送订单信息
     * @param data
     * @return json
     */
    @RequestMapping("sent")
    public @ResponseBody
    JSONObject sentOrder(@RequestParam(required = false)String data,@RequestParam(required = false)String key){
        JSONObject json = new JSONObject();
        try {
            data = StringUtil.getErpData(data,key);
            log.debug("param:"+ data);
            json = ordMasterService.sendOrder(data);
        }catch(Exception e){
            json.put("resultCode", Constants.FAIL);
            json.put("message", e.getMessage());
        }
        log.debug("result:"+json);
        return json;
    }
}
