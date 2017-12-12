package net.dlyt.qyds.web.controller.mmb_contact;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import net.dlyt.qyds.common.dto.MmbContact;
import net.dlyt.qyds.common.form.MmbContactExt;
import net.dlyt.qyds.common.form.MmbLevelManagerForm;
import net.dlyt.qyds.web.context.PamsDataContext;
import net.dlyt.qyds.web.service.MmbContactService;
import net.dlyt.qyds.web.service.MmbLevelManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by YiLian on 16/8/8.
 */
@Controller
@RequestMapping("/mmb_contact")
public class MmbContactController {

    @Autowired
    private MmbContactService mmbContactService;

    /**
     * 一览
     *
     * @param form
     * @return
     */
    @RequestMapping("getList")
    @ResponseBody
    public JSONObject getList(MmbContactExt form) {

        return mmbContactService.getList(form);
    }


}
