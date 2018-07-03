package net.dlyt.qyds.web.service.impl;


import com.alibaba.fastjson.JSONObject;
import net.dlyt.qyds.dao.ext.ShpOrgMapperExt;
import net.dlyt.qyds.web.service.ShpOrgService;
import net.dlyt.qyds.common.dto.ShpOrg;
import net.dlyt.qyds.web.service.common.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by wanglijie on 16/7/29.
 */
@Service
public class ShpOrgServiceImpl implements ShpOrgService {

    @Autowired
    private ShpOrgMapperExt shpOrgMapperExt;


    /**
     * 获取所有店铺组织
     *
     * @return
     */
    public JSONObject getOrg() {
        JSONObject map = new JSONObject();
        try {
            List<ShpOrg> list = shpOrgMapperExt.getOrg();
            map.put("data", list);
            map.put("message", "");
            map.put("resultCode", Constants.NORMAL);
        } catch (Exception e) {
            map.put("resultCode", Constants.FAIL);
        }

        return map;
    }



    public JSONObject  getChildOrg(){
        JSONObject map = new JSONObject();
        try {
            List<ShpOrg> list = shpOrgMapperExt.getChildOrg();
            map.put("data", list);
            map.put("message", "");
            map.put("resultCode", Constants.NORMAL);
        } catch (Exception e) {
            map.put("resultCode", Constants.FAIL);
        }
        return map;
    }
    @Transactional(readOnly = false)
    public void addOrg(ShpOrg dto) {
        shpOrgMapperExt.addOrg(dto);
    }

    @Transactional(readOnly = false)
    public void editOrg(ShpOrg dto) {
        shpOrgMapperExt.editOrg(dto);
    }

    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public void deleteOrg(ShpOrg dto) {
        shpOrgMapperExt.updateSortForDel(dto);
        shpOrgMapperExt.deleteOrg(dto);
    }

    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public void updateSort(List<ShpOrg> list) {
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                shpOrgMapperExt.updateSort(list.get(i));
            }
        }
    }
}
