package net.dlyt.qyds.web.controller.test;

import com.alibaba.fastjson.JSONObject;
import net.dlyt.qyds.web.common.Constants;
import net.dlyt.qyds.web.common.HttpRequest;
import net.dlyt.qyds.web.common.StringUtil;
import net.dlyt.qyds.web.service.ErpGoodsService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * Created by zlh on 2016/7/30.
 */
@Controller
@RequestMapping("/erp_goods_test")
public class ErpGoodsTestController {

    @Resource
    private ErpGoodsService service;

    @RequestMapping("input.json")
    @ResponseBody
    //batch:1为整体导入，删除所以旧数据重新导入，0为部分更新，列表数据中只包括变化的数据，需要参照style处理
    public JSONObject input(@RequestParam("data") String data,@RequestParam("batch") int batch){
        JSONObject map = new JSONObject();
        try{
            map = HttpRequest.sendErpPost("/erp_goods/input", data,batch);
        }catch(Exception e){
            map.put("resultCode",Constants.FAIL);
            map.put("message", e.getMessage());
        }
        return map;
    }
}
