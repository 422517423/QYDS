package net.dlyt.qyds.web.service;

import com.alibaba.fastjson.JSONObject;
import net.dlyt.qyds.common.dto.GdsDetail;
import net.dlyt.qyds.common.dto.GdsMaster;
import net.dlyt.qyds.common.dto.GdsMasterExt;
import net.dlyt.qyds.common.dto.GdsSku;

import java.util.List;
import java.util.Map;

/**
 * Created by congkeyan on 2016/7/19.
 */
public interface GdsSkuService {


    //sku保存
    JSONObject save(String data, Map<String, Object> userMap);

    //SKU编辑
    JSONObject edit(String goodsSkuId,String skuKey,String goodsId);

}
