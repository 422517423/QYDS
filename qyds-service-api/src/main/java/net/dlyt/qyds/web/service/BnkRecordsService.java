package net.dlyt.qyds.web.service;

import com.alibaba.fastjson.JSONObject;
import net.dlyt.qyds.common.dto.BnkRecords;
import net.dlyt.qyds.common.dto.ext.BnkRecordsExt;
import net.dlyt.qyds.common.form.BnkRecordsForm;

import java.util.List;

/**
 * Created by wy on 2016/8/3.
 */
public interface BnkRecordsService {

    // 根据商品ID获取商品履历列表
    JSONObject getList(BnkRecordsForm form);

}
