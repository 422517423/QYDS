package net.dlyt.qyds.web.service;

import com.alibaba.fastjson.JSONObject;
import net.dlyt.qyds.common.form.MmbPointRuleForm;

/**
 * Created by YiLian on 16/7/29.
 */
public interface MmbPointRuleService {

    JSONObject getList(MmbPointRuleForm form);

    JSONObject add(MmbPointRuleForm form, String userId);

    JSONObject edit(MmbPointRuleForm form, String userId);

    JSONObject delete(MmbPointRuleForm form, String userId);

    JSONObject getDetail(MmbPointRuleForm form);

    JSONObject getRuleByCode(MmbPointRuleForm form);
}
