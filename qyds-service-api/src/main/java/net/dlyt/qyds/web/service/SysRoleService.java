package net.dlyt.qyds.web.service;

import com.alibaba.fastjson.JSONObject;
import net.dlyt.qyds.common.dto.SysRole;
import net.dlyt.qyds.common.dto.SysRoleMenuKey;

import java.util.List;

/**
 * Created by C_Nagai on 2016/7/4.
 */
public interface SysRoleService {

    //获取角色列表
    JSONObject selectAll();

    // 根据角色ID获取角色信息
    JSONObject getSysRoleById(String id);

    // 根据角色ID更新角色信息
    JSONObject updateByPrimaryKeySelective(SysRole sysRole);

    // 新建角色
    JSONObject insertSelective(SysRole sysRole);

    // 根据角色ID删除角色信息
    JSONObject deleteByPrimaryKey(SysRole sysRole);

    //获取角色对应菜单
    JSONObject getRoleMenu(Integer roleId);

    //角色菜单保存
    JSONObject saveRoleMenu(List<SysRoleMenuKey> list,int roleId);
}
