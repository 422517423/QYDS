package net.dlyt.qyds.web.service;

import com.alibaba.fastjson.JSONObject;

/**
 * Created by zlh on 2016/7/29.
 */
public interface ErpProduceLineService {

    /**
     * 根据Id获取产品线列表
     *
     * @return
     */
    JSONObject selectAll();

    /**
     * 根据ID获取产品线信息
     *
     * @param id
     * @return
     */
    JSONObject getById(String id);

    /**
     * 根据ID更新产品线信息
     *
     * @param record
     */
    JSONObject updateById(String record);

    /**
     * 新建产品线信息
     *
     * @param record
     */
    JSONObject insert(String record);

    /**
     * 根据ID物理删除产品线信息
     *
     * @param id
     */
    JSONObject deleteByPrimaryKey(String id);

    /**
     * 物理删除所以产品线信息
     *
     */
    JSONObject deleteAll();

    /**
     * 删除原有数据,批量导入产品线信息
     *
     * @param list
     */
    JSONObject inputAll(String list);

    /**
     * 批量更新产品线信息
     *
     * @param list
     */
    JSONObject updateByList(String list);
}
