package net.dlyt.qyds.web.service;

import com.alibaba.fastjson.JSONObject;

/**
 * Created by zlh on 2016/7/29.
 */
public interface ErpMemberService {

    /**
     * 根据Id获取会员列表
     *
     * @return
     */
    JSONObject selectAll();

    /**
     * 根据条件获取会员列表
     *
     * @param record
     * @return
     */
    JSONObject selectBySelective(String record);

    /**
     * 根据条件获取会员列表
     *
     * @param record
     * @return
     */
    JSONObject selectByPage(String record);

    /**
     * 根据条件获取会员列表
     *
     * @param record
     * @return
     */
    JSONObject getCountBySelective(String record);

    /**
     * 根据ID获取会员信息
     *
     * @param id
     * @return
     */
    JSONObject getById(String id);

    /**
     * 根据ID更新会员信息
     *
     * @param record
     */
    JSONObject updateById(String record);

    /**
     * 新建会员信息
     *
     * @param record
     */
    JSONObject insert(String record);

    /**
     * 根据ID物理删除会员信息
     *
     * @param id
     */
    JSONObject deleteByPrimaryKey(String id);

    /**
     * 物理删除所以会员信息
     *
     */
    JSONObject deleteAll();

    /**
     * 删除原有数据,批量导入会员信息
     *
     * @param list
     */
    JSONObject inputAll(String list);

    /**
     * 批量更新会员信息
     *
     * @param list
     */
    JSONObject updateByList(String list);

    /**
     * 根据Id获取店员列表
     *
     * @return
     */
    JSONObject selectSaler();
}
