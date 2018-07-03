package net.dlyt.qyds.web.controller.erp;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import net.dlyt.qyds.common.dto.ErpProduceLine;
import net.dlyt.qyds.common.dto.ext.ErpProduceLineExt;
import net.dlyt.qyds.web.common.Constants;
import net.dlyt.qyds.web.service.ErpProduceLineService;
import net.dlyt.qyds.web.service.exception.ExceptionBusiness;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by zlh on 2016/7/30.
 */
@Controller
@RequestMapping("/erp_produce_line")
public class ErpProduceLineController {

    @Resource
    private ErpProduceLineService service;

    @RequestMapping("list")
    public String list(){
        return "page/erp_produce_line/list.html";
    }

    @RequestMapping("getAll")
    public @ResponseBody
    JSONObject getList(String data){
        JSONObject map = new JSONObject();
        try{
            map = service.selectAll();
        }catch(ExceptionBusiness e){
            map.put("resultCode", Constants.FAIL);
            map.put("message", e.getMessage());
        }catch(Exception e){
            map.put("resultCode", Constants.FAIL);
            map.put("message", e.getMessage());
        }
        return map;
    }
}
