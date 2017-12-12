package net.dlyt.qyds.web.service;

import com.alibaba.fastjson.JSONObject;

/**
 * Created by zlh on 2016/7/29.
 */
public interface ErpGoodsSizeService {

    /**
     * 根据Id获取商品尺码列表
     *
     * @return
     */
    JSONObject selectAll();

    /**
     * 根据ID获取商品尺码信息
     *
     * @param key
     * @return
     */
    JSONObject getByKey(String key);

    /**
     * 根据ID更新商品尺码信息
     *
     * @param record
     */
    JSONObject updateByKey(String record);

    /**
     * 新建商品尺码信息
     *
     * @param record
     */
    JSONObject insert(String record);

    /**
     * 根据ID物理删除商品尺码信息
     *
     * @param key
     */
    JSONObject deleteByPrimaryKey(String key);

    /**
     * 物理删除所以商品尺码信息
     *
     */
    JSONObject deleteAll();

    /**
     * 删除原有数据,批量导入商品尺码信息
     *
     * @param list
     */
    JSONObject inputAll(String list);

    /**
     * 批量更新商品尺码信息
     *
     * @param list
     */
    JSONObject updateByList(String list);
}
