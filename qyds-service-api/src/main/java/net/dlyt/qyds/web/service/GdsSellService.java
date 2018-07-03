package net.dlyt.qyds.web.service;

import com.alibaba.fastjson.JSONObject;
import net.dlyt.qyds.common.dto.GdsMasterExt;
import net.dlyt.qyds.common.dto.GdsSell;
import net.dlyt.qyds.common.dto.GdsSku;

import java.util.List;
import java.util.Map;

/**
 * Created by congkeyan on 2016/7/19.
 */
public interface GdsSellService {

    // 查询上架信息表获取商品的上架信息
    // 检索出已经配置的相关推荐
    // 见错处已经配置的配套商品
    JSONObject edit(String goodsId);

    // 保存数据到商品上架信息中
    JSONObject save(String data, Map<String, Object> userMap);

    // 保存数据到商品上架信息中
    JSONObject selectDatas(String goodsIds);

    // 通过ID删除数据
    JSONObject delete(String goodsId, String delGoodsId, String flag, Map<String, Object> userMap);

}
