package net.dlyt.qyds.web.controller.shp_stroe;

import com.alibaba.fastjson.JSONObject;
import net.dlyt.qyds.common.form.ShpStroeDetailForm;
import net.dlyt.qyds.web.service.ShpStroeDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created wy wy on 2016/8/10.
 */
@Controller
@RequestMapping("/shp_stroe_detail")
public class ShpStroeDetailController {
    @Autowired
    private ShpStroeDetailService service;

    @RequestMapping("getList")
    public
    @ResponseBody
    JSONObject getList(ShpStroeDetailForm form) {
        return service.selectAll(form);
    }
}
