package net.dlyt.qyds.web.service;

import com.alibaba.fastjson.JSONObject;
import net.dlyt.qyds.common.dto.MmbMaster;
import net.dlyt.qyds.common.dto.OrdMasterExt;
import net.dlyt.qyds.common.dto.ext.MmbMasterExt;
import net.dlyt.qyds.common.form.MmbMasterForm;

import java.util.List;

/**
 * Created by pancd on 16/7/27.
 */
public interface MmbMasterService {

    List<MmbMasterExt> selectAll(MmbMasterExt ext);

    int getAllDataCount(MmbMasterExt ext);

    JSONObject insertSelective(MmbMaster user);

    JSONObject selectByPrimaryKey(String memerId);

    JSONObject updateByPrimaryKeySelective(MmbMaster user);

    JSONObject deleteByPrimaryKey(MmbMaster user);

    JSONObject undeleteByPrimaryKey(MmbMaster user);

    JSONObject selectBySelective(MmbMaster user);

    JSONObject bindingSelective(MmbMaster sysUser);

    JSONObject loginSelective(MmbMaster sysUser);

    JSONObject getCurrentPoints(MmbMaster sysUser);

    JSONObject registerUser(MmbMaster sysUser);

    JSONObject getAddressList(MmbMaster mmbMaster);

    JSONObject changePassword(MmbMaster sysUser);

    // 店铺管理员通过手机好获取用户信息
    JSONObject getListByPhone(MmbMasterForm form);

    JSONObject changeTelephone(MmbMaster sysUser);

    JSONObject changeGrade(MmbMaster sysUser);

    JSONObject modifyMemberAvatarFromWX(MmbMasterExt mmbMaster);

    JSONObject selectReport();

    List<MmbMasterExt> export(MmbMasterExt ext);

    // TODO: 2017/12/15 临时方法 获取客户总消费金额
    void searchMasterAllPrice();
}
