package net.dlyt.qyds.web.controller.role;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import net.dlyt.qyds.common.dto.SysRole;
import net.dlyt.qyds.common.dto.SysRoleMenuKey;
import net.dlyt.qyds.common.view.SysMenuExt;
import net.dlyt.qyds.web.common.Constants;
import net.dlyt.qyds.web.common.DataUtils;
import net.dlyt.qyds.web.service.SysRoleService;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by C_Nagai on 2016/6/25.
 */
@Controller
@RequestMapping("/role")
public class RoleController {

    @Resource
    private SysRoleService sysRoleService;

    @RequestMapping("list")
    public String list(){
        return "page/role/list.html";
    }

    @RequestMapping("getList")
    public @ResponseBody
    JSONObject getList(String data){
        return sysRoleService.selectAll();
    }

    @RequestMapping("edit")
    public @ResponseBody JSONObject edit(@RequestParam(required = false)String id){
        return sysRoleService.getSysRoleById(id);
    }

    @RequestMapping("save")
    public @ResponseBody JSONObject save(String data){
        JSONObject json = null;
        try{
            SysRole sysRole = (SysRole)JSON.parseObject(data, SysRole.class);
            JSONObject jsonObject = new JSONObject();
            SysRole role = null;
            if (StringUtils.isEmpty(sysRole.getRoleId())) {
                sysRole.setIsValid(Constants.VALID);
                json = sysRoleService.insertSelective(sysRole);
            } else {
                json = sysRoleService.updateByPrimaryKeySelective(sysRole);
            }
            if (json != null) {
                return json;
            }
        }catch(Exception e){
            json = new JSONObject();
            json.put("resultCode", Constants.FAIL);
        }
        return json;
    }

    @RequestMapping("delete")
    public @ResponseBody JSONObject delete(String data){
        JSONObject json = null;
        try{
            SysRole sysRole = (SysRole)JSON.parseObject(data, SysRole.class);
            json = sysRoleService.deleteByPrimaryKey(sysRole);
        }catch(Exception e){
            json = new JSONObject();
            json.put("resultCode", Constants.FAIL);
        }
        return json;
    }

    @RequestMapping("add_menu")
    public @ResponseBody JSONObject add_menu(@RequestParam(required = false)String id){
        JSONObject json = new JSONObject();
        return json;
    }

    @RequestMapping("getRoleMenu.json")
    @ResponseBody
    public JSONObject getRoleMenu(@RequestParam("id") Integer id){
        return sysRoleService.getRoleMenu(id);
    }

    @RequestMapping("saveRoleMenu.json")
    @ResponseBody
    public JSONObject saveRoleMenu(@RequestParam("data") String data,@RequestParam("roleId") int roleId){
        JSONObject map = null;
        try{
            List<SysRoleMenuKey> list = JSONArray.parseArray(data,SysRoleMenuKey.class);
            map = sysRoleService.saveRoleMenu(list,roleId);
        }catch(Exception e){
            map = new JSONObject();
            map.put("resultCode",Constants.FAIL);
        }
        return map;
    }
}
