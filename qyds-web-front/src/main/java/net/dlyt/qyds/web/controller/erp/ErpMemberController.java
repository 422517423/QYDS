package net.dlyt.qyds.web.controller.erp;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import net.dlyt.qyds.common.form.ErpMemberForm;
import net.dlyt.qyds.web.common.Constants;
import net.dlyt.qyds.web.service.ErpMemberService;
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
@RequestMapping("/erp_member")
public class ErpMemberController {

    @Resource
    private ErpMemberService service;

    @RequestMapping("list")
    public String list(){
        return "page/erp_member/list.html";
    }

    @RequestMapping("getAll")
    public @ResponseBody
    JSONObject getAll(){
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
    JSONObject getList(ErpMemberForm form){
        JSONObject map = new JSONObject();
        try{
            map = service.selectByPage(JSON.toJSONString(form));
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
    JSONObject getMember(String id){
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
