package net.dlyt.qyds.web.service.impl;

import com.alibaba.fastjson.JSONObject;
import net.dlyt.qyds.common.dto.SysMenu;
import net.dlyt.qyds.dao.ext.SysMenuMapperExt;
import net.dlyt.qyds.web.service.MenuService;
import net.dlyt.qyds.web.service.common.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by panda on 16/7/4.
 */
@Service("menuService")
@Transactional(readOnly = true)
public class MenuServiceImpl implements MenuService{

    @Autowired
    private SysMenuMapperExt sysMenuMapperExt;

    public JSONObject getMenu() {
        JSONObject map = new JSONObject();

        try{
            List<SysMenu> list = sysMenuMapperExt.getMenu();
            map.put("data",list);
            map.put("message","");
            map.put("resultCode",Constants.NORMAL);
        }catch (Exception e){
            map.put("resultCode", Constants.FAIL);
        }

        return map;
    }

    @Transactional(readOnly = false)
    public JSONObject addMenu(SysMenu dto) {
        JSONObject map = new JSONObject();
        try{
            JSONObject json = new JSONObject();
            sysMenuMapperExt.addMenu(dto);

            map.put("resultCode",Constants.NORMAL);
            json.put("flag",Constants.INSERT);

            json.put("id", dto.getMenuId());
            map.put("data",json);
        }catch(Exception e){
            map.put("resultCode", Constants.FAIL);
        }

        return map;
    }

    @Transactional(readOnly = false)
    public JSONObject editMenu(SysMenu dto) {
        JSONObject map = new JSONObject();
        try{
            JSONObject json = new JSONObject();
            sysMenuMapperExt.editMenu(dto);
            map.put("resultCode",Constants.NORMAL);
            json.put("flag",Constants.UPDATE);
            map.put("data",json);
        }catch(Exception e){
            map.put("resultCode", Constants.FAIL);
        }
        return map;
    }

    @Transactional(readOnly = false,rollbackFor = Exception.class)
    public JSONObject deleteMenu(SysMenu dto) {
        JSONObject json = new JSONObject();
        int retUpdate = sysMenuMapperExt.updateSortForDel(dto);
        if(retUpdate >= 0){
            int retDelete = sysMenuMapperExt.deleteMenu(dto);
            if(retDelete == 1){
                json.put("resultCode",Constants.NORMAL);
            }else {
                json.put("resultCode", Constants.FAIL);
            }
        }else {
            json.put("resultCode", Constants.FAIL);
        }
        return json;
    }

    @Transactional(readOnly = false,rollbackFor = Exception.class)
    public JSONObject updateSort(List<SysMenu> list) {
        JSONObject json = new JSONObject();
        try{
            if(list != null && list.size() >0){
                for(int i=0;i<list.size();i++){
                    sysMenuMapperExt.updateSort(list.get(i));
                }
            }
            json.put("resultCode",Constants.NORMAL);
        }catch(Exception e){
            json.put("resultCode", Constants.FAIL);
        }
        return json;
    }

    public JSONObject getValidMenu(Integer userId) {
        JSONObject map = new JSONObject();

        try{
            if(userId == null || userId == 0){
                map.put("resultCode",Constants.FAIL);
                return map;
            }

            List<SysMenu> list = sysMenuMapperExt.getValidMenu(userId);
            map.put("resultCode",Constants.NORMAL);
            map.put("data",list);
        }catch (Exception e){
            map.put("resultCode",Constants.FAIL);
        }
        map.put("message","");

        return map;
    }

//    @Cacheable(value="${role_memu_cache_name}", key="#roleId")
    public JSONObject getRoleMenu(Integer roleId) {
        JSONObject map = new JSONObject();

        try{
            if(roleId == null || roleId == 0){
                map.put("resultCode",Constants.FAIL);
                return map;
            }
            List<SysMenu> list = sysMenuMapperExt.getRoleMenu(roleId);
            map.put("resultCode",Constants.NORMAL);
            map.put("data",list);
        }catch (Exception e){
            map.put("resultCode",Constants.FAIL);
        }
        map.put("message","");

        return map;
    }
}
