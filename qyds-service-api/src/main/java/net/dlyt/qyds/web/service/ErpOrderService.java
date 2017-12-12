package net.dlyt.qyds.web.service;

import com.alibaba.fastjson.JSONObject;

/**
 * Created by zlh on 2016/8/6.
 */
public interface ErpOrderService {
    /**
     * 批量导入ERP库存主表
     *
     * @param data
     */
    JSONObject inputMaster(String data);
    /**
     * 批量导入ERP库存子表
     *
     * @param data
     */
    JSONObject inputSub(String data);

    /**
     * 根据条件获取库存记录列表
     *
     * @param record
     * @return
     */
    JSONObject getMasterPage(String record);

    /**
     * 根据条件获取库存记录列表
     *
     * @param record
     * @return
     */
    JSONObject getSubPage(String record);
}
