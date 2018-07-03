package net.dlyt.qyds.web.controller.erp;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import net.dlyt.qyds.common.form.ErpBankRecordForm;
import net.dlyt.qyds.web.common.Constants;
import net.dlyt.qyds.web.service.ErpBankRecordService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * Created by zlh on 2016/7/30.
 */
@Controller
@RequestMapping("/erp_bank_record")
public class ErpBankRecordController {

    @Resource
    private ErpBankRecordService service;

    @RequestMapping("list")
    public String list(){
        return "page/erp_bank_record/list.html";
    }

    @RequestMapping("getAll")
    public @ResponseBody
    JSONObject getList(){
        return service.selectAll();
    }

    @RequestMapping("getSumPage")
    public @ResponseBody
    JSONObject getSumList(ErpBankRecordForm form){
        JSONObject map = new JSONObject();
        try{
            String json = JSON.toJSONString(form);
            map = service.selectSumByPage(json);
        }catch(Exception e){
            map.put("resultCode",Constants.FAIL);
            map.put("message", e.getMessage());
        }
        return map;
    }

    @RequestMapping("getRecordPage")
    public @ResponseBody
    JSONObject getRecordList(ErpBankRecordForm form){
        JSONObject map = new JSONObject();
        try{
            String json = JSON.toJSONString(form);
            map = service.selectRecordByPage(json);
        }catch(Exception e){
            map.put("resultCode",Constants.FAIL);
            map.put("message", e.getMessage());
        }
        return map;
    }
}
