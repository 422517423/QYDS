package net.dlyt.qyds.web.service.impl;

import com.alibaba.fastjson.JSONObject;
import net.dlyt.qyds.common.dto.SysRole;
import net.dlyt.qyds.common.dto.SysRoleMenuKey;
import net.dlyt.qyds.common.view.SysMenuExt;
import net.dlyt.qyds.dao.SysRoleMapper;
import net.dlyt.qyds.dao.SysRoleMenuMapper;
import net.dlyt.qyds.dao.ext.SysRoleMapperExt;
import net.dlyt.qyds.web.service.SysRoleService;
import net.dlyt.qyds.web.service.common.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * Created by C_Nagai on 2016/7/4.
 */
@Service("sysRoleService")
@Transactional(readOnly = true)
public class SysRoleServiceImpl implements SysRoleService {

    @Autowired
    private SysRoleMapperExt sysRoleMapperExt;

    @Autowired
    private SysRoleMapper sysRoleMapper;

    @Autowired
    private SysRoleMenuMapper sysRoleMenuMapper;

    /**
     * 获取角色列表
     * @return
     */
    public JSONObject selectAll(){

        JSONObject json = new JSONObject();
        try{
            List<SysRole> list = sysRoleMapperExt.selectAll();
            json.put("data", list);
            json.put("resultCode", Constants.NORMAL);
        }catch(Exception e){
            json.put("resultCode", Constants.FAIL);
        }
        return json;
    }

    /**
     * 根据角色ID获取角色信息
     * @return
     */
    public JSONObject getSysRoleById(String id){
        JSONObject json = new JSONObject();
        try{
            if (!StringUtils.isEmpty(id)) {
                SysRole sysRole = sysRoleMapper.selectByPrimaryKey(Integer.valueOf(id));
                json.put("data", sysRole);
            }
            json.put("resultCode", Constants.NORMAL);
        }catch(Exception e){
            json.put("resultCode", Constants.FAIL);
        }
        return json;
    }

    /**
     * 根据角色ID更新角色信息
     * @return sysRole 角色信息
     */
    @Transactional(readOnly = false,rollbackFor = Exception.class)
    public JSONObject updateByPrimaryKeySelective(SysRole sysRole){
        JSONObject json = new JSONObject();
        SysRole rSysRole = null;
        int retUpdate = sysRoleMapperExt.updateByPrimaryKeySelective(sysRole);
        if(retUpdate == 1) {
            rSysRole = sysRoleMapper.selectByPrimaryKey(sysRole.getRoleId());
            json.put("data", rSysRole);
            json.put("resultCode", Constants.NORMAL);
        }else {
            json.put("resultCode", Constants.FAIL);
        }

        return json;
    }

    /**
     * 插入角色信息
     * @param sysRole
     * @return
     */
    @Transactional(readOnly = false,rollbackFor = Exception.class)
    public JSONObject insertSelective(SysRole sysRole){
        JSONObject json = new JSONObject();
        SysRole rSysRole = null;
        int retInsert = sysRoleMapperExt.insertSelective(sysRole);
        if(retInsert == 1) {
            rSysRole = sysRoleMapper.selectByPrimaryKey(sysRole.getRoleId());
            json.put("data", rSysRole);
            json.put("resultCode", Constants.NORMAL);
        }else {
            json.put("resultCode", Constants.FAIL);
        }

        return json;
    }

    /**
     * 根据角色ID删除角色信息
     * @param sysRole
     * @return
     */
    @Transactional(readOnly = false,rollbackFor = Exception.class)
    public JSONObject deleteByPrimaryKey(SysRole sysRole){
        JSONObject json = new JSONObject();
        try{
            int ret = sysRoleMapperExt.deleteByPrimaryKey(sysRole);
            if(ret == 1){
                json.put("resultCode", Constants.NORMAL);
            }else {
                json.put("resultCode", Constants.FAIL);
            }
        }catch(Exception e){
            json.put("resultCode", Constants.FAIL);
        }
        return json;
    }

    /**
     * 获取角色菜单
     * @param roleId
     * @return List<SysRole>
     */
    public JSONObject getRoleMenu(Integer roleId) {
        JSONObject map = new JSONObject();

        if(roleId != null){
            try{
                List<SysMenuExt> list = sysRoleMapperExt.getRoleMenu(roleId);
                map.put("data",list);
                map.put("resultCode",Constants.NORMAL);
            }catch (Exception e){
                map.put("resultCode",Constants.FAIL);
            }
        }else{
            map.put("resultCode",Constants.FAIL);
        }
        map.put("message","");
        return map;
    }

    /**
     * 角色菜单保存
     * @param list
     */
    @CacheEvict(value="${role_memu_cache_name}", key="#roleId")
    @Transactional(readOnly = false,rollbackFor = Exception.class)
    public JSONObject saveRoleMenu(List<SysRoleMenuKey> list,int roleId) {

        JSONObject map = new JSONObject();
        try{
            if(list != null && list.size() > 0){
                sysRoleMapperExt.clearRoleMenu(list.get(0).getRoleId());
                for(int i=0;i<list.size();i++){
                    sysRoleMenuMapper.insert(list.get(i));
                }
            }else{
                sysRoleMapperExt.clearRoleMenu(roleId);
            }
            map.put("resultCode",Constants.NORMAL);
        }catch(Exception e){
            map.put("resultCode",Constants.FAIL);
        }

        map.put("message","");

        return map;
    }
}
