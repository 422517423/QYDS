package net.dlyt.qyds.web.controller.mmb_level;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import net.dlyt.qyds.common.form.MmbLevelManagerForm;
import net.dlyt.qyds.common.form.MmbLevelRuleForm;
import net.dlyt.qyds.web.context.PamsDataContext;
import net.dlyt.qyds.web.service.MmbLevelManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by YiLian on 16/8/8.
 */
@Controller
@RequestMapping("/mmb_level")
public class MmbLevelManagerController {

    @Autowired
    private MmbLevelManagerService mmbLevelManagerService;

    /**
     * 待升级审批会员一览
     *
     * @param form
     * @return
     */
    @RequestMapping("getList")
    @ResponseBody
    public JSONObject getList(MmbLevelManagerForm form) {

        return mmbLevelManagerService.getList(form);
    }

    /**
     * 确认升级会员等级
     *
     * @param data
     * @return
     */
    @RequestMapping("approval")
    @ResponseBody
    public JSONObject approval(String data) {

        MmbLevelManagerForm form = (MmbLevelManagerForm) JSON.parseObject(data, MmbLevelManagerForm.class);
        String userId = (String) PamsDataContext.get("loginId");
        return mmbLevelManagerService.approval(form, userId);
    }
}
