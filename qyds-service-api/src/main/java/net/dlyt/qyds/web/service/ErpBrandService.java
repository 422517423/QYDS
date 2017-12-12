package net.dlyt.qyds.web.service;

import com.alibaba.fastjson.JSONObject;
import net.dlyt.qyds.common.dto.ErpBrand;
import net.dlyt.qyds.common.dto.ext.ErpBrandExt;

import java.util.List;

/**
 * Created by zlh on 2016/7/29.
 */
public interface ErpBrandService {

    /**
     * 根据Id获取商品品牌列表
     *
     * @return
     */
    JSONObject selectAll();

    /**
     * 根据ID获取商品品牌信息
     *
     * @param id
     * @return
     */
    JSONObject getById(String id);

    /**
     * 根据ID更新商品品牌信息
     *
     * @param record
     */
    JSONObject updateById(String record);

    /**
     * 新建商品品牌信息
     *
     * @param record
     */
    JSONObject insert(String record);

    /**
     * 根据ID物理删除商品品牌信息
     *
     * @param id
     */
    JSONObject deleteByPrimaryKey(String id);

    /**
     * 物理删除所以商品品牌信息
     *
     */
    JSONObject deleteAll();

    /**
     * 删除原有数据,批量导入商品品牌信息
     *
     * @param list
     */
    JSONObject inputAll(String list);

    /**
     * 批量更新商品品牌信息
     *
     * @param list
     */
    JSONObject updateByList(String list);
}
