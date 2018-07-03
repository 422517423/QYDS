package net.dlyt.qyds.web.controller.mmb_group;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import net.dlyt.qyds.common.dto.MmbMaster;
import net.dlyt.qyds.common.form.MmbGroupForm;
import net.dlyt.qyds.web.context.PamsDataContext;
import net.dlyt.qyds.web.service.ComDistrictService;
import net.dlyt.qyds.web.service.MmbGroupService;
import net.dlyt.qyds.web.service.MmbMasterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * Created by YiLian on 16/7/29.
 */
@Controller
@RequestMapping("/mmb_group")
public class MmbGroupController {

    @Autowired
    private MmbGroupService mmbGroupService;

    @Autowired
    private MmbMasterService mmbMasterService;

    @Resource
    private ComDistrictService comDistrictService;

    @RequestMapping("getList")
    @ResponseBody
    public JSONObject getList(MmbGroupForm form) {
        return mmbGroupService.getList(form);
    }

    @RequestMapping("getDetail")
    @ResponseBody
    public JSONObject getDetail(String data) {

        MmbGroupForm form = (MmbGroupForm) JSON.parseObject(data, MmbGroupForm.class);

        return mmbGroupService.getDetail(form);
    }

    @RequestMapping("add")
    @ResponseBody
    public JSONObject add(String data) {

        MmbGroupForm form = (MmbGroupForm) JSON.parseObject(data, MmbGroupForm.class);
        String userId = (String) PamsDataContext.get("loginId");
        return mmbGroupService.add(form, userId);
    }

    @RequestMapping("edit")
    @ResponseBody
    public JSONObject edit(String data) {

        MmbGroupForm form = (MmbGroupForm) JSON.parseObject(data, MmbGroupForm.class);
        String userId = (String) PamsDataContext.get("loginId");
        return mmbGroupService.edit(form, userId);
    }

    @RequestMapping("delete")
    @ResponseBody
    public JSONObject delete(String data) {

        MmbGroupForm form = (MmbGroupForm) JSON.parseObject(data, MmbGroupForm.class);
        String userId = (String) PamsDataContext.get("loginId");
        return mmbGroupService.delete(form, userId);
    }


    @RequestMapping("getMemberList")
    @ResponseBody
    public JSONObject getMemberList(MmbGroupForm form) {
        return mmbGroupService.getMemberList(form);
    }


    @RequestMapping("updateMemberList")
    @ResponseBody
    public JSONObject updateMemberList(String data) {

        MmbGroupForm form = (MmbGroupForm) JSON.parseObject(data, MmbGroupForm.class);
        String userId = (String) PamsDataContext.get("loginId");
        return mmbGroupService.updateMemberList(form, userId);
    }

    /**
     * 地址取得
     */
    @RequestMapping("getProvinces")
    public @ResponseBody JSONObject getProvinces(MmbMaster mmbMaster) {
        return mmbMasterService.getAddressList(mmbMaster);
    }

}
