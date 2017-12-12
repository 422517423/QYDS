package net.dlyt.qyds.web.service;

import com.alibaba.fastjson.JSONObject;
import net.dlyt.qyds.common.dto.ShpMaster;
import net.dlyt.qyds.common.dto.ShpMasterExt;

import java.util.List;

/**
 * Created by wy on 2016/7/18.
 */
public interface ShpMasterService {
    /*获取店铺列表*/
    JSONObject selectAll(String data);

    /* 根据店铺ID获取列表*/
    JSONObject edit(String shopId);

    /*根据ID删除店铺信息*/
    JSONObject delete(String shopId);

    /*保存*/
    JSONObject save(String data);

}
