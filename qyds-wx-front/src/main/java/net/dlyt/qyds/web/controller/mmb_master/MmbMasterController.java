package net.dlyt.qyds.web.controller.mmb_master;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import net.dlyt.qyds.common.dto.MmbMaster;
import net.dlyt.qyds.common.dto.ext.MmbMasterExt;
import net.dlyt.qyds.common.form.MmbMasterForm;
import net.dlyt.qyds.web.common.Constants;
import net.dlyt.qyds.web.common.EncryptUtil;
import net.dlyt.qyds.web.service.MmbMasterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created by C_Nagai on 2016/7/27.
 */
@Controller
@RequestMapping("/mmb_master")
public class MmbMasterController {

    @Autowired
    private MmbMasterService mmbMasterService;

    @RequestMapping("getDetail")
    public
    @ResponseBody
    JSONObject getDetail(String data) {
        JSONObject param = JSONObject.parseObject(data);
        MmbMaster mmbMaster = new MmbMaster();
        mmbMaster.setMemberId(param.getString("memberId"));
        return mmbMasterService.selectBySelective(mmbMaster);
    }

    @RequestMapping("save")
    public
    @ResponseBody
    JSONObject save(String data) {
        JSONObject json = null;
        try {
            MmbMaster mmbMaster = (MmbMaster) JSON.parseObject(data, MmbMaster.class);
            if (StringUtils.isEmpty(mmbMaster.getMemberId())) {
                mmbMaster.setIsValid(Constants.VALID);
                String password = mmbMaster.getPassword();
                password = EncryptUtil.encodeMD5(password.toLowerCase());
                password = EncryptUtil.encodeMD5(password.toLowerCase());
                mmbMaster.setPassword(password);
                json = mmbMasterService.insertSelective(mmbMaster);
            } else {
                mmbMaster.setPassword(null);
                json = mmbMasterService.updateByPrimaryKeySelective(mmbMaster);
            }
            if (json != null) {
                return json;
            }
        } catch (Exception e) {
            json = new JSONObject();
            json.put("resultCode", Constants.FAIL);
        }
        return json;

    }


    @RequestMapping("editPersonal")
    @ResponseBody
    public JSONObject editPersonal(String data) {
        JSONObject json = new JSONObject();
        try {
            MmbMaster mmbMaster = (MmbMaster) JSON.parseObject(data, MmbMaster.class);
            if (StringUtils.isEmpty(mmbMaster.getMemberId())) {
                json.put("resultCode", Constants.FAIL);
                json.put("resultMessage", "缺少主键信息");
            } else {
                json = mmbMasterService.updateByPrimaryKeySelective(mmbMaster);
            }
            if (json != null) {
                return json;
            }
        } catch (Exception e) {
            json.put("resultCode", Constants.FAIL);
        }
        return json;

    }

    @RequestMapping("modifyMemberAvatarFromWX")
    @ResponseBody
    public JSONObject modifyMemberAvatarFromWX(String data) {
        MmbMasterExt mmbMaster = (MmbMasterExt) JSON.parseObject(data, MmbMasterExt.class);
        return mmbMasterService.modifyMemberAvatarFromWX(mmbMaster);

    }


    /**
     * ERP地址列表取得
     *
     * @param data
     * @return
     */
    @RequestMapping("getAddressList")
    @ResponseBody
    public JSONObject getAddressList(String data) {
        JSONObject json = new JSONObject();
        try {
            MmbMaster mmbMaster = (MmbMaster) JSON.parseObject(data, MmbMaster.class);
            json = mmbMasterService.getAddressList(mmbMaster);
        } catch (Exception e) {
            json.put("resultCode", Constants.FAIL);
        }
        return json;

    }

}