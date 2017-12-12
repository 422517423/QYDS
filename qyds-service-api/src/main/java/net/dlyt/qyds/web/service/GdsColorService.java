package net.dlyt.qyds.web.service;

import com.alibaba.fastjson.JSONObject;
import net.dlyt.qyds.common.dto.GdsColoreimage;
import net.dlyt.qyds.common.dto.GdsSku;

import java.util.List;
import java.util.Map;

/**
 * Created by congkeyan on 2016/7/19.
 */
public interface GdsColorService {

    // 保存数据到颜色表中
    JSONObject save(String data, Map<String, Object> userMap);

    // 通过ID获取数据
    JSONObject edit(String goodsColoreId);


}
