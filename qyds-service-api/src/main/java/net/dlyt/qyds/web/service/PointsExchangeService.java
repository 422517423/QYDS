package net.dlyt.qyds.web.service;

import com.alibaba.fastjson.JSONObject;
import net.dlyt.qyds.common.form.GdsMasterForm;
import net.dlyt.qyds.common.form.SkuForm;

import java.util.Map;

/**
 * Created by congkeyan on 2016/7/19.
 */
public interface PointsExchangeService {

    //积分兑换活动种类列表获取
    JSONObject activityTypeService(String memberId);

}
