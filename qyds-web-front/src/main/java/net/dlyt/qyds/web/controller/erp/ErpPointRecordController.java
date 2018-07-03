package net.dlyt.qyds.web.controller.erp;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import net.dlyt.qyds.common.form.ErpPointRecordForm;
import net.dlyt.qyds.web.common.Constants;
import net.dlyt.qyds.web.service.ErpPointRecordService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * Created by zlh on 2016/8/22.
 */
@Controller
@RequestMapping("/erp_point_record")
public class ErpPointRecordController {

    @Resource
    private ErpPointRecordService service;

    @RequestMapping("getRecordPage")
    public @ResponseBody
    JSONObject getRecordList(ErpPointRecordForm form){
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
