package net.dlyt.qyds.web.controller.bnk_records;
import com.alibaba.fastjson.JSONObject;
import net.dlyt.qyds.common.dto.BnkMasterExt;
import net.dlyt.qyds.common.dto.ext.BnkRecordsExt;
import net.dlyt.qyds.common.form.BnkRecordsForm;
import net.dlyt.qyds.web.common.Constants;
import net.dlyt.qyds.web.service.BnkRecordsService;
import net.dlyt.qyds.web.service.exception.ExceptionBusiness;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created by wy on 2016/8/3.
 */
@Controller
@RequestMapping("/bnk_records")
public class BnkRecordsController {
    @Autowired
    private BnkRecordsService service;

    /**
     * 获取列表
     * @param form
     * @return
     */
    @RequestMapping("getList")
    public @ResponseBody JSONObject getList(BnkRecordsForm form){
    return service.getList(form);
    }
}
