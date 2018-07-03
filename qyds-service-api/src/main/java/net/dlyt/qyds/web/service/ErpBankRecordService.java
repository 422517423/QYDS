package net.dlyt.qyds.web.service;

import com.alibaba.fastjson.JSONObject;
import net.dlyt.qyds.common.dto.ext.ErpBankRecordExt;
import net.dlyt.qyds.common.form.ErpBankRecordForm;

import java.util.List;

/**
 * Created by zlh on 2016/8/6.
 */
public interface ErpBankRecordService {

    /**
     * 根据Id获取库存列表
     *
     * @return
     */
    JSONObject selectAll();

    /**
     * 根据条件获取库存列表
     *
     * @param record
     * @return
     */
    JSONObject selectBySelective(String record);

    /**
     * 根据条件获取合计库存列表
     *
     * @param form
     * @return
     */
    JSONObject selectSumByPage(String form);

    /**
     * 根据条件获取合计库存件数
     *
     * @param form
     * @return
     */
    JSONObject getCountSumByPage(String form);

    /**
     * 根据条件获取库存记录列表
     *
     * @param record
     * @return
     */
    JSONObject selectRecordByPage(String record);

    /**
     * 根据ID获取库存信息
     *
     * @param id
     * @return
     */
    JSONObject getById(String id);

    /**
     * 根据ID更新库存信息
     *
     * @param record
     */
    JSONObject updateById(String record);

    /**
     * 新建库存信息
     *
     * @param record
     */
    JSONObject insert(String record);

    /**
     * 根据ID物理删除库存信息
     *
     * @param key
     */
    JSONObject deleteByPrimaryKey(String key);

    /**
     * 物理删除所以库存信息
     *
     */
    JSONObject deleteAll();

    /**
     * 删除原有数据,批量导入库存信息
     *
     * @param data
     */
    JSONObject inputAll(String data);
}
