package net.dlyt.qyds.web.controller.mmb_level_rule;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import net.dlyt.qyds.common.dto.MmbLevelRule;
import net.dlyt.qyds.common.dto.ext.MmbLevelRuleExt;
import net.dlyt.qyds.common.dto.ext.MmbMasterExt;
import net.dlyt.qyds.common.form.MmbGroupForm;
import net.dlyt.qyds.common.form.MmbLevelRuleForm;
import net.dlyt.qyds.web.common.Constants;
import net.dlyt.qyds.web.context.PamsDataContext;
import net.dlyt.qyds.web.service.MmbLevelRuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created by C_Nagai on 2016/7/27.
 */
@Controller
@RequestMapping("/mmb_level_rule")
public class MmbLevelRuleController {

    @Autowired
    private MmbLevelRuleService mmbLevelRuleService;

}