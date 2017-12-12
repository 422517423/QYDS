package net.dlyt.qyds.web.controller.mmb_shp_master;

import com.alibaba.fastjson.JSONObject;
import net.dlyt.qyds.common.dto.ext.MmbMasterExt;
import net.dlyt.qyds.common.form.MmbMasterForm;
import net.dlyt.qyds.web.common.Constants;
import net.dlyt.qyds.web.service.MmbMasterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created by C_Nagai on 2016/9/3.
 */
@Controller
@RequestMapping("/mmb_shp_master")
public class MmbShpMasterController {

    @Autowired
    private MmbMasterService mmbMasterService;

    @RequestMapping("getList")
    public
    @ResponseBody
    JSONObject getList(MmbMasterForm form) {
        return mmbMasterService.getListByPhone(form);
    }
}
