package net.dlyt.qyds.web.service;


import com.alibaba.fastjson.JSONObject;
import net.dlyt.qyds.common.dto.ShpOrg;
import net.dlyt.qyds.common.dto.SysMenu;


import java.util.List;

/**
 * Created by wanglijie on 16/7/29.
 */
public interface ShpOrgService {

    /**
     * 获取所有店铺组织
     * @return
     */
    JSONObject getOrg();
    //获取组织子名称
    JSONObject getChildOrg();
    //添加菜单
    void addOrg(ShpOrg dto);

    //编辑菜单void
    void editOrg(ShpOrg dto);

    //删除菜单
    void deleteOrg(ShpOrg dto);

    //上移、下移菜单
    void updateSort(List<ShpOrg> list);
}
