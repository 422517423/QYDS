package net.dlyt.qyds.web.service;

import com.alibaba.fastjson.JSONObject;

import java.util.List;

/**
 * Created by zlh on 2016/7/29.
 */
public interface ErpGoodsService {

    /**
     * 根据Id获取商品列表
     *
     * @return
     */
    JSONObject selectAll();

    /**
     * 根据商品代码获取商品列表
     *
     * @param code
     * @return
     */
    JSONObject selectByCode(String code);

    /**
     * 根据条件获取商品列表
     *
     * @param record
     * @return
     */
    JSONObject selectBySelective(String record);

    /**
     * 根据条件获取商品列表
     *
     * @param record
     * @return
     */
    JSONObject selectByPage(String record);

    /**
     * 根据条件获取商品列表
     *
     * @param record
     * @return
     */
    JSONObject getCountBySelective(String record);

    /**
     * 根据ID获取商品信息
     *
     * @param id
     * @return
     */
    JSONObject getById(String id);

    /**
     * 根据ID更新商品信息
     *
     * @param record
     */
    JSONObject updateById(String record);

    /**
     * 新建商品信息
     *
     * @param record
     */
    JSONObject insert(String record);

    /**
     * 根据ID物理删除商品信息
     *
     * @param id
     */
    JSONObject deleteByPrimaryKey(String id);

    /**
     * 物理删除所以商品信息
     *
     */
    JSONObject deleteAll();

    /**
     * 删除原有数据,批量导入商品信息
     *
     * @param list
     */
    JSONObject inputAll(String list);

    /**
     * 批量更新商品信息
     *
     * @param list
     */
    JSONObject updateByList(String list);
}
