package net.dlyt.qyds.web.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import net.dlyt.qyds.common.dto.*;
import net.dlyt.qyds.common.dto.ext.MmbMasterExt;
import net.dlyt.qyds.common.dto.ext.MmbSalerExt;
import net.dlyt.qyds.dao.*;
import net.dlyt.qyds.dao.ext.MmbMasterMapperExt;
import net.dlyt.qyds.dao.ext.MmbSalerMapperExt;
import net.dlyt.qyds.dao.ext.ShpOrgMapperExt;
import net.dlyt.qyds.dao.ext.SysUserMapperExt;
import net.dlyt.qyds.web.service.UserService;
import net.dlyt.qyds.web.service.common.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * Created by C_Nagai on 2016/7/6.
 */
@Service("userService")
public class UserServiceImpl implements UserService {

    @Autowired
    private SysUserMapperExt sysUserMapperExt;

    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;
    @Autowired
    private ShpOrgMapperExt shpOrgMapperExt;

    @Autowired
    private MmbMasterMapperExt mmbMasterMapperExt;

    @Autowired
    private MmbSalerMapperExt mmbSalerMapperExt;

    @Autowired
    private MmbMasterMapper mmbMasterMapper;

    @Autowired
    private MmbSalerMapper mmbSalerMapper;


    /**
     * 获取用户列表
     *
     * @return
     */
    public JSONObject selectAll(SysUserExt form) {

        JSONObject json = new JSONObject();
        try {
//            List<SysUserExt> list = sysUserMapperExt.selectAll(Constants.ORGID);
            form.setShpId(Constants.ORGID);
            List<SysUserExt> list = sysUserMapperExt.selectAll(form);
            int allCount = sysUserMapperExt.selectCountAll(form);

            json.put("sEcho", form.getsEcho());
            json.put("iTotalRecords", allCount);
            json.put("iTotalDisplayRecords", allCount);
            json.put("aaData", list);
            json.put("resultCode", Constants.NORMAL);
//
//            json.put("data", list);
//            json.put("resultCode", Constants.NORMAL);
        } catch (Exception e) {
            json.put("resultCode", Constants.FAIL);
        }
        return json;
    }

    public JSONObject selectByPrimaryKey(String userId) {
        JSONObject json = new JSONObject();
        try {
            if (!StringUtils.isEmpty(userId)) {
                SysUserExt user = sysUserMapperExt.selectByPrimaryKey(Integer.valueOf(userId));
                if (user != null) {
                    json.put("data", user);
                }
            }
            json.put("resultCode", Constants.NORMAL);
        } catch (Exception e) {
            json.put("resultCode", Constants.FAIL);
        }
        return json;
    }

    /**
     * 插入用户信息
     *
     * @param sysUserExt
     * @param sysUser
     * @return
     */
    @Transactional
    public JSONObject insertSelective(SysUserExt sysUserExt, SysUser sysUser) {
        JSONObject json = new JSONObject();
        SysUserExt rSysRole = null;
        int retInsert = sysUserMapperExt.insertSelective(sysUser);
        if (retInsert != 1) {
            json.put("resultCode", Constants.FAIL);
            return json;
        }
        //用login_userId去反取user_id
        SysUser sysUserNew = sysUserMapperExt.selectByLoginId(sysUser.getLoginId());
        // 绑定角色关系
        SysUserRole record = new SysUserRole();
        record.setUserId(sysUserNew.getUserId());
        record.setRoleId(sysUserExt.getRoleId());

        int ret = sysUserRoleMapper.insert(record);
        if (ret == 1) {
            // 获取角色信息
            rSysRole = sysUserMapperExt.selectByPrimaryKey(sysUserNew.getUserId());
            json.put("data", rSysRole);
            json.put("resultCode", Constants.NORMAL);
        } else {
            json.put("resultCode", Constants.FAIL);
        }
        return json;
    }

    /**
     * 修改用户信息
     *
     * @param sysUserExt
     * @param sysUser
     * @return
     */
    @Transactional
    public JSONObject updateByPrimaryKeySelective(SysUserExt sysUserExt, SysUser sysUser) {
        JSONObject json = new JSONObject();
        SysUserExt rSysRole = null;
        int retUpdate = sysUserMapperExt.updateByPrimaryKeySelective(sysUser);
        if (retUpdate != 1) {
            json.put("resultCode", Constants.FAIL);
            return json;
        }
        int retRoleDelete = sysUserRoleMapper.deleteByPrimaryKey(sysUser.getUserId());
//        if (retRoleDelete != 1) {
//            json.put("resultCode", Constants.FAIL);
//            return json;
//        }
        SysUserRole record = new SysUserRole();
        record.setUserId(sysUser.getUserId());
        record.setRoleId(sysUserExt.getRoleId());
        int ret = sysUserRoleMapper.insert(record);
        if (ret == 1) {
            // 获取角色信息
            rSysRole = sysUserMapperExt.selectByPrimaryKey(sysUser.getUserId());
            json.put("data", rSysRole);
            json.put("resultCode", Constants.NORMAL);
        } else {
            json.put("resultCode", Constants.FAIL);
        }
        return json;
    }


    /**
     * 修改密码
     *
     * @param sysUser
     * @return
     */
    public JSONObject updateByPrimaryKeySelective(SysUser sysUser) {
        JSONObject json = new JSONObject();
        int ret = sysUserMapperExt.updatePassword(sysUser);
        if (ret == 1) {
            json.put("resultCode", Constants.NORMAL);
        } else {
            json.put("resultCode", Constants.FAIL);
        }
        return json;
    }

    /**
     * 修改密码
     *
     * @param sysUser
     * @return
     */
    @Override
    public JSONObject changePassword(SysUser sysUser) {
        JSONObject json = new JSONObject();
        int ret = sysUserMapperExt.updatePassword(sysUser);
        if (ret == 1) {

            try {

                MmbMasterExt record = new MmbMasterExt();
                record.setTelephone(sysUser.getLoginId());
                record.setNeedColumns(10);
                record.setStartPoint(0);
                List<MmbMasterExt> list = mmbMasterMapperExt.selectAllByPhone(record);
                for (MmbMasterExt mmbMasterExt : list) {
                    MmbMaster mmbMaster = new MmbMaster();
                    mmbMaster.setMemberId(mmbMasterExt.getMemberId());
                    mmbMaster.setPassword(sysUser.getPassword());

                    mmbMasterMapper.updateByPrimaryKeySelective(mmbMaster);
                }

                MmbSalerExt mmbSalerExt = new MmbSalerExt();
                mmbSalerExt.setTelephone(sysUser.getLoginId());
                mmbSalerExt.setNeedColumns(10);
                mmbSalerExt.setStartPoint(0);
                List<MmbSalerExt> mmbSalerExtList = mmbSalerMapperExt.selectAllByPhone(mmbSalerExt);
                for (MmbSalerExt item : mmbSalerExtList) {
                    MmbSaler mmbSaler = new MmbSaler();
                    mmbSaler.setMemberId(item.getMemberId());
                    mmbSaler.setPassword(sysUser.getPassword());

                    mmbSalerMapper.updateByPrimaryKeySelective(mmbSaler);
                }

            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

            json.put("resultCode", Constants.NORMAL);
        } else {
            json.put("resultCode", Constants.FAIL);
        }
        return json;
    }

    /**
     * 重置密码
     *
     * @param sysUser
     * @return
     */
    public JSONObject resetPassword(SysUser sysUser) {
        JSONObject json = new JSONObject();
        int ret = sysUserMapperExt.updateByPrimaryKeySelective(sysUser);
        if (ret == 1) {
            json.put("resultCode", Constants.NORMAL);
        } else {
            json.put("resultCode", Constants.FAIL);
        }
        return json;
    }


    /**
     * 根据用户ID删除用户
     *
     * @param sysUser
     * @return
     */
    public JSONObject deleteByPrimaryKey(SysUser sysUser) {
        JSONObject json = new JSONObject();
        try {

            SysUserExt sysUserExt = sysUserMapperExt.selectByPrimaryKey(sysUser.getUserId());

            int ret = sysUserMapperExt.deleteByPrimaryKey(sysUser);
            if (ret == 1) {

                try {

                    MmbMasterExt record = new MmbMasterExt();
                    record.setTelephone(sysUserExt.getLoginId());
                    record.setNeedColumns(10);
                    record.setStartPoint(0);
                    List<MmbMasterExt> list = mmbMasterMapperExt.selectAllByPhone(record);
                    for (MmbMasterExt mmbMasterExt : list) {
                        MmbMaster mmbMaster = new MmbMaster();
                        mmbMaster.setMemberId(mmbMasterExt.getMemberId());
                        mmbMaster.setDeleted("1");
                        mmbMasterMapper.updateByPrimaryKeySelective(mmbMaster);
                    }

                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }


                json.put("resultCode", Constants.NORMAL);
            } else {
                json.put("resultCode", Constants.FAIL);
            }
        } catch (Exception e) {
            json.put("resultCode", Constants.FAIL);
        }
        return json;
    }

    /**
     * 用户登陆
     *
     * @param sysUser
     * @return
     */
    public JSONObject selectByLoginIdAndPassword(SysUser sysUser) {
        JSONObject json = new JSONObject();
        SysUser rSysUser = sysUserMapperExt.selectByLoginIdAndPassword(sysUser);
        if (rSysUser != null) {
            json.put("data", rSysUser);
            json.put("resultCode", Constants.NORMAL);
        } else {
            json.put("resultCode", Constants.FAIL);
        }
        return json;
    }

    /**
     * 检测用户名是否重复
     *
     * @param loginId
     * @return
     */
    public JSONObject selectByLoginId(String loginId) {
        JSONObject json = new JSONObject();
        SysUser rSysUser = sysUserMapperExt.selectByLoginId(loginId);
        if (rSysUser != null) {
            json.put("data", rSysUser);
            json.put("resultCode", Constants.NORMAL);
        } else {
            json.put("resultCode", Constants.FAIL);
        }
        return json;
    }

}
