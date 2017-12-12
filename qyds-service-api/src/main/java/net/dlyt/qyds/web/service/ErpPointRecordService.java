package net.dlyt.qyds.web.service;

import com.alibaba.fastjson.JSONObject;

/**
 * Created by zlh on 2016/8/6.
 */
public interface ErpPointRecordService {

    /**
     * 根据条件获取积分记录列表
     *
     * @param record
     * @return
     */
    JSONObject selectRecordByPage(String record);

    /**
     * 导入会员积分信息
     *
     * @param data
     */
    JSONObject inputAll(String data);
}
