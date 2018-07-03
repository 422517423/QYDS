package net.dlyt.qyds.web.controller.erp;

import com.alibaba.fastjson.JSONObject;
import net.dlyt.qyds.web.service.ErpBrandService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * Created by zlh on 2016/7/30.
 */
@Controller
@RequestMapping("/erp_brand")
public class ErpBrandController {

    @Resource
    private ErpBrandService service;

    @RequestMapping("list")
    public String list(){
        return "page/erp_brand/list.html";
    }

    @RequestMapping("getAll")
    public @ResponseBody
    JSONObject getList(){
        return service.selectAll();
    }
}
