package net.dlyt.qyds.web.controller.erp;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import net.dlyt.qyds.common.form.ErpGoodsForm;
import net.dlyt.qyds.web.common.Constants;
import net.dlyt.qyds.web.service.ErpGoodsService;
import net.dlyt.qyds.web.service.exception.ExceptionBusiness;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by zlh on 2016/7/30.
 */
@Controller
@RequestMapping("/erp_goods")
public class ErpGoodsController {

    @Resource
    private ErpGoodsService service;

    @RequestMapping("list")
    public String list(){
        return "page/erp_goods/list.html";
    }

    @RequestMapping("getAll")
    public @ResponseBody
    JSONObject getList(){
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

    @RequestMapping("getPage")
    public @ResponseBody
    JSONObject getList(ErpGoodsForm form){
        JSONObject map = new JSONObject();
        try{
            String record = JSON.toJSONString(form);
            map = service.selectByPage(record);
        }catch(ExceptionBusiness e){
            map.put("resultCode", Constants.FAIL);
            map.put("message", e.getMessage());
        }catch(Exception e){
            map.put("resultCode", Constants.FAIL);
            map.put("message", e.getMessage());
        }
        return map;
    }

    @RequestMapping("edit")
    public @ResponseBody
    JSONObject getGoods(String id){
        JSONObject map = new JSONObject();
        try{
            map = service.getById(id);
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
