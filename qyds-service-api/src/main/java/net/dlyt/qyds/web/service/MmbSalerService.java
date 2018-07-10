package net.dlyt.qyds.web.service;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonObject;
import net.dlyt.qyds.common.dto.MmbSaler;
import net.dlyt.qyds.common.dto.SysUser;
import net.dlyt.qyds.common.dto.SysUserExt;
import net.dlyt.qyds.common.dto.ext.MmbSalerExt;
import net.dlyt.qyds.common.form.MmbSalerForm;

import java.util.List;

/**
 * Created by ZLH on 16/12/16.
 */
public interface MmbSalerService {

    List<MmbSalerExt> selectAll(MmbSalerExt ext);

    int getAllDataCount(MmbSalerExt ext);

    JSONObject insertSelective(MmbSaler user);

    JSONObject selectByPrimaryKey(String memerId);

    JSONObject updateByPrimaryKeySelective(MmbSaler user);

    JSONObject deleteByPrimaryKey(MmbSaler user);

    JSONObject selectBySelective(MmbSaler user);

    JSONObject loginSelective(MmbSaler sysUser);

    JSONObject registerUser(MmbSaler sysUser);

    JSONObject getAddressList(MmbSaler mmbSaler);

    JSONObject changePassword(MmbSaler sysUser);

    // 店铺管理员通过手机好获取用户信息
    JSONObject getListByPhone(MmbSalerForm form);

    JSONObject changeTelephone(MmbSaler sysUser);

    JSONObject selectReport();

    JSONObject makeQrCodeForAllSaler();

    SysUserExt getSalerByLoginId(String loginId);
}
