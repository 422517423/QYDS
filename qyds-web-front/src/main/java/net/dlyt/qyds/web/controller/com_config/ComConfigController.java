package net.dlyt.qyds.web.controller.com_config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import net.dlyt.qyds.common.dto.ComConfig;
import net.dlyt.qyds.web.context.PamsDataContext;
import net.dlyt.qyds.web.service.ComConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by YiLian on 2016/12/27.
 */
@Controller
@RequestMapping("/com_config")
public class ComConfigController {

    @Autowired
    private ComConfigService comConfigService;

    @RequestMapping("getLevelRatio")
    public
    @ResponseBody
    JSONObject getLevelRatio() {
        return comConfigService.getLevelRatio();
    }

    @RequestMapping("editLevelRatio")
    public
    @ResponseBody
    JSONObject editLevelRatio(String data) {
        ComConfig form = (ComConfig) JSON.parseObject(data, ComConfig.class);
        String userId = (String) PamsDataContext.get("loginId");
        form.setUpdateUserId(userId);
        return comConfigService.edit(form);
    }


}
