package net.dlyt.qyds.web.controller.mmb_point_record;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import net.dlyt.qyds.common.dto.ext.MmbMasterExt;
import net.dlyt.qyds.common.dto.ext.MmbPointRecordExt;
import net.dlyt.qyds.common.form.MmbMasterForm;
import net.dlyt.qyds.common.form.MmbPointRecordForm;
import net.dlyt.qyds.web.common.Constants;
import net.dlyt.qyds.web.service.MmbPointRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created by YiLian on 16/7/28.
 */
@Controller
@RequestMapping("/mmb_point_record")
public class MmbPointRecordController {

    @Autowired
    private MmbPointRecordService mmbPointRecordService;


    @RequestMapping("pointRecord")
    public
    @ResponseBody
    JSONObject pointRecord(MmbPointRecordForm form) {
        JSONObject map = new JSONObject();
        try{
            map = mmbPointRecordService.selectRecordByPage(form);
        }catch(Exception e){
            map.put("resultCode",Constants.FAIL);
            map.put("message", e.getMessage());
        }
        return map;
    }

}
