package net.dlyt.qyds.web.controller.erp;

import com.alibaba.fastjson.JSONObject;
import net.dlyt.qyds.web.common.Constants;
import net.dlyt.qyds.web.service.ErpGoodsColorService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * Created by zlh on 2016/7/26.
 */
@Controller
@RequestMapping("/erp_color")
public class ErpGoodsColorController {

    @Resource
    private ErpGoodsColorService service;

    @RequestMapping("list")
    public String list(){
        return "page/erp_color/list.html";
    }

    @RequestMapping("getAll")
    public @ResponseBody
    JSONObject getList(){
        JSONObject map = new JSONObject();
        try{
            map = service.selectAll();
        }catch(Exception e){
            map.put("resultCode", Constants.FAIL);
            map.put("message", e.getMessage());
        }
        return map;
    }
}
