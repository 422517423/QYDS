package net.dlyt.qyds.web.service;

import com.alibaba.fastjson.JSONObject;
import net.dlyt.qyds.common.dto.GdsDetail;
import net.dlyt.qyds.common.dto.GdsMaster;
import net.dlyt.qyds.common.dto.GdsMasterExt;
import net.dlyt.qyds.common.dto.GdsSuitlist;
import net.dlyt.qyds.common.form.GdsMasterForm;

import java.util.List;
import java.util.Map;

/**
 * Created by congkeyan on 2016/7/19.
 */
public interface GdsSuitlistService {

    // 根据店铺ID获取商品品牌列表
    JSONObject getList(GdsMasterForm form);

    // 保存数据到商品SKU表中
    JSONObject save(String data, Map<String, Object> userMap);

    // 获取已经选择的商品信息
    JSONObject getSelectList(GdsMasterForm form);

    //删除已经选择的套装列表
    JSONObject delete(String suitId);

}
