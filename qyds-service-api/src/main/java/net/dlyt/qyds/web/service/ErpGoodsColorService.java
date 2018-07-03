package net.dlyt.qyds.web.service;

import com.alibaba.fastjson.JSONObject;
import net.dlyt.qyds.common.dto.ErpGoodsColor;
import net.dlyt.qyds.common.dto.ext.ErpGoodsColorExt;

import java.util.List;

/**
 * Created by zlh on 2016/7/26.
 */
public interface ErpGoodsColorService {

    /**
     * 根据Id获取颜色列表
     *
     * @return
     */
    JSONObject selectAll();

    /**
     * 根据条件获取颜色列表
     *
     * @param form
     * @return
     */
    JSONObject selectBySelective(String form);

    /**
     * 根据ID获取颜色信息
     *
     * @param id
     * @return
     */
    JSONObject getById(String id);

    /**
     * 根据ID更新颜色信息
     *
     * @param record
     */
    JSONObject updateById(String record);

    /**
     * 新建颜色信息
     *
     * @param record
     */
    JSONObject insert(String record);

    /**
     * 根据ID物理删除颜色信息
     *
     * @param id
     */
    JSONObject deleteByPrimaryKey(String id);

    /**
     * 物理删除所以颜色信息
     *
     */
    JSONObject deleteAll();

    /**
     * 删除原有数据,批量导入颜色信息
     *
     * @param list
     */
    JSONObject inputAll(String list);

    /**
     * 批量更新颜色信息
     *
     * @param list
     */
    JSONObject updateByList(String list);
}
