package net.dlyt.qyds.web.service;

import com.alibaba.fastjson.JSONObject;
import net.dlyt.qyds.common.form.MmbGoodsExtForm;
import net.dlyt.qyds.common.form.MmbLevelRuleForm;

/**
 * Created by C_Nagai on 2016/7/27.
 */
public interface MmbLevelRuleService {

    JSONObject getList(MmbLevelRuleForm form);

    JSONObject edit(MmbLevelRuleForm form, String userId);

    JSONObject getDetail(MmbLevelRuleForm form);

    JSONObject add(MmbLevelRuleForm form, String userId);

    JSONObject delete(MmbLevelRuleForm form, String userId);

    JSONObject addGoods(MmbGoodsExtForm form);

    JSONObject getGoodsList(String memberLevelId);

    JSONObject deleteGoods(MmbGoodsExtForm form);
}
