package net.dlyt.qyds.web.controller.mmb_group;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import net.dlyt.qyds.common.form.MmbGroupForm;
import net.dlyt.qyds.web.context.PamsDataContext;
import net.dlyt.qyds.web.service.MmbGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by YiLian on 16/7/29.
 */
@Controller
@RequestMapping("/mmb_group")
public class MmbGroupController {

    @Autowired
    private MmbGroupService mmbGroupService;

}
