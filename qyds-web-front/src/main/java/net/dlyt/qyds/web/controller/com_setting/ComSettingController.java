package net.dlyt.qyds.web.controller.com_setting;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import net.dlyt.qyds.common.dto.ext.ComSettingExt;
import net.dlyt.qyds.web.service.ComSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by cjk on 2016/12/14.
 */
@Controller
@RequestMapping("/com_setting")
public class ComSettingController {

    @Autowired
    ComSettingService comSettingService;

    @RequestMapping("getDetail")
    @ResponseBody
    public JSONObject getDetail() {
        return comSettingService.getDetail();
    }

    @RequestMapping("edit")
    @ResponseBody
    public JSONObject edit(String data) {
        ComSettingExt form = (ComSettingExt) JSON.parseObject(data, ComSettingExt.class);
        return comSettingService.edit(form);
    }

}
