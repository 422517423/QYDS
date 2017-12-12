package net.dlyt.qyds.web.service;

import com.alibaba.fastjson.JSONObject;
import net.dlyt.qyds.common.dto.SysRole;
import net.dlyt.qyds.common.dto.SysUser;
import net.dlyt.qyds.common.dto.SysUserExt;

import java.util.List;

/**
 * Created by C_Nagai on 2016/7/6.
 */
public interface UserService {

    //获取用户列表
    JSONObject selectAll(SysUserExt form);

    // 新建用户
    JSONObject insertSelective(SysUserExt sysUserExt, SysUser sysUser);

    // 修改用户
    JSONObject updateByPrimaryKeySelective(SysUserExt sysUserExt, SysUser sysUser);

    // 获取用户信息
    JSONObject selectByPrimaryKey(String userId);

    // 根据用户ID删除用户
    JSONObject deleteByPrimaryKey(SysUser sysUser);

    // 获取用户信息
    JSONObject selectByLoginIdAndPassword(SysUser sysUser);

    // 检测用户名是否存在
    JSONObject selectByLoginId(String loginId);

    // 修改密码
    JSONObject updateByPrimaryKeySelective(SysUser sysUser);

    // 重置密码
    JSONObject resetPassword(SysUser sysUser);

    /**
     * 修改密码
     *
     * @param sysUser
     * @return
     */
    JSONObject changePassword(SysUser sysUser);
}
