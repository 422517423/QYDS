package net.dlyt.qyds.web.service;

import com.alibaba.fastjson.JSONObject;

/**
 * Created by zlh on 2016/7/29.
 */
public interface ErpStoreService {

    /**
     * 根据Id获取门店列表
     *
     * @return
     */
    JSONObject selectAll();

    /**
     * 根据ID获取门店信息
     *
     * @param id
     * @return
     */
    JSONObject getById(String id);

    /**
     * 根据ID更新门店信息
     *
     * @param record
     */
    JSONObject updateById(String record);

    /**
     * 新建门店信息
     *
     * @param record
     */
    JSONObject insert(String record);

    /**
     * 根据ID物理删除门店信息
     *
     * @param id
     */
    JSONObject deleteByPrimaryKey(String id);

    /**
     * 物理删除所以门店信息
     *
     */
    JSONObject deleteAll();

    /**
     * 删除原有数据,批量导入门店信息
     *
     * @param list
     */
    JSONObject inputAll(String list);

    /**
     * 批量更新门店信息
     *
     * @param list
     */
    JSONObject updateByList(String list);
}
