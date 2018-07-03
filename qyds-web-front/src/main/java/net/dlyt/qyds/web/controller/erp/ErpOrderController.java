package net.dlyt.qyds.web.controller.erp;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import net.dlyt.qyds.common.form.ErpBankRecordForm;
import net.dlyt.qyds.web.common.Constants;
import net.dlyt.qyds.web.service.ErpBankRecordService;
import net.dlyt.qyds.web.service.ErpOrderService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
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

    @RequestMapping("list")
    public String list(){
        return "page/erp_order/list.html";
    }

    @RequestMapping("getMasterPage")
    public @ResponseBody
    JSONObject getMasterPage(ErpBankRecordForm form){
        JSONObject map = new JSONObject();
        try{
            String json = JSON.toJSONString(form);
            map = service.getMasterPage(json);
        }catch(Exception e){
            map.put("resultCode",Constants.FAIL);
            map.put("message", e.getMessage());
        }
        return map;
    }

    @RequestMapping("getSubPage")
    public @ResponseBody
    JSONObject getSubPage(ErpBankRecordForm form){
        JSONObject map = new JSONObject();
        try{
            String json = JSON.toJSONString(form);
            map = service.getSubPage(json);
        }catch(Exception e){
            map.put("resultCode",Constants.FAIL);
            map.put("message", e.getMessage());
        }
        return map;
    }
}
