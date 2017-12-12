package net.dlyt.qyds.web.controller.mmb_point_rule;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import net.dlyt.qyds.common.form.MmbPointRuleForm;
import net.dlyt.qyds.web.context.PamsDataContext;
import net.dlyt.qyds.web.service.MmbPointRuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created by YiLian on 16/7/29.
 */
@Controller
@RequestMapping("/mmb_point_rule")
public class MmbPointRuleController {

    @Autowired
    private MmbPointRuleService mmbPointRuleService;


    @RequestMapping("getList")
    @ResponseBody
    public JSONObject getList(MmbPointRuleForm form) {
        return mmbPointRuleService.getList(form);
    }

    @RequestMapping("getDetail")
    @ResponseBody
    public JSONObject getDetail(String data) {

        MmbPointRuleForm form = (MmbPointRuleForm) JSON.parseObject(data, MmbPointRuleForm.class);

        return mmbPointRuleService.getDetail(form);
    }

    @RequestMapping("add")
    @ResponseBody
    public JSONObject add(String data) {

        MmbPointRuleForm form = (MmbPointRuleForm) JSON.parseObject(data, MmbPointRuleForm.class);
        String userId = (String) PamsDataContext.get("loginId");
        return mmbPointRuleService.add(form, userId);
    }

    @RequestMapping("edit")
    @ResponseBody
    public JSONObject edit(String data) {

        MmbPointRuleForm form = (MmbPointRuleForm) JSON.parseObject(data, MmbPointRuleForm.class);
        String userId = (String) PamsDataContext.get("loginId");
        return mmbPointRuleService.edit(form, userId);
    }

    @RequestMapping("delete")
    @ResponseBody
    public JSONObject delete(String data) {

        MmbPointRuleForm form = (MmbPointRuleForm) JSON.parseObject(data, MmbPointRuleForm.class);
        String userId = (String) PamsDataContext.get("loginId");
        return mmbPointRuleService.delete(form, userId);
    }

}
