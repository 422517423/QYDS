package net.dlyt.qyds.web.service;

import com.alibaba.fastjson.JSONObject;
import net.dlyt.qyds.common.dto.SysMenu;

import java.util.List;

/**
 * Created by panda on 16/7/4.
 */
public interface MenuService {

    //获取菜单列表
    JSONObject getMenu();

    //添加菜单
    JSONObject addMenu(SysMenu dto);

    //编辑菜单void
    JSONObject editMenu(SysMenu dto);

    //删除菜单
    JSONObject deleteMenu(SysMenu dto);

    //上移、下移菜单
    JSONObject updateSort(List<SysMenu> list);

    JSONObject getValidMenu(Integer userId);

    JSONObject getRoleMenu(Integer roleId);
}
