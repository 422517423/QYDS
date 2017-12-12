package net.dlyt.qyds.web.controller.bnk_master;

import com.alibaba.fastjson.JSONObject;
import net.dlyt.qyds.common.dto.BnkMaster;
import net.dlyt.qyds.common.dto.BnkMasterExt;
import net.dlyt.qyds.common.form.BnkMasterForm;
import net.dlyt.qyds.web.common.Constants;
import net.dlyt.qyds.web.service.BnkMasterService;
import net.dlyt.qyds.web.service.exception.ExceptionBusiness;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringArrayPropertyEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created by wy on 2016/8/2.
 */
@Controller
@RequestMapping("/bnk_master")
public class BnkMasterController {
    @Autowired
    private BnkMasterService bnkMasterService;

    /**
     * 获取库存数据
     * @param form
     * @return
     */
    @RequestMapping("getList")
    public
    @ResponseBody
    JSONObject getList(BnkMasterForm form) {
        return bnkMasterService.selectAll(form);
    }

    /**
     * 根据ID获取
     * @param bankId
     * @return
     */
    @RequestMapping("edit.json")
    public
    @ResponseBody
    JSONObject edit(String bankId) {
        return bnkMasterService.selectByPrimaryKey(bankId);
    }

    /**
     * 商品再入库
     * @param data
     * @return
     */
    @RequestMapping("update.json")
    public
    @ResponseBody
    JSONObject update(String data) {
        return bnkMasterService.save(data);
    }
}
