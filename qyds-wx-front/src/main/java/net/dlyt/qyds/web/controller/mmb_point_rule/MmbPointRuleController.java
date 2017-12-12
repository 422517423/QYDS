package net.dlyt.qyds.web.controller.mmb_point_rule;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import net.dlyt.qyds.common.form.MmbPointRuleForm;
import net.dlyt.qyds.web.common.Constants;
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

    /**
     * 根据的规则编码获取对应的规则信息
     *
     * @param data ruleCode:规则编码
     *             常用规则编码:消费积分规则=10、生日积分规则=90、积分抵值规则=70
     * @return
     */
    @RequestMapping("getRuleByCode")
    @ResponseBody
    public JSONObject getRuleByCode(String data) {

        JSONObject json = new JSONObject();
        try {

            MmbPointRuleForm form = (MmbPointRuleForm) JSON.parseObject(data, MmbPointRuleForm.class);

            json = mmbPointRuleService.getRuleByCode(form);

        } catch (Exception e) {
            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", e.getMessage());
        }
        return json;
    }

}
