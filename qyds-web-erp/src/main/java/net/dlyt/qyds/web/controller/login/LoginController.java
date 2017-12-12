package net.dlyt.qyds.web.controller.login;

import com.alibaba.fastjson.JSONObject;
import net.dlyt.qyds.web.common.Constants;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by C_Nagai on 2016/6/25.
 */
@Controller
@RequestMapping("/auth")
public class LoginController {

    @RequestMapping("401")
    public @ResponseBody
    JSONObject unauthorized(){
        JSONObject json = new JSONObject();
        json.put("resultCode", Constants.UNAUTHORIZED);
        json.put("operation", Constants.FAIL);
        json.put("message","unauthorized");
        return json;
    }

    @RequestMapping("403")
    public @ResponseBody
    JSONObject forbidden(){
        JSONObject json = new JSONObject();
        json.put("resultCode", Constants.FORBIDDEN);
        json.put("operation", Constants.FAIL);
        json.put("message","forbidden");
        return json;
    }

}
