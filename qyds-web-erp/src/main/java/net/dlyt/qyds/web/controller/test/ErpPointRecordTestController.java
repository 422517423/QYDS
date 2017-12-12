package net.dlyt.qyds.web.controller.test;

import com.alibaba.fastjson.JSONObject;
import net.dlyt.qyds.web.common.Constants;
import net.dlyt.qyds.web.common.HttpRequest;
import net.dlyt.qyds.web.common.StringUtil;
import net.dlyt.qyds.web.service.ErpPointRecordService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * Created by zlh on 2016/7/30.
 */
@Controller
@RequestMapping("/erp_point_record_test")
public class ErpPointRecordTestController {

    @Resource
    private ErpPointRecordService service;

    @RequestMapping("input.json")
    @ResponseBody
    public JSONObject input(@RequestParam(required = false)String data){
        JSONObject result = new JSONObject();
        try {
            //发送 POST 请求
            result = HttpRequest.sendErpPost("/erp_point_record/input", data);
        } catch (Exception e) {
            result.put("resultCode",Constants.FAIL);
            result.put("message", e.getMessage());
        }

        return result;
    }
}
